package com.trnql.sample_interplay;

import android.os.Bundle;
import android.widget.TextView;
import com.trnql.smart.base.SmartCompatActivity;
import com.trnql.smart.people.PersonEntry;

import java.util.List;

/**
 * Created by dustin on 11/24/15.
 */
public class PeopleData extends SmartCompatActivity {

    TextView peopleData;
    TextView peopleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_data);
        peopleData = (TextView) findViewById(R.id.tv_people_data);
        peopleTitle = (TextView) findViewById(R.id.tv_people_title);
        _setupUI(MainActivity.latestPeople);
    }

    @Override
    protected void smartPeopleChange(List<PersonEntry> people) {
        _setupUI(MainActivity.latestPeople);
    }

    private void _setupUI(List<PersonEntry> people){
        if(people != null) {
            int searchRadius = getPeopleManager().getSearchRadius();
            String userToken = getPeopleManager().getUserToken();
            peopleTitle.setText("SmartPeople Data for users within " + searchRadius + " meters\n\n" +
            "Your user token: " + userToken + "\n");


            StringBuilder sb = new StringBuilder();
            for (PersonEntry person : people) {
                sb.append(person.toString());
                sb.append("\n");
            }
            peopleData.setText(sb.toString());
        }
    }
}
