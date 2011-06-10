package com.xmatters.mobile.android;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.ViewFlipper;

import com.xmatters.mobile.android.service.SoapResult;

public class MainTabActivity extends TabActivity {
	
	private static final String SETTINGS_TAB_TAG = "settings";
	private static final String MYALERTS_TAB_TAG = "myalerts";
	
	private ViewFlipper settingsTabContent;
	private LinearLayout myalertsTabContent;
	private SettingsPresenter settingsPresenter;
	private MyDevicesPresenter mydevicesPresenter;
	private MyAlertsPresenter myalertsPresenter;
	
	private TabHost tabs;
	private View settingsView;
	private View mydevicesView;
	private View myalertsView;
	private SharedPreferences userPreferences;
	private LayoutInflater inflater;
	
	private String selectedDeviceName;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);

    	inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userPreferences = getSharedPreferences(Constants.SETTINGS_PREFERENCES, 0);
        
        tabs = getTabHost();

        TabHost.TabSpec registrationTab = initRegistrationTab();
        tabs.addTab(registrationTab);
        
        TabHost.TabSpec myalertsTab = initMyAlertsTab();
        tabs.addTab(myalertsTab);
        
        tabs.setOnTabChangedListener(new OnTabChangeListener(){
        	public void onTabChanged(String tag) {
        	    if(SETTINGS_TAB_TAG.equals(tag)) {
        	    	settingsTabContent.setDisplayedChild(0);
        	    	settingsPresenter.show();
        	    	tabs.setCurrentTab(0);
        	    }
        	    if(MYALERTS_TAB_TAG.equals(tag)) {
        	    	myalertsPresenter.show();
        	    	tabs.setCurrentTab(1);
        	    }
        	}});
        
    }
    
    @Override
	public void onResume() {
    	super.onResume();
    	
    	settingsPresenter.show();
        tabs.setCurrentTab(0);
    }
    
    private TabHost.TabSpec initRegistrationTab() {
        settingsTabContent = (ViewFlipper) findViewById(R.id.settings_tab_content);
        settingsView = inflater.inflate(R.layout.settings, null);
        settingsPresenter = new SettingsPresenter(settingsView, userPreferences);
        settingsPresenter.getProceedButton().setOnClickListener(proceedButtonOnClickListener);
		settingsPresenter.getCancelButton().setOnClickListener(cancelButtonOnClickListener);
		
        mydevicesView = inflater.inflate(R.layout.mydevices, null);
        mydevicesPresenter = new MyDevicesPresenter(MainTabActivity.this, mydevicesView, userPreferences);
		mydevicesPresenter.getRegisterButton().setOnClickListener(registerButtonOnClickListener);
		mydevicesPresenter.getBackToSettingsButton().setOnClickListener(settingsButtonOnClickListener);
		mydevicesPresenter.getDeviceList().setOnItemSelectedListener(deviceSelectedListener);
		
        settingsTabContent.addView(settingsView, 0,  new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        settingsTabContent.addView(mydevicesView, 1, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        TabHost.TabSpec registrationSpec = tabs.newTabSpec(SETTINGS_TAB_TAG).setIndicator("Settings",
        		getResources().getDrawable(R.drawable.settings)).setContent(R.id.settings_tab_content);
        
        return registrationSpec;
    }
    
    private TabHost.TabSpec initMyAlertsTab() {
    	myalertsTabContent = (LinearLayout) findViewById(R.id.myalerts_tab_content);
    	myalertsView = inflater.inflate(R.layout.myalerts, null);
    	myalertsPresenter = new MyAlertsPresenter(MainTabActivity.this, myalertsView, userPreferences);
    	
    	myalertsTabContent.addView(myalertsView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    	
        TabHost.TabSpec myalertsSpec = tabs.newTabSpec(MYALERTS_TAB_TAG)
        					.setIndicator("My Alerts", getResources().getDrawable(R.drawable.myalerts))
        					.setContent(R.id.myalerts_tab_content);
        
        return myalertsSpec;
    }
    
	private OnClickListener proceedButtonOnClickListener = new OnClickListener() {
		public void onClick(View view) {	
			mydevicesPresenter.show();
			settingsTabContent.showNext();
		}
	};
	
	private OnClickListener cancelButtonOnClickListener = new OnClickListener() {
	    public void onClick(View view) {
	    	myalertsPresenter.show();
			tabs.setCurrentTab(1);
	    }
	};
	
	private OnClickListener registerButtonOnClickListener = new OnClickListener() {
		public void onClick(View view) {

			// TODO: SoapResult doesn't contain the proper status and message. Need to
			// fix soap service to properly parse the returned soap message.
			SoapResult soapResult = mydevicesPresenter.registerDevice(selectedDeviceName);
			//final boolean isSuccessful = soapResult.isSuccessful(); 
			String message = "Registration successful.";
//			if (!isSuccessful) {
//				message = soapResult.getMessage();
//			}

			new AlertDialog.Builder(MainTabActivity.this)
					.setMessage(message)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									//if (isSuccessful) {
										myalertsPresenter.show();
										tabs.setCurrentTab(1);
									//}
								}
							}).show();
		}
	};

	private OnClickListener settingsButtonOnClickListener = new OnClickListener() {
		public void onClick(View view) {
			settingsTabContent.showPrevious();
		}
	};
	
	private OnItemSelectedListener deviceSelectedListener = new OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			selectedDeviceName = (String) parent.getItemAtPosition(position);	
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};
}
