package com.xmatters.mobile.android;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsPresenter {

	private View settingsView = null;
	private SharedPreferences userPreferences = null;
	
	private EditText serverEditText = null;
	private EditText companyEditText = null;
	private EditText userEditText = null;
	private EditText passwordEditText = null;
//	
//	private String serverUrl = null;
//	private String company = null;
//	private String username = null;
//	private String password = null;
	
	private Button proceedButton = null;
	private Button cancelButton = null;
	
	public SettingsPresenter(View view, SharedPreferences preferences) {
		this.settingsView = view;
		this.userPreferences = preferences;
		
		this.proceedButton = (Button) settingsView.findViewById(R.id.proceed);
		this.cancelButton = (Button) settingsView.findViewById(R.id.cancel);

		this.serverEditText = (EditText) settingsView.findViewById(R.id.serverValue);
		this.companyEditText = (EditText) settingsView.findViewById(R.id.companyValue);
		this.userEditText = (EditText) settingsView.findViewById(R.id.userText);
		this.passwordEditText = (EditText) settingsView.findViewById(R.id.passwordText);
	}
		
	public void show() {
		String serverUrl = userPreferences.getString(Constants.SERVER_URL, "");
		String company = userPreferences.getString(Constants.COMPANY, "");
		String username = userPreferences.getString(Constants.USERNAME, "");
		String password = userPreferences.getString(Constants.PASSWORD, "");
		
		serverEditText.setText(serverUrl);
		companyEditText.setText(company);
		userEditText.setText(username);
		passwordEditText.setText(password);
	}

	public void save() {
		String serverUrl = serverEditText.getText().toString();
		String company = companyEditText.getText().toString();
		String username = userEditText.getText().toString();
		String password = passwordEditText.getText().toString();

		if (company.trim().length() == 0) {
			company = "Default Company";
		}

		SharedPreferences.Editor editor = userPreferences.edit();
		editor.putString(Constants.SERVER_URL, serverUrl);
		editor.putString(Constants.COMPANY, company);
		editor.putString(Constants.USERNAME, username);
		editor.putString(Constants.PASSWORD, password);
		editor.commit();
	}
	
	public Button getProceedButton() {
		return proceedButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}
}