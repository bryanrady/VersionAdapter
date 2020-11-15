package com.bryanrady.versionadapter.v10.saf;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bryanrady.versionadapter.R;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class SafActivity extends AppCompatActivity {

    private Button btn_choose_file;
    private Button btn_create_file;
    private Button btn_delete_file;
    private Button btn_modify_file1;
    private Button btn_modify_file2;
    private Button btn_read_file1;
    private Button btn_read_file2;

    private static final int REQUEST_CHOOSE_FILE = 101;
    private static final int REQUEST_CREATE_FILE = 102;

    private Uri mOperateUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v10_saf);

        btn_choose_file = findViewById(R.id.btn_choose_file);
        btn_create_file = findViewById(R.id.btn_create_file);
        btn_delete_file = findViewById(R.id.btn_delete_file);
        btn_modify_file1 = findViewById(R.id.btn_modify_file1);
        btn_modify_file2 = findViewById(R.id.btn_modify_file2);
        btn_read_file1 = findViewById(R.id.btn_read_file1);
        btn_read_file2 = findViewById(R.id.btn_read_file2);
        
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
        btn_delete_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOperateUri != null){
                    deleteFile(mOperateUri);
                }
            }
        });
        btn_modify_file1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOperateUri != null){
                    modifyFile(mOperateUri);
                }
            }
        });
        btn_modify_file2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOperateUri != null){
                    modifyFile2(mOperateUri);
                }
            }
        });
        btn_read_file1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOperateUri != null){
                    readTextFromUri(mOperateUri);
                }
            }
        });
        btn_read_file2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOperateUri != null){
                    readBitmapFromUri(mOperateUri);
                }
            }
        });
    }

    private void chooseFile() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, REQUEST_CHOOSE_FILE);
        }
    }

    private void createFile() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            // 文件名称
            intent.putExtra(Intent.EXTRA_TITLE, System.currentTimeMillis() + ".txt");
            startActivityForResult(intent, REQUEST_CREATE_FILE);
        }
    }

    private void deleteFile(Uri uri){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                boolean deleteDocument = DocumentsContract.deleteDocument(getContentResolver(), uri);
                if(deleteDocument){
                    Toast.makeText(this, "文件删除成功！", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "找不到要删除的文件！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void modifyFile(Uri uri){
        OutputStream os = null;
        try {
            os = getContentResolver().openOutputStream(uri);
            if (os != null){
                String text = "test storage access framework modify file!";
                os.write(text.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            Toast.makeText(this, "修改文件失败！", Toast.LENGTH_SHORT).show();
        } finally {
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void modifyFile2(Uri uri){
        ParcelFileDescriptor pfd = null;
        FileOutputStream fos = null;
        try {
            pfd = getContentResolver().openFileDescriptor(uri, "w");
            if (pfd != null){
                fos = new FileOutputStream(pfd.getFileDescriptor());
                String text = "test storage access framework modify file!";
                fos.write(text.getBytes("UTF-8"));
            }
        } catch (Exception e) {
            Toast.makeText(this, "修改文件失败！", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (pfd != null){
                try {
                    pfd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap readBitmapFromUri(Uri uri){
        ParcelFileDescriptor pfd = null;
        try {
            pfd = getContentResolver().openFileDescriptor(uri, "r");
            if (pfd != null){
                return BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
            }
        } catch (Exception e) {
            Toast.makeText(this, "通过uri获取bitmap失败！", Toast.LENGTH_SHORT).show();
        } finally {
            if (pfd != null){
                try {
                    pfd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        } catch (Exception e) {
            Toast.makeText(this, "读取文件失败！", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CHOOSE_FILE:
                    if (data != null){
                        Uri uri = data.getData();
                        Log.d("wangqingbin","uri == " + uri);

                        mOperateUri= uri;
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
