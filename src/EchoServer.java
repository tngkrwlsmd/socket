import java.net.*;
import java.io.*;

public class EchoServer {
    public static void main(String[] args) throws Exception {
        try {
            ServerSocket svSocket = new ServerSocket(11000);
            System.out.println("waiting for connection.");

            Socket socket = svSocket.accept();
            InetAddress indaar = socket.getInetAddress();
            System.out.println(indaar.getHostAddress() + " connection established.");
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String str = null;
            while ((str = br.readLine()) != null) {
                System.out.println("received message: " + str);
                pw.println(str);
                pw.flush();
            }

            pw.close(); br.close();
            socket.close(); svSocket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
