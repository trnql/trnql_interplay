package com.trnql.sample_interplay;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.trnql.smart.base.SmartCompatActivity;
import com.trnql.smart.location.AddressEntry;
import com.trnql.smart.weather.WeatherEntry;


public class WeatherData extends SmartCompatActivity {

private WeatherEntry weather = MainActivity.lastWeather;

private TextView currentConditions;
private TextView highLow;
private TextView feelsLike;
private TextView forecast;
private TextView rain;
private TextView wind;
private TextView uvIndex;
private TextView humidity;
private TextView sunrise;
private TextView sunset;
private TextView learnMoreText;


@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_weather_data);

  _initViews();
  _updateViews();
}

  @Override
  protected void smartWeatherChange(WeatherEntry weather) {
    this.weather = weather;
    _updateViews();
  }

  private void _updateViews() {
  if (weather != null) {
    currentConditions.setText(weather.getCurrentConditionsDescriptionAsString());
    highLow.setText(weather.getHiLoAsString());
    feelsLike.setText(weather.getFeelsLikeAsString());
    forecast.setText(weather.getForecastAsString());
    rain.setText(weather.getRainAsString());
    wind.setText(weather.getWindAsString());
    uvIndex.setText(weather.getUVIndexAsString());
    humidity.setText(weather.getHumidityAsString());
    sunrise.setText(weather.getSunriseAsString());
    sunset.setText(weather.getSunsetAsString());

    if (rain.length() == 0) {
      rain.setText("Dry as a bone");
    }
  }
}

private void _initViews() {
  currentConditions = (TextView) findViewById(R.id.tv_current_conditions);
  highLow = (TextView) findViewById(R.id.tv_high_low);
  feelsLike = (TextView) findViewById(R.id.tv_feels_like);
  forecast = (TextView) findViewById(R.id.tv_forecast);
  rain = (TextView) findViewById(R.id.tv_rain);
  wind = (TextView) findViewById(R.id.tv_wind);
  uvIndex = (TextView) findViewById(R.id.tv_uv_index);
  humidity = (TextView) findViewById(R.id.tv_humidity);
  sunrise = (TextView) findViewById(R.id.tv_sunrise);
  sunset = (TextView) findViewById(R.id.tv_sunset);
  learnMoreText = (TextView) findViewById(R.id.tv_learn_more);
  learnMoreText.setClickable(true);
  learnMoreText.setMovementMethod(LinkMovementMethod.getInstance());
  String text = "<a href='http://www.trnql.com/guides/'> Learn how at trnql.com/guides </a>";
  learnMoreText.setText(Html.fromHtml(text));
}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
  // Inflate the menu; this adds items to the action bar if it is present.
  getMenuInflater().inflate(R.menu.menu_weather_data, menu);
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
