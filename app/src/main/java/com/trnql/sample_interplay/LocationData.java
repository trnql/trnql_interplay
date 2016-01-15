package com.trnql.sample_interplay;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.trnql.smart.base.SmartCompatActivity;
import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.location.LocationEntry;


public class LocationData extends SmartCompatActivity {

private LocationEntry lastLocation = MainActivity.lastLocation;
private AddressEntry  lastAddress  = MainActivity.lastAddress;

private TextView tv_latitude;
private TextView tv_longitude;
private TextView tv_altitude;
private TextView tv_bearing;
private TextView tv_speed;
private TextView tv_time;
private TextView tv_address;
private TextView tv_county;
private TextView tv_areaCode;
private TextView tv_country;
private TextView tv_countryCode;
private TextView tv_featureName;
private TextView learnMoreText;

@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_location_data);

  _initViews();
  _updateViews();
}

  @Override
  protected void smartLatLngChange(LocationEntry location) {
    lastLocation = location;
    _updateViews();
  }

  @Override
  protected void smartAddressChange(AddressEntry address) {
    lastAddress = address;
    _updateViews();
  }

  private void _updateViews() {
  if(lastLocation != null) {
    tv_latitude.setText(String.valueOf(lastLocation.getLatitude()));
    tv_longitude.setText(String.valueOf(lastLocation.getLongitude()));
    tv_altitude.setText(String.valueOf(lastLocation.getAltitude()));
    tv_bearing.setText(String.valueOf(lastLocation.getBearing()));
    tv_speed.setText(String.valueOf(lastLocation.getSpeed()));
    tv_time.setText(String.valueOf(lastLocation.getTime()));
  }
  if(lastAddress != null) {
    tv_address.setText(String.valueOf(lastAddress.toString()));
    tv_county.setText(String.valueOf(lastAddress.getSubAdminArea()));
    tv_areaCode.setText(String.valueOf(lastAddress.getPhone()));
    tv_country.setText(String.valueOf(lastAddress.getCountryName()));
    tv_countryCode.setText(String.valueOf(lastAddress.getCountryCode()));
    tv_featureName.setText(String.valueOf(lastAddress.getFeatureName()));
  }
}

private void _initViews() {
  tv_latitude = (TextView) findViewById(R.id.tv_latitude);
  tv_longitude = (TextView) findViewById(R.id.tv_longitude);
  tv_altitude = (TextView) findViewById(R.id.tv_altitude);
  tv_bearing = (TextView) findViewById(R.id.tv_bearing);
  tv_speed = (TextView) findViewById(R.id.tv_speed);
  tv_time = (TextView) findViewById(R.id.tv_time);
  tv_address = (TextView) findViewById(R.id.tv_address);
  tv_county = (TextView) findViewById(R.id.tv_county);
  tv_areaCode = (TextView) findViewById(R.id.tv_area_code);
  tv_country = (TextView) findViewById(R.id.tv_country);
  tv_countryCode = (TextView) findViewById(R.id.tv_country_code);
  tv_featureName = (TextView) findViewById(R.id.tv_feature);

  learnMoreText = (TextView) findViewById(R.id.tv_learn_more);
  learnMoreText.setClickable(true);
  learnMoreText.setMovementMethod(LinkMovementMethod.getInstance());
  String text = "<a href='http://www.trnql.com/guides/'> Learn how at trnql.com/guides </a>";
  learnMoreText.setText(Html.fromHtml(text));
}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
  // Inflate the menu; this adds items to the action bar if it is present.
  getMenuInflater().inflate(R.menu.menu_location_data, menu);
  return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
  // Handle action bar item clicks here. The action bar will
  // automatically handle clicks on the Home/Up button, so long
  // as you specify a parent activity in AndroidManifest.xml.
  int id = item.getItemId();

  //noinspection SimplifiableIfStatement
  if (id == R.id.action_settings) {
    return true;
  }

  return super.onOptionsItemSelected(item);
}
}
