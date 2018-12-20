package tk.lorddarthart.pushovertestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBPush extends SQLiteOpenHelper implements BaseColumns {
    public static final String DATABASE_NAME = "tk.lorddarthart.pushovertestapp.db";
    public static int DATABASE_VERSION = 1;

    public static final String DATABASE_PUSHHISTORY = "pushhistory";
    public static final String PUSHHISTORY_PUSHID = "pushtitle";
    public static final String PUSHHISTORY_PUSHTITLE = "pushtitle";
    public static final String PUSHHISTORY_PUSHTEXT = "pushtext";
    public static final String PUSHHISTORY_PUSHTIME = "pushtime";
    public static final String PUSHHISTORY_PUSHKEY = "pushkey";

    public static final String DATABASE_CREATE_PUSHHISTORY_SCRIPT = "create table "
            + DATABASE_PUSHHISTORY
            + " (" + PUSHHISTORY_PUSHID + " integer not null primary key autoincrement, "
            + PUSHHISTORY_PUSHTITLE + " text not null, "
            + PUSHHISTORY_PUSHTEXT + " text not null, "
            + PUSHHISTORY_PUSHKEY + " text not null, "
            + PUSHHISTORY_PUSHTIME + " long not null)";

    DBPush(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBPush(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_PUSHHISTORY_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void savePushToHistory(SQLiteDatabase mSqLiteDatabase, long pushtime, String pushtitle, String pushtext, String pushkey) {

        ContentValues newValues = new ContentValues();
        newValues.put(DBPush.PUSHHISTORY_PUSHTIME, pushtime);
        newValues.put(DBPush.PUSHHISTORY_PUSHTITLE, pushtitle);
        newValues.put(DBPush.PUSHHISTORY_PUSHTEXT, pushtext);
        newValues.put(DBPush.PUSHHISTORY_PUSHKEY, pushkey);

        mSqLiteDatabase.insertWithOnConflict(DBPush.DATABASE_PUSHHISTORY, null, newValues, SQLiteDatabase.CONFLICT_REPLACE);
    }
}