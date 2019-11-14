package com.example.salecheck;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    TimerTask timertask;
    Timer timer;
    Context context;
    Cursor resultset;
    Elements content=null;
    String nowMoney="";
    MemoDbHelper memoDbHelper;
    Intent sendInent;
    public MyService(){

    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        Log.e("LOG", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("서비스 시작");
        
            timertask = new TimerTask()
            {
                @Override
                public void run()
                {
                    Document doc = null;
                    try {
                        memoDbHelper = new MemoDbHelper(context);
                        SQLiteDatabase db = memoDbHelper.getWritableDatabase();

                        String sql = "select * from "+ MemoContract.MemoEntry.TABLE_NAME;//테이블을 검색하는 문자열을 저장
                        resultset = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)

                        System.out.println("테이블안의 행의 갯수"+resultset.getCount());
                        if(resultset.getCount()!=0) {
                            for (int i = 0; i < resultset.getCount(); i++) {
                                resultset.moveToNext();// 첫번째에서 다음 레코드가 없을때까지 읽음
                                String str_id = resultset.getString(0); //아이디
                                String str_url = resultset.getString(2); // 주소

                                String mone1 = "";
                                String mone2 = "";
                                mone1 = resultset.getString(3).trim();
                                mone2 = resultset.getString(4).trim();
                                int str_Money = Integer.parseInt(mone1);// 현재 가격
                                int str_chMoney = Integer.parseInt(mone2); // 설정 가격

                                doc = Jsoup.connect(str_url).get();//주소를 참소하여 html 구문분서
                                content = doc.select("span.txt_mall_price01");//구문정보에서 최저가 정보를 검색 저장
                                nowMoney="";
                                nowMoney = arryhab(content.text().substring(0, content.text().length() - 1).trim().split(","),nowMoney);
                                ContentValues contentValues = new ContentValues();

                                System.out.println("새로운값 받아오는중 " + str_id + " " + str_Money + " " + str_chMoney);
                                if (str_Money < str_chMoney) {//설정한 가격보다 현재상품가격이 낮아지면 실행 시킨다

                                    Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.image0);
                                    sendInent = new Intent(getApplicationContext(), openList.class);
                                    sendInent.putExtra("Url",str_url);
                                    PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
                                            sendInent,
                                            PendingIntent.FLAG_CANCEL_CURRENT);
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        NotificationChannel notificationChannel =
                                                new NotificationChannel(
                                                        "channel",
                                                        "알람",
                                                        NotificationManager.IMPORTANCE_DEFAULT
                                                );
                                        notificationChannel.setDescription("알람");
                                        notificationManager.createNotificationChannel(notificationChannel);
                                    }
                                    NotificationCompat.Builder mBulider =
                                            new NotificationCompat.Builder(context, "channel")
                                                    .setSmallIcon(R.drawable.image0)
                                                    .setContentTitle("세일중!")
                                                    .setContentText("빨리빨리 사세요!")
                                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                                    .setLargeIcon(mLargeIconForNoti)
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setAutoCancel(true)
                                                    .setContentIntent(mPendingIntent);

                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    mNotificationManager.notify(0, mBulider.build());

                                    contentValues.put(MemoContract.MemoEntry.nowMoney, nowMoney);
                                    String sqla = "_ID = " + str_id;
                                    db.update(MemoContract.MemoEntry.TABLE_NAME, contentValues, sqla, null);//새로검색한 값으로 변경
                                    contentValues.put(MemoContract.MemoEntry.changeMoney, "0");
                                    db.update(MemoContract.MemoEntry.TABLE_NAME, contentValues,  sqla, null);//데이터 베이스에서 삭제후 알림
                                } else {
                                    contentValues.put(MemoContract.MemoEntry.nowMoney, nowMoney);
                                    String sqla = "_ID = " + str_id;
                                    db.update(MemoContract.MemoEntry.TABLE_NAME, contentValues, sqla, null);//새로검색한 값으로 변경
                                }
                            }
                        }
                        resultset.close();
                        db.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            timer = new Timer();
            timer.schedule(timertask, 1000, 10000);    // 1초 후에 최초 실행하고, 이후 10초 간격으로 계속 반복해서 실행

        return Service.START_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet 어쩌구저쩌구");
    }


    public String arryhab(String arrp[],String arr){
        for (int i=0;i<arrp.length;i++){
            arr = arr + arrp[i];
        }
        return arr;
    }
}