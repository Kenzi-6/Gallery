package com.example.gallery2;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageDetailActivity extends AppCompatActivity {

    // создание строковой переменной, переменной изображения
    // и переменная для класса ScaleGestureDetector
    String imgPath;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;

    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        // получаем данные, которые передали из класса адаптера
        imgPath = getIntent().getStringExtra("imgPath");

        // инициализация изображения.
        imageView = findViewById(R.id.idIVImage);

        // инициализируем scalegesturedetector для увеличения и уменьшения масштаба нашего изображения.
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        // получаем файл изображения по его пути
        File imgFile = new File(imgPath);

        // если файл существует, то мы загружаем это изображение в imageView
        if (imgFile.exists()) {
            Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).into(imageView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        //касания
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            // устанавливаем масштаб изображения
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

        // устанавливаем scalex и scaley для отображения изобрражения
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}