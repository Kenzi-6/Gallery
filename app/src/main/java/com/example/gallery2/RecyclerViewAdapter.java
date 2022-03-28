package com.example.gallery2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    // переменные для контекста и списка массивов
    private final Context context;
    private final ArrayList<String> imagePathArrayList;

    // конструктор
    public RecyclerViewAdapter(Context context, ArrayList<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // расширение макета в этом методе, который мы создали
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // файл из пути, который сохранили в списке
        File imgFile = new File(imagePathArrayList.get(position));

        //проверка на существование файла
        if (imgFile.exists()) {

            // если файл существует, то отображаем этот файл в imageview с помощью библиотеки picasso
            Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).into(holder.imageIV);

            // добавляем прослушиватель кликов к элементу recyclerview
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ImageDetailActivity.class);
                    //передаем путь к изображению
                    i.putExtra("imgPath", imagePathArrayList.get(position));
                    context.startActivity(i);
                }
            });
        }
    }

    //возвращает размер recyclerview
    @Override
    public int getItemCount() {
        return imagePathArrayList.size();
    }

    // Класс ViewHolder для обработки RecyclerView
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // переменная для отображения
        private final ImageView imageIV;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            // инициализация с помощью идентификаторов
            imageIV = itemView.findViewById(R.id.idIVImage);
        }
    }
}