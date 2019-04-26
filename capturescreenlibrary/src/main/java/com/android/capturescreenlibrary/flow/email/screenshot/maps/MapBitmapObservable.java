package com.android.capturescreenlibrary.flow.email.screenshot.maps;

import android.graphics.Bitmap;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import rx.Observable;
import rx.Subscriber;

/* default */ final class MapBitmapObservable {

    @MainThread
    /* default */ static Observable<LocatedBitmap> create(@NonNull final MapView mapView) {
        final int[] locationOnScreen = new int[] {0, 0};
        mapView.getLocationOnScreen(locationOnScreen);

        return Observable.create(new Observable.OnSubscribe<LocatedBitmap>() {
            @Override
            public void call(final Subscriber<? super LocatedBitmap> subscriber) {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull final GoogleMap googleMap) {
                        googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                            @Override
                            public void onSnapshotReady(@Nullable final Bitmap bitmap) {
                                if (bitmap != null) {
                                    subscriber.onNext(
                                            new LocatedBitmap(bitmap, locationOnScreen));

                                    subscriber.onCompleted();
                                } else {
                                    subscriber.onError(new MapSnapshotFailedException());
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private MapBitmapObservable() {
        // This constructor intentionally left blank.
    }

}
