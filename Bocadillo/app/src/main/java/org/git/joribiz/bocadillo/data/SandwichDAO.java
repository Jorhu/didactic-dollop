package org.git.joribiz.bocadillo.data;

import android.database.Cursor;

import org.git.joribiz.bocadillo.models.Sandwich;

public class SandwichDAO implements DBContract.SandwichEntry {
    private SQLiteHelper sqLiteHelper;

    public SandwichDAO(SQLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    public Cursor getSandwichByID(int id) {
        return sqLiteHelper.getReadableDatabase().query(
                TABLE_NAME,
                null,
                _ID + "=" + id,
                null,
                null,
                null,
                null);
    }

    public Cursor getSandwichByName(String name) {
        return sqLiteHelper.getReadableDatabase().query(
                TABLE_NAME,
                null,
                KEY_NAME + "=" + name,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getAllSandwiches() {
        return sqLiteHelper.getReadableDatabase().query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public long insertSandwich(Sandwich sandwich) {
        return sqLiteHelper.getWritableDatabase().insert(
                TABLE_NAME,
                null,
                sandwich.toContentValues()
        );
    }
}
