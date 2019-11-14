package com.example.salecheck;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDbHelper extends SQLiteOpenHelper {

    // 싱글턴 방식
    // 하나의 인스턴스만 가져도 된다.
    // singleton
    // have one instance
    public static MemoDbHelper sInstance;

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Memo.db";
    public static final String SQL_CREATE_ENTERS =
            String.format(
                    "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                    MemoContract.MemoEntry.TABLE_NAME,
                    MemoContract.MemoEntry._ID,
                    MemoContract.MemoEntry.COLUMN_TITLE,
                    MemoContract.MemoEntry.COLUMN_URL,
                    MemoContract.MemoEntry.nowMoney,
                    MemoContract.MemoEntry.changeMoney,
                    MemoContract.MemoEntry.PICTURE_URL);

    public static final String SQL_DELETE_ENTERS =
            "DROP TABLE IF EXISTS " + MemoContract.MemoEntry.TABLE_NAME;

    public static MemoDbHelper getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MemoDbHelper(context);
        }
        return sInstance;
    }

    public MemoDbHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    // 최초의 DB 생성 부분
    // First DB generation part
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>1){
            db.execSQL(SQL_DELETE_ENTERS);
            onCreate(db);
        }
    }


}