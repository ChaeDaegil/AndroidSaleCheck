package com.example.salecheck;

import android.provider.BaseColumns;

public final class MemoContract {
        public MemoContract() {}
        public static abstract class MemoEntry implements BaseColumns {

            public static final String TABLE_NAME = "itemList";

            public static final String COLUMN_TITLE = "itemName";

            public static final String COLUMN_URL = "itemURL";

            public static final String nowMoney = "nMoney";

            public static final String changeMoney = "chMoney";

            public static final String PICTURE_URL = "picture";

        }

    }