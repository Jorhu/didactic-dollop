package org.git.joribiz.pmm.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.model.Sandwich;

import java.util.ArrayList;

public class NewOrderActivity extends AppCompatActivity {
    private ArrayList<Sandwich> sandwichesOrdered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        // Si el usuario no ha pedido nada mostramos el texto de advertencia
        if (getIntent().getParcelableExtra("sandwiches") == null) {

        } else {
            sandwichesOrdered.addAll(getIntent().<Sandwich>getParcelableArrayListExtra("sandwiches"));

        }

    }
}
