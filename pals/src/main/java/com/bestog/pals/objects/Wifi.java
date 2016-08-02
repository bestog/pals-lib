package com.bestog.pals.objects;

/**
 * Class: Wifi
 *
 * @author bestog
 */
public class Wifi {

    public String mac;
    public int channel;
    public int freq;
    public int signal;

    public Wifi() {
    }

    public Wifi(String mac, int channel, int freq, int signal) {
        this.mac = mac;
        this.channel = channel;
        this.freq = freq;
        this.signal = signal;
    }
}
