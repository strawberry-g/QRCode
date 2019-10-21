package com.example.mylistview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mylistview.R;

import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {
    private Context context;
    private Bitmap bitmap;
    private String content;

    public LinearAdapter(Context context,Bitmap bitmap,String content) {
        this.context = context;
        this.bitmap = bitmap;
        this.content = content;
    }

    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recyleview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, final int position) {
        holder.imageView.setImageBitmap(bitmap);
        holder.textView.setText("AAA");
    }

    @Override
    public int getItemCount() {
        return 6;
    }


    class LinearViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public LinearViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
