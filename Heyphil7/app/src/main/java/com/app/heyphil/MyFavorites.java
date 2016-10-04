package com.app.heyphil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Created by ABDalisay on 10/3/2016.
 */
public class MyFavorites extends Activity {
    ListView lv_favorite;
    ListAdapter favorite_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorites);
        lv_favorite=(ListView)findViewById(R.id.lv_favorite_provider);
        favorite_adapter = new SimpleAdapter(
                MyFavorites.this, Data.favorite,
                R.layout.item_favorites, new String[] { "ProviderName", "ProviderAddress",
                "ProviderContact", "ProviderCode"}, new int[] { R.id.pname,
                R.id.paddress, R.id.mobile, R.id.pcode});
        lv_favorite.setAdapter(favorite_adapter);
    }
}
