package com.zirconi.etc;

import com.zirconi.db.DBTools;
import com.zirconi.db.DomDBHelper;
import com.zirconi.entity.DomEntity;
import com.zirconi.huaxiaclient.R;

import android.app.Application;
import android.content.SharedPreferences;

public class GlobalApplication extends Application {

	public DomEntity gDomObj;
	private String ServerAddr;
	private String AcademicCode;
	//private boolean RememberLogin;
	private boolean HasLogin;
	private SharedPreferences settings;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.gDomObj = null;
		// get default settings
		// if no setting , set it.
		settings = getSharedPreferences("com.zirconi.huaxiaclient_preferences",
				0);
		this.isHasLogin();
		//this.isRememberLogin();
		this.getAcademicCode();
		this.getServerAddr();
	}

//	public boolean isRememberLogin() {
//		this.RememberLogin = settings.getBoolean("rememberlogin", true);
//		if (RememberLogin == false) {
//			this.RememberLogin = false;
//			settings.edit().putBoolean("rememberlogin", false).commit();
//		} else {
//			settings.edit().putBoolean("rememberlogin", true).commit();
//		}
//		return RememberLogin;
//	}

//	public void setRememberLogin(boolean rememberLogin) {
//		RememberLogin = rememberLogin;
//	}

	public DomEntity getgDomObj() {
		return gDomObj;
	}

	public void setgDomObj(DomEntity gDomObj) {
		this.gDomObj = gDomObj;
	}

	public String getServerAddr() {
		this.ServerAddr = settings.getString("serveraddr", null);
		if (ServerAddr == null) {
			String a = (String) getResources().getTextArray(
					R.array.sa_serverlistentity)[0];
			settings.edit().putString("serveraddr", a).commit();
			ServerAddr = a;
		}
		return ServerAddr;
	}

	public void setServerAddr(String serverAddr) {
		ServerAddr = serverAddr;
	}

	public String getAcademicCode() {
		this.AcademicCode = settings.getString("academiccode", null);
		if (AcademicCode == null) {
			String b = (String) getResources().getTextArray(
					R.array.sa_academiclistentity)[0];
			settings.edit().putString("academiccode", b).commit();
			AcademicCode = b;
		}
		return AcademicCode;
	}

	public void setAcademicCode(String academicCode) {
		this.AcademicCode = academicCode;
	}

	public boolean isHasLogin() {
		DomDBHelper dbHelper = new DomDBHelper(getApplicationContext());

		if (DBTools.GetCursor(dbHelper, "STUINFO").moveToNext()) {
			this.HasLogin = true;
			//if has login , read data from DB
			this.gDomObj = DBTools.GetDom(dbHelper);
		}

		else {
			this.HasLogin = false;
		}
		return HasLogin;
	}

	public void setHasLogin(boolean hasLogin) {
		HasLogin = hasLogin;
	}

}
