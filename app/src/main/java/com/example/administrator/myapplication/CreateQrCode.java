package com.example.administrator.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import net.duohuo.dhroid.activity.BaseActivity;

import java.util.Hashtable;

/**
 * Author:JsonLu
 * DateTime:2015/9/19 9:04
 * Email:luxd@i_link.cc
 * Desc:
 */
public class CreateQrCode extends BaseActivity {


    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showqrcode);
        try {
            bitmap =Create2DCode("6923450657713");
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (bitmap!=null){
            ImageView imageView = (ImageView)findViewById(R.id.qrcode_img);

            imageView.setImageBitmap(bitmap);
        }

    }

    public static Bitmap Create2DCode(String text) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //BarcodeFormat.EAN_13 条形码标准  建议宽高5:2
        //BarcodeFormat.QR_CODE二维码标准  建议宽高1:1
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 600, 300, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


}
