package com.xmatters.mobile.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	private SharedPreferences userPreferences = null;
	
	private EditText serverEditText = null;
	private EditText companyEditText = null;
	private EditText userEditText = null;
	private EditText passwordEditText = null;
	
	private String serverUrl = null;
	private String company = null;
	private String username = null;
	private String password = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		Button registerButton = (Button) findViewById(R.id.proceed);
		registerButton.setOnClickListener(proceedListener);
		
		Button resetButton = (Button) findViewById(R.id.cancel);
		resetButton.setOnClickListener(cancelListener);

		userPreferences = getSharedPreferences(Constants.SETTINGS_PREFERENCES, 0);
		serverEditText = (EditText) findViewById(R.id.serverValue);
		companyEditText = (EditText) findViewById(R.id.companyValue);
		userEditText = (EditText) findViewById(R.id.userText);
		passwordEditText = (EditText) findViewById(R.id.passwordText);
		
		serverUrl = userPreferences.getString(Constants.SERVER_URL, "");
		company = userPreferences.getString(Constants.COMPANY, "");
		username = userPreferences.getString(Constants.USERNAME, "");
		password = userPreferences.getString(Constants.PASSWORD, "");
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		serverEditText.setText(serverUrl);
		companyEditText.setText(company);
		userEditText.setText(username);
		passwordEditText.setText(password);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private OnClickListener proceedListener = new OnClickListener() {
		public void onClick(View view) {
			Log.d("SettingActivity", "Proceed");
			
			serverUrl = serverEditText.getText().toString();
			company = companyEditText.getText().toString();
			username = userEditText.getText().toString();
			password = passwordEditText.getText().toString();

			if (company.trim().length() == 0) {
				company = "Default Company";
			}

			SharedPreferences settings = getSharedPreferences(
					Constants.SETTINGS_PREFERENCES, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(Constants.SERVER_URL, serverUrl);
			editor.putString(Constants.COMPANY, company);
			editor.putString(Constants.USERNAME, username);
			editor.putString(Constants.PASSWORD, password);
			editor.commit();
			
			startActivity(new Intent(SettingsActivity.this, MyDevicesActivity.class));
		}
	};
	
	private OnClickListener cancelListener = new OnClickListener() {
	    public void onClick(View view) {
			Log.d("SettingActivity", "Cancel");
			finish();
	    	startActivity(new Intent(SettingsActivity.this, MyAlertsActivity.class));
	    }
	};
}