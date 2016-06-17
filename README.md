# Privacy Aware Location Service Library
[![Download](https://api.bintray.com/packages/bestog/pals/pals/images/download.svg)](https://bintray.com/bestog/pals/pals/_latestVersion)
[![API](https://img.shields.io/badge/API-11%2B-green.svg)](https://github.com/bestog/pals-lib/tree/master)

__Privacy Aware Location Service Library for Android -- MLS, OpenCellID, OpenBMap and OpenWLANMap__

The GPS localization is realized in Android through a Google service. Google can thereby save user profiles and track the movements of the user. This library prevents the localization on Google interface and uses only free-usable anonymous location-services.
Data on the surrounding wireless networks and cell towers are sent to the location-service. As a result, you get the calculated approximate GPS location of the user.

_Be clever and keep your private data anonymous!_

__Pull-Requests are allowed and encouraged!__

## Getting Started

__Maven__
```
<dependency>
  <groupId>com.bestog.pals</groupId>
  <artifactId>pals</artifactId>
  <version>2.0</version>
  <type>pom</type>
</dependency>
```

__Gradle__
```
compile 'com.bestog.pals:pals:2.0'
```

## Usage
__Permissions (AndroidManifest)__
```
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>
// Only for submit-request
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

__Basics__
```java
// Initialize Pals
Pals pals = new Pals(context);
// Enable Provider
pals.enableProvider(LocationProvider.PROVIDER_MOZILLA);
pals.enableProvider(LocationProvider.PROVIDER_OPENBMAP);
...
```
__Request__
A request that returns the current approximate position of the user as a result.
```java
pals.request(new IRequest() {
    @Override
    public void onComplete(GeoResult result, boolean valid) {
        // Result - Example -----------------
        double lat = result.getLatitude();
        double lon = result.getLongitude();
        int acc = result.getAccuracy();
        boolean validRequest = valid;
        // ----  YOUR CODE ------------------
    }
});
```
__Submit__
To guarantee accurate position of a user, the database of location-services with new data needs to be filled.
Help us with it and collect new data!
```java
pals.submit(new ISubmit() {
    @Override
    public void onComplete(boolean valid) {
        // Result - Example -----------
        boolean validSubmit = valid;
        // ----  YOUR CODE ------------
    }
});
```

## Provider

__Mozilla Location Service__
`LocationProvider.PROVIDER_MOZILLA`
[+] Request
[+] Submit

__OpenBMap__
`LocationProvider.PROVIDER_OPENBMAP`
[+] Request
[-] Submit

__OpenCellID__
`LocationProvider.PROVIDER_OPENCELLID`
[+] Request
[-] Submit

__OpenMap__
`LocationProvider.PROVIDER_OPENMAP`
[+] Request
[-] Submit

__Google Geolocation__
`LocationProvider.PROVIDER_GOOGLE`
[+] Request
[-] Submit

## Functions

#### enableProvider
Enable a location-provider for the geolocation.
```java
void enableProvider(String provider);
```

#### disableProvider
Disable a location-provider for the geolocation.
```java
void disableProvider(String provider);
```

#### enabledProvider
Get all enabled location-provider as a comma-seperated string
```java
String enabledProvider();
```

#### request
Executes a request and sends at the end the result to the listener.
```java
void request(IRequest listener);
```

#### submit
Executes a submit, collect data from current position and send to a location-provider.
```java
void submit(ISubmit listener);
```

#### isProviderEnabled
Is the specific location-provider enabled?
```java
boolean isProviderEnabled(String provider);
```

#### setTrilaterateAlg
Set a specific algorithm for the calculation
```java
void setTrilaterateAlg(String alg);
```