package tyt.android.bigplanettracks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MyLocationService extends Service implements LocationListener {
	
	public static int Notification_RecordTrack = 0;
	private static NotificationManager mNotificationManager;
	
	private long minTime; // ms
	private float minDistance; // m
	private Handler locationHandler;
	
	public MyLocationService() {
		minTime = BigPlanet.minTime;
		minDistance = BigPlanet.minDistance;
		locationHandler = BigPlanet.locationHandler;
	}
	
	@Override
	public void onCreate() {
		Log.d("MyLocationService", "Service: onCreate()");
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		setNotification(this, Notification_RecordTrack);
		//startGPSLocationListener();
		if (locationHandler != null) {
			Message m = locationHandler.obtainMessage(BigPlanet.MethodStartGPSLocationListener, 0, 0, null);
			locationHandler.sendMessage(m);
		} else {
			// stop service
			Intent intent = new Intent(this, MyLocationService.class);
			this.stopService(intent);
		}
	}
	
	@Override
	public void onDestroy() {
		clearNotification(Notification_RecordTrack);
		if (!BigPlanet.isFollowMode)
			BigPlanet.finishGPSLocationListener();
		Log.d("MyLocationService", "Service: onDestroy()");
	}
	
	@Override
	public IBinder onBind(Intent i) {
		return null;
	}
	
	@Override
	public void onLocationChanged(Location location) {
//		String longitude = String.valueOf(location.getLongitude());
//		String latitude = String.valueOf(location.getLatitude());
//		Log.i("Location", location.getProvider()+" onLocationChanged(): latitude="+latitude+", longitude="+longitude);
		BigPlanet.currentLocation = location;
		BigPlanet.inHome = true;
		BigPlanet.isMapInCenter = false;
		if (!BigPlanet.isGPSTracking) {
			if (BigPlanet.isFollowMode) {
				//goToMyLocation(location, PhysicMap.getZoomLevel());
				Message m = locationHandler.obtainMessage(BigPlanet.MethodGoToMyLocation, 0, 0, location);
				locationHandler.sendMessage(m);
			} 
			// non-reach code because GPS is disabled
//			else {
//				addMarker(location, PhysicMap.getZoomLevel());
//			}
		} else { // isGPSTracking = true
			if ((location.hasAccuracy() && location.getAccuracy()<30) || !location.hasAccuracy()) {
				if (BigPlanet.isFollowMode) {
					//trackMyLocation(location, PhysicMap.getZoomLevel());
					Message m = locationHandler.obtainMessage(BigPlanet.MethodTrackMyLocation, 0, 0, location);
					locationHandler.sendMessage(m);
				} else {
					//addMarker(location, PhysicMap.getZoomLevel());
					Message m = locationHandler.obtainMessage(BigPlanet.MethodAddMarker, 0, 0, location);
					locationHandler.sendMessage(m);
					//mapControl.invalidate(); // not works if leaving activity and entering again
					//mapControl.updateScreen(); // works
					m = locationHandler.obtainMessage(BigPlanet.MethodUpdateScreen, 0, 0, null);
					locationHandler.sendMessage(m);
				}
			}
		}
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		Log.i("Location", provider + " is disabled.");
		if (provider.equals("gps")) {
			String networkProvider = LocationManager.NETWORK_PROVIDER;
			BigPlanet.locationManager.requestLocationUpdates(networkProvider, minTime, minDistance, BigPlanet.networkLocationListener);
			Log.i("Location", networkProvider +" requestLocationUpdates() "+ minTime +" "+ minDistance);
		}
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		Log.i("Location", provider + " is enabled.");
		if (provider.equals("gps")) {
			BigPlanet.locationManager.requestLocationUpdates(provider, minTime, minDistance, BigPlanet.gpsLocationListener);
		} else {
			BigPlanet.locationManager.requestLocationUpdates(provider, minTime, minDistance, BigPlanet.networkLocationListener);
		}
		Log.i("Location", provider +" requestLocationUpdates() "+ minTime +" "+ minDistance);
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		int numSatellites = extras.getInt("satellites", 0);
		BigPlanet.locationProvider = provider+" "+status+" "+numSatellites;
		if (status == 0) {
			Log.i("Location", provider + " is OUT OF SERVICE");
		} else if (status == 1) {
			Log.i("Location", provider + " is TEMPORARILY UNAVAILABLE");
			// invoke network's requestLocationUpdates() if not tracking
			if (provider.equals("gps") && !BigPlanet.isGPSTracking) {
				String networkProvider = LocationManager.NETWORK_PROVIDER;
				BigPlanet.locationManager.requestLocationUpdates(networkProvider, minTime, minDistance, BigPlanet.networkLocationListener);
				Log.i("Location", networkProvider +" requestLocationUpdates() "+ minTime +" "+ minDistance);
			}
		} else {
			Log.i("Location", provider + " is AVAILABLE");
			// gpsLocationListener has higher priority than networkLocationListener
			if (provider.equals("gps")) {
				BigPlanet.locationManager.removeUpdates(BigPlanet.networkLocationListener);
			}
		}
		//BigPlanet.setActivityTitle(BigPlanet.this);
		Message m = locationHandler.obtainMessage(BigPlanet.MethodSetActivityTitle, 0, 0, null);
		locationHandler.sendMessage(m);
	}
	
	protected static void setNotification(Context context, int notificationId) {
		int iconId = 0;
		String contentTitle = null;
		String contentText = null;
		
		if (notificationId == Notification_RecordTrack) {
			iconId = R.drawable.globe;
			contentTitle = context.getString(R.string.app_name);
			contentText = context.getString(R.string.notify_recording);
		}
		
		Intent notifyIntent = new Intent(context, BigPlanet.class);
		PendingIntent pendingIntent = 
			PendingIntent.getActivity(context, 0, notifyIntent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

		Notification notification = new Notification();
		notification.flags = Notification.FLAG_NO_CLEAR;
		notification.icon = iconId;
		notification.defaults = Notification.DEFAULT_SOUND;
		
		notification.setLatestEventInfo(context, contentTitle, contentText, pendingIntent);
		mNotificationManager.notify(notificationId, notification);
	}
	
	protected static void clearNotification(int notificationId) {
		mNotificationManager.cancel(notificationId);
	}
}