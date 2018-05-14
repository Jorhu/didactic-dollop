package org.git.joribiz.bocadillo.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.git.joribiz.bocadillo.data.DBContract;

import java.io.Serializable;

public class Sandwich implements DBContract, Parcelable {
    private String name;
    private String ingredients;
    private float price;
    private int photo_id;

    public Sandwich(String name, String ingredients, float price, int photo_id) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.photo_id = photo_id;
    }

    public Sandwich(Parcel in) {
        name = in.readString();
        ingredients = in.readString();
        price = in.readFloat();
        photo_id = in.readInt();
    }

    public Sandwich(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(SandwichEntry.KEY_NAME));
        this.ingredients = cursor.getString(cursor.getColumnIndex(SandwichEntry.KEY_INGREDIENTS));
        this.price = cursor.getFloat(cursor.getColumnIndex(SandwichEntry.KEY_PRICE));
        this.photo_id = cursor.getInt(cursor.getColumnIndex(SandwichEntry.KEY_PHOTO_ID));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(ingredients);
        dest.writeFloat(price);
        dest.writeInt(photo_id);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        ingredients = in.readString();
        price = in.readFloat();
        photo_id = in.readInt();
    }

    public static final Creator<Sandwich> CREATOR = new Creator<Sandwich>() {
        @Override
        public Sandwich createFromParcel(Parcel in) {
            return new Sandwich(in);
        }

        @Override
        public Sandwich[] newArray(int size) {
            return new Sandwich[size];
        }
    };

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(SandwichEntry.KEY_NAME, name);
        values.put(SandwichEntry.KEY_INGREDIENTS, ingredients);
        values.put(SandwichEntry.KEY_PRICE, price);
        values.put(SandwichEntry.KEY_PHOTO_ID, photo_id);

        return values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }
}
