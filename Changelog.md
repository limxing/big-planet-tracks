# Changelog #

### 2011/07/10 : Big Planet Tracks 2.1 ###
  * Add the orange arrow for indicating the phone's direction using the magnetic heading function from source code of MyTracks
  * Add map magnification (including 1.5x, 2.0x, 2.5x, 3.0x levels)
  * Show live/real-time statistics such as time, speed, distance, and elevation. (i.e. Display them while track is being recorded.)
  * Optimize the map cache mechanism of the map engine to avoid the map display twinkling problem
  * Disable the incremental zooming effect to improve the performance
  * Improve the compatibility of importing a GPX file that lacks of time field
  * Redesign the operation mode of Bookmark function
  * Be compatible with Android 3.1 (Honeycomb) Tablet
<br />

### 2010/07/16 : Big Planet Tracks 2.0a ###
  * Enable the background service when recording a track
  * Keep the screen on when viewing the map
  * Make the map moving more smoother
  * ~~Intelligent power saving: enable GPS receiver (it will consume power) only when recording a track or in auto-follow mode~~
  * Start receiving the GPS locations when starting the app, and stop receiving the GPS locations only when leaving the app
<br />

### 2010/06/17 : Big Planet Tracks 2.0 ###
  * Rename from Big Planet TW to Big Planet Tracks
  * Import GPS track files (.gpx) into the database
  * Record GPS track and display on the map simultaneously
  * Export GPS track to a file (.gpx)
  * Show a GPS track's time, distance, average speed, maximum speed and number of waypoints
  * Full support all timezones, show the correct GMT time of GPS tracks
  * Display the loaded track and the recording track on the map simultaneously
  * Full support for different screen resolutions of the mobile phones
  * Intelligent power saving: enable GPS receiver (it will consume power) only when recording a track or in auto-follow mode
<br />

### 2010/04/18 : Big Planet TW 1.6.8 ###
  * Online maps (Network needed)
  * Offline maps (SQLite database created by using Mobile Atlas Creator 1.7)
  * Change different SQLite database maps dynamically (e.g. Taiwan.sqlitedb, Japan.sqlitedb, and so on.)
  * Map zoom in/out
  * Go to my location (i.e. auto-follow the GPS locations) on the map
  * Search (Network needed)
  * Bookmarks
  * Adjust map offset (mainly solve the map shift issue in China)
  * Reference scale on the map