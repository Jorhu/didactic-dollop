package org.git.joribiz.pmm.model;

import android.content.ContentValues;
import android.database.Cursor;

import org.git.joribiz.pmm.data.DBContract;

public class User implements DBContract {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(Cursor cursor) {
        this.email = cursor.getString(cursor.getColumnIndex(UserEntry.KEY_EMAIL));
        this.password = cursor.getString(cursor.getColumnIndex(UserEntry.KEY_PASSWORD));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(UserEntry.KEY_EMAIL, email);
        values.put(UserEntry.KEY_PASSWORD, password);

        return values;
    }
}
