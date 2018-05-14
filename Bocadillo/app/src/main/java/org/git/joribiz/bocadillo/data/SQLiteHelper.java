package org.git.joribiz.bocadillo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper implements DBContract {
    private static SQLiteHelper sInstance;

    /**
     * Constructor privado para evitar que se creen nuevas instancias de la clase, así se hace
     * obligatorio el uso del método getInstance().
     */
    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SQLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SQLiteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserEntry.CREATE_TABLE);
        db.execSQL(SandwichEntry.CREATE_TABLE);
        db.execSQL(OrderEntry.CREATE_TABLE);
        db.execSQL(SandwichOrderEntry.CREATE_TABLE);
        db.execSQL("INSERT INTO user (email, password) VALUES (\"admin@gmail.com\", \"admin\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserEntry.DROP_TABLE);
        db.execSQL(SandwichEntry.DROP_TABLE);
        db.execSQL(OrderEntry.DROP_TABLE);
        db.execSQL(SandwichOrderEntry.DROP_TABLE);

        onCreate(db);
    }
}
