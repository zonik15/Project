package com.app.heyphil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class PhilcareProducts extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_philcare_products);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPageAndroid);
        ProductAdapter adapterView = new ProductAdapter(this);
        mViewPager.setAdapter(adapterView);
    }
}
