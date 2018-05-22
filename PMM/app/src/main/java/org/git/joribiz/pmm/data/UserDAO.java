package org.git.joribiz.pmm.data;

import android.database.Cursor;

import org.git.joribiz.pmm.model.User;

public class UserDAO implements DBContract.UserEntry {
    private SQLiteHelper sqLiteHelper;

    public UserDAO(SQLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    public Cursor getUserByID(int id) {
        return sqLiteHelper.getReadableDatabase().query(
                TABLE_NAME,
                null,
                _ID + "=" + id,
                null,
                null,
                null,
                null);
    }

    public Cursor getUserByEmail(String email) {
        // Hay que entrecomillar el email porque la @ da problemas
        email = String.format("\"%s\"", email);
        return sqLiteHelper.getReadableDatabase().query(
                TABLE_NAME,
                null,
                KEY_EMAIL + "=" + email,
                null,
                null,
                null,
                null
        );
    }

    public int getUserIDByEmail(String email) {
        int id = 0;
        Cursor cursor = sqLiteHelper.getReadableDatabase().query(
                TABLE_NAME,
                new String[]{_ID},
                KEY_EMAIL + "=" + email,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(_ID));
        }
        cursor.close();
        return id;
    }

    public Cursor getAllUsers() {
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

    public void insertUser(User user) {
        sqLiteHelper.getWritableDatabase().insert(
                TABLE_NAME,
                null,
                user.toContentValues()
        );
    }
}
