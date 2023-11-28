package com.example.urbus_firebase;

import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig {
    public static void init(Context context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
