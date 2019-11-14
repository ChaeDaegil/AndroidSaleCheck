package com.example.salecheck;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class openList extends AppCompatActivity {
    RecyclerView recyclerView = null;
    Cursor resultset;
    ItemList item;
    ItemList itemlist;
    Adapter adapter=null;//아답터
    MemoDbHelper memoDbHelper;
    Context context = this;
    int check=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemlistlayout);
        adapter = new Adapter();

        SQLiteDatabase db = MemoDbHelper.getsInstance(this).getWritableDatabase();
        String sql = "select * from "+ MemoContract.MemoEntry.TABLE_NAME;
        resultset = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
        int count = resultset.getCount();   // db에 저장된 행 개수를 읽어온다

        for (int i = 0; i < count; i++) {
            resultset.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
            String str_id = resultset.getString(0);   // 첫번째 속성 아이디
            String str_name = resultset.getString(1);   // 두번째 속성 이름
            String str_url = resultset.getString(2); // 세번째 속성 주소
            String str_nMoney = resultset.getString(3);   // 네번째 속성 설정한 돈
            String str_chMoney = resultset.getString(4); // 다섯번째 속성 현재돈
            String str_picture = resultset.getString(5); // 여섯번째 속성 사진
            itemlist = new ItemList();
            itemlist.itemName = str_name;
            itemlist.position = Integer.parseInt(str_id);
            itemlist.address = str_url;
            itemlist.money = str_nMoney;
            itemlist.chMOney = str_chMoney;
            itemlist.picture = str_picture;
            //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //View view = (LinearLayout) getLayoutInflater().inflate(R.layout.item_layout,null, false);

            recyclerView = findViewById(R.id.recyclerView);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(layoutManager);

            adapter.addItem(itemlist);

            recyclerView.setAdapter(adapter);
        }
        adapter.setOnItemClickListener(new OnPersonItemClickListener() {
            @Override
            public void onItemClick(Adapter.ViewHolder holder, View view, int position) {
                item = adapter.getItem(position);
                check = position;
                //Toast.makeText(getApplicationContext(), "아이템 선택됨 : " + item.getName(), Toast.LENGTH_LONG).show();
                Dialog();
            }
        });
    }
    public void Dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("원하는 가격을 입력해주세요");
        final EditText setMoney = new EditText(openList.this);
        builder.setView(setMoney);
        builder.setNeutralButton("이동",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("Url",item.address); /*송신*/

                        startActivity(intent);
                    }
                });
        builder.setPositiveButton("가격설정",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        memoDbHelper = new MemoDbHelper(context);
                        SQLiteDatabase db = memoDbHelper.getWritableDatabase();

                        ContentValues contentValues = new ContentValues();
                        String value = setMoney.getText().toString();
                        System.out.println(value);
                        contentValues.put(MemoContract.MemoEntry.changeMoney, value);
                        String sqla = "_ID = " + item.position;
                        db.update(MemoContract.MemoEntry.TABLE_NAME, contentValues, sqla, null);//새로검색한 값으로 변경
                        db.close();

                    }
                });
        builder.setNegativeButton("삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        memoDbHelper = new MemoDbHelper(context);
                        SQLiteDatabase db = memoDbHelper.getWritableDatabase();

                        ContentValues contentValues = new ContentValues();
                        String sqla = "_ID = " + item.position;
                        adapter.removeitem(check);
                        db.delete(MemoContract.MemoEntry.TABLE_NAME, sqla, null);//데이터 베이스에서 삭제
                        db.close();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
        builder.show();
    }
}
