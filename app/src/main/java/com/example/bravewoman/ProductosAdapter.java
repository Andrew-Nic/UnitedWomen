package com.example.bravewoman;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;


public class ProductosAdapter extends FirestoreRecyclerAdapter<ListProductos, ProductosAdapter.productosViewHolder> {


    private OnItemClickListener listener;

    public ProductosAdapter(@NonNull FirestoreRecyclerOptions<ListProductos> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull productosViewHolder holder, int position, @NonNull ListProductos model) {
        holder.vNomProducto.setText(model.getmNombreProducto());
        holder.vNomNegocio.setText(model.getmNombreNegocio());
        holder.vPreProducto.setText("$ "+model.getPrecioProducto());

        Picasso.get().load(model.getFotoProducto()).into(holder.vFotoProducto);
    }

    @NonNull
    @Override
    public productosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_publicaciones,parent,false);
        return new productosViewHolder(view);
    }

    class productosViewHolder extends RecyclerView.ViewHolder {
        private TextView vNomProducto, vNomNegocio,vPreProducto;
        private ImageView vFotoProducto;


        public productosViewHolder(@NonNull View itemView) {
            super(itemView);

            vNomProducto = itemView.findViewById(R.id.nombreProducto);
            vNomNegocio = itemView.findViewById(R.id.nombreNegocio);
            vPreProducto = itemView.findViewById(R.id.precioProducto);
            vFotoProducto = itemView.findViewById(R.id.fotoProducto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener!=null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
