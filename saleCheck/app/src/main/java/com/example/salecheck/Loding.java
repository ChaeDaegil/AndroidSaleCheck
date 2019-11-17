package com.example.salecheck;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class Loding extends AppCompatActivity {
    @SuppressLint("WrongViewCast")
    Context context =this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        ImageView backgroundimg = (ImageView)findViewById(R.id.backgroundimg);
        ImageView loding = (ImageView)findViewById(R.id.lodingimage);

        backgroundimg.setImageResource(R.drawable.backimg);


        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(loding);
        Glide.with(this).load(R.drawable.loading5).into(loding);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(context ,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
