package com.trnql.sample_interplay;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.trnql.smart.activity.ActivityEntry;
import com.trnql.smart.base.SmartCompatActivity;
import com.trnql.zen.core.observableprops.ObservablePropertyListener;


public class ActivityData extends SmartCompatActivity {

private ActivityEntry activityData = MainActivity.lastActivity;

private TextView vehicleTextValue;
private TextView bicycleTextValue;
private TextView walkingTextValue;
private TextView runningTextValue;
private TextView footTextValue;
private TextView stillTextValue;
private TextView tiltingTextValue;
private TextView learnMoreText;


@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_activity_data);

  _initViews();
  _updateViews();
}

  @Override
  protected void smartActivityChange(ActivityEntry userActivity) {
    activityData = userActivity;
    _updateViews();
  }

  private void _updateViews() {
  if (activityData != null) {
    vehicleTextValue.setText(String.valueOf(activityData.isInVehicle()));
    bicycleTextValue.setText(String.valueOf(activityData.isOnBicycle()));
    walkingTextValue.setText(String.valueOf(activityData.isWalking()));
    runningTextValue.setText(String.valueOf(activityData.isRunning()));
    footTextValue.setText(String.valueOf(activityData.isOnFoot()));
    stillTextValue.setText(String.valueOf(activityData.isStill()));
    tiltingTextValue.setText(String.valueOf(activityData.isTilting()));
  }
}

private void _initViews() {
  vehicleTextValue = (TextView) findViewById(R.id.tv_vehicle_value);
  bicycleTextValue = (TextView) findViewById(R.id.tv_bicycle_value);
  walkingTextValue = (TextView) findViewById(R.id.tv_walking_value);
  runningTextValue = (TextView) findViewById(R.id.tv_running_value);
  footTextValue = (TextView) findViewById(R.id.tv_foot_value);
  stillTextValue = (TextView) findViewById(R.id.tv_still_value);
  tiltingTextValue = (TextView) findViewById(R.id.tv_tilting_value);
  learnMoreText = (TextView) findViewById(R.id.tv_learn_more);
  learnMoreText.setClickable(true);
  learnMoreText.setMovementMethod(LinkMovementMethod.getInstance());
  String text = "<a href='http://www.trnql.com/guides/'> Learn how at trnql.com/guides </a>";
  learnMoreText.setText(Html.fromHtml(text));
}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
  // Inflate the menu; this adds items to the action bar if it is present.
  getMenuInflater().inflate(R.menu.menu_activity_data, menu);
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
