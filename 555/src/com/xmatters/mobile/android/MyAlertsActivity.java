package com.xmatters.mobile.android;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MyAlertsActivity extends Activity {
	private WebView webView;
	private Dialog progressDialog;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myalerts);

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		progressDialog = new Dialog(this, R.style.TransparentProgressDialog);
		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setIndeterminate(true);
		progressDialog.setContentView(progressBar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void onResume() {
		super.onResume();
		show();
	}
	
	public void show() {
		progressDialog.show();
		SharedPreferences settings = getSharedPreferences(
				Constants.SETTINGS_PREFERENCES, 0);
		String serverUrl = settings.getString(Constants.SERVER_URL, null);
		String company = settings.getString(Constants.COMPANY, null);
		String username = settings.getString(Constants.USERNAME, null);
		String password = settings.getString(Constants.PASSWORD, null);

		if (serverUrl != null && company != null && username != null
				&& password != null) {

			try {
				StringBuffer url = new StringBuffer(serverUrl);
				url.append("/jsp/MobileEventList.jsp?cn=");
				url.append(URLEncoder.encode(company, "UTF-8"));
				url.append("&un=");
				url.append(URLEncoder.encode(username, "UTF-8"));
				url.append("&pw=");
				url.append(URLEncoder.encode(password, "UTF-8"));
				
				webView.setWebViewClient(new WebViewClient() {
					public void onLoadResource(WebView view, String url) {
						int i=0;
					}
					
		            public void onPageFinished(WebView view, String url) {
		                if (progressDialog.isShowing()) {
		                	progressDialog.dismiss();
		                }
		            }
		        });
				
				webView.loadUrl(url.toString());
			} catch (Exception ex) {
			}
		} else {
			webView.loadData("<html><head/><body>Please ensure your settings are correct.<body></html>",
					"text/html", "UTF-8");
		}		
	}
}