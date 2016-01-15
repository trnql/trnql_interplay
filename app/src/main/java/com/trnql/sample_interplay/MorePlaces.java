package com.trnql.sample_interplay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.trnql.smart.places.PlaceEntry;

import java.util.List;

/**
 * Created by dustin on 11/11/15.
 */
public class MorePlaces extends AppCompatActivity{

    List<PlaceEntry> places;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_places);

        places = MainActivity.lastPlaceList;
        ListView listView = (ListView) findViewById(R.id.place_list);
        ArrayAdapter<PlaceEntry> adapter = new ArrayAdapter<PlaceEntry>(this, android.R.layout.simple_list_item_1, places);
        listView.setAdapter(adapter);

    }
}
