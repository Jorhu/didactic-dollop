package org.git.joribiz.bocadillo.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.git.joribiz.bocadillo.R;
import org.git.joribiz.bocadillo.models.Sandwich;

import java.util.ArrayList;

public class ShowcaseFragment extends Fragment {
    private ArrayList<Sandwich> sandwiches;

    public ShowcaseFragment() {}

    public static ShowcaseFragment newInstance(ArrayList<Sandwich> sandwiches) {
        ShowcaseFragment showcaseFragment = new ShowcaseFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList("sandwiches", sandwiches);
        showcaseFragment.setArguments(args);

        return showcaseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sandwiches.addAll(getArguments().<Sandwich>getParcelableArrayList("sandwiches"));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showcase, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_showcase_recycler);

        return view;
    }

}
