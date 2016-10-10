package com.means.rabbit.utils;

import net.duohuo.dhroid.util.Perference;

public class RabbitPerference extends Perference {
	// 第一次登陆
	public int isFirst = 0;

	public String name;
	public String email;
	public String groupname;
	public String nickname;
	public String ordercount;
	public String phone;
	public String sex;
	public String faceimg_s;
	public String msgcount;
	public String catid = "1";
	public String cityname;
	public String password;
	public int langType = 0;

	public void clear() {
		this.name = "";
		this.groupname = "";
		this.nickname = "";
		this.ordercount = "";
		this.phone = "";
		this.sex = "";
		this.faceimg_s = "";
		this.msgcount = "";
		this.password = "";
		commit();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOrdercount() {
		return ordercount;
	}

	public void setOrdercount(String ordercount) {
		this.ordercount = ordercount;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFaceimg_s() {
		return faceimg_s;
	}

	public void setFaceimg_s(String faceimg_s) {
		this.faceimg_s = faceimg_s;
	}

	public String getMsgcount() {
		return msgcount;
	}

	public void setMsgcount(String msgcount) {
		this.msgcount = msgcount;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public int getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}

	public int getLangType() {
		return langType;
	}

	public void setLangType(int langType) {
		this.langType = langType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	

}
