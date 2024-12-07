package Chat;

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost"; // 서버 주소
    private static final int SERVER_PORT = 12345; // 서버 포트

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JTextArea messageArea = new JTextArea(20, 50); // 메시지 창
        JTextField inputField = new JTextField(50); // 메시지 입력 창
        JButton sendButton = new JButton("보내기"); // 메시지 보내기 버튼

        // JFrame 포함 관계: (inputField, sendButton) ⊂ panel ⊂ frame
        messageArea.setEditable(false);
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);
        frame.getContentPane().add(panel, "South");

        frame.pack(); // tkinter의 python과 똑같은 기능
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame 객체가 종료되면, 클라이언트 프로그램의 main 스레드도 종료하도록 함
        frame.setVisible(true);

        try (
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // 닉네임 입력
            String nickname = JOptionPane.showInputDialog(frame, "이름:");
            frame.setTitle(nickname);
            out.println(nickname);

            sendButton.addActionListener(e -> {
                String message = inputField.getText();
                out.println(message);
                inputField.setText("");
            });

            inputField.addActionListener(e -> {
                String message = inputField.getText();
                out.println(message);
                inputField.setText("");
            });

            String incomingMessage;
            while ((incomingMessage = in.readLine()) != null) { // 서버 코드에서 브로드캐스트 된 메시지를 출력
                messageArea.append(incomingMessage + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}