package com.example.salecheck;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private WebView mWebView;

    Context context;
    Elements content=null;
    String nowMoney="";
    String itemName ="";
    String picture= "";
    Intent intent;
    Intent getIntentUrl=null;//수신 받기위한 intent
    Intent foregroundServiceIntent;//서비스 시작을 위한 intent
    MemoDbHelper memoDbHelper;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // 웹뷰 셋팅
        getIntentUrl = getIntent();

        if(getIntentUrl.getExtras()!=null){
            mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
            mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
            String url = getIntentUrl.getExtras().getString("Url");
            mWebView.loadUrl(url);//웹뷰 실행
            mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
            mWebView.getSettings().setDomStorageEnabled(true);
        }else{
            mWebView = (WebView) findViewById(R.id.webView);//xml 자바코드 연결
            mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
            mWebView.loadUrl("https://chaedaegil.github.io/github.io/danawaTest.html");//웹뷰 실행
            //https://chaedaegil.github.io/github.io/danawaTest.html
            mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
            mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
            mWebView.getSettings().setDomStorageEnabled(true);

            foregroundServiceIntent = new Intent(this, MyService.class);
            startService(foregroundServiceIntent);
        }

        memoDbHelper = new MemoDbHelper(this);
        SQLiteDatabase db = memoDbHelper.getWritableDatabase();


    }
/******
    public void salCheck(){
        timertask = new TimerTask()
        {
            @Override
            public void run()
            {
                Document doc = null;
                try {
                    SQLiteDatabase db = MemoDbHelper.getsInstance(context).getWritableDatabase();
                    String sql = "select * from "+ MemoContract.MemoEntry.TABLE_NAME;//테이블을 검색하는 문자열을 저장
                    resultset = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
                    int count = resultset.getCount();   // db에 저장된 행 개수를 읽어온다

                    for (int i = 0; i < count; i++) {
                        resultset.moveToNext();// 첫번째에서 다음 레코드가 없을때까지 읽음
                        String str_name = resultset.getString(0); //이름
                        String str_url = resultset.getString(2); // 주소

                        String mone1="";
                        String mone2="";
                        mone1 = arryhab(resultset.getString(3).trim().split(","),mone1);
                        mone2 = arryhab(resultset.getString(4).trim().split(","),mone2);
                        int str_Money = Integer.parseInt(mone1);// 설정 가격
                        int str_chMoney = Integer.parseInt(mone2); // 지금 가격
                        doc = Jsoup.connect(str_url).get();//주소를 참소하여 html 구문분서
                        content = doc.select("span.txt_mall_price01");//구문정보에서 최저가 정보를 검색 저장
                        changeMoney = content.text().substring(0,content.text().length()-1);//원 부분을 제거
                        nowMoney = content.text().substring(0,content.text().length()-1);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MemoContract.MemoEntry.changeMoney, changeMoney);
                        System.out.println("새로운값 받아오는중 " + str_name + " " + nowMoney + " " + changeMoney);
                        if(str_Money > str_chMoney){//설정한 가격보다 현재상품가격이 낮아지면 실행 시킨다

                            Bitmap mLargeIconForNoti = BitmapFactory.decodeResource(getResources(),R.drawable.image0);
                            PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this,0,
                                    new Intent(getApplicationContext(),MainActivity.class),
                                    PendingIntent.FLAG_CANCEL_CURRENT);
                            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
                                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
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
                                    new NotificationCompat.Builder(MainActivity.this,"channel")
                                            .setSmallIcon(R.drawable.image0)
                                            .setContentTitle("세일중!")
                                            .setContentText("빨리빨리 사세요!")
                                            .setDefaults(Notification.DEFAULT_VIBRATE)
                                            .setLargeIcon(mLargeIconForNoti)
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            .setAutoCancel(true)
                                            .setContentIntent(mPendingIntent);

                            NotificationManager mNotificationManager =
                                    (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                            mNotificationManager.notify(0,mBulider.build());

                            String sqla = "_ID = " + str_name;
                            db.delete(MemoContract.MemoEntry.TABLE_NAME, sqla, null);//데이터 베이스에서 삭제후 알림
                        }else {
                            String sqla = "_ID = " + str_name;
                            db.update(MemoContract.MemoEntry.TABLE_NAME, contentValues, sqla, null);//새로검색한 값으로 변경
                        }
                    }
                    db.close();
                    //가격비교하고 푸시알람 추가 필요
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer = new Timer();
        timer.schedule(timertask, 1000, 10000);    // 1초 후에 최초 실행하고, 이후 10초 간격으로 계속 반복해서 실행
    }
******/
    public String arryhab(String arrp[]){
        String arr = "";
        for (int i=0;i<arrp.length;i++){
            arr = arr + arrp[i];
        }
        return arr;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        String nowUrl = mWebView.getUrl();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Document doc = Jsoup.connect(nowUrl).get();
                content = doc.select("title");
                itemName = content.text();//상품이름 저장
                content = doc.select("span.txt_mall_price01");
                if(content.text()!="") {
                    nowMoney = content.text().substring(0, content.text().length() - 1);//현재가격저장
                    nowMoney = arryhab(nowMoney.trim().split(","));//가격을 받아와서 , 을 빼고 값을 저장
                    content = doc.select("#productBlog-image-item-0 img");
                    picture = content.attr("src");
                    System.out.println(picture);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MemoContract.MemoEntry.COLUMN_TITLE, itemName);
                    contentValues.put(MemoContract.MemoEntry.COLUMN_URL, nowUrl);
                    contentValues.put(MemoContract.MemoEntry.nowMoney, nowMoney);
                    contentValues.put(MemoContract.MemoEntry.changeMoney, nowMoney);
                    contentValues.put(MemoContract.MemoEntry.PICTURE_URL, picture);

                    SQLiteDatabase db = MemoDbHelper.getsInstance(context).getWritableDatabase();
                    db.insert(MemoContract.MemoEntry.TABLE_NAME, null, contentValues);
                    db.close();

                }
                nowMoney = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu) ;

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert :
                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                jsoupAsyncTask.execute();
                return false;
            case R.id.action_list :
                intent = new Intent(MainActivity.this ,openList.class);
                startActivity(intent);
                return false;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }
}

