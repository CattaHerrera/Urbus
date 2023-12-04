package com.example.urbus_firebase;

import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodedWaypoint;

import javax.security.auth.callback.Callback;

    public interface DirectionsApiCallback  extends PendingResult.Callback<DirectionsResult> {
        void onDirectionsSuccess(DirectionsResult directionsResult, GeocodedWaypoint[] geocodedWaypoints);
        void onDirectionsFailure(Throwable throwable);
    }


