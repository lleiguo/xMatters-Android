package com.xmatters.mobile.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xmatters.mobile.android.service.SoapResult;
import com.xmatters.mobile.android.service.SoapService;

public class SettingsActivity extends Activity implements OnClickListener {
	private SoapService soapService = null;

	String serverUrl = null;
	String companyName = null;

	String username = null;

	String password = null;

	private ServiceConnection soapServiceConnection = new ServiceConnection() {
		// Register this device once the service is connected.
		public void onServiceConnected(ComponentName className, IBinder binder) {
			soapService = ((SoapService.LocalBinder) binder).getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			soapService = null;
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		Button button = (Button) findViewById(R.id.register);
		button.setOnClickListener(this);

		// Bind this activity to soap service.
		Intent intent = new Intent();
		intent.setClassName("com.xmatters.mobile.android.service",
				"com.xmatters.mobile.android.service.SoapService");
		bindService(intent, soapServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(soapServiceConnection);
	}

	public void onClick(View view) {
		Log.d("SettingActivity", "registering...");

		EditText serverEditText = (EditText) findViewById(R.id.serverValue);
		EditText companyEditText = (EditText) findViewById(R.id.companyValue);
		EditText userEditText = (EditText) findViewById(R.id.userText);
		EditText passwordEditText = (EditText) findViewById(R.id.passwordText);

		serverUrl = serverEditText.getText().toString();
		companyName = companyEditText.getText().toString();

		username = userEditText.getText().toString();

		password = passwordEditText.getText().toString();

		if (companyName.trim().length() == 0) {
			companyName = "Default Company";
		}

		soapService = new SoapService();
		SoapResult result = soapService.registerDevice(serverUrl, companyName,
				username, password);
		String message = "Registration Successful.";
		if (!result.isSuccessful()) {
			message = result.getMessage();
		}

		new AlertDialog.Builder(SettingsActivity.this).setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
}