package com.kiegame.mobile.zxing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: var_rain.
 * Created date: 2020/2/10.
 * Description: 二维码解析器
 */
public abstract class QRCodeAnalyzer extends ImageCapture.OnImageCapturedCallback {

    private MultiFormatReader reader;
    private int width;
    private int height;
    private Matrix matrix;
    private int x;
    private int y;
    private int box;

    /**
     * 构造方法
     */
    protected QRCodeAnalyzer() {
        reader = new MultiFormatReader();

        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.CODE_128);

        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        reader.setHints(hints);
    }

    @Override
    public void onCaptureSuccess(@NonNull ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.capacity()];
        buffer.get(data);
        try {
            Result result = analyzeByteArray(data);
            decodeSuccess(result);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } finally {
            decodeSuccess(null);
            image.close();
        }
    }

    @Override
    public void onError(int imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
        decodeSuccess(null);
    }

    /**
     * 解析字节数据
     *
     * @param data 数据
     * @return 识别后的数据或null
     */
    private Result analyzeByteArray(byte[] data) throws NotFoundException {
        int[] pixels = toPixels(data);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return reader.decodeWithState(bitmap);
    }

    /**
     * byte数组转int数组
     *
     * @param bytes 数据
     * @return 返回int数组
     */
    private int[] toPixels(byte[] bytes) {
        Bitmap bitmap = rotateAndCropBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        return pixels;
    }

    /**
     * 旋转裁剪位图
     *
     * @param bitmap 源位图对象
     * @return 返回裁剪后的位图对象
     */
    private Bitmap rotateAndCropBitmap(Bitmap bitmap) {
        if (matrix == null) {
            int height = Math.max(bitmap.getWidth(), bitmap.getHeight());
            int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
            // 裁剪之后才旋转,所以这里要用横向的裁剪坐标,X轴和Y轴对换一下
            y = width / 4;
            x = (height - (width / 2)) / 2;
            box = width / 2;
            matrix = new Matrix();
            matrix.setRotate(90);
            this.width = box;
            this.height = box;
        }
        return Bitmap.createBitmap(bitmap, x, y, box, box, matrix, false);
    }

    /**
     * 识别完成
     */
    public abstract void decodeSuccess(Result result);
}
