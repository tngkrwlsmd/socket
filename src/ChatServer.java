import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {
        try (ServerSocket svSocket = new ServerSocket(11000)) {
            System.out.println("Waiting for chatting...");
            Socket socket = svSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

            OutputStream out = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out), true);

            Scanner sc = new Scanner(System.in);

            RcvThread rcvThread = new RcvThread(socket);
            rcvThread.start();

            String sMsg;
            while ((sMsg = sc.nextLine()) != null) {
                if (sMsg.equals(".")) break;
                pw.println(sMsg);
            }

            sc.close();
            pw.close();
            socket.close();
            System.out.println("Server terminated.");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}

class RcvThread extends Thread {
    private Socket socket;

    public RcvThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream in = socket.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String rMsg;
            while ((rMsg = br.readLine()) != null) {
                System.out.println("[" + socket.getInetAddress().getHostAddress() + "] " + rMsg);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
