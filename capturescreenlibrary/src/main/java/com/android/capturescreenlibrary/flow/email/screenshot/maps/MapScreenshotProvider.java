package com.android.capturescreenlibrary.flow.email.screenshot.maps;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.view.ViewGroup;

import com.android.capturescreenlibrary.flow.email.screenshot.BaseScreenshotProvider;
import com.android.capturescreenlibrary.utilities.ActivityUtils;
import com.android.capturescreenlibrary.utilities.Logger;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

import static android.view.View.VISIBLE;

public final class MapScreenshotProvider extends BaseScreenshotProvider {

    private static final Func2<Bitmap, List<LocatedBitmap>, Bitmap> BITMAP_COMBINING_FUNCTION
            = new Func2<Bitmap, List<LocatedBitmap>, Bitmap>() {
                @Override
                public Bitmap call(
                        final Bitmap baseLocatedBitmap,
                        final List<LocatedBitmap> overlayLocatedBitmaps) {

                    final Canvas canvas = new Canvas(baseLocatedBitmap);

                    for (final LocatedBitmap locatedBitmap : overlayLocatedBitmaps) {
                        final int[] overlayLocation = locatedBitmap.getLocation();

                        canvas.drawBitmap(
                                locatedBitmap.getBitmap(),
                                overlayLocation[0],
                                overlayLocation[1],
                                MAP_PAINT);
                    }

                    return baseLocatedBitmap;
                }
    };

    private static final Paint MAP_PAINT = new Paint();

    static {
        MAP_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
    }

    public MapScreenshotProvider(
            @NonNull final Context applicationContext,
            @NonNull final Logger logger) {

        super(applicationContext, logger);
    }

    @NonNull
    @Override
    public Observable<Bitmap> getScreenshotBitmap(@NonNull final Activity activity) {
        final Observable<Bitmap> nonMapViewsBitmapObservable = getNonMapViewsBitmap(activity);

        final View rootView = ActivityUtils.getRootView(activity);
        final List<MapView> mapViews = locateMapViewsInHierarchy(rootView);

        if (mapViews.isEmpty()) {
            return nonMapViewsBitmapObservable;
        } else {
            final Observable<List<LocatedBitmap>> mapViewBitmapsObservable
                    = getMapViewBitmapsObservable(mapViews);

            return Observable
                    .zip(nonMapViewsBitmapObservable, mapViewBitmapsObservable, BITMAP_COMBINING_FUNCTION);
        }
    }

    @NonNull
    private Observable<List<LocatedBitmap>> getMapViewBitmapsObservable(@NonNull final List<MapView> mapViews) {
        return Observable
                .from(mapViews)
                .concatMap(new Func1<MapView, Observable<LocatedBitmap>>() {
                    @Override
                    public Observable<LocatedBitmap> call(@NonNull final MapView mapView) {
                        return MapBitmapObservable.create(mapView);
                    }
                })
                .toList();
    }

    @NonNull
    @VisibleForTesting
    protected List<MapView> locateMapViewsInHierarchy(@NonNull final View view) {
        final List<MapView> result = new ArrayList<>();

        final Queue<View> viewsToProcess = new LinkedList<>();
        viewsToProcess.add(view);

        while (!viewsToProcess.isEmpty()) {
            final View viewToProcess = viewsToProcess.remove();

            if (viewToProcess instanceof MapView && viewToProcess.getVisibility() == VISIBLE) {
                result.add((MapView) viewToProcess);
            } else if (viewToProcess instanceof ViewGroup) {
                final ViewGroup viewGroup = (ViewGroup) viewToProcess;

                for (int childIndex = 0; childIndex < viewGroup.getChildCount(); childIndex++) {
                    viewsToProcess.add(viewGroup.getChildAt(childIndex));
                }
            }
        }

        return result;
    }

}
