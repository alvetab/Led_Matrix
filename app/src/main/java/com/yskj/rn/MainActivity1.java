package com.yskj.rn;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appled.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class MainActivity1 extends AppCompatActivity {
    public final static byte[] CmdGetInfo = {-86, 85, -1, -1, 6, 0, 1, 0, 65, 3, 10, 0};
    String data = null;
       static {
        System.loadLibrary("ucrop");
        System.loadLibrary("iconv");
        System.loadLibrary("SDL2");
        System.loadLibrary("SDL2_ttf");
        System.loadLibrary("ys_parse");
    }
    public static native boolean cropCImg(String str, String str2, int i, int i2, int i3, int i4, float f, float f2, int i5, int i6, int i7, int i8) throws IOException, OutOfMemoryError;

    public static native byte[] nativeParseBin(byte[] bArr, int i);
    public static String parseBinToJson(byte[] bin, int len) {
        if (bin == null || bin.length <= 0) {
            return null;
        }
        //Log.e("yskj", "bin->json1: " + ConvertUtil.bytesToHexString(bin, 0, len));
       String m = new String(nativeParseBin(bin, len));
        //Log.d("yskj", "bin->json2: " + m);
        return m;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // SoLoader.init(this, /* native exopackage */ false);
        // String data = parseBinToJson(CmdGetInfo, CmdGetInfo.length) + "";
        //DGClient dgclient = new DGClient();


        //try {
        String data = YsParserLib.parseBinToJson(CmdGetInfo, CmdGetInfo.length) + "";
        boolean m1 = false;
        /*try {
            m1 = cropCImg("", "", 1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(m1);*/
        //DGClient.DGClient();
       /* } catch (SocketException e) {
            e.printStackTrace();
        }*/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
