package com.xmatters.mobile.android;

import java.net.URLEncoder;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MyAlertsPresenter {

	private Context context;
	private SharedPreferences userPreferences;

	private View myalertsView;
	
	private WebView browser;
	private Dialog progressDialog;
	
	public MyAlertsPresenter(Context context, View view, SharedPreferences preferences) {
		
		this.context = context;
		this.myalertsView = view;
		this.userPreferences = preferences;
		
		browser = (WebView) this.myalertsView.findViewById(R.id.webview);
		browser.getSettings().setJavaScriptEnabled(true);
		
		ProgressBar progressBar = new ProgressBar(this.context);
		progressBar.setIndeterminate(true);
		progressDialog = new Dialog(this.context, R.style.TransparentProgressDialog);
		progressDialog.setContentView(progressBar, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	
	public void show() {
		progressDialog.show();
		String serverUrl = userPreferences.getString(Constants.SERVER_URL, null);
		String company = userPreferences.getString(Constants.COMPANY, null);
		String username = userPreferences.getString(Constants.USERNAME, null);
		String password = userPreferences.getString(Constants.PASSWORD, null);

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
				
				browser.setWebViewClient(new WebViewClient() {
					public void onLoadResource(WebView view, String url) {
					}
					
		            public void onPageFinished(WebView view, String url) {
		                if (progressDialog.isShowing()) {
		                	progressDialog.dismiss();
		                }
		            }
		        });
				
				browser.loadUrl(url.toString());
			} catch (Exception ex) {
			}
		} else {
			browser.loadData("<html><head/><body>Please ensure your settings are correct.<body></html>",
					"text/html", "UTF-8");
		}		
	}
}