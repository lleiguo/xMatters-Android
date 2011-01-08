package com.xmatters.mobile.android;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class xMattersTabActivity extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, SettingsActivity.class);

        spec = tabHost.newTabSpec("settings").setIndicator("Settings",
                          res.getDrawable(R.drawable.settings))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MyAlertsActivity.class);
        spec = tabHost.newTabSpec("myalerts").setIndicator("My Alerts",
                          res.getDrawable(R.drawable.myalerts))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);

    }

}
