package org.git.joribiz.bocadillo.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.git.joribiz.bocadillo.R;
import org.git.joribiz.bocadillo.models.Sandwich;

import java.util.ArrayList;
import java.util.Locale;

public class SandwichListAdapter extends RecyclerView.Adapter<SandwichListAdapter.ViewHolder> {
    private ArrayList<Sandwich> sandwiches;
    private ItemClickListener itemClickListener;

    /**
     * Cualquier actividad que use este adaptador, podrá disponer de esta interfaz para responder
     * a los clicks del usuario.
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView sandwichPhoto;
        TextView sandwichName;
        TextView sandwichPrice;

        ViewHolder(View itemView) {
            super(itemView);
            sandwichPhoto = itemView.findViewById(R.id.item_sandwich_photo);
            sandwichName = itemView.findViewById(R.id.item_sandwich_name);
            sandwichPrice = itemView.findViewById(R.id.item_sandwich_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public SandwichListAdapter(ArrayList<Sandwich> sandwiches) {
        this.sandwiches = new ArrayList<>();
        this.sandwiches.addAll(sandwiches);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sandwich, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sandwich sandwich = sandwiches.get(position);
        holder.sandwichPhoto.setImageResource(sandwich.getPhotoId());
        holder.sandwichName.setText(sandwich.getName());
        holder.sandwichPrice.setText(
                String.format(Locale.getDefault(), "%.2f €", sandwich.getPrice())
        );
    }

    @Override
    public int getItemCount() {
        return sandwiches.size();
    }

    /**
     * Método auxiliar para obtener el ítem clickado por el usuario.
     */
    public Sandwich getItem(int position) {
        return sandwiches.get(position);
    }

    /**
     * Con este método, sobreescribiremos la respuesta a los clicks del usuario.
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
