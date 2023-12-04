package com.example.urbus_firebase;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urbus_firebase.R;
import com.google.android.material.tabs.TabLayout;

public class HorarioFragment extends Fragment {

    public HorarioFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_horario, container, false);

        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
        ViewPager viewPager = rootView.findViewById(R.id.viewPager);

        // Conectar el TabLayout y ViewPager
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
}
