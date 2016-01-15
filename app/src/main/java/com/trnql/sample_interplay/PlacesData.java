package com.trnql.sample_interplay;

import android.os.Bundle;
import android.widget.TextView;
import com.trnql.smart.base.SmartCompatActivity;
import com.trnql.smart.places.PlaceEntry;
import com.trnql.smart.places.PlaceType;

/**
 * Created by dustin on 11/11/15.
 */
public class PlacesData extends SmartCompatActivity {

    PlaceEntry place;

    TextView tv_name;
    TextView tv_lat;
    TextView tv_lng;
    TextView tv_address;
    TextView tv_website;
    TextView tv_placeId;
    TextView tv_vicinity;
    TextView tv_rating;
    TextView tv_iconURL;
    TextView tv_price;
    TextView tv_phoneNum;
    TextView tv_phoneNumIntl;
    TextView tv_placeUrl;
    TextView tv_hours;
    TextView tv_types;
    TextView tv_reviews;
    TextView tv_images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_data);

        place = MainActivity.lastPlace;

        _initViews();
        _updateViews();
    }

    private void _updateViews() {
        if(place != null){
            if(place.getName() != null){
                tv_name.setText(place.getName());
            }
            tv_lat.setText(String.valueOf(place.getLatitude()));
            tv_lng.setText(String.valueOf(place.getLongitude()));
            if(place.getAddress() != null){
                tv_address.setText(place.getAddress());
            }
            if(place.getPlaceId() != null){
                tv_placeId.setText(place.getPlaceId());
            }
            if(place.getWebsite() != null){
                tv_website.setText(place.getWebsite());
            }
            if(place.getVicinity() != null){
                tv_vicinity.setText(place.getVicinity());
            }
            if(place.getIconUrl() != null){
                tv_iconURL.setText(place.getIconUrl());
            }
            if(place.getPriceLevelString() != null){
                tv_price.setText(place.getPriceLevelString());
            }
            if(place.getPhoneNumber() != null){
                tv_phoneNum.setText(place.getPhoneNumber());
            }
            if(place.getIntlPhoneNumber() != null){
                tv_phoneNumIntl.setText(place.getIntlPhoneNumber());
            }
            if(place.getGoogleMapsUrl() != null){
                tv_placeUrl.setText(place.getGoogleMapsUrl());
            }
            if(place.getHoursString() != null){
                tv_hours.setText(place.getHoursString());
            }
            if(place.getTypes() != null){
                StringBuilder sb = new StringBuilder();
                String prefix = "";
                for(PlaceType type : place.getTypes()){
                    sb.append(prefix);
                    prefix = ", ";
                    sb.append(type.toString());
                }
                tv_types.setText(sb.toString());
            }
            if(place.getRating() != null){
                tv_rating.setText(String.valueOf(place.getRating()));
            }
            if(place.getImages() != null){
                tv_images.setText("Found " + place.getImages().size() + " images for this place");
            }
            if(place.getReviews() != null){
                tv_reviews.setText("Found " + place.getReviews().size() + " reviews for this place");
            }
        }
    }

    private void _initViews() {
        tv_name = (TextView) findViewById(R.id.tv_place_name);
        tv_lat = (TextView) findViewById(R.id.tv_place_lat);
        tv_lng = (TextView) findViewById(R.id.tv_place_lng);
        tv_address = (TextView) findViewById(R.id.tv_place_address);
        tv_website = (TextView) findViewById(R.id.tv_places_website);
        tv_placeId = (TextView) findViewById(R.id.tv_place_id);
        tv_vicinity = (TextView) findViewById(R.id.tv_place_vicinity);
        tv_rating = (TextView) findViewById(R.id.tv_place_rating);
        tv_iconURL = (TextView) findViewById(R.id.tv_place_iconurl);
        tv_price = (TextView) findViewById(R.id.tv_place_price);
        tv_phoneNum = (TextView) findViewById(R.id.tv_place_phone);
        tv_phoneNumIntl = (TextView) findViewById(R.id.tv_place_phone_intl);
        tv_placeUrl = (TextView) findViewById(R.id.tv_place_url);
        tv_hours = (TextView) findViewById(R.id.tv_place_hours);
        tv_types = (TextView) findViewById(R.id.tv_place_types);
        tv_reviews = (TextView) findViewById(R.id.tv_place_reviews);
        tv_images = (TextView) findViewById(R.id.tv_places_images);
    }
}
