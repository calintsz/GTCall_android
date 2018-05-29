package kr.co.sketchlab.gtcall.shlib.location;

public class MyLocation {
	private String title;
	private double longitude;
	private double latitude;
	private int	ID;
	private String spotID;
	
	// 해외 위치 여부
	private boolean isOversea = false;
	
	public MyLocation(){
		ID = 0;
		longitude = 0;
		latitude = 0;
		spotID = "0";
		title= "title";
		
		isOversea = false;
	}
		
	public MyLocation(int id){
		this();
		ID = id;
	}
	public int getID(){
		return ID;
	}
	public void setID(int id){
		ID= id;
	}

	public void setLocation(double lati, double longi){
		longitude = longi;
		latitude = lati;
	}

	public void setTitle(String ti){
		title = ti;
	}
	public String getTitle(){
		return title;
	}
	public void setSpotID(String id){
		spotID=id;
	}
	public String getSpotID(){
		return spotID;
	}
	
	public double getLongitude(){
		return longitude;
	}	
	public double getLatitude(){
		return latitude;
	}

	// 해외 위치여부
	public boolean isOversea() {
		return isOversea;
	}

	// 해외위치여부 설정
	public void setOversea(boolean oversea) {
		isOversea = oversea;
	}

	@Override
	public boolean equals(Object obj) {
		MyLocation o = (MyLocation) obj;
		if(this.latitude == o.latitude && this.longitude == o.longitude) {
			return true;
		}
		return false;
	}
}