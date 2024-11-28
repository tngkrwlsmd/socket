import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SendDatagram {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in);
             DatagramSocket ds = new DatagramSocket()) {

            InetAddress ia = InetAddress.getByName("127.0.0.1"); // IP 주소 한 번만 생성

            while (true) {
                String sMsg = sc.nextLine().trim();
                byte[] messageBytes = sMsg.getBytes();
                DatagramPacket dp = new DatagramPacket(messageBytes, messageBytes.length, ia, 12000);
                ds.send(dp);

                if (sMsg.equals(".")) break;
            }

            System.out.println("Sending message completed!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
