package com.example.appled;

//import com.sun.tools.javac.util.Context;

import android.content.Context;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public  class YsUdpLib {
    public static final byte[] CmdGetInfo = {-86, 85, -1, -1, 6, 0, 1, 0, 65, 3, 10, 0};
    static final int MAX_BUFFER_SIZE = 2048;
    static final String MULTICAST_ADDR = "255.255.255.255";
    /* access modifiers changed from: private */
    public static int SNO = -65535;
    static final int SO_READ_TIMEOUT = 5000;
    public static final String TAG = "ReactNativeJS";
    static final int sBindPort = 9001;
    private static YsUdpLib sInstance = null;
    static final int sToPort = 9090;
    //private static ThreadPolicy spolicy = new Builder().permitAll().build();
    public volatile boolean canStop = false;
    public volatile boolean isScanning = false;
    //private Context mContext = null;
    private Thread mCurrThread = null;
    /* access modifiers changed from: private */
    public InetAddress mDeviceAddress = null;
    /* access modifiers changed from: private */
    public YsUdpListener mListener = null;
    /* access modifiers changed from: private */
    public MulticastSocket mSocketBroadcast = null;
    /* access modifiers changed from: private */
    public DatagramSocket mSocketDevice = null;

    /* renamed from: com.yskj.rn.YsUdpLib$YsUdpListener */
    public interface YsUdpListener {
        void onUdpDone(int i, boolean z, String str);
    }
    public static final class YsUdpType {
        public static int SCAN_DEVICE = 1;
        public static int SEND_PROGRAM = 2;
    }
    public void stopUdps() {
        /*if (VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(spolicy);
        }*/
        if (this.mSocketBroadcast != null) {
            this.mSocketBroadcast.disconnect();
        }
        this.canStop = true;
        if (this.mCurrThread != null) {
            try {
                this.mCurrThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mCurrThread = null;
        }
    }
    public static YsUdpLib fromContext(Context c) {
        if (sInstance == null) {
            sInstance = new YsUdpLib();
        }
        return sInstance;
    }
    public void registerListener(YsUdpListener l) {
        this.mListener = l;
    }

    public void unregisterListener() {
        /*if (VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(spolicy);
        }*/
        this.mListener = null;
        stopUdps();
        if (this.mSocketBroadcast != null) {
            this.mSocketBroadcast.close();
        }
    }
    public void setupBroadcast() {
       /* if (Thread.currentThread().getName().toLowerCase().contains("main") && VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(spolicy);
        }*/
        if (this.mSocketBroadcast == null) {
            try {
                InetAddress byName = InetAddress.getByName(MULTICAST_ADDR);
                this.mSocketBroadcast = new MulticastSocket(null);
                this.mSocketBroadcast.setReceiveBufferSize(2048);
                this.mSocketBroadcast.setSoTimeout(5000);
                this.mSocketBroadcast.setReuseAddress(true);
                this.mSocketBroadcast.bind(new InetSocketAddress(sBindPort));
                this.mSocketBroadcast.setBroadcast(true);
                this.mSocketBroadcast.setReuseAddress(true);
                this.mSocketBroadcast.setLoopbackMode(true);
            } catch (Throwable th) {
                this.mSocketBroadcast = null;
            }
        }
    }
    public void scanDevices() {
        stopUdps();
        if (this.isScanning) {
           // Toast.makeText(this.mContext, "", 0).show();
           System.out.println("scanning");
            return;
        }
        this.canStop = false;
        this.isScanning = true;
        this.mCurrThread = new Thread(new Runnable() {
            public void run() {
                String data = null;
                boolean isOk = false;
                YsUdpLib.this.setupBroadcast();
                if (YsUdpLib.this.mSocketBroadcast != null && !YsUdpLib.this.canStop) {
                    try {
                        InetAddress multicast_addr = InetAddress.getByName(YsUdpLib.MULTICAST_ADDR);
                        DatagramPacket p = new DatagramPacket(YsUdpLib.CmdGetInfo, 0,
                                YsUdpLib.CmdGetInfo.length);
                        p.setAddress(multicast_addr);
                        p.setPort(YsUdpLib.sToPort);
                        YsUdpLib.this.mSocketBroadcast.send(p);
                        byte[] message = null;
                        while (true) {
                            if (YsUdpLib.this.canStop) {
                                break;
                            }
                            byte[] buf = new byte[2048];
                            DatagramPacket datagramPacket = new DatagramPacket(buf, 2048);
                            YsUdpLib.this.mSocketBroadcast.receive(datagramPacket);
                            int recvLength = datagramPacket.getLength();
                            if (recvLength >= 4) {
                                message = new byte[recvLength];
                                System.arraycopy(buf, 0, message, 0, recvLength);
                            }
                            data = YsParserLib.parseBinToJson(message, recvLength) + "";
                            if (data.contains("ack") && data.contains("dev_info")) {
                                YsUdpLib.this.mDeviceAddress = datagramPacket.getAddress();
                                YsUdpLib.this.setupSocketIfNeed(true);
                                break;
                            }
                            YsUdpLib.this.mSocketBroadcast.send(p);
                        }
                        isOk = data != null;
                    } catch (Exception e) {
                        System.out.println( "excepcion" + e);
                    }
                }
                YsUdpLib.this.isScanning = false;
                if (YsUdpLib.this.mListener != null && !YsUdpLib.this.canStop) {
                    YsUdpLib.this.mListener.onUdpDone(YsUdpType.SCAN_DEVICE, isOk, data);
                }
            }
        });
        this.mCurrThread.start();
    }
    public void setupSocketIfNeed(boolean recreate) {
        /*if (Thread.currentThread().getName().toLowerCase().contains("main") && VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(spolicy);
        }*/
        if (recreate && this.mSocketDevice != null) {
            this.mSocketDevice.disconnect();
            this.mSocketDevice.close();
            this.mSocketDevice = null;
        }
        if (this.mSocketDevice == null) {
            try {
                this.mSocketDevice = new DatagramSocket(null);
                this.mSocketDevice.setReceiveBufferSize(2048);
                this.mSocketDevice.setSoTimeout(5000);
                this.mSocketDevice.setReuseAddress(true);
                this.mSocketDevice.connect(this.mDeviceAddress, sToPort);
            } catch (Throwable th) {
                this.mSocketDevice = null;
            }
        }
    }
    public void sendLedProgram(final String jsonData, final int index) {


        /* renamed from: com.yskj.rn.YsUdpLib$YsUdpType */


        if (jsonData != null && jsonData.length() > 0) {
          Thread mCurrThread = new Thread(new Runnable() {
                public void run() {
                    YsUdpLib.SNO = YsUdpLib.SNO + 1;
                    String data = null;
                    boolean isOk = false;
                    boolean sentAck = false;
                    //sendLedProgram.this.setupSocketIfNeed(false);
                    if (YsUdpLib.this.mSocketDevice != null && !YsUdpLib.this.canStop) {
                        try {
                            byte[] programData = jsonData.getBytes("utf-8");
                            byte[] programData2 = YsParserLib.nativeParseJson(programData, YsUdpLib.SNO,
                                    programData.length);
                            if (programData2 != null && programData2.length > 3) {
                                //MainApplication.Log("---send[Bytes]-------",
                                //        ConvertUtil.bytesToHexString(programData2, 0, programData2.length));
                                int offset = 0;
                                int lenVal = ByteBuffer.wrap(programData2, 0, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
                                int total = programData2.length;
                                int timeoutCount = 0;
                                while (true) {
                                    if (YsUdpLib.this.canStop) {
                                        break;
                                    } else if (offset + lenVal + 2 <= total) {
                                        try {
                                            DatagramPacket p = new DatagramPacket(programData2, offset + 2, lenVal);
                                            p.setAddress(YsUdpLib.this.mDeviceAddress);
                                            p.setPort(YsUdpLib.sToPort);
                                            YsUdpLib.this.mSocketDevice.send(p);
                                            byte[] buf = new byte[2048];
                                            DatagramPacket datagramPacket = new DatagramPacket(buf, 2048);
                                            YsUdpLib.this.mSocketDevice.receive(datagramPacket);
                                            int recvLength = datagramPacket.getLength();
                                            if (recvLength >= 4) {
                                                //data = YsParserLib.parseBinToJson(buf, recvLength) + "";
                                                //data = YsParserLib.parseBinToJson(buf, recvLength) + "";
                                                if ((data.contains("ack") && data.contains("sno")) || (data.contains("cmd") && data.contains("ok"))) {
                                                    timeoutCount = 0;
                                                    if (index >= 0) {
                                                       /* WritableNativeMap mm = new WritableNativeMap();
                                                        mm.putInt("index", index);
                                                        mm.putInt(NotificationCompat.CATEGORY_PROGRESS, (int) ((((float) offset) * 100.0f) / ((float) total)));
                                                        ((RCTDeviceEventEmitter) cxt.getJSModule(RCTDeviceEventEmitter.class)).emit("adProgress", mm);
                                                    */}
                                                    sentAck = true;
                                                    offset += lenVal + 2;
                                                    if (offset + 2 >= total) {
                                                        isOk = true;
                                                        //MainApplication.Log("---send[ACK]-------", data);
                                                        break;
                                                    }
                                                    lenVal = ByteBuffer.wrap(programData2, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
                                                }
                                            } else {
                                                continue;
                                            }
                                        } catch (Exception e) {
                                           // MainApplication.Log("Udp Resent", "Resent for: " + e);
                                            timeoutCount++;
                                            if (timeoutCount > 3) {
                                                break;
                                            }
                                        }
                                    } else if (sentAck) {
                                        isOk = true;
                                    }
                                }
                            } else if (YsUdpLib.this.mListener != null) {
                                YsUdpLib.this.mListener.onUdpDone(YsUdpType.SEND_PROGRAM, false, "");
                                return;
                            } else {
                                return;
                            }
                        } catch (Exception e2) {
                            //MainApplication.Log("Udp Exception", "run: " + e2);
                        }
                    }
                    YsUdpLib.this.isScanning = false;
                    if (YsUdpLib.this.mListener != null && !YsUdpLib.this.canStop) {
                        YsUdpLib.this.mListener.onUdpDone(YsUdpType.SEND_PROGRAM, isOk, data);
                    }
                }
            });
            this.mCurrThread.start();
        } else if (this.mListener != null) {
            this.mListener.onUdpDone(YsUdpType.SEND_PROGRAM, false, "");
        }

    }
}
