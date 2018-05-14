package org.git.joribiz.bocadillo.models;

import android.content.ContentValues;
import android.database.Cursor;

import org.git.joribiz.bocadillo.data.DBContract;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Por simplicidad, la clase Order va a ser la que posea la relación many2many con Sandwich y
 * actualize la tabla auxiliar ya que, desde el punto de vista del uso de la app, no tiene sentido
 * tener un pedido sin bocadillos mientras que los bocadillos sí pueden existir sin estar en un
 * pedido. Tener una relación verdaderamente bidireccional no merece la pena.
 */
public class Order implements DBContract {
    private String address;
    private String date;
    private int userId;

    public Order(String address, int userId) {
        // Obtenemos la fecha y hora del momento en el que se hace un nuevo pedido.
        Date date = new Date();

        this.address = address;
        this.date = DateFormat.getDateTimeInstance().format(date);
        this.userId = userId;
    }

    public Order(Cursor cursor) {
        this.address = cursor.getString(cursor.getColumnIndex(OrderEntry.KEY_ADDRESS));
        this.date = cursor.getString(cursor.getColumnIndex(OrderEntry.KEY_DATE));
        this.userId = cursor.getInt(cursor.getColumnIndex(OrderEntry.KEY_USER_ID));
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(OrderEntry.KEY_ADDRESS, address);
        values.put(OrderEntry.KEY_DATE, date);
        values.put(OrderEntry.KEY_USER_ID, userId);

        return values;
    }
}
