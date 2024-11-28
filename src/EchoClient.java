import java.net.*; 
import java.io.*;
import java.util.Scanner; 

public class EchoClient{
  public static void main(String[] args) { 
    try{ 
      Socket socket = new Socket("127.0.0.1", 11000);
      Scanner sc = new Scanner(System.in);
      OutputStream out = socket.getOutputStream();
      InputStream in = socket.getInputStream ();
      PrintWriter pw = new PrintWriter (new OutputStreamWriter (out) ); 
      BufferedReader br= new BufferedReader(new InputStreamReader(in) );
      String sMsg = null; 
      while((sMsg = sc.nextLine()) != null ){
        if (sMsg.equals(".")) break;
        pw.println (sMsg);
        pw.flush ();
        String rsvMsg = br.readLine ();
        System.out.println ("echo massage from server :" + rsvMsg); 
      } 
      pw.close (); br.close (); sc.close();
      socket.close (); 
    } catch (Exception e) { 
      System.out.println (e); 
    } 
  } 
}