package org.git.joribiz.pmm.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.adapters.SandwichListAdapter;
import org.git.joribiz.pmm.data.SQLiteHelper;
import org.git.joribiz.pmm.data.SandwichDAO;
import org.git.joribiz.pmm.fragments.SandwichDetailsFragment;
import org.git.joribiz.pmm.fragments.SandwichListFragment;
import org.git.joribiz.pmm.model.Sandwich;
import org.git.joribiz.pmm.model.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener,
        SandwichListAdapter.ItemClickListener {
    private static final int REQUEST_USER = 0;
    private String userEmail;
    private SandwichListAdapter sandwichListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         // Redirigimos al usuario al login
         Intent intent = new Intent(this, LoginActivity.class);
         startActivityForResult(intent, REQUEST_USER);

        // Añadimos el listener para los cambios en el back stack
        getSupportFragmentManager().addOnBackStackChangedListener(this);

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
        // Cargamos el primer fragment
        SandwichListFragment sandwichListFragment = new SandwichListFragment();
        sandwichListFragment.setFragmentAdapter(sandwichListAdapter);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main_container, sandwichListFragment);
        transaction.addToBackStack("");
        transaction.commit();
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
        // TODO
    }

    /**
     * Cuando se pulsa el botón back se llama a este método para ejecutar el popBackStack().
     */
    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
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
                SandwichDetailsFragment fragment = SandwichDetailsFragment.newInstance(sandwich);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_main_container, fragment);
                transaction.addToBackStack("");
                transaction.commit();
            }
        }, 500);
    }

    /**
     * Habilita el botón Up solo si hay elementos en el back stack, sin contar el primer fragment.
     */
    public void shouldDisplayHomeUp(){
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 1;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }
}
