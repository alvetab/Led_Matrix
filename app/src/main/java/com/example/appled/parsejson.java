package com.example.appled;

public class parsejson {

    public static native byte[] nativeParseBin(byte[] bArr, int i);
    static {
        //System.load("C:/Users/hector/IdeaProjects/datagram/lib/libSDL2.so");
        //System.load("C:/Users/hector/IdeaProjects/datagram/lib/libSDL2_ttf.so");
        System.load("C:/Users/hector/IdeaProjects/datagram/lib/libiconv.so");
        //System.load("C:/Users/hector/IdeaProjects/datagram/lib/libys_parse.so");
    }

    public static String parseBinToJson(byte[] bin, int len) {
        if (bin == null || bin.length <= 0) {
            return null;
        }
        //Log.e("yskj", "bin->json1: " + ConvertUtil.bytesToHexString(bin, 0, len));
        String m = new String(nativeParseBin(bin, len));
        //Log.d("yskj", "bin->json2: " + m);
        return m;
    }
}
