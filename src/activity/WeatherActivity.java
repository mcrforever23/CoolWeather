package activity;




import service.AutoUpdateService;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

import com.coolweather.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;//显示城市名
	private TextView publishText;//显示发布时间
	private TextView weatherDespText;//显示天气描述
	private TextView temp1Text;//显示气温1
	private TextView temp2Text;//显示气温2
	private TextView currentDateText;//显示当前日期
	private Button switchCity;//切换城市按钮
	private Button refreshWeather;//更新天气按钮
	private TextView littletips;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//初始化各种控件
		weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText=(TextView)findViewById(R.id.city_name);
		publishText=(TextView)findViewById(R.id.publish_text);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
		temp1Text=(TextView)findViewById(R.id.temp1);
		temp2Text=(TextView)findViewById(R.id.temp2);
		currentDateText=(TextView)findViewById(R.id.current_date);
		String countryCode=getIntent().getStringExtra("country_code");
		if(!TextUtils.isEmpty(countryCode)){
			//有县级代号时就去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		}else{
			//没有县级代号时就直接显示本地天气
			showWeather();
		}
		switchCity =(Button)findViewById(R.id.switch_city);
		refreshWeather=(Button)findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}
	public void onClick(View v)//返回和刷新按钮的点击事件
	{
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent=new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			Toast.makeText(this, "同步完成", Toast.LENGTH_SHORT).show();
			SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode=prefs.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherInfo(weatherCode);
			}
			publishText.setText("今天"+prefs.getString("publish_time", "")+"发布");
			break;
		default:
			break;
		}
	}
	
	//查询县级代号所对应的天气代号
	private void queryWeatherCode(String countryCode){
		String address = "http://www.weather.com.cn/data/list3/city"+
	countryCode+".xml";
		queryFromServer(address,"countryCode");
	}
	//查询天气代号对应的天气情况
	private void queryWeatherInfo(String weatherCode){
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey="+
	weatherCode;
		Log.d("WeatherActivity","进入queryserver");
		queryFromServer(address,"weatherCode");
		Log.d("WeatherActivity","进入完毕");
	}
	//根据传入的地址和类型向服务器查询天气代号或者天气信息
	private void queryFromServer(final String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
			public void onFinish(final String response){
				Log.d("WeatherActivity","进入onfinish方法");
				if("countryCode".equals(type)){
					Log.d("WeatherActivity","countrycode正确");
					if(!TextUtils.isEmpty(response)){
						//从服务器返回的数据中解析出天气代号
					   Log.d("WeatherActivity","服务器返回了天气代号");
						String[] array=response.split("\\|");
						if(array!=null && array.length==2){
							String weatherCode=array[1];
							Log.d("WeatherActivity","进入天气查询模块");
							queryWeatherInfo(weatherCode);	
						
					
						}
							
					}
				}else if ("weatherCode".equals(type)){
					//处理服务器返回的天气信息
					Log.d("WeatherActivity","weathercode正确");
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					Log.d("WeatherActivity","执行完处理json操作");
					runOnUiThread(new Runnable(){
						public void run(){
							showWeather();
							Log.d("WeatherActivity","显示天气情况");
						}
					});
				}
			}
			
			public void onError(Exception e){				
				runOnUiThread(new Runnable(){
					public void run(){
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	//从SharedPreferences文件读取存储的天气信息，并显示到界面上
	private void showWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天"+prefs.getString("publish_time", "")+"发布");
		//currentDateText.setText(prefs.getString("current_date", ""));
	    currentDateText.setText(prefs.getString("little_tips", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent=new Intent(this,AutoUpdateService.class);
		startService(intent);
	}
}
