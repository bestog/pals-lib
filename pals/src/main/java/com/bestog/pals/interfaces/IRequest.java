package com.bestog.pals.interfaces;

import com.bestog.pals.utils.GeoResult;

public interface IRequest {
    void onComplete(GeoResult geoResult, boolean valid);
}
