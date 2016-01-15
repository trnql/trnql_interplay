package com.trnql.sample_interplay;

/**
 * Created by dustin on 8/11/15.
 */
enum ImageSource {
    STREETMAPS, PLACES;

    private final String SM_API_KEY = "PUT_GOOGLE_MAPS_API_KEY_HERE";

    String buildUrlString(String latString, String lngString){
        if (this == STREETMAPS){
            return "https://maps.googleapis.com/maps/api/streetview?size=400x400&location=" + latString + "," + lngString + "&key=" + SM_API_KEY + "&fov=90&heading=150&pitch=10";
        }else if (this == PLACES){
            return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latString + "," + lngString + "&radius=750&key=" + SM_API_KEY;
        }
        return null;
    }

    String buildUrlString(String photoReference){
        return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference=" +
                photoReference + "&key=" + SM_API_KEY;
    }

    //NOIMAGE: String streetMapsUrl = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location=36.873141,-121.337644&key=" + SM_API_KEY + "&fov=90&heading=150&pitch=10";
}
