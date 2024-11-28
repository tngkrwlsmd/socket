import java.io.*;
import java.net.*;

public class ReceiveDatagram {
    public static void main(String[] args) {
        try (DatagramSocket ds = new DatagramSocket(12000)) {
            System.out.println("Waiting for Datagrams...");

            while (true) {
                byte[] data = new byte[1000];
                DatagramPacket dp = new DatagramPacket(data, data.length);
                ds.receive(dp);

                String rMsg = new String(dp.getData(), 0, dp.getLength()).trim();
                System.out.println(dp.getAddress().getHostAddress() + " : " + rMsg);

                if (rMsg.equals(".")) {
                    break;
                }
            }

            System.out.println("Receiving Datagrams completed!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
