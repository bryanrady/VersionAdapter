package com.bryanrady.versionadapter.v10.scope;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bryanrady.versionadapter.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumRecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private List<Uri> mImageUriList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_album);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                recyclerView.setLayoutManager(new GridLayoutManager(AlbumRecyclerActivity.this, 3));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                AlbumRecyclerAdapter adapter = new AlbumRecyclerAdapter(AlbumRecyclerActivity.this, mImageUriList, recyclerView.getWidth()/3);
                recyclerView.setAdapter(adapter);

                getImagesFromAlbum(adapter);

                return false;
            }
        });

    }

    /**
     * https://blog.csdn.net/guolin_blog/article/details/105419420
     *
     * Android Q 获取相册中的图片   在作用域存储当中获取手机相册里的图片  这种方式在所有的Android系统版本中都适用
     *
     * 不同于过去可以直接获取到相册中图片的绝对路径，在作用域存储当中，我们只能借助MediaStore API获取到图片的Uri
     */
    private void getImagesFromAlbum(AlbumRecyclerAdapter adapter){
        //先通过ContentResolver获取到了相册中所有图片
        //获取每张图片的id，然后借助ContentUris将id拼装成一个完整的Uri对象
        //一张图片的Uri格式大致是：content://media/external/images/media/321
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                        MediaStore.MediaColumns.DATE_ADDED + " desc");
                if (cursor != null){
                    while (cursor.moveToNext()){
                        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
                        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                        Log.e("wangqingbin","id == " + id + ", uri == " + uri);
                        mImageUriList.add(uri);
                    }
                    cursor.close();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //展示图片
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

}
