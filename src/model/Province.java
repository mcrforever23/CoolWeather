package model;

public class Province {
	private int id;
	private String provinceName;
	private String provinceCode;
	
	public int getID() {
		return id;
	}
	public void  setID(int id) {
		this.id=id;
		
	}
	public String getProvinceName(){
		return provinceName;
	}
	public void setProvinceName(String provinceName){
		this.provinceName=provinceName;
	}
	public String getProvinceCode(){
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode){
		this.provinceCode=provinceCode;
	}

}
