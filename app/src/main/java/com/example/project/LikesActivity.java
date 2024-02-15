package com.example.project;

import static com.example.project.NewItemActivity.readJsonFromFile;
import static com.example.project.NewItemActivity.writeJsonToFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LikesActivity extends AppCompatActivity {
    List<Item> items = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        try {
            items = new Gson().fromJson(readJsonFromFile(LikesActivity.this.getFilesDir()
                            + File.separator + "like.json"),
                    new TypeToken<List<Item>>() {
                    }.getType());
        } catch (Exception e){}
        ListView listView = findViewById(R.id.LikeList);
        listView.setAdapter(new WowAdapter(items));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item)listView.getItemAtPosition(i);
                ListView lw = findViewById(R.id.dlc_list);
                lw.setAdapter(new WowAdapter(item.change_items));
            }
        });
        findViewById(R.id.MainPage_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LikesActivity.this,MainActivity.class));
            }
        });
        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeJsonToFile(LikesActivity.this,"","like.json");
                items = new ArrayList<>();
                listView.setAdapter(new WowAdapter(items));
            }
        });
    }
    class WowAdapter extends BaseAdapter{
        List<Item> itemsList = new ArrayList<>();
        @Override
        public int getCount() {
            try{
                return itemsList.size();
            } catch (Exception e){ return 0;}
        }
        public WowAdapter(List<Item> ls){
        itemsList = ls;
        }

        @Override
        public Object getItem(int i) {
            return itemsList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           View wow = View.inflate(LikesActivity.this,R.layout.barter_item,null);
           try{
               TextView textView = wow.findViewById(R.id.name_txt);
               ImageView imageView = wow.findViewById(R.id.image_img);
               textView.setText(itemsList.get(i).name);
               imageView.setImageURI(Uri.parse(itemsList.get(i).id_image));
           } catch (Exception e){
           }
           return wow;
        }
    }
}
