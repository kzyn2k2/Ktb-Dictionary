package com.ktbbeta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class DBHelper extends SQLiteOpenHelper{

    private static String DB_NAME = "Dictionary_src.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private boolean mNeedUpdate = false;

    public DBHelper(Context context) throws IOException, SQLException {
        super(context, DB_NAME, null, DB_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;

        copyDataBase();

        openDataBase();
        checkTblExists(mContext);
    }

    private boolean checkTblExists(Context context) throws IOException {
        String query = "Select name from sqlite_master where tbl_name = 'tblmain'";
        Cursor cursor = mDataBase.rawQuery(query, null);
        if(cursor != null && cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File("/data/data/com.ktbbeta/databases/Dictionary_src.db");
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                InputStream mInput = mContext.getAssets().open(DB_NAME);
                System.out.println(mInput.available());
                Log.d("bytes", String.valueOf(mInput.available()));
                copyDBFile();
            } catch (IOException mIOException) {
                InputStream mInput = mContext.getAssets().open(DB_NAME);
                Log.e("error", String.valueOf(mInput.available()));
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    public Cursor getAll() {
        return mDataBase.rawQuery("SELECT no AS _id, kana, my_imi,kanji,part_of_speech,ji_ta,rank,eg,jp_imi FROM tblmain ORDER BY no", null);
    }

    public Cursor search(String name, boolean chk) {
        if (!name.matches("")) {
            if (chk) {
                return getReadableDatabase().rawQuery("SELECT no AS _id, kana, my_imi,kanji,part_of_speech,ji_ta,rank,rank,eg,jp_imi FROM tblmain WHERE kana LIKE \"" + name + "\" or kanji LIKE \"" + name + "\" or my_imi LIKE \"" + name + "\" ORDER BY no", null);
            }
            return getReadableDatabase().rawQuery("SELECT no AS _id, kana, my_imi,kanji,part_of_speech,ji_ta,rank,rank,eg,jp_imi FROM tblmain WHERE kana LIKE \"" + name + "%\" or kanji LIKE \"" + name + "%\" or my_imi LIKE \"" + name + "%\" ORDER BY no", null);
        }
        return getAll();
    }

    public String getItem(Cursor c, int index) {
        return c.getString(index);
    }

    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        System.out.println(mInput.available());
        Log.d("bytes", String.valueOf(mInput.available()));
        // InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream("/data/data/com.ktbbeta/databases/Dictionary_src.db");
        byte[] mBuffer = new byte[1024];
        int mLength;

        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase("/data/data/com.ktbbeta/databases/Dictionary_src.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            mNeedUpdate = true;
    }

}
