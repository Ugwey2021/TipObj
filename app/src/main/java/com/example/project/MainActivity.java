package com.example.project;

import static com.example.project.NewItemActivity.writeJsonToFile;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    List<Item> items = new ArrayList<>();
    int activeItem_id = 0;
    ImageView base_image;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            items = new Gson().fromJson(readJsonFromFile(MainActivity.this.getFilesDir()+
                            File.separator+"item.json"),
                    new TypeToken<List<Item>>(){}.getType());

        base_image = findViewById(R.id.imageView2);
        try{
            base_image.setImageURI(Uri.parse(items.get(activeItem_id).id_image));
        } catch(Exception e) {}
        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NewItemActivity.class));
            }
        });
        findViewById(R.id.button_likeView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LikesActivity.class));
            }
        });
        gestureDetector = new GestureDetector(this,this);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            gestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                if (diffX > 0) {
                    String json = "";
                    List<Item> likeList = new ArrayList<>();
                    likeList = new Gson().fromJson(readJsonFromFile(MainActivity.this.getFilesDir()
                                    +File.separator+"like.json"),
                            new TypeToken<List<Item>>(){}.getType());
                    if(likeList == null){
                        likeList = new ArrayList<>();
                    }
                    likeList.add(items.get(activeItem_id));
                    json = new Gson().toJson(likeList);
                    writeJsonToFile(MainActivity.this, json, "like.json");
                    activeItem_id++;
                    try{
                        base_image.setImageURI(Uri.parse(items.get(activeItem_id).id_image));
                    } catch(Exception e) {
                        Toast.makeText(MainActivity.this, "Рекомендации еще будут", Toast.LENGTH_SHORT).show();
                        activeItem_id = 0;
                        base_image.setImageURI(Uri.parse(items.get(activeItem_id).id_image));
                    }
                } else {
                    activeItem_id++;
                    try{
                        base_image.setImageURI(Uri.parse(items.get(activeItem_id).id_image));
                    } catch(Exception e) {}
                }
                return true;
            }
        } else {
            if (Math.abs(diffY) > 100 && Math.abs(velocityY) > 100) {
                if (diffY > 0) {
                    Toast.makeText(this, "Swipe Down", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Swipe Up", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        }

        return false;
    }
    private static String readJsonFromFile(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            return jsonString.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}