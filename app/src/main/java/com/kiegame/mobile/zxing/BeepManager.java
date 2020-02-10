package com.kiegame.mobile.zxing;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.kiegame.mobile.Game;
import com.kiegame.mobile.R;

import java.io.Closeable;
import java.io.IOException;

/**
 * 扫码成功后的震动和声音
 */
public final class BeepManager implements MediaPlayer.OnErrorListener, Closeable {

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;

    private Activity activity;
    private MediaPlayer mediaPlayer;
    private boolean playBeep = true;

    /**
     * 构造函数
     */
    public BeepManager() {
        this.activity = Game.ins().activity();
        this.mediaPlayer = null;
        updatePrefs();
    }

    /**
     * 加载声音
     */
    public synchronized void updatePrefs() {
        if (playBeep && mediaPlayer == null) {
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = buildMediaPlayer();
        }
    }

    /**
     * 播放声音并震动
     */
    public synchronized void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 创建媒体播放器
     *
     * @return {@link MediaPlayer}
     */
    private MediaPlayer buildMediaPlayer() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try (AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.beep)) {
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            mediaPlayer.release();
            return null;
        }
    }

    @Override
    public synchronized boolean onError(MediaPlayer mp, int what, int extra) {
        close();
        updatePrefs();
        return true;
    }

    @Override
    public synchronized void close() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
