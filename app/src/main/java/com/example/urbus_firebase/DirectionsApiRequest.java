package com.example.urbus_firebase;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodedWaypoint;

public class DirectionsApiRequest {
    private DirectionsApiCallback callback;

    public void setCallback(DirectionsApiCallback callback) {
        this.callback = callback;
    }

    // Otros métodos de DirectionsApiRequest...

    // Llamar al método correspondiente de tu callback donde sea necesario
    public void onResponse() {
        if (callback != null) {
            DirectionsResult directionsResult = new DirectionsResult();
            GeocodedWaypoint[] geocodedWaypoints = new GeocodedWaypoint[0];
            callback.onDirectionsSuccess(directionsResult, geocodedWaypoints);
        } else {
            // Manejar el caso en que no hay un callback configurado
        }
    }
}
