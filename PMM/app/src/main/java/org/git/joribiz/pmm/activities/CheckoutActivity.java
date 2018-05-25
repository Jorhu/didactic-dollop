package org.git.joribiz.pmm.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.model.Sandwich;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {
    private ArrayList<Sandwich> sandwichesOrdered;
    private float orderPrice;
    private int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();

        sandwichesOrdered = new ArrayList<>();
        sandwichesOrdered.addAll(intent.<Sandwich>getParcelableArrayListExtra("sandwichesOrdered"));

        orderPrice= intent.getFloatExtra("price", 0);
        userID = intent.getIntExtra("ID", 0);
    }
}
