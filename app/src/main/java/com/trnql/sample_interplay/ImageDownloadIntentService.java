package com.trnql.sample_interplay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.trnql.smart.base.SmartIntentService;
import com.trnql.zen.core.observableprops.ImageHelper;
import com.trnql.zen.rpcclient.NetCallback;
import com.trnql.zen.utlis.AndroidUtils;
import com.trnql.zen.utlis.IconPaths;
import org.json.JSONArray;
import zen.rpc.json.me.JSONObject;


public class ImageDownloadIntentService extends SmartIntentService {
    private static final String ACTION_DOWNLOAD_IMAGE = "com.trnql.sample_interplay.action.DOWNLOAD_STREETMAP_IMAGE";

    private static final int MIN_VALID_FILE_SIZE_BYTES = 7500;

    // TODO: Rename parameters
    // private static final String EXTRA_URL = "com.trnql.sample_interplay.extra.URL";
    private static final String EXTRA_LAT = "com.trnql.sample_interplay.extra.LAT";
    private static final String EXTRA_LNG = "com.trnql.sample_interplay.extra.LNG";

    private static final long TIME_WINDOW_FOR_REFRESH_MS = 1000 * 60 * 2; // 2 mins
    private static final int NUM_QUERIES_PER_WINDOW = 2;
    private static final long MIN_REQUEST_INTERVAL_MS = 500;
    private static long lastWindowStart = 0;
    private static long numQueriesSinceLastWindowStart = 0;
    private static long lastQueryTime = 0;

    static boolean rateLimitNotExceeded() {
        // refresh time window if enough time has elapsed
        long timeSinceLastWindowStart = System.currentTimeMillis() - lastWindowStart;
        boolean timeWindowValidForRefresh = timeSinceLastWindowStart > TIME_WINDOW_FOR_REFRESH_MS;
        if (timeWindowValidForRefresh) {
            AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: Refreshing Time Window for ImageDownloadIntentService");
            lastWindowStart = System.currentTimeMillis();
            numQueriesSinceLastWindowStart = 0;
        }
        boolean queriesBelowThreshold = numQueriesSinceLastWindowStart < NUM_QUERIES_PER_WINDOW;
        long timeSinceLastQuery = System.currentTimeMillis() - lastQueryTime;
        boolean lastQueryThresholdMet = timeSinceLastQuery > MIN_REQUEST_INTERVAL_MS;
        AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: numQueriesSinceLastWindowStart: " + numQueriesSinceLastWindowStart,
                ", timeSinceLastQuery: " + timeSinceLastQuery + "ms");
        return queriesBelowThreshold && lastQueryThresholdMet;
    }

    public static void startActionDownloadImage(Context context, String latString, String lngString) {
        Intent intent = new Intent(context, ImageDownloadIntentService.class);
        intent.setAction(ACTION_DOWNLOAD_IMAGE);
        intent.putExtra(EXTRA_LAT, latString);
        intent.putExtra(EXTRA_LNG, lngString);
        context.startService(intent);
    }


    public ImageDownloadIntentService() {
        super("ImageDownloadIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            lastQueryTime = System.currentTimeMillis();

            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_IMAGE.equals(action)) {
                final String latString = intent.getStringExtra(EXTRA_LAT);
                final String lngString = intent.getStringExtra(EXTRA_LNG);
                handleActionDownloadImage(latString, lngString);
            }
        }
    }

    private void handleActionDownloadImage(final String latString, final String lngString) {

        String streetMapUrl = ImageSource.STREETMAPS.buildUrlString(latString, lngString);

        Runnable r = getAppData().netClient.createHttpGETTask(getAppData(),
                streetMapUrl,
                null,
                new NetCallback() {
                    public void begin_inBGT() {
                        AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: SV image download begin", latString, ", " + lngString);
                        numQueriesSinceLastWindowStart++;
                    }

                    public void ok_inBGT(final byte[] imgBytes) {
                        AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: SV image download complete", latString, ", " + lngString);
                        if (_isValidImage(imgBytes)) {
                            AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: SV img valid, setting OP for img and time", latString, ", " + lngString);
                            //Bitmap bm = _convertBytesToBitmap(imgBytes);
                            JSONObject jsonObject = ImageHelper.getJSONwithImage(imgBytes);
                            getPopManager().setValue(R.id.pop_location_bitmap, jsonObject);
                            //getObservablePropertyManager().setValue(R.id.op_location_bitmap, bm);
                        } else {
                            AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: SV img invalid, trying GP", latString, ", " + lngString);
                            String placesUrl = ImageSource.PLACES.buildUrlString(latString, lngString);
                            _queryNearbyPlaces(placesUrl);
                        }
                    }
                });
        getAppData().netClient.runRpcTask(r);

    }

    private void _queryNearbyPlaces(String url) {
        Runnable r =
                getAppData()
                        .netClient
                        .createHttpGETTask(getAppData(),
                                url,
                                null,
                                new NetCallback() {
                                    @Override
                                    public void begin_inBGT() {
                                        AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: GP places query begin");
                                    }

                                    @Override
                                    public void ok_inBGT(byte[] bb) {
                                        String photo = _parsePlacesQuery(bb);
                                        if (photo != null) {
                                            String photoUrl = ImageSource.PLACES.buildUrlString(photo);
                                            _downloadPlacesPhoto(photoUrl);
                                        } else {
                                            AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: photoreference is null / not found.");
                                            numQueriesSinceLastWindowStart--;
                                        }
                                    }
                                });
        getAppData().netClient.runRpcTask(r);
    }

    private String _parsePlacesQuery(byte[] bb) {
        AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: Starting JSON Decoding of places query");
        String jsonResponseString = new String(bb);
        try {
            org.json.JSONObject jsonResponse = new org.json.JSONObject(jsonResponseString);
            JSONArray jsonResultsArray = jsonResponse.getJSONArray("results");
            for (int i = 0; i < jsonResultsArray.length(); i++) {
                org.json.JSONObject placeObject = jsonResultsArray.getJSONObject(i);
                if (placeObject.has("photos")) {
                    JSONArray photosArray = placeObject.getJSONArray("photos");
                    org.json.JSONObject firstPhoto = photosArray.getJSONObject(0);
                    String photoReference = firstPhoto.getString("photo_reference");
                    AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: photoreference: " + photoReference);
                    return photoReference;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void _downloadPlacesPhoto(final String url) {
        Runnable r =
                getAppData()
                        .netClient
                        .createHttpGETTask(getAppData(),
                                url,
                                null,
                                new NetCallback() {
                                    @Override
                                    public void begin_inBGT() {
                                        AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: GP img download begin", url);
                                    }

                                    @Override
                                    public void ok_inBGT(final byte[] imgBytes) {
                                        AndroidUtils.log(IconPaths.TRNQL_Check, "ISSUE13: GP img download complete", url);
                                        if (_isValidImage(imgBytes)) {

                                            JSONObject jsonObject = new JSONObject();
                                            ImageHelper.putImageInJSON(jsonObject, imgBytes);
                                            getPopManager().setValue(R.id.pop_location_bitmap, jsonObject);

                                            //Bitmap bm = _convertBytesToBitmap(imgBytes);
                                            //getObservablePropertyManager().setValue(R.id.op_location_bitmap, bm);
                                        } else {
                                            AndroidUtils.log(IconPaths.TRNQL_Check, "Google Places photo invalid. Do not change the OP");
                                            numQueriesSinceLastWindowStart--;
                                        }
                                    }
                                });
        getAppData().netClient.runRpcTask(r);
    }

    private boolean _isValidImage(byte[] imgBytes) {
        int byteArrayLength = imgBytes.length;
        AndroidUtils.log(IconPaths.TRNQL_Check, "image byteArrayLength length: " + String.valueOf(byteArrayLength));
        return byteArrayLength > MIN_VALID_FILE_SIZE_BYTES;
    }

    private Bitmap _convertBytesToBitmap(byte[] imgBytes) {
        Bitmap networkBitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        return networkBitmap;
    }
}
