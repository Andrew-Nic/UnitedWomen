package com.example.bravewoman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class tiendasAdapter extends FirestoreRecyclerAdapter <listTTiendas,tiendasAdapter.tiendasViewHolder> {


    public tiendasAdapter(@NonNull FirestoreRecyclerOptions<listTTiendas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull tiendasViewHolder holder, int position, @NonNull listTTiendas model) {
        holder.vNombreTienda.setText(model.getNombreNegocio());
        holder.vDescripcionTienda.setText(model.descNegocio);

        Picasso.get().load(model.getFotoPerfil()).into(holder.vPerfil);
    }

    @NonNull
    @Override
    public tiendasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.storeslist,parent,false);
        return new tiendasViewHolder(view);
    }


    public class tiendasViewHolder extends RecyclerView.ViewHolder {
        private TextView vNombreTienda,vDescripcionTienda;
        private ImageView vPerfil;

        public tiendasViewHolder(@NonNull View itemView) {
            super(itemView);

            vNombreTienda=itemView.findViewById(R.id.tvNombre);
            vDescripcionTienda=itemView.findViewById(R.id.tvDescription);
            vPerfil=itemView.findViewById(R.id.ivPerfil);
        }
    }
}
