package org.git.joribiz.pmm.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.git.joribiz.pmm.data.DBContract;

public class Sandwich implements DBContract, Parcelable {
    private String name;
    private String ingredients;
    private float price;
    private int photoId;

    public Sandwich(String name, String ingredients, float price, int photoId) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.photoId = photoId;
    }

    public Sandwich(Cursor cursor) {
        this.name = cursor.getString(cursor.getColumnIndex(SandwichEntry.KEY_NAME));
        this.ingredients = cursor.getString(cursor.getColumnIndex(SandwichEntry.KEY_INGREDIENTS));
        this.price = cursor.getFloat(cursor.getColumnIndex(SandwichEntry.KEY_PRICE));
        this.photoId = cursor.getInt(cursor.getColumnIndex(SandwichEntry.KEY_PHOTO_ID));
    }

    private Sandwich(Parcel in) {
        name = in.readString();
        ingredients = in.readString();
        price = in.readFloat();
        photoId = in.readInt();
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
        dest.writeInt(photoId);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        ingredients = in.readString();
        price = in.readFloat();
        photoId = in.readInt();
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
        values.put(SandwichEntry.KEY_PHOTO_ID, photoId);

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

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }
}
