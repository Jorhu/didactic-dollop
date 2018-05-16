package org.git.joribiz.pmm.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.adapters.SandwichListAdapter;
import org.git.joribiz.pmm.data.SQLiteHelper;
import org.git.joribiz.pmm.data.SandwichDAO;
import org.git.joribiz.pmm.fragments.SandwichDetailsFragment;
import org.git.joribiz.pmm.fragments.SandwichListFragment;
import org.git.joribiz.pmm.model.Sandwich;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements SandwichListAdapter.ItemClickListener {
    private SandwichListAdapter sandwichListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         // Redirigimos al usuario al login
         Intent intent = new Intent(this, LoginActivity.class);
         startActivity(intent);


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

        // Cargamos el primer fragment
        sandwichListAdapter = new SandwichListAdapter(sandwiches);
        sandwichListAdapter.setItemClickListener(this);
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

    /**
     *  Este método es el que sobreescribe el evento onClick() del SandwichListAdapter
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
}
