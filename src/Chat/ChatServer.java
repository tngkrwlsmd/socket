package Chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345; // 서버 포트
    private static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());
    // synchronized Set() : 여러 클라이언트 연결을 관리하는 서버에서 동시에 여러 스레드가 이 데이터 구조에 접근할 때 발생할 수 있는 충돌을 방지함

    public static void main(String[] args) {
        System.out.println("연결 대기중...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) { // 다수의 클라이언트와 연결하기 위해 반복문 사용
                new ClientHandler(serverSocket.accept()).start(); // 개별 클라이언트와 통신하는 소켓의 스레드 시작
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread { // 개별 클라이언트와 통신하기 위한 내부 클래스
        private Socket socket;
        private PrintWriter out;
        private String nickname;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader in = new BufferedReader(isr)
            ) {
                out = new PrintWriter(socket.getOutputStream(), true);

                nickname = in.readLine(); // 닉네임 받기
                broadcast(nickname + " 님이 참가했습니다."); // broadcast(): 다수에게 메시지를 전달하는 브로드캐스트 함수, 하단에 정의됨.

                // synchronized : 스레드 동기화 구문
                synchronized (clientHandlers) { // 클라이언트 리스트에 추가
                    clientHandlers.add(this);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(nickname + ": " + message);
                }
            } catch (IOException e) { } 
            finally {
                synchronized (clientHandlers) {
                    clientHandlers.remove(this); // 클라이언트 제거 및 연결 종료
                    broadcast(nickname + " 님이 나가셨습니다.");
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(String message) {
            synchronized (clientHandlers) {
                for (ClientHandler client : clientHandlers) {
                    client.out.println(message); // client(ClientHandler 객체)에 저장된 메시지를 브로드캐스트
                }
            }
        }
    }
}