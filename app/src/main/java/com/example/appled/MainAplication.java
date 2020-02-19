package com.example.appled;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MainAplication {

    public final static byte[] CmdGetInfo = {-86, 85, -1, -1, 6, 0, 1, 0, 65, 3, 10, 0};
    public String jsonstring = "del_all,1";
    public Integer index = -1;
    public static void MainAplication() {

        try {
            InetAddress ip = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 43, (byte) 1});
            //byte[] ipAddr = new byte[] { 192, 168, 43, 1 };

            //byte[] data = {0x1, 0x2};

            //InetAddress addr = InetAddress.get(ipAddr);
            int port = 9090;
            String jsonstring = "del_all,1";
            byte[] jsonbyte = jsonstring.getBytes();
            DatagramPacket p = new DatagramPacket(CmdGetInfo, CmdGetInfo.length,ip, port);
            //DatagramPacket p = new DatagramPacket(jsonbyte, jsonbyte.length, ip, port);
            //DatagramPacket request = new DatagramPacket(data, data.length, ip, port);
            DatagramSocket socket = new DatagramSocket();
            socket.send(p);
            if (ip.isReachable(5000)){
                System.out.println("Host is reachable");}
                        else {

                System.out.println("Sorry ! We can't reach to this host");}


                //DatagramPacket p = new DatagramPacket(programData2, offset + 2, lenVal);
               //p.setAddress(ip);
              // p.setPort(port);
               //socket.send(p);
                byte[] buf = new byte[2048];
                DatagramPacket datagramPacket = new DatagramPacket(buf, 2048);
                socket.receive(datagramPacket);
                int recvLength = datagramPacket.getLength();
                if (recvLength >= 4) {
                    String str = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    if (str.contains("ack") && str.contains("dev_info")) {

                    }

                    System.out.println(str);
                    System.out.println("si");
                    socket.close();

                }
            socket.close();
                System.out.println(datagramPacket.toString());
            System.out.println("ho");


                //DatagramSocket socket1 = new DatagramSocket();
                //socket.send(cmd);
                //socket.receive(cmd);



            //sendLedProgram.registerListener(this);


        } catch (SocketException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


    }
        //YsUdpLib.fromContext(null).scanDevices();
        //YsUdpLib.fromContext(null).sendLedProgram("del_all,1",-1);
    }



