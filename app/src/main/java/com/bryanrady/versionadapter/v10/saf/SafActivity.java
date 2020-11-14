package com.bryanrady.versionadapter.v10.saf;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bryanrady.versionadapter.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SafActivity extends AppCompatActivity {

    private Button btn_choose_file;
    private Button btn_create_file;

    private static final int REQUEST_CHOOSE_FILE = 101;
    private static final int REQUEST_CREATE_FILE = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v10_saf);

        btn_choose_file = findViewById(R.id.btn_choose_file);
        btn_create_file = findViewById(R.id.btn_create_file);
        
        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });
        btn_create_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFile();
            }
        });
    }

    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        // 文件名称
        intent.putExtra(Intent.EXTRA_TITLE, System.currentTimeMillis() + ".txt");
        startActivityForResult(intent, REQUEST_CREATE_FILE);
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CHOOSE_FILE);
    }

    private Bitmap getBitmapFromUri(Uri uri){
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null){
                //如果是拍照的话，注意图片旋转，后置摄像头
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                pfd.close();
                return bitmap;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readTextFromUri(Uri uri){
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            if (is != null){
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * https://blog.csdn.net/qq_17766199/article/details/104199446
     * @param uri
     */
    private void modifyFile(Uri uri){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CHOOSE_FILE:
                    if (data != null && data.getData() != null){
                        Uri uri = data.getData();
                        Log.d("wangqingbin","uri == " + uri);

                        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null){
                            cursor.moveToFirst();
                            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                            String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                            Log.d("wangqingbin","id == " + id + ", displayName == " + displayName + ", size == " + size);
                            cursor.close();
                        }
                    }
                    break;
                case REQUEST_CREATE_FILE:
                    if (data != null && data.getData() != null){
                        Uri uri = data.getData();
                        Log.d("wangqingbin","uri22 == " + uri);
                    }
                    break;
            }
        }
    }
}
