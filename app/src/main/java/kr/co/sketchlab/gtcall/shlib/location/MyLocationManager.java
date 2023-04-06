package kr.co.sketchlab.gtcall.shlib.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import java.util.List;

/**
 * Created by shp on 2017. 3. 10..
 */


public class MyLocationManager implements LocationListener {
    public static interface TimeoutListenerFromManager {
        public void onFired(boolean isGpsOnly);
    }
    public static interface LocationChangeListenerFromManager {
        public void onChange(Location current);
    }

    private Context mContext;
    private LocationManager lmgr;
    private Location mCurrentLocation;
    //	private TimeoutListener listener2 = null;
//	private LocationChangeListener listener = null;
    // test
    private TimeoutListenerFromManager listenerTimeout = null;
    private LocationChangeListenerFromManager listenerChange = null;
    private String provider = null;

    private static final int MILLISECONDS_PER_SECOND = 1000; // Milliseconds per second
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;// 5초마다 갱신
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;  // Update frequency in milliseconds

    private long TIMEOUT = 8000;

    public MyLocationManager(Context context) {
        mContext = context;
        lmgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void setTimeout(long timeout) {
        this.TIMEOUT = timeout;
    }

    public Location getLastLocation() {
        if (provider == null)
            return null;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return lmgr.getLastKnownLocation(provider);
    }

    public boolean isGpsOnly() {
        if (lmgr == null) {
            return false;
        }

        List<String> providerList = lmgr.getProviders(true);
        for (String e : providerList) {
            Log.e(getClass().getSimpleName(), e);
        }
        if (!providerList.contains(LocationManager.NETWORK_PROVIDER) && providerList.contains(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean enableLocation(boolean bsingle) {
//        LogUtil.logError(getClass(), "enableLocation");
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        provider = lmgr.getBestProvider(criteria, true);
//        LogUtil.logError(getClass(), "best provider : " + provider);

        if (provider == null) {
//            LogUtil.logError(getClass(), "provider is null");
            return false;
        } else {
            mCurrentLocation = null;

            Looper myLooper = Looper.myLooper();
            final Handler myHandler = new Handler(myLooper);
            myHandler.postDelayed(new Runnable() {
                public void run() {
                    if (mCurrentLocation == null) {
                        // test
//		        		 if(listener2!=null) {
//		        			 listener2.onFired();
//		        		 }
                        if (listenerTimeout != null) {
                            listenerTimeout.onFired(isGpsOnly());
                        }
                    }
//                    disableLocation();
                }
            }, TIMEOUT);

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return false;
            }
            lmgr.requestLocationUpdates(provider, UPDATE_INTERVAL, 1, this, myLooper);

//			if(bsingle == false){
//				lmgr.requestLocationUpdates(provider, UPDATE_INTERVAL, 50, this, myLooper);
//				lmgr.requestSingleUpdate(provider, this, myLooper); // test
//			}
//		    else {
//	    		List<String> providers = lmgr.getAllProviders();
//
//				if(providers.contains(LocationManager.NETWORK_PROVIDER)) {
//					lmgr.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,this,myLooper);
//				} else {
//					return false;
//				}
//		    }
            return true;
        }
    }

    public void disableLocation() {
        //LogUtil.logError(getClass(), "disableLocation");
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        try {
            lmgr.removeUpdates(this);
        } catch (Exception e) {

        }
    }

    @Override
    public void onLocationChanged(Location arg0) {
        //LogUtil.logError(MyLocationManager2.class,"onLocationChanged");
//        disableLocation();
        mCurrentLocation = arg0;

        //Util.setSavedLocation(mContext, arg0.getLatitude(), arg0.getLongitude());

//		if(listener!=null){
//			listener.onChange(mCurrentLocation);
//		}

        // test
        if(listenerChange != null) {
            listenerChange.onChange(mCurrentLocation);
        }
    }

    //    public void setLocationChangeListener(LocationChangeListener listener){
//    	this.listener = listener;
//    }
    // test
    public void setLocationChangeListener(LocationChangeListenerFromManager listener) {
        this.listenerChange = listener;
    }
    public void setTimeoutListener(TimeoutListenerFromManager listener) {
        this.listenerTimeout = listener;
    }
    // test end
//    public void setTimeoutListener(TimeoutListener listener){
//    	this.listener2 = listener;
//    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//		switch(status){
//			case LocationProvider.OUT_OF_SERVICE:
//				Toast.makeText(mContext,R.string.outofservice, Toast.LENGTH_SHORT).show();
//				break;
//			case LocationProvider.TEMPORARILY_UNAVAILABLE:
//				Toast.makeText(mContext, R.string.tempunavailable, Toast.LENGTH_SHORT).show();
//				break;
//			case LocationProvider.AVAILABLE:
//				Toast.makeText(mContext, R.string.available, Toast.LENGTH_SHORT).show();
//				break;
//		}
    }


    public interface OnSingleLocationReceivedListener {
        void onSingleLocationReceived(double lat, double lng);
    }
    public static void getSingleLocation(Context context, int timeout, final OnSingleLocationReceivedListener listener) {
        if(listener == null) {
            return;
        }

        final MyLocationManager locationManager = new MyLocationManager(context);
        locationManager.setLocationChangeListener(new MyLocationManager.LocationChangeListenerFromManager() {
            @Override
            public void onChange(Location current) {
                locationManager.disableLocation();
                listener.onSingleLocationReceived(current.getLatitude(), current.getLongitude());
            }
        });
        locationManager.setTimeoutListener(new MyLocationManager.TimeoutListenerFromManager() {
            @Override
            public void onFired(boolean isGpsOnly) {
                locationManager.disableLocation();
                Location lastLocation = locationManager.getLastLocation();
                double lat = 0, lng = 0;
                if(lastLocation != null) {
                    lat = lastLocation.getLatitude();
                    lng = lastLocation.getLongitude();
                }
                listener.onSingleLocationReceived(lat, lng);
            }
        });
        locationManager.setTimeout(timeout);
        locationManager.enableLocation(false);
    }
}
