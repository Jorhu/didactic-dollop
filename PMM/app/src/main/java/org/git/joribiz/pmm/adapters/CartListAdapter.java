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
    private ArrayList<Sandwich> sandwichesOrdered;
    private RemoveItemListener removeItemListener;

    public CartListAdapter(ArrayList<Sandwich> sandwichesOrdered) {
        this.sandwichesOrdered = new ArrayList<>();
        this.sandwichesOrdered.addAll(sandwichesOrdered);
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
        Sandwich sandwich = sandwichesOrdered.get(position);
        holder.sandwichPhoto.setImageResource(sandwich.getPhotoId());
        holder.sandwichName.setText(sandwich.getName());
        holder.sandwichPrice.setText(
                String.format(Locale.getDefault(), "%.2f €", sandwich.getPrice()));
    }

    @Override
    public int getItemCount() {
        return sandwichesOrdered.size();
    }

    public Sandwich getSandwich(int position) {
        return sandwichesOrdered.get(position);
    }

    public void setRemoveItemListener(RemoveItemListener removeItemListener) {
        this.removeItemListener = removeItemListener;
    }

    /**
     * Añade un bocadillo a la lista de bocadillos pedidos.
     */
    public void addSandwich(Sandwich sandwich) {
        sandwichesOrdered.add(sandwich);
        /* Este método es para notificar que se ha añadido un item a la lista y que por tanto esta
        debe recargarse  */
        notifyDataSetChanged();
    }

    /**
     * Elimina un bocadillo de la lista de bocadillos pedidos.
     */
    public void removeSandwich(int position) {
        sandwichesOrdered.remove(position);
       /* Este método es para notificar que se ha eliminado un item de la lista y hacer uso de las
       animaciones predeterminadas de RecyclerView */
       notifyItemRemoved(position);
    }

    /**
     * Calcula el coste total de los bocadillos pedidos.
     */
    public float calculateTotalPrice() {
        float totalPrice = 0;
        for (Sandwich sandwich : sandwichesOrdered) {
            totalPrice += sandwich.getPrice();
        }
        return totalPrice;
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
