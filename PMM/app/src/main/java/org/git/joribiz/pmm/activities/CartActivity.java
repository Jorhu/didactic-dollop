package org.git.joribiz.pmm.activities;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.adapters.CartListAdapter;
import org.git.joribiz.pmm.model.Sandwich;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements
        CartListAdapter.RemoveItemListener {
    private CartListAdapter cartListAdapter;
    private ArrayList<Sandwich> sandwichesOrdered;
    private TextView totalPriceText;
    private float totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        RecyclerView recyclerView = findViewById(R.id.activity_cart_recyclerView);
        TextView emptyCartText = findViewById(R.id.activity_cart_empty_cart_text);
        totalPriceText = findViewById(R.id.activity_cart_price);
        FloatingActionButton checkoutFAB = findViewById(R.id.activity_cart_checkoutFAB);

        sandwichesOrdered = new ArrayList<>();
        sandwichesOrdered.addAll(getIntent().getExtras().<Sandwich>getParcelableArrayList("sandwiches"));

        /* Si el usuario no ha pedido nada mostramos el texto de advertencia y ocultamos el resto
        del layout */
        if (sandwichesOrdered.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            checkoutFAB.setVisibility(View.GONE);
            totalPriceText.setVisibility(View.GONE);
            emptyCartText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            checkoutFAB.setVisibility(View.VISIBLE);
            totalPriceText.setVisibility(View.VISIBLE);
            emptyCartText.setVisibility(View.GONE);

            cartListAdapter = new CartListAdapter(sandwichesOrdered);
            cartListAdapter.setRemoveItemListener(this);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(cartListAdapter);

            calculatePrice();
        }

        checkoutFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Sobreescribo este método para asi poder controlar el carrito del usuario y que sea
     * consistente entre actividades.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Calcula el precio total del pedido y lo muestra en pantalla.
     */
    public void calculatePrice() {
        totalPrice = 0;
        for (Sandwich sandwich : sandwichesOrdered) {
            totalPrice += sandwich.getPrice();
        }
        totalPriceText.setText(getString(R.string.total_price)
                + String.format(Locale.getDefault(), " %.2f €", totalPrice));
    }

    @Override
    public void onRemoveItemClick(View view, final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sandwichesOrdered.remove(position);
                cartListAdapter.removeItem(position);
                calculatePrice();
            }
        }, 500);
    }
}
