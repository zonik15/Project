package com.app.heyphil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import CustomList.CustomList;

public class ProductList extends Activity {
    ListView list;
    String[] proID = Data.productID.toArray(new String[Data.productID.size()]);
    String[] proContent = Data.productContent.toArray(new String[Data.productContent.size()]);
    String[] product = Data.productList.toArray(new String[Data.productList.size()]);
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        CustomList adapter = new
               CustomList(ProductList.this,proID, proContent, product);
        list=(ListView)findViewById(R.id.list);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
 
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(ProductList.this, "You Clicked at " +proID[+ position], Toast.LENGTH_SHORT).show();
 
                    }

                });
 
    }
 
}
