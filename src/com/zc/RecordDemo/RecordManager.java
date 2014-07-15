package com.zc.RecordDemo;

import android.media.MediaRecorder;
import android.os.Environment;

/**
 * 7/4/14  12:19 PM
 * Created by JustinZhang.
 */
public class RecordManager {

    private MediaRecorder mRecorder = null;
    private String filePath = null;

    public static final int NUM_CHANNELS = 1;
    public static final int SAMPLE_RATE = 8000;
    public static final int BITRATE = 16;
    public static final int MODE = 1;
    public static final int QUALITY = 2;

    public RecordManager() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioChannels(NUM_CHANNELS);
        mRecorder.setAudioSamplingRate(SAMPLE_RATE);
        mRecorder.setAudioEncodingBitRate(BITRATE);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        filePath = Environment.getExternalStorageDirectory()+"/audio.3pg";


    }
}
