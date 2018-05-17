package org.git.joribiz.bocadillo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.git.joribiz.bocadillo.R;
import org.git.joribiz.bocadillo.models.Sandwich;

import java.util.Locale;

public class SandwichDetailsFragment extends Fragment {
    private Sandwich sandwich;

    public static SandwichDetailsFragment newInstance(Sandwich sandwich) {
        Bundle args = new Bundle();
        SandwichDetailsFragment fragment = new SandwichDetailsFragment();

        args.putParcelable("sandwich", sandwich);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sandwich = getArguments().getParcelable("sandwich");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sandwich_details, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView sandwichPhoto = view.findViewById(R.id.fragment_sandwich_details_photo);
        TextView sandwichName = view.findViewById(R.id.fragment_sandwich_details_name);
        TextView sandwichIngredients = view.findViewById(R.id.fragment_sandwich_details_ingredients);
        TextView sandwichPrice = view.findViewById(R.id.fragment_sandwich_details_price);

        sandwichPhoto.setImageResource(sandwich.getPhotoId());
        sandwichName.setText(sandwich.getName());
        sandwichIngredients.setText(sandwich.getIngredients());
        sandwichPrice.setText(
                String.format(Locale.getDefault(), "%.2f €", sandwich.getPrice())
        );
    }
}
