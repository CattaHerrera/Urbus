package com.example.urbus_firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {

    private OnVerMapaClickListener onVerMapaClickListener;

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options, OnVerMapaClickListener onVerMapaClickListener) {
        super(options);
        this.onVerMapaClickListener = onVerMapaClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {

        holder.nombre.setText(model.getNombre());
        holder.origen.setText(model.getOrigen());
        holder.destino.setText(model.getDestino());
        holder.costo.setText(model.getCosto());
        holder.tiempo.setText(model.getTiempo());
        holder.parada.setText(model.getParada());
        holder.distancia.setText(model.getDistancia());

        Glide.with(holder.img.getContext())
                .load(model.getSurl())
                .placeholder(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onVerMapaClickListener != null) {
                    onVerMapaClickListener.onVerMapaClick(model);
                }
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView nombre, origen, destino, costo, tiempo, parada, distancia;
        Button btnVerMapa;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img1);
            nombre = itemView.findViewById(R.id.txtNombre);
            origen = itemView.findViewById(R.id.txtOrigen);
            destino = itemView.findViewById(R.id.txtDestino);
            costo = itemView.findViewById(R.id.txtCosto);
            tiempo = itemView.findViewById(R.id.txtTiempo);
            parada = itemView.findViewById(R.id.txtParada);
            distancia = itemView.findViewById(R.id.txtDistancia);

            btnVerMapa = itemView.findViewById(R.id.btnVerMapa);
        }
    }

    // Mueve la interfaz aquí
    public interface OnVerMapaClickListener {
        void onVerMapaClick(MainModel mainModel);
    }
}
