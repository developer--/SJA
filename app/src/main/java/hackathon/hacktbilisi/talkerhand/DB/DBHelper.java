package hackathon.hacktbilisi.talkerhand.DB;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shota on 12/19/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Combination";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_JOKER_DB_TABLE = "CREATE TABLE "+
            CTable.TABLE_NAME + "("+
            CTable.id +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            CTable.WORD +" text,"+
            CTable.ACTIONS + " text"+
            ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_JOKER_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
