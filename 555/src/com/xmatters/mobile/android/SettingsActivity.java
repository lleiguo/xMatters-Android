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
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.xmatters.mobile.android.service.SoapResult;
import com.xmatters.mobile.android.service.SoapService;

public class SettingsActivity extends Activity implements OnClickListener {
	
	private String serverUrl = null;
	private String companyName = null;
	private String username = null;
	private String password = null;

	private SoapService soapService = null;
	
	private ServiceConnection soapServiceConnection = new ServiceConnection() {
		// Register this device once the service is connected.
		public void onServiceConnected(ComponentName className, IBinder binder) {
			soapService = ((SoapService.LocalBinder) binder).getService();
			//soapService = ISoapService.Stub.asInterface((IBinder)binder);
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
		boolean flag = getApplicationContext().bindService(new Intent(this, SoapService.class), soapServiceConnection, Context.BIND_AUTO_CREATE);
		int i = 0;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getApplicationContext().unbindService(soapServiceConnection);
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

		String message = "Registration successful.";
		SoapResult soapResult = soapService.registerDevice(serverUrl, companyName,
				username, password);

		if (!soapResult.isSuccessful()) {
			message = soapResult.getMessage();
		}
		
		new AlertDialog.Builder(SettingsActivity.this).setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
}