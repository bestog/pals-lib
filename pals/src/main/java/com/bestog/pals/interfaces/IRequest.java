package com.bestog.pals.interfaces;

import com.bestog.pals.objects.GeoResult;

/**
 * Interface: IRequest
 */
public interface IRequest {
    /**
     * Request is complete
     *
     * @param geoResult GeoResult
     * @param valid     boolean
     */
    void onComplete(GeoResult geoResult, boolean valid);
}
