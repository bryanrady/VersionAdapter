package com.bryanrady.versionadapter.v10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bryanrady.versionadapter.R;
import com.bryanrady.versionadapter.v10.saf.SafActivity;
import com.bryanrady.versionadapter.v10.scope.ScopeStorageActivity;

public class CompatV10Activity extends AppCompatActivity {

    private Button btn_scope_storage;
    private Button btn_saf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compat_v10);

        btn_scope_storage = findViewById(R.id.btn_scope_storage);
        btn_saf = findViewById(R.id.btn_saf);

        btn_scope_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompatV10Activity.this, ScopeStorageActivity.class);
                startActivity(intent);
            }
        });

        btn_saf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompatV10Activity.this, SafActivity.class);
                startActivity(intent);
            }
        });

    }


}
