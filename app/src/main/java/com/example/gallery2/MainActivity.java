package com.example.gallery2;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import com.example.gallery2.RecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    // в строке ниже мы создаем переменные для arraylist, recyclerview и класса адаптера
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ArrayList<String> imagePaths;
    private RecyclerView imagesRV;
    private RecyclerViewAdapter imageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // вызов метода для запроса разрешения на чтение внешнего хранилища
        requestPermissions();

        // создание нового списка массивов и инициализация recyclerview
        imagePaths = new ArrayList<>();
        imagesRV = findViewById(R.id.idRVImages);
        prepareRecyclerView();
    }

    private boolean checkPermission() {
        // в этом методе мы проверяем, предоставлены ли разрешения или нет, и возвращаем результат
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        if (checkPermission()) {
            // если разрешения уже предоставлены, мы вызываем получение всех изображений из нашего внешнего хранилища
            Toast.makeText(this, "Permissions granted..", Toast.LENGTH_SHORT).show();
            getImagePath();
        } else {
            // если разрешения не предоставлены вызываем метод для запроса разрешения
            requestPermission();
        }
    }

    private void requestPermission() {
        //в строке ниже мы запрашиваем разрешения на чтение внешнего хранилища
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void prepareRecyclerView() {

        // инициализация адаптера
        imageRVAdapter = new RecyclerViewAdapter(MainActivity.this, imagePaths);
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 4);
        // устанавливаем менеджер и адаптер для recyclerview
        imagesRV.setLayoutManager(manager);
        imagesRV.setAdapter(imageRVAdapter);
    }
    // добавляем все пути к изображениям
    @SuppressLint("NotifyDataSetChanged")
    private void getImagePath() {
        // проверка, установлена ли на устройстве SD-карта
        boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent) {

            // если SD-карта присутствует, мы создаем новый список, в
            // который получаем данные изображений с их идентификаторами
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

            // строку для упорядочения изображений по строкам
            final String orderBy = MediaStore.Images.Media._ID;

            // этот метод сохранит все изображения из галереи в cursor
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

            // получение общего количества изображений
            int count = cursor.getCount();

            //запускаем цикл для добавления пути к файлу изображения в наш список массивов
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);   // перемещаем положение cursor
                // получаем путь к файлу изображения
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imagePaths.add(cursor.getString(dataColumnIndex));  // добавляем этот путь в список массивов
            }
            imageRVAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // этот метод вызывается после предоставления разрешений
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // проверяем на разрешение
            case PERMISSION_REQUEST_CODE:
                // разрешено ли
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        // если разрешено, мы выводим всплывающее сообщение
                        // и вызываем метод для получения пути к изображению
                        Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show();
                        getImagePath();
                    } else {
                        // если не разрешено, мы закрываем приложение и показываем всплывающее сообщение
                        Toast.makeText(this, "Permissions denined, Permissions are required to use the app..", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}