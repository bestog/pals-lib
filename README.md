# Privacy Aware Location Service Library
[![API](https://img.shields.io/badge/API-11%2B-green.svg)](https://github.com/florent37/ViewAnimator/tree/master)


GPS is the most common approach to locate a smartphone-user. The GPS location is determined in Android by Google services. Google stores user profiles about their movement. Thus privacy is not protected!

This library uses location-services to treat the data anonymous. These can be add to your project as a library.

__Pull Requests are welcome!__

## Getting Started
1. Download the [latest version](https://github.com/bestog/pals-lib/releases) from GitHub
2. Copy the `palslib.jar` file in your `/libs/`-directory
3. Add this library to your dependencies
4. Ready!

## Usage
```java
// Initialize Pals
Pals pals = new Pals(Context ctx);

// Enable Provider
pals.enableProvider(LocationProvider.PROVIDER_MOZILLA);
pals.enableProvider(LocationProvider.PROVIDER_OPENBMAP);
...

// Request
pals.request(new IRequest() {
    @Override
    public void onComplete(GeoResult result, boolean valid) {
    	// Result - Example -----------------
        // double lat = result.getLatitude();
        // double lon = result.getLongitude();
        // int lat = result.getAccuracy();
        // boolean validRequest = valid;
        // ----  YOUR CODE ------------------
    }
});
```

## Provider
`LocationProvider.PROVIDER_MOZILLA`

`LocationProvider.PROVIDER_OPENBMAP`

`LocationProvider.PROVIDER_GOOGLE`

`LocationProvider.PROVIDER_OPENCELLID`

`LocationProvider.PROVIDER_OPENMAP`

## Functions

#### enableProvider
Enable a provider for the geolocation.
```java
enableProvider(String provider);
```

#### disableProvider
Disable a provider for the geolocation.
```java
disableProvider(String provider);
```

#### request
Executes a request and sends at the end the result to the listener.
```java
request(IRequest listener);
```

#### isProviderEnabled
Is LocationProvider enabled?
```java
isProviderEnabled(String provider);
```
