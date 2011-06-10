package com.xmatters.mobile.android;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.xmatters.mobile.android.service.SoapResult;
import com.xmatters.mobile.android.service.SoapService;
import com.xmatters.mobile.android.service.SoapUserDeviceStatus;

public class MyDevicesPresenter {

	private Context context;
	private SharedPreferences userPreferences;
	private SoapService soapService;

	private ListView deviceList;
	private View mydevicesView;

	private Button registerButton;
	private Button settingsButton;
	
	public MyDevicesPresenter(Context context, View view, SharedPreferences preferences) {
		this.context = context;
		this.mydevicesView = view;
		this.userPreferences = preferences;

		this.soapService = new SoapService();
		this.registerButton = (Button) mydevicesView.findViewById(R.id.register);
		this.settingsButton = (Button) mydevicesView.findViewById(R.id.settings);
		this.deviceList = (ListView) mydevicesView.findViewById(R.id.deviceList);
	}
	
	public void show() {
		String serverUrl = userPreferences.getString(Constants.SERVER_URL, "");
		String company = userPreferences.getString(Constants.COMPANY, "");
		String username = userPreferences.getString(Constants.USERNAME, "");
		String password = userPreferences.getString(Constants.PASSWORD, "");
		
		List<SoapUserDeviceStatus> statusList = soapService
				.findUserDeviceStatus(serverUrl, company, username, password);
		String[] deviceNameArr = new String[statusList.size()];
		int idx = 0;
		for (SoapUserDeviceStatus status : statusList) {
			deviceNameArr[idx++] = status.getDeviceName();
		}

		deviceList.setAdapter(new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, deviceNameArr));
	}

	public SoapResult registerDevice(String deviceName) {
		String serverUrl = userPreferences.getString(Constants.SERVER_URL, "");
		String company = userPreferences.getString(Constants.COMPANY, "");
		String username = userPreferences.getString(Constants.USERNAME, "");
		String password = userPreferences.getString(Constants.PASSWORD, "");
		
		SharedPreferences.Editor editor = userPreferences.edit();
		editor.putString(Constants.DEVICE_NAME, deviceName);
		editor.commit();

		return soapService.registerDevice(serverUrl,
				company, username, password, deviceName);
	}
	
	public ListView getDeviceList() {
		return deviceList;
	}

	public Button getRegisterButton() {
		return registerButton;
	}

	public Button getBackToSettingsButton() {
		return settingsButton;
	}
}