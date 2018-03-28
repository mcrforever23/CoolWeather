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
	private TextView cityNameText;//��ʾ������
	private TextView publishText;//��ʾ����ʱ��
	private TextView weatherDespText;//��ʾ��������
	private TextView temp1Text;//��ʾ����1
	private TextView temp2Text;//��ʾ����2
	private TextView currentDateText;//��ʾ��ǰ����
	private Button switchCity;//�л����а�ť
	private Button refreshWeather;//����������ť
	private TextView littletips;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//��ʼ�����ֿؼ�
		weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText=(TextView)findViewById(R.id.city_name);
		publishText=(TextView)findViewById(R.id.publish_text);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
		temp1Text=(TextView)findViewById(R.id.temp1);
		temp2Text=(TextView)findViewById(R.id.temp2);
		currentDateText=(TextView)findViewById(R.id.current_date);
		String countryCode=getIntent().getStringExtra("country_code");
		if(!TextUtils.isEmpty(countryCode)){
			//���ؼ�����ʱ��ȥ��ѯ����
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		}else{
			//û���ؼ�����ʱ��ֱ����ʾ��������
			showWeather();
		}
		switchCity =(Button)findViewById(R.id.switch_city);
		refreshWeather=(Button)findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}
	public void onClick(View v)//���غ�ˢ�°�ť�ĵ���¼�
	{
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent=new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			Toast.makeText(this, "ͬ�����", Toast.LENGTH_SHORT).show();
			SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode=prefs.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherInfo(weatherCode);
			}
			publishText.setText("����"+prefs.getString("publish_time", "")+"����");
			break;
		default:
			break;
		}
	}
	
	//��ѯ�ؼ���������Ӧ����������
	private void queryWeatherCode(String countryCode){
		String address = "http://www.weather.com.cn/data/list3/city"+
	countryCode+".xml";
		queryFromServer(address,"countryCode");
	}
	//��ѯ�������Ŷ�Ӧ���������
	private void queryWeatherInfo(String weatherCode){
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey="+
	weatherCode;
		Log.d("WeatherActivity","����queryserver");
		queryFromServer(address,"weatherCode");
		Log.d("WeatherActivity","�������");
	}
	//���ݴ���ĵ�ַ���������������ѯ�������Ż���������Ϣ
	private void queryFromServer(final String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){
			public void onFinish(final String response){
				Log.d("WeatherActivity","����onfinish����");
				if("countryCode".equals(type)){
					Log.d("WeatherActivity","countrycode��ȷ");
					if(!TextUtils.isEmpty(response)){
						//�ӷ��������ص������н�������������
					   Log.d("WeatherActivity","��������������������");
						String[] array=response.split("\\|");
						if(array!=null && array.length==2){
							String weatherCode=array[1];
							Log.d("WeatherActivity","����������ѯģ��");
							queryWeatherInfo(weatherCode);	
						
					
						}
							
					}
				}else if ("weatherCode".equals(type)){
					//������������ص�������Ϣ
					Log.d("WeatherActivity","weathercode��ȷ");
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					Log.d("WeatherActivity","ִ���괦��json����");
					runOnUiThread(new Runnable(){
						public void run(){
							showWeather();
							Log.d("WeatherActivity","��ʾ�������");
						}
					});
				}
			}
			
			public void onError(Exception e){				
				runOnUiThread(new Runnable(){
					public void run(){
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	//��SharedPreferences�ļ���ȡ�洢��������Ϣ������ʾ��������
	private void showWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����"+prefs.getString("publish_time", "")+"����");
		//currentDateText.setText(prefs.getString("current_date", ""));
	    currentDateText.setText(prefs.getString("little_tips", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent=new Intent(this,AutoUpdateService.class);
		startService(intent);
	}
}
