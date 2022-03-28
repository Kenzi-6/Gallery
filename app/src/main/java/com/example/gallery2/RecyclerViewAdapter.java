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

    // создаем переменную для нашего контекста и списка массивов
    private final Context context;
    private final ArrayList<String> imagePathArrayList;

    // создание конструктора
    public RecyclerViewAdapter(Context context, ArrayList<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout in this method which we have created.
        // Раздувать макет в этом методе, который мы создали.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // в строке ниже мы получаем файл из
        // пути, который мы сохранили в нашем списке
        File imgFile = new File(imagePathArrayList.get(position));

        // в строке ниже мы проверяем, существует файл или нет
        if (imgFile.exists()) {

            // если файл существует, то мы отображаем этот файл в вашем imageview с помощью библиотеки picasso
            Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).into(holder.imageIV);

            // в строке ниже мы добавляем прослушиватель кликов к нашему элементу recyclerview.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // inside on click listener we are creating a new intent
                    // внутри прослушивателя щелчков мыши мы создаем новое намерение
                    Intent i = new Intent(context, ImageDetailActivity.class);

                    // в строке ниже мы передаем путь к изображению нашему новому activity
                    i.putExtra("imgPath", imagePathArrayList.get(position));
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        // этот метод возвращает
        // размер recyclerview
        return imagePathArrayList.size();
    }

    // Класс ViewHolder для обработки RecyclerView.
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        // создание переменной для отображения
        private final ImageView imageIV;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            // инициализация с помощью идентификаторов.
            imageIV = itemView.findViewById(R.id.idIVImage);
        }
    }
}