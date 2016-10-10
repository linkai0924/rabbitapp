package com.means.rabbit.bean;

import android.text.TextUtils;

public class DistrictEB {

	public String countryid;
	public String cityid;
	public String districtid;
	
	public String countryName = "";
	public String cityName = "";
	public String districtName = "";
	
	public String territoryName(){
		String str = countryName;
		if (!TextUtils.isEmpty(cityName)) {
			str += "/" + cityName ;
		}
		if (!TextUtils.isEmpty(districtName)) {
			str += "/" + districtName ;
		}
		return str;
	}
	
	public String getCountryid() {
		return countryid;
	}
	public void setCountryid(String countryid) {
		this.countryid = countryid;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getDistrictid() {
		return districtid;
	}
	public void setDistrictid(String districtid) {
		this.districtid = districtid;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}


}
