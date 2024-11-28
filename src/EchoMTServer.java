import java.net.*;
import java.io.*;
public class EchoMTServer{
  public static void main (String[] args ) {
    try (ServerSocket svSocket = new ServerSocket(11000)) {
      System.out.println ("waiting for connection." );
      while (true) {
        Socket socket = svSocket.accept();

        EchoThread ecthread= new EchoThread(socket);
         ecthread.start();
      }
    } catch(Exception e) {
      System.out.println(e);
    }
  }
}

class EchoThread extends Thread{
  private Socket socket;
  public EchoThread(Socket socket){
    this.socket = socket;
  }

public void run(){
  try{
    InetAddress inaddr= socket.getInetAddress();
    System.out.println(inaddr.getHostAddress() + " connection established.");
    OutputStream out = socket.getOutputStream();
    InputStream in = socket.getInputStream();
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
    BufferedReader br= new BufferedReader(new InputStreamReader(in));

    String str = null;
    while ((str = br.readLine()) != null) {
      System.out.println("received message from client : " + str);
      pw.println(str);
      pw.flush();
    }
      pw.close(); br.close();
      socket.close();
    } catch (Exception e){
      System.out.println(e);
    }
  }
}