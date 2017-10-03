/*  Program 'txt4tts_RU'. Text for TTS formating for RU language.
    Copyright (C) 2017 Yuri Stepanenko stepanenkoyra@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this program. If not, see http://www.gnu.org/licenses/
*/


package yras.txt4tts_ru;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import yras.txt4tts_ru.common.Dict_File_TYPE_enum;
import yras.txt4tts_ru.common.TXT_to_XML;
import yras.txt4tts_ru.common.XML_Formatter;
import yras.txt4tts_ru.common.XML_SAX_Paragraph_to_Sentence_split;
import yras.txt4tts_ru.common.XML_SAX_Sentences_and_RegEXP;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final String FOLDER_NAME = "txt4tts_RU";
    private Context context;
    private Button Button_OpenFile;
    private Button Button_Unpack;
    private Button Button_Exec;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        context = this;
        
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        
        
        
        


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Log.d("4", "TXT_to_XML :    permissionCheck != PackageManager.PERMISSION_GRANTED");
            //  ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            //  readDataExternal();
        }


        Button_Unpack = (Button) findViewById(R.id.Button_Unpack);
        Button_Unpack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d("4", "... ok4");
               // TextView_1.setText("UNPACK");

                AssetManager assetManadger = getAssets();
                String AppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FOLDER_NAME + File.separator;
                CopyFromAssets.copyAssetFolder(assetManadger, "dic", AppPath + "dic");
                CopyFromAssets.copyAssetFolder(assetManadger, "tests", AppPath + "tests");
            }
        });


        Button_OpenFile = (Button) findViewById(R.id.Button_OpenFile);
        Button_OpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                OpenFileDialog fileDialog = new OpenFileDialog(context).setFilter(".*\\.txt");
                fileDialog.show();
*/

                OpenFileDialog fileDialog = new OpenFileDialog(context).setFilter(".*\\.txt").setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String fileName) {
                        Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
                        //TextView_1.setText(fileName);
                    }
                });
                fileDialog.show();
            }
        });


        Button_Exec = (Button) findViewById(R.id.Button_Exec);
        Button_Exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  TextView_1.setText("TXT_TO_XML");

                AssetManager assetManadger = getAssets();
                String AppPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + FOLDER_NAME + File.separator;

                TXT_to_XML txt2xml = new TXT_to_XML();

                Log.d("4", "TXT_to_XML <-----1------");
                txt2xml.convert(AppPath + "tests/test1/test1.txt", AppPath + "tests/test1/res.xml");
                Log.d("4", "TXT_to_XML <-----2------");
                XML_Formatter.Format_simple(AppPath + "tests/test1/res.xml", AppPath + "tests/test1/res_p.xml");
                Log.d("4", "TXT_to_XML <-----3------");

                XML_SAX_Paragraph_to_Sentence_split p2s = new XML_SAX_Paragraph_to_Sentence_split();
                p2s.split(AppPath, AppPath + "tests/test1/res_p.xml", AppPath + "tests/test1/resp.xml");

                XML_Formatter.Format_simple(AppPath + "tests/test1/resp.xml", AppPath + "tests/test1/resp_s.xml");


                XML_SAX_Sentences_and_RegEXP p2s_Rex = new XML_SAX_Sentences_and_RegEXP();

                // p2s_Rex.find_and_replace(AppPath + "tests/test1/resp_s.xml", AppPath + "tests/test1/resp_s1.xml", AppPath + "dic/vse_vsyo.rex", Dict_File_TYPE_enum.REX);
                // XML_Formatter.Format_simple(AppPath + "tests/test1/resp_s1.xml", AppPath + "tests/test1/resp_s2.xml");


                p2s_Rex.find_and_replace(AppPath + "tests/test1/resp_s.xml", AppPath + "tests/test1/resp_s3.xml", AppPath + "dic/chisla.rex", Dict_File_TYPE_enum.REX);
                XML_Formatter.Format_simple(AppPath + "tests/test1/resp_s3.xml", AppPath + "tests/test1/resp_s4.xml");

            }
        });
        
            
        
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createFolder();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

        }
    }

    public static File createFolder() {
        File root = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
        if (!root.isDirectory()) {
            if (root.mkdirs()) {
               // Log.d(TAG, "mkdir success!");
            } else {
               // Log.d(TAG, "mkdir fail!");
            }
        }
        return root;
    }

    private static boolean copyAssetFolder(AssetManager assetManager,
                                           String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            Log.d("tag", "TXT_to_XML  1");
            new File(toPath).mkdirs();
            Log.d("tag", "TXT_to_XML  2");
            boolean res = true;
            for (String file : files) {
                Log.d("tag", "TXT_to_XML  file: " + file);
                if (file.contains("."))
                    res &= copyAsset(assetManager,
                            fromAssetPath + File.separator + file,
                            toPath + File.separator + file);
                else
                    res &= copyAssetFolder(assetManager,
                            fromAssetPath + File.separator + file,
                            toPath + File.separator + file);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tag", "TXT_to_XML Failed to copyAssetFolder", e);

            return false;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
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
