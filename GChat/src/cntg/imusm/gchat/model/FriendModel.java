package cntg.imusm.gchat.model;

import com.google.android.gms.maps.model.LatLng;

public class FriendModel {
	public FriendModel(String name, String status, double lat, double lng) {
		this.name = name;
		this.status = status;
		this.longitude = lng;
		this.latitude = lat;
	}

	public String name;
	public double longitude;
	public double latitude;
	public String status;
	
	public LatLng getLatLng() {
		return new LatLng(this.latitude, this.longitude);
	}
}
