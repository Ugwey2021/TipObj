package com.example.project;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {
    //public int source_image;
    public String name;
    public String category;
    public String id_image;
    public List<Item> change_items;
    public Item(String nameS,String categoryS,String id_img,List<Item> change_itemss){
        name = nameS;
        category = categoryS;
        id_image  = id_img;
        change_items = change_itemss;
    }
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", image=" + id_image +
                '}';
    }
}
