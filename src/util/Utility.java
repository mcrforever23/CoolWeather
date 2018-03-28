package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.CoolWeatherDB;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import model.City;
import model.Country;
import model.Province;

public class Utility {
	//处理返回的省级数据
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[]allProvinces=response.split(",");
			if(allProvinces !=null&&allProvinces.length>0){
				for(String p:allProvinces){
					String[]array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析出来的数据存储到Province表
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	//处理返回的市级数据
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities !=null&&allCities.length>0){
				for(String c:allCities){
					String[]array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	//处理返回的县级数据
	public static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB,
			String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCountries=response.split(",");
			if(allCountries!=null&&allCountries.length>0){
				for(String c:allCountries){
					String[]array=c.split("\\|");
					Country country=new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
			return false;
	}
	//解析服务器返回的JSON数据，并存储到本地
	public static void handleWeatherResponse(Context context,String response){
		try{
			JSONObject jsonObject=new JSONObject(response);
			JSONObject data=jsonObject.getJSONObject("data");
			JSONArray forecast=data.getJSONArray("forecast");

			String cityName=data.getString("city");
			
			String littletips=data.getString("ganmao");
			Log.d("wo", littletips);
			JSONObject array1=forecast.getJSONObject(0);
			String temp1=array1.getString("high");
			String temp2=array1.getString("low");
			String weatherDesp=array1.getString("type");
			String publishTime=array1.getString("date");
			saveWeatherInfo(context,cityName,temp1,temp2,
					weatherDesp,publishTime,littletips);
			
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	//将服务器返回的天气信息存储到sharedpreferences文件中
	public static void saveWeatherInfo(Context context,String cityName,
			String temp1,String temp2,String weatherDesp,String 
			publishTime,String littletips){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.putString("little_tips", littletips);
		editor.commit();
	}
	
	
	
}
