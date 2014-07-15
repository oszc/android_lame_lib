package com.zc.RecordDemo;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;

import java.io.*;

/**
 * 7/1/14  11:00 AM
 * Created by JustinZhang.
 */
public class MyAudioRecorder {

    private static final String TAG = "MyAudioRecorder";
    private AudioRecord mRecorder = null;
    //private MediaPlayer mPlayer = null;

    public static final int SAMPLE_RATE = 16000;

    private Mp3Conveter mConveter;
    private short[] mBuffer;
    private boolean mIsRecording = false;
    private File mRawFile;
    private File mEncodedFile;


    public void prepare(){
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);
        mBuffer = new short[bufferSize];
        mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        mConveter = new Mp3Conveter();
    }

    /**
     * 开始录音
     */
    public void startRecording() {

        if(mIsRecording){
            return;
        }

        Log.e(TAG, "startRcording");
        if (mRecorder == null) {
            Log.e(TAG,"mRocorder is nul this should not happen");
            return;
        }
        mIsRecording = true;
        mRecorder.startRecording();
        mRawFile = getFile("raw");
        startBufferedWrite(mRawFile);
    }

    private void startBufferedWrite(final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataOutputStream output = null;
                try {
                    output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                    while (mIsRecording) {

                        if(mIsPause){
                            continue;
                        }

                        int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);
                        for (int i = 0; i < readSize; i++) {
                            output.writeShort(mBuffer[i]);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        try {
                            output.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private boolean mIsPause=false;

    public void pauseRecording(){
            mIsPause = true;
    }

    public void restartRecording(){
            mIsPause = false;
    }


    public void stopRecording() {
        Log.e(TAG, "stopRecording");
        if (mRecorder == null) {
            return;
        }
        if(!mIsRecording){
            return;
        }
        mRecorder.stop();
        mIsPause = false;
        mIsRecording = false;
        mEncodedFile = getFile("mp3");
        mConveter.encodeFile(mRawFile.getAbsolutePath(), mEncodedFile.getAbsolutePath());
    }

    /*
    public void startPlaying() {
        Log.e(TAG, "startPlayingstartPlaying");
        if (mPlayer != null) {
            return;
        }
        mPlayer = new MediaPlayer();
        try {
            Log.e("DDD", "DATA SOURCE: " + mEncodedFile.getAbsolutePath());
            mPlayer.setDataSource(mEncodedFile.getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mPlayer.release();
                    mPlayer = null;
                }
            });
        } catch (IOException e) {
            Log.e(TAG, e.toString() + "\nprepare() failed");
        }
    }
    */

    /*
    public void stopPlaying() {
        Log.e(TAG, "stopPlaying");
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
    */

    public void release() {
        /*
        Log.e(TAG, "release");
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        */
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mIsPause = false;
            mIsRecording = false;
        }

        if(mConveter!=null)
            mConveter.destroyEncoder();
    }

    private File getFile(final String suffix) {
        Time time = new Time();
        time.setToNow();
        File f= new File(Environment.getExternalStorageDirectory(), time.format("%Y%m%d%H%M%S") + "." + suffix);
        Log.e(TAG,"file address:"+f.getAbsolutePath());
        return f;
    }
}
