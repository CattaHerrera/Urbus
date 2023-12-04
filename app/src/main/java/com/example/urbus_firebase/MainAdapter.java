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
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.MyViewHolder> {

    private OnVerMapaClickListener onVerMapaClickListener;
    private static OnFavoritoClickListener onFavoritoClickListener;


    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options, OnVerMapaClickListener onVerMapaClickListener, OnFavoritoClickListener onFavoritoClickListener) {
        super(options);
        this.onVerMapaClickListener = onVerMapaClickListener;
        this.onFavoritoClickListener = onFavoritoClickListener;

    }

    public interface OnFavoritoClickListener {
        void onFavoritoClick(MainModel mainModel);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainModel model) {
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

        // Configurar el botón de favoritos y su comportamiento
        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFavoritoClickListener != null) {
                    onFavoritoClickListener.onFavoritoClick(model);
                }
            }
        });

        // Cambiar el aspecto del botón según el estado de favorito
        if (model.isFavorito()) {
            holder.btnFav.setBackgroundResource(R.drawable.icon_favorito);  // Usar tu icono de corazón relleno
        } else {
            holder.btnFav.setBackgroundResource(R.drawable.icon_fav); // Usar tu icono de corazón vacío
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView nombre, origen, destino, costo, tiempo, parada, distancia;
        Button btnVerMapa, btnFav;

        public MyViewHolder(@NonNull View itemView) {
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
            btnFav = itemView.findViewById(R.id.btnFav);

            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFavoritoClickListener != null) {
                        onFavoritoClickListener.onFavoritoClick(getItem(getAdapterPosition()));
                    }
                }
            });
        }
    }

    // Interfaz para manejar clics en el botón "Ver Mapa"
    public interface OnVerMapaClickListener {
        void onVerMapaClick(MainModel mainModel);
    }


}
