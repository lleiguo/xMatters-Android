package com.xmatters.mobile.android.service;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

//public class SoapService extends Service {
public class SoapService {
	// private final IBinder localBinder = new LocalBinder();

	private static final String WEB_SERVICE_URL = "/api/services/AlarmPointWebService";
	private static final String NAMESPACE = "http://www.alarmpoint.com/webservices/schema";

	// public class LocalBinder extends Binder {
	// public SoapService getService() {
	// return SoapService.this;
	// }
	// }

	public SoapResult registerDevice(String serverUrl, String companyName,
			String username, String password, String deviceName) {
		String absoluteWebServiceUrl = serverUrl + WEB_SERVICE_URL;

		SoapObject userServiceProvider = new SoapObject(NAMESPACE,
				"userServiceProvider");
		userServiceProvider.addProperty("identifier",
				"Apple Push User Service Provider");
		userServiceProvider.addProperty("name", "");

		SoapObject applePushDevice = new SoapObject(NAMESPACE,
				"applePushDevice");
		applePushDevice.addProperty("active", true);
		applePushDevice.addProperty("default", true);
		applePushDevice.addProperty("delay", 0);
		applePushDevice.addProperty("externallyOwned", "false");
		applePushDevice.addProperty("name", deviceName);
		applePushDevice.addProperty("priorityThreshold", "LOW");
		applePushDevice.addProperty("userServiceProvider", userServiceProvider);
		applePushDevice
				.addProperty("apnToken",
						"afdf7aa22adf911d3205e44affaea66651785a22fc389a59ad92e68c81185cd6");
		applePushDevice.addProperty("appleDeviceId",
				"1f63bbc568c4cad39ddce80bcbd2209b4f7bbd6b");

		SoapObject registerDeviceParams = new SoapObject(NAMESPACE,
				"registerDeviceParams");
		registerDeviceParams.addProperty("applePushDevice", applePushDevice);
		registerDeviceParams.addProperty("owner", username);
		registerDeviceParams.addProperty("ownerPassword", password);

		SoapObject mobileRegisterDevice = new SoapObject(NAMESPACE,
				"MobileRegisterDevice");
		mobileRegisterDevice.addProperty("clientTimestamp", "");
		mobileRegisterDevice.addProperty("clientIP", "");
		mobileRegisterDevice.addProperty("clientOSUser", "");
		mobileRegisterDevice.addProperty("company", companyName);
		mobileRegisterDevice.addProperty("registerDeviceParams",
				registerDeviceParams);

		SoapResult result = new SoapResult();
		try {
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(mobileRegisterDevice);
			HttpTransportSE httpTransport = new HttpTransportSE(
					absoluteWebServiceUrl);
			// httpTransport.debug = true;
			httpTransport.call(absoluteWebServiceUrl + "/"
					+ "MobileRegisterDevice", envelope);
			SoapObject response = (SoapObject) envelope.getResponse();
			result.setStatus(response.getProperty(1).toString());
			result.setMessage(response.getProperty(2).toString());
		} catch (Exception ex) {
			result.setStatus("System Exception");
			result.setStatus(ex.getMessage());
		}

		return result;
	}

	public List<SoapUserDeviceStatus> findUserDeviceStatus(String serverUrl,
			String companyName, String username, String password) {

		List<SoapUserDeviceStatus> results = new ArrayList<SoapUserDeviceStatus>();

		String absoluteWebServiceUrl = serverUrl + WEB_SERVICE_URL;

		SoapObject params = new SoapObject(NAMESPACE, "params");
		params.addProperty("deviceType", "APPLE_PUSH");
		params.addProperty("owner", username);
		params.addProperty("ownerPassword", password);

		SoapObject mobileFindUserDeviceStatus = new SoapObject(NAMESPACE,
				"MobileFindUserDeviceStatus");
		mobileFindUserDeviceStatus.addProperty("clientTimestamp", "");
		mobileFindUserDeviceStatus.addProperty("clientIP", "");
		mobileFindUserDeviceStatus.addProperty("clientOSUser", "");
		mobileFindUserDeviceStatus.addProperty("company", companyName);
		mobileFindUserDeviceStatus.addProperty("params", params);

		try {
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(mobileFindUserDeviceStatus);
			HttpTransportSE httpTransport = new HttpTransportSE(
					absoluteWebServiceUrl);
			// httpTransport.debug = true;
			httpTransport.call(absoluteWebServiceUrl + "/"
					+ "MobileFindUserDeviceStatus", envelope);

			SoapObject response = (SoapObject) envelope.getResponse();
			SoapObject soapFindUserDeviceStatus = (SoapObject) response
					.getProperty(0);

			int deviceCount = soapFindUserDeviceStatus.getPropertyCount();
			for (int i = 0; i < deviceCount; ++i) {
				SoapUserDeviceStatus status = new SoapUserDeviceStatus();
				SoapObject soapFindUserApplePushDeviceStatus = (SoapObject) soapFindUserDeviceStatus
						.getProperty(i);
				if (soapFindUserApplePushDeviceStatus.getPropertyCount() == 2) {
					SoapPrimitive appleDeviceId = (SoapPrimitive) soapFindUserApplePushDeviceStatus
							.getProperty(0);
					SoapPrimitive deviceName = (SoapPrimitive) soapFindUserApplePushDeviceStatus
							.getProperty(1);

					if (appleDeviceId != null) {
						status.setDeviceId(appleDeviceId.toString());
					}
					if (deviceName != null) {
						status.setDeviceName(deviceName.toString());
					}
					results.add(status);
				}
			}
		} catch (Exception ex) {
			// Ignore for now
		}

		return results;
	}

	// @Override
	// public IBinder onBind(Intent intent) {
	// return localBinder;
	// }
}
