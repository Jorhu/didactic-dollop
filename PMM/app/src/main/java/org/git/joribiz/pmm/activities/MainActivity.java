package org.git.joribiz.pmm.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.adapters.BottomNavigationAdapter;
import org.git.joribiz.pmm.adapters.CartListAdapter;
import org.git.joribiz.pmm.adapters.SandwichListAdapter;
import org.git.joribiz.pmm.adapters.UserOrderAdapter;
import org.git.joribiz.pmm.data.OrderDAO;
import org.git.joribiz.pmm.data.SQLiteHelper;
import org.git.joribiz.pmm.data.SandwichDAO;
import org.git.joribiz.pmm.data.UserDAO;
import org.git.joribiz.pmm.fragments.CartFragment;
import org.git.joribiz.pmm.fragments.ProfileFragment;
import org.git.joribiz.pmm.fragments.SandwichDetailsFragment;
import org.git.joribiz.pmm.fragments.SandwichListFragment;
import org.git.joribiz.pmm.model.Order;
import org.git.joribiz.pmm.model.Sandwich;
import org.git.joribiz.pmm.model.User;
import org.git.joribiz.pmm.pagers.NoSwipePager;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements
        SandwichListAdapter.ItemClickListener,
        SandwichListAdapter.ItemLongClickListener,
        CartListAdapter.RemoveItemListener,
        SandwichDetailsFragment.AddButtonClickListener,
        CartFragment.CheckoutButtonClickListener{
    // BottomNavigation Bar, de una librería externa
    private AHBottomNavigation bottomNavigation;

    // ViewPager personalizado
    private NoSwipePager viewPager;

    // Adaptador del ViewPager
    private BottomNavigationAdapter pageAdapter;

    // Fragments
    private ProfileFragment profileFragment;
    private SandwichListFragment sandwichListFragment;
    private SandwichDetailsFragment sandwichDetailsFragment;
    private CartFragment cartFragment;

    // Adaptadores de los fragments
    private UserOrderAdapter userOrderAdapter;
    private SandwichListAdapter sandwichListAdapter;
    private CartListAdapter cartListAdapter;

    // En esta varible se guardará el usuario que ha accedido a la app
    private User user;

    // Para almacenar los bocadillos obtenidos de la base de datos
    private ArrayList<Sandwich> sandwiches;

    // Para almacenar los pedidos hechos por el usuario obtenidos de la base de datos
    private ArrayList<Order> userOrders;

    // Para preparar el pedido del usuario
    private ArrayList<Sandwich> sandwichesOrdered;

    // Para llevar la cuenta del precio del pedido del usuario
    private float orderPrice;

    // Con esta variable llevaremos la cuenta de bocadillos pedidos por el usuario
    private int cartCount = 0;

    // Para controlar si el usuario está viendo los detalles de un bocadillo o no
    private boolean showingDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = findViewById(R.id.activity_main_bottom_navigation);

        // Inicialización de las variables
        user = getIntent().getParcelableExtra("user");
        userOrders = new ArrayList<>();
        sandwiches = new ArrayList<>();
        sandwichesOrdered = new ArrayList<>();
        orderPrice = 0;

        /* Inicializamos los datos consultando la base de datos
        TODO: ¿Hacer una tarea asíncrona para consultar la base de datos? */
        setupData();

        // Inicializamos y configuramos los adapters
        setupAdapters();

        // Inicializamos y configuramos los fragments
        setupFragments();

        // Configuramos la Navigation Bar
        setupViewPager();
        setupBottomNavigation();
    }

    /**
     * Controla el comportamiento de la app al pulsar el botón back.
     */
    @Override
    public void onBackPressed() {
        // Si se están viendo los detalles de algún bocadillo, volvemos a la lista de bocadillos
        if (showingDetails) {
            showingDetails = false;
            viewPager.setCurrentItem(1);
        } else {
            // Si no, comportamiento normal pero impidiendo volver al login
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *  Sobreescribe el evento onClick() de SandwichListAdapter.
     */
    @Override
    public void onItemClick(View view, final int position) {
        Sandwich sandwich = sandwichListAdapter.getSandwich(position);
        // Creamos una nueva instancia de SandwichDetailsFragment
        sandwichDetailsFragment = SandwichDetailsFragment.newInstance(sandwich);

        // Comprobamos si al adaptador ya se le ha añadido una instancia de SandwichDetailsFragment
        if (pageAdapter.getCount() == 3) {
            pageAdapter.addFragment(sandwichDetailsFragment);
        } else {
            // Reemplazamos la antigua instancia de SandwichDetails
            pageAdapter.setFragment(3, sandwichDetailsFragment);
        }

        // Retrasamos el cambio de fragment para que la animación al hacer click se reproduzca
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showingDetails = true;
                viewPager.setCurrentItem(3);
            }
        }, 500);
    }

    /**
     *  Sobreescribe el evento onLongClick() de SandwichListAdapter.
     */
    @Override
    public void onLongItemClick(int position) {
        sandwichesOrdered.add(sandwichListAdapter.getSandwich(position));
        cartListAdapter.addSandwich(sandwichListAdapter.getSandwich(position));

        // Incrementamos el número de bocadillos en el carrito y actualizamos la notificación
        cartCount++;
        setCartNotification(cartCount);

        orderPrice = cartListAdapter.calculateTotalPrice();
        cartFragment.setTotalPriceText(orderPrice);
    }

    /**
     * Sobreescribe el evento onClick() de SandwichDetailsFragment.
     */
    @Override
    public void onAddButtonClick() {
        /* Si hemos llegado hasta aquí, significa que ya tenemos una instancia del fragment y
        podemos pedirle que nos devuelva el bocadillo pedido por el usuario */
        sandwichesOrdered.add(sandwichDetailsFragment.getSandwich());
        cartListAdapter.addSandwich(sandwichDetailsFragment.getSandwich());


        cartCount++;
        setCartNotification(cartCount);

        // Actualizamos el precio del pedido
        orderPrice = cartListAdapter.calculateTotalPrice();
        cartFragment.setTotalPriceText(orderPrice);
    }

    /**
     * Sobreescribe el evento onClick() de CartListAdapter.
     */
    @Override
    public void onRemoveItemClick(View view, final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sandwichesOrdered.remove(position);
                cartListAdapter.removeSandwich(position);

                cartCount--;
                setCartNotification(cartCount);

                orderPrice = cartListAdapter.calculateTotalPrice();
                cartFragment.setTotalPriceText(orderPrice);
            }
        }, 200);
    }

    /**
     * Sobreescribe el evento onClick() de CartFragment.
     */
    @Override
    public void onCheckoutButtonClick() {
        // TODO
    }

    /**
     * Realiza la consulta a la base de datos para obtener todos los datos necesarios.
     */
    private void setupData() {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(getApplicationContext());
        UserDAO userDAO = new UserDAO(sqLiteHelper);
        SandwichDAO sandwichDAO = new SandwichDAO(sqLiteHelper);
        OrderDAO orderDAO = new OrderDAO(sqLiteHelper);

        Cursor cursor = sandwichDAO.getAllSandwiches();
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                sandwiches.add(new Sandwich(cursor));
            }
        }

        int userID = userDAO.getUserIDByEmail(String.format("\"%s\"", user.getEmail()));
        cursor = orderDAO.getOrderByUserID(userID);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                userOrders.add(new Order(cursor));
            }
        }
        sqLiteHelper.close();
        cursor.close();
    }

    /**
     * Configuración de los fragments
     */
    private void setupAdapters() {
        userOrderAdapter = new UserOrderAdapter(userOrders);
        sandwichListAdapter = new SandwichListAdapter(sandwiches);
        cartListAdapter = new CartListAdapter(sandwichesOrdered);

        sandwichListAdapter.setItemClickListener(this);
        sandwichListAdapter.setItemLongClickListener(this);
        cartListAdapter.setRemoveItemListener(this);
    }

    /**
     * Configuración de los fragments.
     */
    private void setupFragments() {
        profileFragment = ProfileFragment.newInstance(user.getEmail());
        sandwichListFragment = new SandwichListFragment();
        cartFragment = new CartFragment();

        profileFragment.setUserOrderAdapter(userOrderAdapter);
        sandwichListFragment.setSandwichListAdapter(sandwichListAdapter);
        cartFragment.setCartListAdapter(cartListAdapter);
    }

    /**
     * Configuración del ViewPager.
     */
    private void setupViewPager() {
        viewPager = findViewById(R.id.activity_main_container);
        viewPager.setPagingEnabled(false);
        pageAdapter = new BottomNavigationAdapter(getSupportFragmentManager());

        pageAdapter.addFragment(profileFragment);
        pageAdapter.addFragment(sandwichListFragment);
        pageAdapter.addFragment(cartFragment);

        viewPager.setAdapter(pageAdapter);
    }

    /**
     * Configuración de la Bottom Navigation Bar para poder usarla.
     */
    private void setupBottomNavigation() {
        // Listener para manejar el cambio de pestañas
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Si no se selecciona un fragment que ya estuviese seleccionado...
                if (!wasSelected) {
                    // El usuario ya no está viendo los detalles de un bocadillo
                    showingDetails = false;
                    viewPager.setCurrentItem(position);
                }
                return true;
            }
        });

        // Creamos los objetos que irán en la barra de navegación y los añadimos
        AHBottomNavigationItem[] items =  {
                new AHBottomNavigationItem(
                        getString(R.string.my_profile),
                        R.mipmap.baseline_account_circle_white_24),
                new AHBottomNavigationItem(
                        getString(R.string.menu),
                        R.mipmap.baseline_contact_support_white_24),
                new AHBottomNavigationItem(
                        getString(R.string.my_order),
                        R.mipmap.baseline_shopping_cart_white_24)
        };
        bottomNavigation.addItems(Arrays.asList(items));

        // Posición por defecto de la Bottom Navigation Bar
        bottomNavigation.setCurrentItem(1);
    }

    /**
     * Controla y estable las notificaciones del carrito de la compra.
     */
    public void setCartNotification(int cartCount) {
        String notification;
        if (cartCount == 0) {
            notification = "";
        } else {
            notification = Integer.toString(cartCount);
        }
        bottomNavigation.setNotification(notification, 2);
    }
}
