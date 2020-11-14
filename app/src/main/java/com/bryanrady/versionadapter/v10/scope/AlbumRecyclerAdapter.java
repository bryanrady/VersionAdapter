package com.bryanrady.versionadapter.v10.scope;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bryanrady.versionadapter.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AlbumRecyclerAdapter extends RecyclerView.Adapter<AlbumRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Uri> mImageUriList;
    private final int mImageSize;

    public AlbumRecyclerAdapter(Context context, List<Uri> imageUriList, int imageSize){
        this.mContext = context;
        this.mImageUriList = imageUriList;
        this.mImageSize = imageSize;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_album_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv.getLayoutParams().width = mImageSize;
        holder.iv.getLayoutParams().height = mImageSize + 30;
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.bg_album_loading)
                .override(mImageSize, mImageSize + 30);
        Glide.with(mContext).load(mImageUriList.get(position)).apply(options).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return mImageUriList != null ? mImageUriList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }

}

