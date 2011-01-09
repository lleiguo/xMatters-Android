package com.xmatters.mobile.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button button = (Button)findViewById(R.id.register);
        button.setOnClickListener(this);
    }

	public void onClick(View arg0) {
		//Call Jan's method to register
		Log.d("SettingActivity", "registering...");
	}
}