package com.bestog.pals.objects;

/**
 * Clas: ProviderResponse
 */
public class ProviderResponse {

    // Response (success)
    public final String response;
    // Response (error)
    public final String error;
    // Is response successful?
    public boolean success = false;

    /**
     * Constructor
     *
     * @param response String
     * @param error    String
     */
    public ProviderResponse(String response, String error) {
        this.success = (response.length() > 0);
        this.response = response;
        this.error = error;
    }

}
