package com.example.project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.Attributes;

public class NewItemActivity extends AppCompatActivity {
    EditText nameText;
    EditText categoryText;
    List<Item> itemList = new ArrayList<>();
    int active_image = 0;
    List<Item> items = new ArrayList<>();
    static final int PICK_IMAGE_REQUEST = 1;
    String choiceImage;
    ImageView choiceImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Button button_addItem = findViewById(R.id.button_addItem);
        choiceImageView = findViewById(R.id.ChoiceImage);
        Button choiceImage_btn = findViewById(R.id.ChoiceImage_btn);
        Button addLikeItem = findViewById(R.id.button_likeItem);
        ListView ListLikeItems = findViewById(R.id.ListLikesItems);
        nameText = findViewById(R.id.textView_nameItem);
        categoryText = findViewById(R.id.textView_category);
        button_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = "";
                itemList = new Gson().fromJson(readJsonFromFile(NewItemActivity.this.getFilesDir()
                                +File.separator+"item.json"),
                        new TypeToken<List<Item>>(){}.getType());
                if(itemList == null){
                    itemList = new ArrayList<>();
                }
                if(items == null){
                    items = new ArrayList<>();
                }
                Item item = new Item(nameText.getText().toString(), categoryText.getText().toString(),
                        choiceImage,items);
                itemList.add(item);
                if (itemList != null) {
                    json = new Gson().toJson(itemList);
                } else { nameText.setText("null");}
                writeJsonToFile(NewItemActivity.this, json, "item.json");
            }
        });
        addLikeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item = new Item(nameText.getText().toString(), categoryText.getText().toString(),
                        choiceImage,new ArrayList<>());
                items.add(item);
                ListLikeItems.setAdapter(new NewAdapter());
            }
        });
       choiceImage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

       findViewById(R.id.DependetsAdd_btn).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(NewItemActivity.this,MainActivity.class));
           }
       });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Получаем URI выбранного изображения
            choiceImage = data.getData().toString();
            choiceImageView.setImageURI(data.getData());
        }
    }
    public static String readJsonFromFile(String filePath) {
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
    public static void writeJsonToFile(Context context, String jsonContent, String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class NewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            try{
                return items.size();
            } catch (Exception e){ return 0;}
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View wow = View.inflate(NewItemActivity.this,R.layout.barter_item,null);
            try{
                TextView textView = wow.findViewById(R.id.name_txt);
                ImageView imageView = wow.findViewById(R.id.image_img);
                textView.setText(items.get(i).name);
                imageView.setImageURI(Uri.parse(items.get(i).id_image));
            } catch (Exception e){
            }
            return wow;
        }
    }
}


