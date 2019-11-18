package com.example.salecheck;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>

        implements OnPersonItemClickListener {
    Bitmap bitmap;
    LinkedList<ItemList> items = new LinkedList<ItemList>();

    OnPersonItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_layout, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ItemList item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {//아이템의 사이즈를 리턴한다
        return items.size();
    }

    public void addItem(ItemList item) {//아이템을 추가
        items.add(item);

    }
    public void removeitem(int index) {//아이템을 삭제
        items.remove(index);

    }
    public void setItems(LinkedList<ItemList> items) {//
        this.items = items;
    }

    public ItemList getItem(int position) {
        return items.get(position);
    }

    public int getPosition(ItemList item) {
        items.indexOf(item);
        return items.indexOf(item);
    }

    public void setItem(int position, ItemList item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnPersonItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemname;
        TextView itemMoney;
        ImageView itemImage;

        public ViewHolder(View itemView, final OnPersonItemClickListener listener) {
            super(itemView);


            itemname = itemView.findViewById(R.id.itemName);
            itemMoney = itemView.findViewById(R.id.nowMoney);
            itemImage = itemView.findViewById(R.id.ItemimageView);



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                    return false;
                }
            });
        }

        public Bitmap rebit(){
            bitmap;
            return bitmap;
        }
        public void setItem(final ItemList item){
                itemname.setText(item.itemName);
                itemMoney.setText(item.getMoney());
            Thread mThread = new Thread() {

                @Override
                public void run() {

                    try {
                        URL url = new URL(item.picture); // URL 주소를 이용해서 URL 객체 생성

                        //  아래 코드는 웹에서 이미지를 가져온 뒤
                        //  이미지 뷰에 지정할 Bitmap을 생성하는 과정

                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);

                    } catch(IOException ex) {

                    }
                }
            };

            mThread.start(); // 웹에서 이미지를 가져오는 작업 스레드 실행.
            try {
                mThread.join();
                itemImage.setImageBitmap(bitmap);
            } catch (InterruptedException e) {

            }

        }

    }

}
