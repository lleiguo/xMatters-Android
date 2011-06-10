package com.xmatters.mobile.android;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.xmatters.mobile.android.service.SoapResult;
import com.xmatters.mobile.android.service.SoapService;
import com.xmatters.mobile.android.service.SoapUserDeviceStatus;

public class MyDevicesActivity extends Activity {

	private SharedPreferences userPreferences;
	private String serverUrl;
	private String company;
	private String username;
	private String password;
	private String deviceName;
	private SoapService soapService;

	private ListView deviceListView;

	// private ServiceConnection soapServiceConnection = new ServiceConnection()
	// {
	// // Register this device once the service is connected.
	// public void onServiceConnected(ComponentName className, IBinder binder) {
	// soapService = ((SoapService.LocalBinder) binder).getService();
	// // soapService = ISoapService.Stub.asInterface((IBinder)binder);
	// }
	//
	// public void onServiceDisconnected(ComponentName className) {
	// soapService = null;
	// }
	// };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydevices);

		Button registerButton = (Button) findViewById(R.id.register);
		registerButton.setOnClickListener(registerListener);

		Button settingsButton = (Button) findViewById(R.id.settings);
		settingsButton.setOnClickListener(settingsListener);

		deviceListView = (ListView) findViewById(R.id.deviceList);

		deviceListView.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				deviceName = (String) parent.getItemAtPosition(position);	
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		userPreferences = getSharedPreferences(
				Constants.SETTINGS_PREFERENCES, 0);

		serverUrl = userPreferences.getString(Constants.SERVER_URL,
				"");
		company = userPreferences.getString(Constants.COMPANY, "");
		username = userPreferences.getString(Constants.USERNAME, "");
		password = userPreferences.getString(Constants.PASSWORD, "");
		
		// Bind this activity to soap service.
		// boolean flag = getApplicationContext().bindService(
		// new Intent(this, SoapService.class), soapServiceConnection,
		// Context.BIND_AUTO_CREATE);

		soapService = new SoapService();
	}

	@Override
	protected void onStart() {
		super.onStart();

		List<SoapUserDeviceStatus> statusList = soapService
				.findUserDeviceStatus(serverUrl, company, username, password);
		String[] deviceNameArr = new String[statusList.size()];
		int idx = 0;
		for (SoapUserDeviceStatus status : statusList) {
			deviceNameArr[idx++] = status.getDeviceName();
		}

		deviceListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, deviceNameArr));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// getApplicationContext().unbindService(soapServiceConnection);
	}

	private OnClickListener registerListener = new OnClickListener() {
		public void onClick(View view) {
			Log.d("SettingActivity", "registering...");

			if (deviceName == null) {
				deviceName = "iPhone";
			}
			
			SharedPreferences.Editor editor = userPreferences.edit();
			editor.putString(Constants.DEVICE_NAME, deviceName);
			editor.commit();

			SoapResult soapResult = soapService.registerDevice(serverUrl,
					company, username, password, deviceName);

			String message = "Registration successful.";
			if (!soapResult.isSuccessful()) {
				message = soapResult.getMessage();
			}

			new AlertDialog.Builder(MyDevicesActivity.this)
					.setMessage(message)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();

			finish();
			startActivity(new Intent(MyDevicesActivity.this,
					MyAlertsActivity.class));
		}
	};

	private OnClickListener settingsListener = new OnClickListener() {
		public void onClick(View view) {
			Intent intent = new Intent(MyDevicesActivity.this,
					SettingsActivity.class);
			startActivity(intent);
		}
	};
}