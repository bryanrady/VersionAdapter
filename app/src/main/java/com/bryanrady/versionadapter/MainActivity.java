package com.bryanrady.versionadapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bryanrady.versionadapter.v10.CompatV10Activity;
import com.bryanrady.versionadapter.v10.scope.ScopeStorageActivity;
import com.bryanrady.versionadapter.v11.CompatV11Activity;

public class MainActivity extends AppCompatActivity {

    private Button btn_compat_v7;
    private Button btn_compat_v10;
    private Button btn_compat_v11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_compat_v7 = findViewById(R.id.btn_compat_v7);
        btn_compat_v10 = findViewById(R.id.btn_compat_v10);
        btn_compat_v11 = findViewById(R.id.btn_compat_v11);

        checkPermission();

        btn_compat_v7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btn_compat_v10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompatV10Activity.class);
                startActivity(intent);
            }
        });
        btn_compat_v11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompatV11Activity.class);
                startActivity(intent);
            }
        });

//        EnvironmentUtil.getDir();
//        ContextUtil.getDir(this);

    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    }, 1);
        }
    }

}