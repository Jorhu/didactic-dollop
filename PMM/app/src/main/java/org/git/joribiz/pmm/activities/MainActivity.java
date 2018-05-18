package org.git.joribiz.pmm.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.actionitembadge.library.ActionItemBadge;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.adapters.SandwichListAdapter;
import org.git.joribiz.pmm.data.SQLiteHelper;
import org.git.joribiz.pmm.data.SandwichDAO;
import org.git.joribiz.pmm.fragments.SandwichDetailsFragment;
import org.git.joribiz.pmm.fragments.SandwichListFragment;
import org.git.joribiz.pmm.model.Sandwich;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        SandwichListAdapter.ItemClickListener,
        SandwichListAdapter.ItemLongClickListener,
        SandwichDetailsFragment.AddButtonClickListener {
    private static final int REQUEST_USER = 0;
    private SandwichDetailsFragment sandwichDetailsFragment;
    private SandwichListAdapter sandwichListAdapter;
    // En esta varible se guardará el email del usuario que ha accedido a la app
    private String userEmail;
    // Este array lo usaremos para preparar el pedido del usuario
    private ArrayList<Sandwich> sandwichesOrdered;
    // Con esta variable llevaremos la cuenta de bocadillos pedidos por el usuario
    private int cartCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         /*// Redirigimos al usuario al login
         Intent intent = new Intent(this, LoginActivity.class);
         startActivityForResult(intent, REQUEST_USER);*/

        // Insertamos los datos de prueba en la base da datos
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(this);
        SandwichDAO sandwichDAO = new SandwichDAO(sqLiteHelper);
        sandwichDAO.insertMockData();

        // Consulta a la base de datos de los bocadillos
        Cursor cursor = sandwichDAO.getAllSandwiches();

        // Obtenemos un array con todos los bocadillos
        ArrayList<Sandwich> sandwiches = new ArrayList<>();
        while (cursor.moveToNext()) {
            sandwiches.add(new Sandwich(cursor));
        }

        // Cerramos el cursor y la base de datos
        sqLiteHelper.close();
        cursor.close();

        // Creamos el adaptador y le añadimos los listeners
        sandwichListAdapter = new SandwichListAdapter(sandwiches);
        sandwichListAdapter.setItemClickListener(this);
        sandwichListAdapter.setItemLongClickListener(this);
        // Cargamos el primer fragment
        SandwichListFragment sandwichListFragment = new SandwichListFragment();
        sandwichListFragment.setFragmentAdapter(sandwichListAdapter);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_container, sandwichListFragment);
        transaction.commit();

        // Instanciamos el array para empezar a realizar el pedido y reniciamos el contador
        sandwichesOrdered = new ArrayList<>();
        cartCount = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Mover la consulta a la base de datos aquí
    }

    /**
     * Una vez el usuario se autentifique con éxito, guardaremos su email para identificarlo más
     * adelante.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_USER) {
            if (resultCode == RESULT_OK) {
                userEmail = data.getStringExtra("email");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Hago uso de una biblioteca externa para gestionar el carrito de la compra
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Si el carrito tiene algún bocadillo...
        if (cartCount > 0) {
            // Oculto el carrito de la compra vacío que no tiene animación
            menu.findItem(R.id.empty_shopping_cart).setVisible(false);
            // Este es el gestor de la animación del carrito
            ActionItemBadge.update(
                    this,
                    menu.findItem(R.id.shopping_cart),
                    getDrawable(R.mipmap.baseline_shopping_cart_white_24),
                    ActionItemBadge.BadgeStyles.RED,
                    cartCount
            );
        } else {
            menu.findItem(R.id.empty_shopping_cart).setVisible(true);
            ActionItemBadge.hide(menu.findItem(R.id.shopping_cart));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.empty_shopping_cart:
                intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            case R.id.shopping_cart:
                intent = new Intent(this, CartActivity.class);
                intent.putParcelableArrayListExtra("sandwiches", sandwichesOrdered);
                startActivity(intent);
                return true;
            case R.id.my_profile:
                // TODO
                return true;
            case R.id.about:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *  Este método es el que sobreescribe el evento onClick() de SandwichListAdapter.
     */
    @Override
    public void onItemClick(View view, final int position) {
        final Sandwich sandwich = sandwichListAdapter.getItem(position);
        // Retrasamos el cambio de fragment para que la animación al hacer click se reproduzca
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sandwichDetailsFragment = SandwichDetailsFragment.newInstance(sandwich);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_main_container, sandwichDetailsFragment);
                transaction.addToBackStack("");
                transaction.commit();
            }
        }, 500);
    }

    /**
     *  Este método es el que sobreescribe el evento onClick() de SandwichListAdapter.
     */
    @Override
    public void onLongItemClick(int position) {
        sandwichesOrdered.add(sandwichListAdapter.getItem(position));
        cartCount++;
        invalidateOptionsMenu();
    }

    /**
     * Este método es el que sobreescribe el evento onClick() de SandwichDetailsFragment.
     */
    @Override
    public void onAddButtonClick() {
        /* Si hemos llegado hasta aquí, significa que ya tenemos una instancia del fragment y
        podemos pedirle que nos devuelva el bocadillo pedido por el usuario */
        sandwichesOrdered.add(sandwichDetailsFragment.getSandwich());
        // Incrementamos el número de bocadillos en el carrito
        cartCount++;
        // Volvemos a dibujar el menú
        invalidateOptionsMenu();
    }
}
