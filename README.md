# Privacy Aware Location Service Library
[![Download](https://api.bintray.com/packages/bestog/pals/pals/images/download.svg)](https://bintray.com/bestog/pals/pals/_latestVersion)
[![API](https://img.shields.io/badge/API-11%2B-green.svg)](https://github.com/bestog/pals-lib/tree/master)
[![Build Status](https://travis-ci.org/bestog/pals-lib.svg?branch=master)](https://travis-ci.org/bestog/pals-lib)

__Privacy Aware Location Service Library for Android -- MLS, OpenCellID, OpenBMap and OpenWLANMap__

The GPS localization is realized in Android through a Google service. Google can thereby save user profiles and track the movements of the user. This library prevents the localization on Google interface and uses only free-usable anonymous location-services.
Data on the surrounding wireless networks and cell towers are sent to the location-service. As a result, you get the calculated approximate GPS location of the user.

_Be clever and keep your private data anonymous!_

__Pull-Requests are allowed and encouraged!__

## Getting Started

__Maven__
```xml
<dependency>
  <groupId>com.bestog.pals</groupId>
  <artifactId>pals</artifactId>
  <version>2.3</version>
  <type>pom</type>
</dependency>
```

__Gradle__
```java
compile 'com.bestog.pals:pals:2.3'
```

## Usage
__AndroidManifest.xml__
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
// Only for submit-request
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

__Basics__
```java
// Initialize Pals
Pals pals = new Pals(context);

// Enable Provider
// - Location-Provider with own token
pals.enableProvider(LocationProvider.PROVIDER_MOZILLA, "1a2b3c4d5e6f7g8hi9");
// - Location-Provider with fallback-token
pals.enableProvider(LocationProvider.PROVIDER_MOZILLA);
// Disable Provider
pals.disabledProvider(LocationProvider.PROVIDER_MOZILLA);
...
```
[Available Location-Provider](#provider)

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

Support the location services and collect new data!
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

## <a name="provider"></a>Provider

| Location-Provider | Constant | Request | Submit | Access-Token |
| :--- | :--- | :---: | :---: | :---: |
|Mozilla Location|`LocationProvider.PROVIDER_MOZILLA`|**X**|**X**|**X**|
|OpenCellID|`LocationProvider.PROVIDER_OPENCELLID`|**X**| |**X**|
|OpenBMap|`LocationProvider.PROVIDER_OPENBMAP`|**X**| | |
|OpenWLANMap|`LocationProvider.PROVIDER_OPENMAP`|**X**| | |
|Google Geolocation|`LocationProvider.PROVIDER_GOOGLE`|**X**| |**X**|

## Functions

#### enableProvider
Enable a location-provider for the geolocation.
```java
// with own token
void enableProvider(String provider, String token);
// with fallback-token
void enableProvider(String provider);
```

#### disableProvider
Disable a location-provider for the geolocation.
```java
void disableProvider(String provider);
```

#### getEnabledProviders
Get all enabled location-provider as a list with location-provider-objects
```java
Map<String, LocationProvider> getEnabledProviders();
```

#### getEnabledProviderList
Get all enabled location-provider as a list
```java
ArrayList<String> getEnabledProviderList();
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

## License

    Copyright 2016 - bestog

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
