package org.git.joribiz.pmm.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.adapters.UserOrderAdapter;
import org.git.joribiz.pmm.model.Order;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private UserOrderAdapter userOrderAdapter;
    private String userEmail;

    public static ProfileFragment newInstance(String userEmail) {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();

        args.putString("email", userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString("email");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView nameText = view.findViewById(R.id.fragment_profile_name);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_profile_recycler);

        nameText.setText(userEmail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userOrderAdapter);
    }

    public void setUserOrderAdapter(UserOrderAdapter userOrderAdapter) {
        this.userOrderAdapter = userOrderAdapter;
    }
}
