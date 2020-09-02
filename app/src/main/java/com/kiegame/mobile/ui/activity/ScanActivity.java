package com.kiegame.mobile.ui.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.animation.LinearInterpolator;

import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.Result;
import com.kiegame.mobile.R;
import com.kiegame.mobile.databinding.ActivityScanBinding;
import com.kiegame.mobile.settings.Setting;
import com.kiegame.mobile.ui.base.BaseActivity;
import com.kiegame.mobile.utils.Pixel;
import com.kiegame.mobile.utils.Text;
import com.kiegame.mobile.zxing.QRCodeAnalyzer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by: var_rain.
 * Created date: 2020/2/7.
 * Description: 扫码界面
 */
public class ScanActivity extends BaseActivity<ActivityScanBinding> implements SensorEventListener {

    private static final float TOO_DARK_LUX = 20.0f;
    private static final float BRIGHT_ENOUGH_LUX = 450.0f;
    private final int RESULT_CODE_SCAN = 10086;

    private ValueAnimator animator;
    private Preview preview;
    private ImageAnalysis imageAnalysis;
    private Executor analyzeExecutor;
    private Executor mainExecutor;
    private SensorManager sensorManager;
    private Camera camera;
    private String title;
    private Sensor sensor;
    private SoundPool sp;
    private SparseIntArray sounds;
    private float volumeRatio;
    private Vibrator vibrator;
    private boolean isSuccess = false;

    @Override
    protected int onLayout() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onObject() {
        binding.setActivity(this);
        title = getIntent().getStringExtra(Setting.APP_SCAN_TITLE);
        mainExecutor = ContextCompat.getMainExecutor(this);
        analyzeExecutor = Executors.newSingleThreadExecutor();

        this.initializeSensorManager();
        this.initSoundPool();
    }

    @Override
    protected void onView() {
        binding.tvScanTitle.setText(Text.empty(title) ? "扫码支付" : title);
        binding.svCamera.post(this::updateCamera);
        binding.ivScanBox.post(this::startAnim);
    }

    @Override
    protected void onData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animator != null) {
            animator.resume();
        }
        if (sensorManager != null && sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animator != null) {
            animator.pause();
        }
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sp != null) {
            sp.release();
        }
    }

    /**
     * 初始化声音播放器
     */
    private void initSoundPool() {
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        sp = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attributes)
                .build();
        sounds = new SparseIntArray();
        sounds.put(0, sp.load(this, R.raw.beep, 1));
        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (am != null) {
            float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeRatio = audioCurrentVolume / audioMaxVolume;
        }
    }

    /**
     * 播放声音并震动
     */
    private void playSoundAndVibrate() {
        if (sp != null) {
            sp.play(sounds.get(0), volumeRatio, volumeRatio, 1, 0, 1);
        }
        if (vibrator != null && volumeRatio == 0.0f) {
            vibrator.vibrate(200L);
        }
    }

    /**
     * 初始化传感器
     */
    private void initializeSensorManager() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
    }

    /**
     * 更新相机
     */
    private void updateCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        binding.svCamera.getDisplay().getRealMetrics(metrics);
        int screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels);
        int rotation = binding.svCamera.getDisplay().getRotation();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                preview = new Preview.Builder()
                        .setTargetAspectRatio(screenAspectRatio)
                        .setTargetRotation(rotation)
                        .build();
                preview.setPreviewSurfaceProvider(binding.svCamera.getPreviewSurfaceProvider());
                imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetAspectRatio(screenAspectRatio)
                        .setTargetRotation(rotation)
                        .build();
                imageAnalysis.setAnalyzer(analyzeExecutor, new QRCodeAnalyzer() {
                    @Override
                    public void decodeSuccess(Result result) {
                        if (result != null && !isSuccess) {
                            isSuccess = true;
                            playSoundAndVibrate();
                            camera.getCameraControl().enableTorch(false);
                            Intent intent = new Intent().putExtra(Setting.APP_SCAN_CONTENT, result.getText());
                            setResult(RESULT_CODE_SCAN, intent);
                            finish();
                        }
                    }
                });
                if (cameraProvider != null) {
                    cameraProvider.unbindAll();
                    camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, mainExecutor);
    }

    /**
     * 获取相机比率
     *
     * @param width  宽度
     * @param height 高度
     * @return {@link AspectRatio}
     */
    private int aspectRatio(int width, int height) {
        double previewRatio = Math.max(width, height) / Math.min(width, height);
        double RATIO_4_3_VALUE = 4.0 / 3.0;
        double RATIO_16_9_VALUE = 16.0 / 9.0;
        if (Math.abs(previewRatio - RATIO_4_3_VALUE) <= Math.abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    /**
     * 初始化动画
     */
    private void startAnim() {
        animator = ValueAnimator.ofFloat(0, binding.ivScanBox.getHeight() - Pixel.measureHeight(binding.ivScanLine));
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(animation -> {
            if (binding != null && binding.ivScanLine != null) {
                binding.ivScanLine.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];
        if (camera != null) {
            CameraControl control = camera.getCameraControl();
            if (lux <= TOO_DARK_LUX) {
                control.enableTorch(true);
            } else if (lux >= BRIGHT_ENOUGH_LUX) {
                control.enableTorch(false);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
