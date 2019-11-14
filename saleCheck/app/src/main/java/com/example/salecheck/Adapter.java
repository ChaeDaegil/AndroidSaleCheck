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
        Bitmap bitmap = getBitmap(item.picture);
        System.out.println(item.picture);
        System.out.println(bitmap);
        viewHolder.setItem(item,bitmap);
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

    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        Bitmap retBitmap = null;
        try{
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true); //url로 input받는 flag 허용
            connection.connect(); //연결
            is = connection.getInputStream();
            retBitmap = BitmapFactory.decodeStream(is);
        }catch(Exception e) {
            e.printStackTrace();
            return null; }
        finally {
            if(connection!=null) {
                connection.disconnect();
            }
            return retBitmap;
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


        public void setItem(ItemList item,Bitmap bitmap){
                System.out.println(item.itemName+" "+itemMoney+" "+item.picture);
                itemname.setText(item.itemName);
                itemMoney.setText(item.getMoney());
                //itemImage.setImageBitmap(bitmap);

        }

    }

}
