package model;

public class Country {
	private int id;
	private String countryName;
	private String countryCode;
	private int cityId;
	
	public int getID() {
		return id;
	}
	public void  setID(int id) {
		this.id=id;
		
	}
	public String getCountryName(){
		return countryName;
	}
	public void setCountryName(String cityName){
		this.countryName=cityName;
	}
	public String getCountryCode(){
		return countryCode;
	}
	public void setCountryCode(String countryCode){
		this.countryCode=countryCode;
	}
	public int getCityId(){
		return cityId;
	}
	public void setCityId(int cityId){
		this.cityId=cityId;
	}
}
