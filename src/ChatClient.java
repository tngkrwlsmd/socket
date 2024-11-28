import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 11000);
             OutputStream out = socket.getOutputStream();
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(out), true);
             Scanner sc = new Scanner(System.in)) {

            RecvThread recvThread = new RecvThread(socket);
            recvThread.start();

            String sMsg;
            while ((sMsg = sc.nextLine()) != null) {
                if (sMsg.equals(".")) break;
                pw.println(sMsg);
            }

            System.out.println("Client terminated.");

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}

class RecvThread extends Thread {
    private Socket socket;

    public RecvThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream in = socket.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in))) {

            String rMsg;
            while ((rMsg = br.readLine()) != null) {
                System.out.println("[Server] " + rMsg);
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}