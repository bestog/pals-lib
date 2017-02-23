package com.bestog.pals.objects;

public class ProviderResponse {

    public final String response;
    public final String error;
    public boolean success = false;

    public ProviderResponse(String response, String error) {
        this.success = (response.length() > 0);
        this.response = response;
        this.error = error;
    }

}
