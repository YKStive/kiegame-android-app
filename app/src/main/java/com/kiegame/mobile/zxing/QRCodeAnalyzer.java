package com.kiegame.mobile.zxing;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.kiegame.mobile.Game;

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
public abstract class QRCodeAnalyzer implements ImageAnalysis.Analyzer {

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
    public void analyze(@NonNull ImageProxy image) {
        try {
            Result result = analyzeByteArray(image);
            decodeSuccess(result);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } finally {
            image.close();
        }
    }

    /**
     * 解析字节数据
     *
     * @param image 数据
     * @return 识别后的数据或null
     */
    private Result analyzeByteArray(ImageProxy image) throws NotFoundException {
        int[] pixels = toPixels(image);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        return reader.decodeWithState(bitmap);
    }

    /**
     * 转换为Bitmap
     *
     * @param image {@link ImageProxy}
     * @return {@link Bitmap}
     */
    private Bitmap convertToBitmap(ImageProxy image) {
        final ByteBuffer yuvBytes = this.convertToByteBuffer(image);
        final RenderScript rs = RenderScript.create(Game.ins().activity());
        final Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        final Allocation allocationRgb = Allocation.createFromBitmap(rs, bitmap);
        final Allocation allocationYuv = Allocation.createSized(rs, Element.U8(rs), yuvBytes.array().length);
        allocationYuv.copyFrom(yuvBytes.array());
        ScriptIntrinsicYuvToRGB scriptYuvToRgb = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));
        scriptYuvToRgb.setInput(allocationYuv);
        scriptYuvToRgb.forEach(allocationRgb);
        allocationRgb.copyTo(bitmap);
        allocationYuv.destroy();
        allocationRgb.destroy();
        rs.destroy();
        return rotateAndCropBitmap(bitmap);
    }

    /**
     * 转换成 {@link ImageProxy}
     *
     * @param image 相机预览数据
     * @return {@link ByteBuffer}
     */
    private ByteBuffer convertToByteBuffer(final ImageProxy image) {
        final Rect crop = image.getCropRect();
        final int width = crop.width();
        final int height = crop.height();
        final ImageProxy.PlaneProxy[] planes = image.getPlanes();
        final byte[] rowData = new byte[planes[0].getRowStride()];
        final int bufferSize = width * height * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8;
        final ByteBuffer output = ByteBuffer.allocateDirect(bufferSize);
        int channelOffset = 0;
        int outputStride;
        for (int planeIndex = 0; planeIndex < 3; planeIndex++) {
            if (planeIndex == 0) {
                outputStride = 1;
            } else if (planeIndex == 1) {
                channelOffset = width * height + 1;
                outputStride = 2;
            } else {
                channelOffset = width * height;
                outputStride = 2;
            }
            final ByteBuffer buffer = planes[planeIndex].getBuffer();
            final int rowStride = planes[planeIndex].getRowStride();
            final int pixelStride = planes[planeIndex].getPixelStride();
            final int shift = (planeIndex == 0) ? 0 : 1;
            final int widthShifted = width >> shift;
            final int heightShifted = height >> shift;
            buffer.position(rowStride * (crop.top >> shift) + pixelStride * (crop.left >> shift));
            for (int row = 0; row < heightShifted; row++) {
                final int length;
                if (pixelStride == 1 && outputStride == 1) {
                    length = widthShifted;
                    buffer.get(output.array(), channelOffset, length);
                    channelOffset += length;
                } else {
                    length = (widthShifted - 1) * pixelStride + 1;
                    buffer.get(rowData, 0, length);
                    for (int col = 0; col < widthShifted; col++) {
                        output.array()[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }
                if (row < heightShifted - 1) {
                    buffer.position(buffer.position() + rowStride - length);
                }
            }
        }
        return output;
    }

    /**
     * byte数组转int数组
     *
     * @return 返回int数组
     */
    private int[] toPixels(ImageProxy image) {
        Bitmap bitmap = convertToBitmap(image);
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
