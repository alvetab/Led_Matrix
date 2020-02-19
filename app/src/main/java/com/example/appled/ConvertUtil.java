package com.example.appled;

import java.io.UnsupportedEncodingException;

/* renamed from: com.yskj.rn.ConvertUtil */
public class ConvertUtil {

    /* renamed from: com.yskj.rn.ConvertUtil$USER_OP */
    public enum USER_OP {
        ADD(0),
        DEL(1),
        BLACK(2);
        
        private char value;

        private USER_OP(char value2) {
            this.value = value2;
        }

        USER_OP(int i) {
        }

        public char value() {
            return this.value;
        }
    }

    public static byte[] ip4ToByte4(String ip) {
        byte[] result = new byte[4];
        try {
            String[] ipArr = ip.split("\\.");
            for (int i = 0; i < ipArr.length; i++) {
                result[i] = (byte) (Integer.parseInt(ipArr[i]) & 255);
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static String byte2String(byte b) {
        return Byte.toString(b);
    }

    public static String bytesToHexString(byte[] src, int off, int len) {
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            if (src == null || len <= 0) {
                return null;
            }
            for (int i = off; i < off + len; i++) {
                String hv = Integer.toHexString(src[i] & 255);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] hexStringToBytes(String hexString, int byteLen) {
        if (byteLen <= 0) {
            return null;
        }
        if (hexString == null || "".equals(hexString)) {
            return new byte[byteLen];
        }
        int length = byteLen;
        String hexString2 = hexString.replaceAll(":", "");
        if (hexString2.length() > byteLen * 2) {
            return new byte[byteLen];
        }
        while (hexString2.length() < length * 2) {
            hexString2 = "0" + hexString2;
        }
        char[] hexChars = hexString2.toUpperCase().toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String byte4ToIp4(byte[] ipArr, int offset) {
        if (ipArr == null) {
            return null;
        }
        int len = ipArr.length - offset;
        int pre = (len - 1) + offset;
        if (len < 4) {
            return null;
        }
        String ip = "";
        int i = 0;
        while (i < 4) {
            try {
                String ip2 = ip + Integer.valueOf(ipArr[i + offset] & 255);
                if (i >= pre) {
                    return ip2;
                }
                ip = ip2 + ".";
                i++;
            } catch (Exception e) {
                return null;
            }
        }
        return ip;
    }

    public static byte intToByte(int i) {
        return (byte) (i & 255);
    }

    public static byte[] intToByte2(int i) {
        return new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] intToByte4(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] shortToByte2(short i) {
        return new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static byte[] longToByte8(long l) {
        return new byte[]{(byte) ((int) ((l >> 56) & 255)), (byte) ((int) ((l >> 48) & 255)), (byte) ((int) ((l >> 40) & 255)), (byte) ((int) ((l >> 32) & 255)), (byte) ((int) ((l >> 24) & 255)), (byte) ((int) ((l >> 16) & 255)), (byte) ((int) ((l >> 8) & 255)), (byte) ((int) (l & 255))};
    }

    public static int byte22Int(byte[] b, int off, boolean le) {
        if (b == null) {
            return 0;
        }
        return (65280 & (b[off] << 8)) | (b[off + 1] & 255);
    }

    public static int byte2Int(byte[] b, int off, int len) {
        if (b == null) {
            return 0;
        }
        int result = 0;
        for (int i = off; i < off + len; i++) {
            result = (result << 8) | (b[i] & 255);
        }
        return result;
    }

    public static short byte2Short(byte[] b, int off) {
        if (b == null) {
            return 0;
        }
        short result = 0;
        for (int i = off; i < off + 2; i++) {
            result = (short) ((b[i] & 255) | ((short) (result << 8)));
        }
        return result;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r1v0, types: [byte, int] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r3v0, types: [byte, int] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String byte2UnicodeBE(byte[] r6, int r7, int r8) {
        /*
            java.lang.StringBuffer r4 = new java.lang.StringBuffer
            java.lang.String r5 = ""
            r4.<init>(r5)
            r2 = r7
        L_0x0008:
            int r5 = r8 + r7
            if (r2 >= r5) goto L_0x0026
            byte r3 = r6[r2]
            if (r3 >= 0) goto L_0x0012
            int r3 = r3 + 256
        L_0x0012:
            int r5 = r2 + 1
            byte r1 = r6[r5]
            if (r1 >= 0) goto L_0x001a
            int r1 = r1 + 256
        L_0x001a:
            int r5 = r1 << 8
            int r5 = r5 + r3
            char r0 = (char) r5
            if (r0 == 0) goto L_0x0023
            r4.append(r0)
        L_0x0023:
            int r2 = r2 + 2
            goto L_0x0008
        L_0x0026:
            java.lang.String r5 = r4.toString()
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yskj.p005rn.ConvertUtil.byte2UnicodeBE(byte[], int, int):java.lang.String");
    }

    public static char[] wchar2UnicodeLE(char[] achar, int len) {
        System.out.println("before transform");
        for (int i = 0; i < len; i++) {
            System.out.print(Integer.toHexString(achar[i]) + " ");
        }
        char[] tchar = new char[len];
        for (int j = 0; j < len; j += 2) {
            tchar[j] = achar[j + 1];
            tchar[j + 1] = achar[j];
        }
        System.out.println("\nafter transform");
        for (int i2 = 0; i2 < len; i2++) {
            System.out.print(Integer.toHexString(tchar[i2]) + " ");
        }
        return tchar;
    }

    public static char[] string2UnicodeCharArray(String str, int len) throws UnsupportedEncodingException {
        byte[] srcbytes = str.getBytes("UTF-16LE");
        char[] retchars = new char[len];
        for (int i = 0; i < len; i++) {
            if (i >= srcbytes.length) {
                retchars[i] = 0;
            } else {
                retchars[i] = (char) srcbytes[i];
            }
        }
        return retchars;
    }

    public static byte[] string2UnicodeByteArray(String str, int len) throws UnsupportedEncodingException {
        byte[] srcbytes = str.getBytes("UTF-16LE");
        byte[] retbytes = new byte[len];
        for (int i = 0; i < len; i++) {
            if (i >= srcbytes.length) {
                retbytes[i] = 0;
            } else {
                retbytes[i] = srcbytes[i];
            }
        }
        return retbytes;
    }

    public static long byteArray2long(byte[] bs, int off, int len) {
        if (bs == null) {
            return 0;
        }
        long result = 0;
        for (int i = off; i < off + len; i++) {
            result += byte2long(bs[i], (off + len) - i);
        }
        return result;
    }

    public static long byteArray2long(byte[] bs) {
        if (bs == null) {
            return 0;
        }
        return byteArray2long(bs, 0, bs.length);
    }

    public static long byte2long(byte b, int level) {
        return ((long) (b & 255)) * ((long) Math.pow(16.0d, (double) level));
    }

    public static byte[] subByte(byte[] data, int off, int len) {
        byte[] d = new byte[len];
        for (int j = off; j < len + off; j++) {
            d[j - off] = data[j];
        }
        return d;
    }

    public static byte[] getBooleanArray(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }
}
