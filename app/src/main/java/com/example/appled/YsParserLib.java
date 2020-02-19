package com.example.appled;//import com.yskj.led.MainApplication;
import android.content.Context;


import com.facebook.soloader.SoLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
//import p012tg.ztb.tggifdecoder.GifDecoder;

/* renamed from: com.yskj.rn.YsParserLib */
public class YsParserLib {
    public static native byte[] nativeParseBin(byte[] bArr, int i);

    public static native byte[] nativeParseJson(byte[] bArr, int i, int i2);

    public static native byte[] nativeZlibCompress(byte[] bArr, int i);

    static {

        SoLoader.loadLibrary("iconv");
        SoLoader.loadLibrary("SDL2");
        SoLoader.loadLibrary("SDL2_ttf");
        SoLoader.loadLibrary("ys_parse");
        //SoLoader.loadLibrary("fb");
        SoLoader.loadLibrary("folly_json");
        //SoLoader.loadLibrary("gifimage");
        SoLoader.loadLibrary("glog");
        SoLoader.loadLibrary("glog_init");
        SoLoader.loadLibrary("gnustl_shared");
        SoLoader.loadLibrary("icu_common");
        //SoLoader.loadLibrary("imagepipeline");
        SoLoader.loadLibrary("jsc");
        //SoLoader.loadLibrary("reactnativejni");
        SoLoader.loadLibrary("realmreact");
        SoLoader.loadLibrary("simpleconfiglib");
        SoLoader.loadLibrary("gnustl_shared");
        SoLoader.loadLibrary("icu_common");
        SoLoader.loadLibrary("ucrop");
        //SoLoader.loadLibrary("yoga");
        
        /*
        ReLinker.loadLibrary(context,"iconv");
        ReLinker.loadLibrary(context,"SDL2");
        ReLinker.loadLibrary(context,"SDL2_ttf");
        ReLinker.loadLibrary(context,"ys_parse");*/
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x0108 A[SYNTHETIC, Splitter:B:52:0x0108] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x010d A[Catch:{ IOException -> 0x0111 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */

    public static void copyAssetFilesToData(/*android.content.Context r21, */String r22, String[] r23, String[] r24) {


        throw new UnsupportedOperationException("Method not decompiled: com.yskj.p005rn.YsParserLib.copyAssetFilesToData(android.content.Context, java.lang.String, java.lang.String[], java.lang.String[]):void");
    }

    /*public static void copyAssetsFolder(Context context, String assetsPath, String savePath) {
        try {
            String[] fileNames = context.getAssets().list(assetsPath);
            if (fileNames.length > 0) {
                new File(savePath).mkdirs();
                for (String fileName : fileNames) {
                    copyAssetsFolder(context, assetsPath + "/" + fileName, savePath + "/" + fileName);
                }
                return;
            }
            InputStream is = context.getAssets().open(assetsPath);
            FileOutputStream fos = new FileOutputStream(new File(savePath));
            byte[] buffer = new byte[409600];
            while (true) {
                int byteCount = is.read(buffer);
                if (byteCount != -1) {
                    fos.write(buffer, 0, byteCount);
                } else {
                    fos.flush();
                    is.close();
                    fos.close();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void initLib(/*Context context*/) {
        copyAssetFilesToData(/*context.getApplicationContext(), */"led_screen/font", new String[]{"fangsong.ttf",
        "heiti.ttf", "hupo.ttf", "kaiti.ttf", "lishu.ttf", "songti.ttc", "yahei.ttf", "youyuan.ttf"}, new String[]{".ttf", ".ttf", ".ttf", ".ttf", ".ttf", ".ttc", ".ttf", ".ttf"});
        //String path = "/data/data/" + context.getPackageName() + "/bgs";
       // String str = "/data/data/" + context.getPackageName() + "/led_sucai";
        //copyAssetsFolder(context.getApplicationContext(), "bgs", path);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public static byte[] hexStr2Bytes(String src) {
        String src2 = src.trim().replace(" ", "").toUpperCase(Locale.US);
        int iLen = src2.length() / 2;
        byte[] ret = new byte[iLen];
        for (int i = 0; i < iLen; i++) {
            int m = (i * 2) + 1;
            ret[i] = (byte) (Integer.decode("0x" + src2.substring(i * 2, m) + src2.substring(m, m + 1)).intValue() & 255);
        }
        return ret;
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

    /*public static HashMap nativeParseGif(byte[] gifPath, int dstW, int dsH, boolean tiled) {
        Toast.makeText(MainApplication.getInstance(), C0856R.string.dealing, 1).show();
        HashMap ret = null;
        GifDecoder decoder = new GifDecoder();
        try {
            String str = new String(gifPath, "utf-8");
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            decoder.read(fileInputStream, 0);
            int frameCount = decoder.getFrameCount();
            int i = 0;
            int byteSize = 0;
            String delays = "";
            ByteArrayOutputStream allBytes = new ByteArrayOutputStream();
            while (true) {
                Bitmap bm = decoder.getNextFrame();
                if (bm == null) {
                    break;
                }
                delays = delays + (decoder.getNextDelay() / 10) + "#";
                Bitmap nnew = RNNativeApi.scaleBitmap(bm, dstW, dsH, tiled);
                if (nnew != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    nnew.compress(CompressFormat.PNG, 100, baos);
                    byte[] bmpBytes = new BmpConverter().convert(baos.toByteArray());
                    if (bmpBytes != null) {
                        allBytes.write(bmpBytes);
                        byteSize += bmpBytes.length;
                    }
                }
                nnew.recycle();
                i++;
            }
            if (delays.length() > 1 && byteSize > 1) {
                String base64 = Base64.encodeToString(nativeZlibCompress(allBytes.toByteArray(), allBytes.size()), 2);
                HashMap ret2 = new HashMap();
                try {
                    ret2.put("delay_frame", delays);
                    ret2.put("base64", base64);
                    ret = ret2;
                } catch (Exception e) {
                    e = e;
                    ret = ret2;
                    e.printStackTrace();
                    decoder.release();
                    return ret;
                }
            }
        } catch (Exception e2) {
            Exception e = e2;
            e.printStackTrace();
            decoder.release();
            return ret;
        }
        decoder.release();
        return ret;
    }*/
}
