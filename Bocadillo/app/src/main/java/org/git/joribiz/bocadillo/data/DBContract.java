package org.git.joribiz.bocadillo.data;

import android.provider.BaseColumns;

/**
 * Esta interfaz incorpora todas las constantes e interfaces necesarias para extructurar la base de
 * datos de la app. Las interfaces internas extienden a su vez la interfaz BaseColumns, que
 * entre otras cosas es la que permite usar la clase Cursor en otras partes de la aplicacción.
 */
public interface DBContract {
    String PRIMARY_KEY = " PRIMARY KEY";
    String FOREIGN_KEY = " FOREIGN KEY";
    String UNIQUE = " UNIQUE ";
    String REFERENCES = " REFERENCES ";
    String ON_DELETE = " ON DELETE";
    String ON_UPDATE = " ON UPDATE";
    String CASCADE = " CASCADE";
    String INTEGER_TYPE = " INTEGER";
    String REAL_TYPE = " REAL";
    String TEXT_TYPE = " TEXT";
    String NOT_NULL = " NOT NULL";
    String COMA_SEP = ", ";

    String DATABASE_NAME = "database.db";
    int DATABASE_VERSION = 1;

    interface UserEntry extends BaseColumns {
        String TABLE_NAME = "user";
        String KEY_EMAIL = "email";
        String KEY_PASSWORD = "password";

        String[] KEY_ARRAY = {
                KEY_EMAIL,
                KEY_PASSWORD
        };

        /**
         * CREATE TABLE user (
         *      _id INTEGER PRIMARY KEY,
         *      email TEXT NOT NULL,
         *      password TEXT NOT NULL,
         *      UNIQUE (email)
         * )
         */
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + INTEGER_TYPE + PRIMARY_KEY + COMA_SEP
                + KEY_EMAIL + TEXT_TYPE + NOT_NULL + COMA_SEP
                + KEY_PASSWORD + TEXT_TYPE + NOT_NULL + COMA_SEP
                + UNIQUE + "(" + KEY_EMAIL + ")"
                + ")";

        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    interface SandwichEntry extends BaseColumns {
        String TABLE_NAME = "sandwich";
        String KEY_NAME = "name";
        String KEY_INGREDIENTS = "ingredients";
        String KEY_PRICE = "price";
        String KEY_PHOTO_ID = "photo_id";



        String[] KEY_ARRAY = {
                KEY_NAME,
                KEY_INGREDIENTS,
                KEY_PRICE
        };

        /**
         * CREATE TABLE sandwich (
         *      _id INTEGER PRIMARY KEY,
         *      name TEXT NOT NULL,
         *      ingredients TEXT NOT NULL,
         *      price REAL NOT NULL,
         *      photo_id INTEGER,
         *      UNIQUE (name)
         * )
         */
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + INTEGER_TYPE + PRIMARY_KEY + COMA_SEP
                + KEY_NAME + TEXT_TYPE + NOT_NULL + COMA_SEP
                + KEY_INGREDIENTS + TEXT_TYPE + NOT_NULL + COMA_SEP
                + KEY_PRICE + REAL_TYPE + NOT_NULL + COMA_SEP
                + KEY_PHOTO_ID + INTEGER_TYPE + COMA_SEP
                + UNIQUE + "(" + KEY_NAME + ")"
                + ")";

        String DROP_TABLE = "DROP TABLE IF EXITS " + TABLE_NAME;
    }

    interface OrderEntry extends BaseColumns {
        // Hay que entrecomillar el nombre de la tabla, ya que order es una palabra reservada
        String TABLE_NAME = "\"order\"";
        String KEY_ADDRESS = "address";
        String KEY_DATE = "date";
        String KEY_USER_ID = "user_id";

        String[] KEY_ARRAY = {
                KEY_ADDRESS,
                KEY_DATE,
                KEY_USER_ID
        };

        /**
         * CREATE TABLE order (
         *      _id INTEGER PRIMARY KEY,
         *      address TEXT NOT NULL,
         *      date TEXT NOT NULL,
         *      user_id INTEGER NOT NULL,
         *      FOREIGN KEY (user_id) REFERENCES user (_id) ON DELETE CASCADE
         * )
         */
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + INTEGER_TYPE + PRIMARY_KEY + COMA_SEP
                + KEY_ADDRESS + TEXT_TYPE + NOT_NULL + COMA_SEP
                + KEY_DATE + TEXT_TYPE + NOT_NULL + COMA_SEP
                + KEY_USER_ID + INTEGER_TYPE + NOT_NULL + COMA_SEP
                + FOREIGN_KEY + "(" + KEY_USER_ID + ")"
                + REFERENCES + UserEntry.TABLE_NAME + "(" + UserEntry._ID + ")"
                + ON_DELETE + CASCADE
                + ")";

        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /**
     * Esta clase sirve para modelar la tabla auxiliar necesaria en la relación many2many entre
     * la tabla sandwich y la tabla order.
     */
    interface SandwichOrderEntry extends BaseColumns {
        String TABLE_NAME = "sandwich_order";
        String KEY_SANDWICH_ID = "sandwich_id";
        String KEY_ORDER_ID = "order_id";

         String[] KEY_ARRAY = {
                KEY_SANDWICH_ID,
                KEY_ORDER_ID
        };

        /**
         * CREATE TABLE sandwich_order (
         *      _id INTEGER PRIMARY KEY,
         *      sandwich_id INTEGER NOT NULL,
         *      order_id INTEGER NOT NULL,
         *      FOREIGN KEY (sandwich_id) REFERENCES sandwich (_id),
         *      FORREIGN KEY (order_id) REFERENCES order (_id)
         * )
         */
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + INTEGER_TYPE + PRIMARY_KEY + COMA_SEP
                + KEY_SANDWICH_ID + INTEGER_TYPE + NOT_NULL + COMA_SEP
                + KEY_ORDER_ID + INTEGER_TYPE + NOT_NULL + COMA_SEP
                + FOREIGN_KEY + "(" + KEY_SANDWICH_ID + ")"
                + REFERENCES + SandwichEntry.TABLE_NAME + "(" + SandwichEntry._ID + ")" + COMA_SEP
                + FOREIGN_KEY + "(" + KEY_ORDER_ID + ")"
                + REFERENCES + OrderEntry.TABLE_NAME + "(" + OrderEntry._ID + ")"
                + ")";

        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
