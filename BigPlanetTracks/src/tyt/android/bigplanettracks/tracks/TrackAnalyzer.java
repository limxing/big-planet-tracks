package tyt.android.bigplanettracks.tracks;

import java.util.ArrayList;
import java.util.Collections;

import tyt.android.bigplanettracks.BigPlanet;

import android.location.Location;
import android.util.Log;

/**
 * @author TYTung, taiyuchen
 * @version 0.1
 */
public class TrackAnalyzer {
	
	public static final int measureVersion = 1;
	
	private String trackName;
	private String trackDescription;
	private String startGMTTime;
	private ArrayList<Location> locationList;
	private	String trackSource;
	
	private ArrayList<Float> speedList;
	private long totalTime;
	private float totalDistance;
	private float averageSpeed;
	private float maximumSpeed;
	private int trackPoints;
	
	public TrackAnalyzer(String trackName, String trackDescription, String startGMTTime, 
			ArrayList<Location> locationList, String trackSource) {
		this.trackName = trackName;
		this.trackDescription = trackDescription;
		this.startGMTTime = startGMTTime;
		this.locationList = locationList;
		this.trackSource = trackSource;
		
		speedList = new ArrayList<Float>();
		totalTime = 0;
		totalDistance = 0f;
		averageSpeed = 0f;
		maximumSpeed = 0f;
		trackPoints = locationList.size();
	}
	
	public TrackAnalyzer(String trackName, String trackDescription, String startGMTTime, 
			ArrayList<Location> locationList) {
		this(trackName, trackDescription, startGMTTime, locationList, null);
	}
	
	private void analyze() {
		Log.i("Message", "Perform TrackAnalyzer");
		if (locationList.size() > 1) {
			computeTotalTime();
			computeTotalDistance();
			computeAverageSpeed();
			computeMaximumSpeed();
		}
		Log.i("Message", "totalTime="+MyTimeUtils.getTimeString(totalTime));
		Log.i("Message", "totalDistance="+totalDistance+"m");
		Log.i("Message", "averageSpeed="+averageSpeed+"km/hr");
		Log.i("Message", "maximumSpeed="+maximumSpeed+"km/hr");
		Log.i("Message", "trackPoints="+trackPoints);
	}
	
	public void analyzeAndUpdate(long trackID) {
		Log.i("Message", "trackName="+trackName);
		Log.i("Message", "trackDescription="+trackDescription);
		Log.i("Message", "trackStartGMTTime="+startGMTTime);
		analyze();
		BigPlanet.DBAdapter.open();
		BigPlanet.DBAdapter.updateTrack(trackID, totalTime, totalDistance, 
				averageSpeed, maximumSpeed, trackPoints, measureVersion);
		Log.i("Message", "measureVersion has been updated: "+measureVersion);
	}

	public void analyzeAndSave() {
		Log.i("Message", "trackName="+trackName);
		Log.i("Message", "trackDescription="+trackDescription);
		Log.i("Message", "trackStartGMTTime="+startGMTTime);

		BigPlanet.DBAdapter.open();
		long trackID = BigPlanet.DBAdapter.insertTrack(trackName, trackDescription, startGMTTime, locationList, trackSource);
		Log.i("Message", "insertTrack() finished");
		
		analyze();
		
		BigPlanet.DBAdapter.updateTrack(trackID, totalTime, totalDistance, 
				averageSpeed, maximumSpeed, trackPoints, measureVersion);
		Log.i("Message", "Insert a new track successfully");
		Log.i("Message", "-------------------------------");
	}
	
	private void computeTotalTime() {
		Location firstLocation = locationList.get(0);
		Location lastLocation = locationList.get(locationList.size()-1);
		long firstTimePoint = firstLocation.getTime();
		long lastTimePoint = lastLocation.getTime();
		totalTime = lastTimePoint-firstTimePoint;
	}
	
	private void computeTotalDistance() {
		totalDistance = 0;
		float speed;
		for (int i=0; i<locationList.size(); i++) {
			if (i >= 1) {
				Location location = locationList.get(i);
				Location previous_location = locationList.get(i-1);
				float distance = location.distanceTo(previous_location);
//				if (distance <= 10) {
					totalDistance = totalDistance + distance;
//				} else {
//					totalDistance = totalDistance + 0;
//				}
				long time = location.getTime()-previous_location.getTime();
//				System.out.println("ms M: "+time+"\t"+distance);
				if (distance == 0 || time == 0) {
					speed = 0f;
				} else {
					speed = (distance / (time/1000)) *3600/1000; // km/hr
				}
				speedList.add(speed);
			}
		}
		// filter out the possible incorrect speed
		ArrayList<Float> candicateSpeedList = new ArrayList<Float>();
		for (int i=0; i<speedList.size(); i++) {
//			System.out.println(i+"\t"+speedList.get(i));
			if (i >= 2) {
				float nextSpeed = speedList.get(i);
				float previousSpeed = (speedList.get(i-1)+speedList.get(i-2))/2;
				if (Math.abs(nextSpeed - previousSpeed) < 1.5) { // magic number
					candicateSpeedList.add(nextSpeed);
				}
			}
		}
		if (candicateSpeedList.size() > 0) {
			speedList.clear();
			speedList.addAll(candicateSpeedList);
		}
	}
	
	private void computeAverageSpeed() {
		averageSpeed = (totalDistance / (totalTime/1000)) *3600/1000; // km/hr
	}
	
	private void computeMaximumSpeed() {
		Collections.sort(speedList);
		if (speedList.size() > 0) {
			maximumSpeed = speedList.get(speedList.size()-1);
		} else {
			maximumSpeed = 0;
		}
	}
	
	public long getTotalTime() { 
		return totalTime;
	}
	
	public float getTotalDistance() {
		return totalDistance;
	}
	
	public float getAverageSpeed() {
		return averageSpeed;
	}
	
	public float getMaximumSpeed() {
		return maximumSpeed;
	}
	
	public int getTrackPoints() {
		return locationList.size();
	}
	
}
