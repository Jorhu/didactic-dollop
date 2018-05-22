package org.git.joribiz.pmm.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.git.joribiz.pmm.data.DBContract;

public class User implements DBContract, Parcelable {
    private String email;
    private String password;
    // TODO: ¿Añadir foto de perfil de usuario?

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(Cursor cursor) {
        this.email = cursor.getString(cursor.getColumnIndex(UserEntry.KEY_EMAIL));
        this.password = cursor.getString(cursor.getColumnIndex(UserEntry.KEY_PASSWORD));
    }

    private User(Parcel in) {
        email = in.readString();
        password = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
    }

    public void readFromParcel(Parcel in) {
        email = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
