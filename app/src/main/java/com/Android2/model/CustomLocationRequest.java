package com.Android2.model;

import android.location.LocationManager;

public class CustomLocationRequest{
	
	private long mPollInterval = 1000;
	private float mMinDistinPoll = 1.f;
	private int mNumOfUpdates = 0;
	private String mLocationProvider = LocationManager.GPS_PROVIDER;
	
	public CustomLocationRequest(){
	}
	
	public void setPollInterval(long interval){
		mPollInterval = interval;
	}
	
	public void setMinDistanceForPoll(float dist){
		mMinDistinPoll = dist;
	}
	
	public void setLocationProvider(String provider){
		mLocationProvider = provider;
	}
	
	public long getPollInterval(){
		return mPollInterval;
	}
	
	public float getMinDistanceForPoll(){
		return mMinDistinPoll;
	}
	
	public String getLocationProvider(){
		return mLocationProvider;
	}
	
	public void setNumberOfUpdates(int num){
		mNumOfUpdates = num;
	}
	
	public int getNumberOfUpdates(){
		return mNumOfUpdates;
	}
}