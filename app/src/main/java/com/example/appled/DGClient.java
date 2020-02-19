package com.example.appled;

import android.os.StrictMode;

import com.yskj.rn.YsParserLib;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DGClient {

    public final static byte[] CmdGetInfo = {-86, 85, -1, -1, 6, 0, 1, 0, 65, 3, 10, 0};
    public String jsonstring = "del_all,1";
    public Integer index = -1;

    public static void DGClient() throws SocketException{
        DatagramSocket socket = new DatagramSocket();
        System.out.println("Client start");
        String data = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            byte[] message = null;
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

            socket.send(p);
                //DatagramPacket p = new DatagramPacket(programData2, offset + 2, lenVal);
               //p.setAddress(ip);
              // p.setPort(port);
               //socket.send(p);
                byte[] buf = new byte[2048];
                p = new DatagramPacket(buf, buf.length,ip, port);
                socket.receive(p);
            //byte[] buf = new byte[2048];
            //DatagramPacket datagramPacket = new DatagramPacket(buf, 2048);
            //socket.receive(datagramPacket);
            int recvLength = p.getLength();
            if (recvLength >= 4) {
                /*String str = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(str);
                System.out.println("si");
                socket.close();
                */

                message = new byte[recvLength];
                System.arraycopy(buf, 0, message, 0, recvLength);
            }
               data = YsParserLib.parseBinToJson(message, recvLength) + "";
            if (data.contains("ack") && data.contains("dev_info")) {
                //YsUdpLib.this.mDeviceAddress = datagramPacket.getAddress();
                //YsUdpLib.this.setupSocketIfNeed(true);
                //break;
                System.out.println(data);
            }


                //System.out.println(str);
                System.out.println("si");
                socket.close();
           //     System.out.println(new String(p.getData()));
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



