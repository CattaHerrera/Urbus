package com.example.urbus_firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {


    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);

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


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView nombre, origen, destino, costo, tiempo, parada, distancia;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView) itemView.findViewById(R.id.img1);
            nombre = (TextView) itemView.findViewById(R.id.txtNombre);
            origen = (TextView) itemView.findViewById(R.id.txtOrigen);
            destino = (TextView) itemView.findViewById(R.id.txtDestino);
            costo = (TextView) itemView.findViewById(R.id.txtCosto);
            tiempo = (TextView) itemView.findViewById(R.id.txtTiempo);
            parada = (TextView) itemView.findViewById(R.id.txtParada);
            distancia = (TextView) itemView.findViewById(R.id.txtDistancia);
        }
    }

}
