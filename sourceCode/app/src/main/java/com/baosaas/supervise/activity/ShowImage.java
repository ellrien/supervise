package com.baosaas.supervise.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;

import com.baosaas.supervise.R;
import com.baosaas.supervise.util.ZoomImageView;


public class ShowImage extends Activity {
    private ZoomImageView showImage;
    private String urisString = null;
    private int screenWidth, screenHeigh;
    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.showimage);
        showImage = (ZoomImageView) findViewById(R.id.showimage);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels; //宽
        screenHeigh = dm.heightPixels; //高

        Intent intent = getIntent();
        bytes = intent.getByteArrayExtra("bitmap");
        if (bytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            showImage.setImageBitmap(zoomImage(bitmap, screenWidth, screenHeigh));
        } else {
            showImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        }
    }

    public Bitmap zoomImage(Bitmap bitmap, int newwidth, int newheight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //计算缩放比例
        float scalewidth = ((float) newwidth) / width;
        float scaleheigt = ((float) newheight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scalewidth, scaleheigt);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
//        this.finish();
        return super.onTouchEvent(event);
    }
}
