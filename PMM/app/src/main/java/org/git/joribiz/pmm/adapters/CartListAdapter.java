package org.git.joribiz.pmm.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.git.joribiz.pmm.R;
import org.git.joribiz.pmm.model.Sandwich;

import java.util.ArrayList;
import java.util.Locale;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    ArrayList<Sandwich> sandwiches;
    private RemoveItemListener removeItemListener;

    public CartListAdapter(ArrayList<Sandwich> sandwiches) {
        this.sandwiches = new ArrayList<>();
        this.sandwiches.addAll(sandwiches);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sandwich_ordered, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sandwich sandwich = sandwiches.get(position);
        holder.sandwichPhoto.setImageResource(sandwich.getPhotoId());
        holder.sandwichName.setText(sandwich.getName());
        holder.sandwichPrice.setText(
                String.format(Locale.getDefault(), "%.2f €", sandwich.getPrice()));
    }

    @Override
    public int getItemCount() {
        return sandwiches.size();
    }

    public Sandwich getItem(int position) {
        return sandwiches.get(position);
    }

    public void removeItem(int position) {
        sandwiches.remove(position);
        // Este método es para hacer uso de las animaciones predeterminadas de RecyclerView
        notifyItemRemoved(position);
    }

    public void setRemoveItemListener(RemoveItemListener removeItemListener) {
        this.removeItemListener = removeItemListener;
    }

    public interface RemoveItemListener {
        void onRemoveItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView sandwichPhoto;
        ImageView removePhoto;
        TextView sandwichName;
        TextView sandwichPrice;

        ViewHolder(View itemView) {
            super(itemView);
            sandwichPhoto = itemView.findViewById(R.id.item_sandwich_ordered_photo);
            removePhoto = itemView.findViewById(R.id.item_sandwich_ordered_remove);
            sandwichName = itemView.findViewById(R.id.item_sandwich_ordered_name);
            sandwichPrice = itemView.findViewById(R.id.item_sandwich_ordered_price);
            removePhoto.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (removeItemListener != null) {
                removeItemListener.onRemoveItemClick(view, getAdapterPosition());
            }
        }
    }
}
