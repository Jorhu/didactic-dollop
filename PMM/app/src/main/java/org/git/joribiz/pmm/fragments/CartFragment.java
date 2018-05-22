package org.git.joribiz.pmm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.adapters.CartListAdapter;

import java.util.Locale;

public class CartFragment extends Fragment implements View.OnClickListener {
    private CartListAdapter cartListAdapter;
    private TextView totalPriceText;
    private CheckoutButtonClickListener checkoutButtonClickListener;

    public interface CheckoutButtonClickListener {
        void onCheckoutButtonClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            checkoutButtonClickListener = (CheckoutButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " " +
                    "must implement CheckoutButtonClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_cart_recycler);
        FloatingActionButton checkoutFAB = view.findViewById(R.id.fragment_cart_checkoutFAB);
        totalPriceText = view.findViewById(R.id.fragment_cart_price);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cartListAdapter);

        checkoutFAB.setOnClickListener(this);
    }

    public void setCartListAdapter(CartListAdapter cartListAdapter) {
        this.cartListAdapter = cartListAdapter;
    }

    public void setTotalPriceText(float totalPrice) {
        totalPriceText.setText(getString(R.string.total_price)
                + String.format(Locale.getDefault(), " %.2f €", totalPrice));
    }

    @Override
    public void onClick(View v) {
        checkoutButtonClickListener.onCheckoutButtonClick();
    }
}
