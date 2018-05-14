package org.git.joribiz.bocadillo.data;

import android.database.Cursor;

import org.git.joribiz.bocadillo.models.Order;

/**
 * Por simplicidad, la clase OrderDAO va a ser la que posea la relación many2many con SandwichDAO y
 * actualize la tabla auxiliar ya que, desde el punto de vista del uso de la app, no tiene sentido
 * tener un pedido sin bocadillos mientras que los bocadillos sí pueden existir sin estar en un
 * pedido. Tener una relación verdaderamente bidireccional no merece la pena.
 */
public class OrderDAO implements DBContract.OrderEntry, DBContract.SandwichOrderEntry {
    private SQLiteHelper sqLiteHelper;

    public OrderDAO(SQLiteHelper sqLiteHelper) {
        this.sqLiteHelper = sqLiteHelper;
    }

    public Cursor getOrderByID(int id) {
        return sqLiteHelper.getReadableDatabase().query(
                DBContract.OrderEntry.TABLE_NAME,
                null,
                _ID + "=" + id,
                null,
                null,
                null,
                null);
    }

    public Cursor getOrderByUserID(int userID) {
        return sqLiteHelper.getReadableDatabase().query(
                DBContract.OrderEntry.TABLE_NAME,
                null,
                KEY_USER_ID + "=" + userID,
                null,
                null,
                null,
                null
        );
    }

    public Cursor getAllOrders() {
        return sqLiteHelper.getReadableDatabase().query(
                DBContract.OrderEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public void insertOrder(Order order) {
        sqLiteHelper.getWritableDatabase().insert(
                DBContract.OrderEntry.TABLE_NAME,
                null,
                order.toContentValues()
        );
    }

    // TODO: Métodos para leer y escribir en la tabla auxiliar
    public Cursor getAllSandwiches(int orderID) {
        return sqLiteHelper.getReadableDatabase().query(
                DBContract.SandwichOrderEntry.TABLE_NAME,
                new String[] {KEY_SANDWICH_ID},
                KEY_ORDER_ID + "=" + orderID,
                null,
                null,
                null,
                null
        );
    }

    public void insertSandwich(int orderID, int sandwichID) {

    }


}
