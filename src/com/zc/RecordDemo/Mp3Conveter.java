package com.zc.RecordDemo;

/**
 * 7/4/14  1:20 PM
 * Created by JustinZhang.
 */
public class Mp3Conveter {
    public static final int NUM_CHANNELS = 1;
    public static final int SAMPLE_RATE = 16000;
    public static final int BITRATE = 8;
    public static final int MODE = 1;
    public static final int QUALITY = 5;

    public native void initEncoder(int numChannels, int sampleRate, int bitRate, int mode, int quality);

    public native void destroyEncoder();

    public native void encodeFile(String sourcePath, String targetPath);

    static {
        System.loadLibrary("mp3lame");
    }

    public Mp3Conveter() {

        initEncoder(NUM_CHANNELS, SAMPLE_RATE, BITRATE, MODE, QUALITY);

    }
}
