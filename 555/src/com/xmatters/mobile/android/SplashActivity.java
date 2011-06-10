package com.xmatters.mobile.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		new Handler().postDelayed(new Runnable(){
		      public void run() {
		        finish();
		        startActivity(new Intent(SplashActivity.this, MainTabActivity.class));
		      }
		  }, 1000);
	}
}