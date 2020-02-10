package com.kiegame.mobile.zxing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    /**
     * 构造方法
     */
    protected QRCodeAnalyzer() {
        reader = new MultiFormatReader();

        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
//        formats.add(BarcodeFormat.AZTEC);
//        formats.add(BarcodeFormat.CODABAR);
//        formats.add(BarcodeFormat.CODE_39);
//        formats.add(BarcodeFormat.CODE_93);
//        formats.add(BarcodeFormat.CODE_128);
//        formats.add(BarcodeFormat.DATA_MATRIX);
//        formats.add(BarcodeFormat.EAN_8);
//        formats.add(BarcodeFormat.EAN_13);
//        formats.add(BarcodeFormat.ITF);
//        formats.add(BarcodeFormat.MAXICODE);
//        formats.add(BarcodeFormat.PDF_417);
//        formats.add(BarcodeFormat.RSS_14);
//        formats.add(BarcodeFormat.RSS_EXPANDED);
//        formats.add(BarcodeFormat.UPC_A);
//        formats.add(BarcodeFormat.UPC_E);
//        formats.add(BarcodeFormat.UPC_EAN_EXTENSION);

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
        int width = image.getWidth();
        int height = image.getHeight();
        try {
            Result result = analyzeByteArray(data, width, height);
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
     * @param data   数据
     * @param width  宽度
     * @param height 高度
     * @return 识别后的数据或null
     */
    private Result analyzeByteArray(byte[] data, int width, int height) throws NotFoundException {
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, toPixels(data, width, height));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return reader.decodeWithState(bitmap);
    }

    /**
     * byte数组转int数组
     *
     * @param bytes 数据
     * @return 返回int数组
     */
    private int[] toPixels(byte[] bytes, int width, int height) {
        int[] pixels = new int[width * height];
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        return pixels;
    }

    /**
     * 识别完成
     */
    public abstract void decodeSuccess(Result result);
}
