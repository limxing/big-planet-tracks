# How To Save Google Maps For Offline Use On Android #

[Google Maps](http://maps.google.com/) is one of the most popular online mapping service which is introduced by Google Inc.

Suppose you are planning for a vacation and need a route map, you can save Google Maps for offline viewing on [Android](http://code.google.com/android/).

The following is the step-by-step of using the PC program [Mobile Atlas Creator](http://mobac.sourceforge.net/) (MOBAC) to create the SQLite database named `BigPlanet_maps.sqlitedb` which can be used by Android apps such as [Big Planet Tracks](http://code.google.com/p/big-planet-tracks/) and [RMaps](http://robertdeveloper.blogspot.com/search/label/rmaps.release):

  1. Install the [Java Runtime Environment](http://java.sun.com/javase/downloads/index.jsp) (JRE or JDK, at least JRE v1.6.0\_14 or later).
  1. Download and decompress [Mobile\_Atlas\_Creator\_1.7\_SQLite.zip](http://android-map.googlecode.com/files/Mobile_Atlas_Creator_1.7_SQLite.zip) (Official Edition + SQLite Libs + Settings for "BigPlanet SQLite" format).
  1. Copy the `libsqlite_jni` corresponding to your OS platform to the MOBAC folder (which `Mobile_Atlas_Creator.jar` is located).
  1. Double click the `Mobile Atlas Creator.exe` on Windows (Run `start.sh` on Linux).
  1. Use the left mouse button to select the download area and right mouse button to move the map after executing the MOBAC.
  1. Click the `"Add selection"` button after selecting the multiple `"Zoom Levels"` of the map.
  1. Confirm the `"Map Source"` to be `"Google Maps"` (default value, you can choose another types).
  1. Confirm the `"Format"` of `"Atlas settings"` to be `"BigPlanet SQLite"` (default value).
  1. Start downloading the map after clicking the `"Create atlas"` button.
  1. After the download is finished, please click the `"Open Atlas Folder"` button to get the database named `BigPlanet_maps.sqlitedb`.
  1. Copy `BigPlanet_maps.sqlitedb` to Android phone's SD card whose folder is `"/sdcard/RMaps/maps/BigPlanetTracks.sqlitedb"` (You can use any filename as long as you use the same filename extension above).
  1. Make sure to enable Android phone's `My Location` setting (check `"Use wireless networks"` and/or `"Use GPS satellites"`).
  1. Finally, please execute Big Planet Tracks or RMaps, the map will be moving to your location (if you successfully receive the latitude and longitude coordinates from Internet or GPS satellites).

![![](http://lh3.ggpht.com/_SZmMjCe-aB0/TCxML_tPCfI/AAAAAAAAUFc/cSwHILWPFEA/s850/Mobile_Atlas_Creator_1.7%20Win7.png)](http://lh3.ggpht.com/_SZmMjCe-aB0/TCxML_tPCfI/AAAAAAAAUFc/cSwHILWPFEA/Mobile_Atlas_Creator_1.7%20Win7.png)

Please visit [here](http://android-map.blogspot.com/2010/02/mobile-atlas-creator-17.html) for Chinese version.