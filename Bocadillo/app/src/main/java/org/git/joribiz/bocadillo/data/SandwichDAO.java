package org.git.joribiz.bocadillo.data;

import android.database.Cursor;

import org.git.joribiz.bocadillo.R;
import org.git.joribiz.bocadillo.models.Sandwich;

public class SandwichDAO implements DBContract.SandwichEntry {
    private SQLiteHelper sqLiteHelper;

    // Array con datos de prueba para poder probar la base de datos
    private static final Sandwich[] MOCK_DATA = {
            new Sandwich(
                    "Sandwich 1",
                    "Lorem ipsum",
                    5.0f,
                    R.drawable.ic_launcher_background
            ),
            new Sandwich(
                    "Sandwich 2",
                    "Lorem ipsum",
                    6.0f,
                    R.drawable.ic_launcher_background
            ),
            new Sandwich(
                    "Sandwich 3",
                    "Lorem ipsum",
                    7.0f,
                    R.drawable.ic_launcher_background
            ),
            new Sandwich(
                    "Sandwich 4",
                    "Lorem ipsum",
                    8.0f,
                    R.drawable.ic_launcher_background
            ),
            new Sandwich(
                    "Sandwich 5",
                    "Lorem ipsum",
                    9.0f,
                    R.drawable.ic_launcher_background
            ),
            new Sandwich(
                    "Sandwich 6",
                    "Lorem ipsum",
                    10.0f,
                    R.drawable.ic_launcher_background
            ),
    };

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

    public void insertMockData() {
        for (Sandwich sandwich : MOCK_DATA) {
            insertSandwich(sandwich);
        }
    }
}
