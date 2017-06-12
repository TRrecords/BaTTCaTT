package com.batcat;

import android.view.*;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.WebView;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends android.support.v7.app.AppCompatActivity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.batcat", "com.batcat.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.batcat", "com.batcat.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.batcat.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.phone.PackageManagerWrapper _pak = null;
public static anywheresoftware.b4a.objects.CSBuilder _cs = null;
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public com.rootsoft.oslibrary.OSLibrary _ra = null;
public anywheresoftware.b4a.phone.PhoneEvents _device = null;
public com.rootsoft.customtoast.CustomToast _ct = null;
public de.donmanfred.pgWheel _pgwheel1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lk = null;
public anywheresoftware.b4a.objects.ListViewWrapper _listview1 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lv2 = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lvmenu = null;
public Object[] _args = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj1 = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj2 = null;
public anywheresoftware.b4a.agraham.reflection.Reflection _obj3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _la = null;
public anywheresoftware.b4a.objects.LabelWrapper _la1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _la2 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _ima = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _icon = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _logo = null;
public com.batcat.batut _bat = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _checkbox1 = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _checkbox2 = null;
public static String _volt = "";
public static String _temp = "";
public static String _level1 = "";
public anywheresoftware.b4a.objects.PanelWrapper _panel2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel3 = null;
public anywheresoftware.b4a.objects.PanelWrapper _lip = null;
public anywheresoftware.b4a.objects.PanelWrapper _setp = null;
public anywheresoftware.b4a.objects.PanelWrapper _smlp = null;
public com.batcat.keyvaluestore _kvs = null;
public com.batcat.keyvaluestore _kvs2 = null;
public com.batcat.keyvaluestore _kvs3 = null;
public com.batcat.keyvaluestore _kvs4 = null;
public com.batcat.keyvaluestore _kvs4sub = null;
public anywheresoftware.b4a.objects.collections.Map _optmap = null;
public anywheresoftware.b4a.objects.LabelWrapper _label5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label6 = null;
public anywheresoftware.b4a.objects.collections.List _proc = null;
public anywheresoftware.b4a.objects.PanelWrapper _panel1 = null;
public com.maximussoft.showtips.BuilderWrapper _showtip = null;
public com.batcat.clsslidingsidebar _slm = null;
public static int _cas = 0;
public static int _size = 0;
public static int _flags = 0;
public static String[] _types = null;
public static String _name = "";
public static String _packname = "";
public static String _date = "";
public static String _time = "";
public static String _l = "";
public static String _ramsize = "";
public anywheresoftware.b4a.objects.collections.List _list = null;
public anywheresoftware.b4a.objects.collections.List _list1 = null;
public anywheresoftware.b4a.objects.collections.List _list2 = null;
public anywheresoftware.b4a.objects.collections.List _list3 = null;
public anywheresoftware.b4a.objects.collections.List _list4 = null;
public anywheresoftware.b4a.objects.collections.List _list5 = null;
public anywheresoftware.b4a.objects.collections.List _list6 = null;
public anywheresoftware.b4a.objects.collections.List _phlis = null;
public anywheresoftware.b4a.objects.collections.List _lis = null;
public anywheresoftware.b4a.objects.collections.List _setlist = null;
public anywheresoftware.b4a.objects.collections.List _datalist = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2 _cd2 = null;
public anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2 _cd = null;
public anywheresoftware.b4a.objects.PanelWrapper _fakeactionbar = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _sd = null;
public anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _sdi = null;
public de.amberhome.objects.appcompat.ACToolbarLightWrapper _actoolbarlight1 = null;
public de.amberhome.objects.appcompat.ACActionBar _toolbarhelper = null;
public de.amberhome.objects.appcompat.ACButtonWrapper _button4 = null;
public static int _c1 = 0;
public static int _c2 = 0;
public static int _c3 = 0;
public static int _c4 = 0;
public static int _c5 = 0;
public static int _c6 = 0;
public static int _c7 = 0;
public static int _c8 = 0;
public static int _c9 = 0;
public static int _c10 = 0;
public static int _c11 = 0;
public static int _c12 = 0;
public static int _c13 = 0;
public static int _c14 = 0;
public static int _c15 = 0;
public com.tchart.materialcolors.MaterialColors _mcl = null;
public anywheresoftware.b4a.objects.collections.List _mclist = null;
public anywheresoftware.b4a.objects.collections.List _ramlist = null;
public com.batcat.klo _klo = null;
public com.batcat.cool _cool = null;
public com.batcat.hw _hw = null;
public com.batcat.starter _starter = null;
public com.batcat.sys _sys = null;
public com.batcat.settings _settings = null;
public com.batcat.charts _charts = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (klo.mostCurrent != null);
vis = vis | (cool.mostCurrent != null);
vis = vis | (sys.mostCurrent != null);
vis = vis | (settings.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
int _barsize = 0;
anywheresoftware.b4a.objects.LabelWrapper _lvm1 = null;
anywheresoftware.b4a.objects.LabelWrapper _lvm2 = null;
 //BA.debugLineNum = 75;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 77;BA.debugLine="Activity.LoadLayout(\"1\")";
mostCurrent._activity.LoadLayout("1",mostCurrent.activityBA);
 //BA.debugLineNum = 80;BA.debugLine="Activity.Title=pak.GetApplicationLabel(\"com.batca";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(_pak.GetApplicationLabel("com.batcat")+" - "+_pak.GetVersionName("com.batcat")));
 //BA.debugLineNum = 85;BA.debugLine="ToolbarHelper.Initialize";
mostCurrent._toolbarhelper.Initialize(mostCurrent.activityBA);
 //BA.debugLineNum = 87;BA.debugLine="ToolbarHelper.Hide";
mostCurrent._toolbarhelper.Hide();
 //BA.debugLineNum = 90;BA.debugLine="ra.Initialize(\"ra\")";
mostCurrent._ra.Initialize(processBA,"ra");
 //BA.debugLineNum = 91;BA.debugLine="bat.Initialize";
mostCurrent._bat._initialize(processBA);
 //BA.debugLineNum = 92;BA.debugLine="ct.Initialize";
mostCurrent._ct.Initialize(processBA);
 //BA.debugLineNum = 93;BA.debugLine="list.Initialize";
mostCurrent._list.Initialize();
 //BA.debugLineNum = 94;BA.debugLine="list1.Initialize";
mostCurrent._list1.Initialize();
 //BA.debugLineNum = 95;BA.debugLine="list2.Initialize";
mostCurrent._list2.Initialize();
 //BA.debugLineNum = 96;BA.debugLine="list3.Initialize";
mostCurrent._list3.Initialize();
 //BA.debugLineNum = 97;BA.debugLine="list4.Initialize";
mostCurrent._list4.Initialize();
 //BA.debugLineNum = 98;BA.debugLine="list5.Initialize";
mostCurrent._list5.Initialize();
 //BA.debugLineNum = 99;BA.debugLine="list6.Initialize";
mostCurrent._list6.Initialize();
 //BA.debugLineNum = 100;BA.debugLine="phlis.Initialize";
mostCurrent._phlis.Initialize();
 //BA.debugLineNum = 101;BA.debugLine="datalist.Initialize";
mostCurrent._datalist.Initialize();
 //BA.debugLineNum = 102;BA.debugLine="setlist.Initialize";
mostCurrent._setlist.Initialize();
 //BA.debugLineNum = 103;BA.debugLine="lv2.Initialize(\"lv2\")";
mostCurrent._lv2.Initialize(mostCurrent.activityBA,"lv2");
 //BA.debugLineNum = 104;BA.debugLine="panel3.Initialize(\"panel3\")";
mostCurrent._panel3.Initialize(mostCurrent.activityBA,"panel3");
 //BA.debugLineNum = 105;BA.debugLine="smlp.Initialize(\"smlp\")";
mostCurrent._smlp.Initialize(mostCurrent.activityBA,"smlp");
 //BA.debugLineNum = 106;BA.debugLine="lip.Initialize(\"lip\")";
mostCurrent._lip.Initialize(mostCurrent.activityBA,"lip");
 //BA.debugLineNum = 107;BA.debugLine="setp.Initialize(\"setp\")";
mostCurrent._setp.Initialize(mostCurrent.activityBA,"setp");
 //BA.debugLineNum = 108;BA.debugLine="lis.Initialize";
mostCurrent._lis.Initialize();
 //BA.debugLineNum = 109;BA.debugLine="proc.Initialize";
mostCurrent._proc.Initialize();
 //BA.debugLineNum = 110;BA.debugLine="lk.Initialize(\"lk\")";
mostCurrent._lk.Initialize(mostCurrent.activityBA,"lk");
 //BA.debugLineNum = 111;BA.debugLine="optmap.Initialize";
mostCurrent._optmap.Initialize();
 //BA.debugLineNum = 112;BA.debugLine="la.Initialize(\"la\")";
mostCurrent._la.Initialize(mostCurrent.activityBA,"la");
 //BA.debugLineNum = 113;BA.debugLine="la1.Initialize(\"la1\")";
mostCurrent._la1.Initialize(mostCurrent.activityBA,"la1");
 //BA.debugLineNum = 114;BA.debugLine="la2.Initialize(\"la2\")";
mostCurrent._la2.Initialize(mostCurrent.activityBA,"la2");
 //BA.debugLineNum = 115;BA.debugLine="ramlist.Initialize";
mostCurrent._ramlist.Initialize();
 //BA.debugLineNum = 116;BA.debugLine="panel3.AddView(lk,10,5,80%x,50%y)";
mostCurrent._panel3.AddView((android.view.View)(mostCurrent._lk.getObject()),(int) (10),(int) (5),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (80),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA));
 //BA.debugLineNum = 117;BA.debugLine="lv2.FastScrollEnabled=True";
mostCurrent._lv2.setFastScrollEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 118;BA.debugLine="logo.InitializeSample(File.DirAssets, \"sulo_log99";
mostCurrent._logo.InitializeSample(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"sulo_log99.png",(int) (48),(int) (48));
 //BA.debugLineNum = 119;BA.debugLine="panel3.Color=Colors.Transparent";
mostCurrent._panel3.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 120;BA.debugLine="lip.Color=Colors.ARGB(255,77,79,79)";
mostCurrent._lip.setColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (77),(int) (79),(int) (79)));
 //BA.debugLineNum = 121;BA.debugLine="lip.Elevation=50dip";
mostCurrent._lip.setElevation((float) (anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))));
 //BA.debugLineNum = 122;BA.debugLine="lip.AddView(lv2,1dip,1dip,-1,-1)";
mostCurrent._lip.AddView((android.view.View)(mostCurrent._lv2.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)),(int) (-1),(int) (-1));
 //BA.debugLineNum = 123;BA.debugLine="cd2.AddView(lip,-1,-1)";
mostCurrent._cd2.AddView((android.view.View)(mostCurrent._lip.getObject()),(int) (-1),(int) (-1));
 //BA.debugLineNum = 124;BA.debugLine="smlp.Enabled=True";
mostCurrent._smlp.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 125;BA.debugLine="showtip.Initialize(\"showtip\")";
mostCurrent._showtip.Initialize(mostCurrent.activityBA,"showtip");
 //BA.debugLineNum = 126;BA.debugLine="ima.Initialize(\"ima\")";
mostCurrent._ima.Initialize(mostCurrent.activityBA,"ima");
 //BA.debugLineNum = 127;BA.debugLine="ima.Bitmap=LoadBitmap(File.DirAssets,\"icon_batcat";
mostCurrent._ima.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"icon_batcat.png").getObject()));
 //BA.debugLineNum = 128;BA.debugLine="panel3.AddView(ima,50,150,450,250)";
mostCurrent._panel3.AddView((android.view.View)(mostCurrent._ima.getObject()),(int) (50),(int) (150),(int) (450),(int) (250));
 //BA.debugLineNum = 130;BA.debugLine="date=DateTime.Date(DateTime.Now)";
mostCurrent._date = anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 131;BA.debugLine="time=DateTime.Time(DateTime.Now)";
mostCurrent._time = anywheresoftware.b4a.keywords.Common.DateTime.Time(anywheresoftware.b4a.keywords.Common.DateTime.getNow());
 //BA.debugLineNum = 133;BA.debugLine="volt=bat.BatteryInformation(7)/1000";
mostCurrent._volt = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (7)]/(double)1000);
 //BA.debugLineNum = 134;BA.debugLine="temp=bat.BatteryInformation(6)/10";
mostCurrent._temp = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (6)]/(double)10);
 //BA.debugLineNum = 135;BA.debugLine="level1=bat.BatteryInformation(0)";
mostCurrent._level1 = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (0)]);
 //BA.debugLineNum = 137;BA.debugLine="If FirstTime=True Then";
if (_firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 138;BA.debugLine="show_tip";
_show_tip();
 //BA.debugLineNum = 140;BA.debugLine="If File.Exists(File.DirDefaultExternal&\"/mnt/cac";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","smap1.txt")) { 
 //BA.debugLineNum = 141;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 }else {
 //BA.debugLineNum = 143;BA.debugLine="File.MakeDir(File.DirDefaultExternal, \"mnt/cac";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"mnt/cache");
 //BA.debugLineNum = 144;BA.debugLine="File.MakeDir(File.DirRootExternal, \"mnt/data\")";
anywheresoftware.b4a.keywords.Common.File.MakeDir(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"mnt/data");
 //BA.debugLineNum = 145;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","sv.txt",mostCurrent._list4);
 //BA.debugLineNum = 146;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","fn.txt",mostCurrent._list1);
 //BA.debugLineNum = 147;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","APnames.txt",mostCurrent._list3);
 //BA.debugLineNum = 148;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl.txt",mostCurrent._level1);
 //BA.debugLineNum = 149;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","volt.txt","5");
 //BA.debugLineNum = 150;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","pn1.txt",mostCurrent._lis);
 //BA.debugLineNum = 151;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","settings.txt",mostCurrent._setlist);
 //BA.debugLineNum = 152;BA.debugLine="File.WriteList(File.DirDefaultExternal&\"/mnt/cach";
anywheresoftware.b4a.keywords.Common.File.WriteList(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","proc.txt",mostCurrent._proc);
 //BA.debugLineNum = 153;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl2.txt","0");
 //BA.debugLineNum = 154;BA.debugLine="File.WriteMap(File.DirDefaultExternal&\"/mnt/cache";
anywheresoftware.b4a.keywords.Common.File.WriteMap(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","smap1.txt",mostCurrent._optmap);
 //BA.debugLineNum = 155;BA.debugLine="ToastMessageShow(\"BC Log loaded! \"&date&\", \"&time";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("BC Log loaded! "+mostCurrent._date+", "+mostCurrent._time),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 160;BA.debugLine="device.Initialize(\"device\")";
mostCurrent._device.Initialize(processBA,"device");
 //BA.debugLineNum = 162;BA.debugLine="Label2.Text= temp &\"°C\"";
mostCurrent._label2.setText(BA.ObjectToCharSequence(mostCurrent._temp+"°C"));
 //BA.debugLineNum = 163;BA.debugLine="Label4.Text= Round2(volt,1) &\" V\"";
mostCurrent._label4.setText(BA.ObjectToCharSequence(BA.NumberToString(anywheresoftware.b4a.keywords.Common.Round2((double)(Double.parseDouble(mostCurrent._volt)),(int) (1)))+" V"));
 //BA.debugLineNum = 167;BA.debugLine="Label3.Text=\"Ver. \"&pak.GetVersionName(\"com.batc";
mostCurrent._label3.setText(BA.ObjectToCharSequence("Ver. "+_pak.GetVersionName("com.batcat")));
 //BA.debugLineNum = 170;BA.debugLine="Dim BarSize As Int: BarSize = 60dip";
_barsize = 0;
 //BA.debugLineNum = 170;BA.debugLine="Dim BarSize As Int: BarSize = 60dip";
_barsize = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60));
 //BA.debugLineNum = 171;BA.debugLine="FakeActionBar.Initialize(\"\")";
mostCurrent._fakeactionbar.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 172;BA.debugLine="FakeActionBar.Color =  Colors.Transparent";
mostCurrent._fakeactionbar.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 173;BA.debugLine="Activity.AddView(FakeActionBar, 0, 0, 100%x, BarS";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._fakeactionbar.getObject()),(int) (0),(int) (0),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_barsize);
 //BA.debugLineNum = 174;BA.debugLine="Activity.AddView(smlp,0,5dip,100%x,65%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._smlp.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (65),mostCurrent.activityBA));
 //BA.debugLineNum = 175;BA.debugLine="slm.Initialize(smlp, 250dip, 1, 1, 100, 200)";
mostCurrent._slm._initialize(mostCurrent.activityBA,mostCurrent._smlp,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (250)),(byte) (1),(byte) (1),(int) (100),(int) (200));
 //BA.debugLineNum = 176;BA.debugLine="slm.ContentPanel.Color = Colors.Transparent";
mostCurrent._slm._contentpanel().setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 177;BA.debugLine="slm.Sidebar.Background = slm.LoadDrawable(\"popup_";
mostCurrent._slm._sidebar().setBackground((android.graphics.drawable.Drawable)(mostCurrent._slm._loaddrawable("popup_top_dark")));
 //BA.debugLineNum = 178;BA.debugLine="slm.SetOnChangeListeners(Me, \"Menu_onFullyOpen\",";
mostCurrent._slm._setonchangelisteners(main.getObject(),"Menu_onFullyOpen","Menu_onFullyClosed","Menu_onMove");
 //BA.debugLineNum = 180;BA.debugLine="lvMenu.Initialize(\"lvMenu\")";
mostCurrent._lvmenu.Initialize(mostCurrent.activityBA,"lvMenu");
 //BA.debugLineNum = 181;BA.debugLine="Dim lvm1,lvm2 As Label";
_lvm1 = new anywheresoftware.b4a.objects.LabelWrapper();
_lvm2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 182;BA.debugLine="lvm1=lvMenu.TwoLinesAndBitmap.Label";
_lvm1 = mostCurrent._lvmenu.getTwoLinesAndBitmap().Label;
 //BA.debugLineNum = 183;BA.debugLine="lvm2=lvMenu.TwoLinesAndBitmap.SecondLabel";
_lvm2 = mostCurrent._lvmenu.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 184;BA.debugLine="lvm1.TextColor = Colors.White";
_lvm1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 185;BA.debugLine="lvm1.textsize=14";
_lvm1.setTextSize((float) (14));
 //BA.debugLineNum = 186;BA.debugLine="lvm2.textsize=12";
_lvm2.setTextSize((float) (12));
 //BA.debugLineNum = 187;BA.debugLine="lvm2.textcolor=Colors.LightGray";
_lvm2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 188;BA.debugLine="lvMenu.TwoLinesAndBitmap.ImageView.Height=42dip";
mostCurrent._lvmenu.getTwoLinesAndBitmap().ImageView.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (42)));
 //BA.debugLineNum = 189;BA.debugLine="lvMenu.TwoLinesAndBitmap.ImageView.Width=42dip";
mostCurrent._lvmenu.getTwoLinesAndBitmap().ImageView.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (42)));
 //BA.debugLineNum = 190;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"System Info\",\"Detai";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("System Info"),BA.ObjectToCharSequence("Detail Phone Info"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Tag.png").getObject()),(Object)(4));
 //BA.debugLineNum = 191;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Statistic\",\"Battery";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Statistic"),BA.ObjectToCharSequence("Battery Cyclus"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"chart.png").getObject()),(Object)(3));
 //BA.debugLineNum = 192;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Power\",\"OS Power Me";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Power"),BA.ObjectToCharSequence("OS Power Menu"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Battery.png").getObject()),(Object)(2));
 //BA.debugLineNum = 193;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Settings\",\"Options";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Settings"),BA.ObjectToCharSequence("Options Menu"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Control.png").getObject()),(Object)(0));
 //BA.debugLineNum = 194;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Info\",\"Version Info";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Info"),BA.ObjectToCharSequence("Version Info, über BC.."),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Rss.png").getObject()),(Object)(5));
 //BA.debugLineNum = 195;BA.debugLine="lvMenu.AddTwoLinesAndBitmap2(\"Exit\",\"Close the Ap";
mostCurrent._lvmenu.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("Exit"),BA.ObjectToCharSequence("Close the Application/Service"),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Power.png").getObject()),(Object)(1));
 //BA.debugLineNum = 196;BA.debugLine="lvMenu.TwoLinesAndBitmap.ItemHeight=95";
mostCurrent._lvmenu.getTwoLinesAndBitmap().setItemHeight((int) (95));
 //BA.debugLineNum = 197;BA.debugLine="lvMenu.Color = Colors.Transparent";
mostCurrent._lvmenu.setColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 198;BA.debugLine="lvMenu.ScrollingBackgroundColor = Colors.Transpar";
mostCurrent._lvmenu.setScrollingBackgroundColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 199;BA.debugLine="slm.Sidebar.AddView(lvMenu, 10dip,9dip, -1, -1)";
mostCurrent._slm._sidebar().AddView((android.view.View)(mostCurrent._lvmenu.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (9)),(int) (-1),(int) (-1));
 //BA.debugLineNum = 201;BA.debugLine="slm.EnableSwipeGesture(True,400,1)";
mostCurrent._slm._enableswipegesture(anywheresoftware.b4a.keywords.Common.True,(int) (400),(byte) (1));
 //BA.debugLineNum = 206;BA.debugLine="lv2.Enabled=True";
mostCurrent._lv2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 208;BA.debugLine="setp.LoadLayout(\"settings_pa\")";
mostCurrent._setp.LoadLayout("settings_pa",mostCurrent.activityBA);
 //BA.debugLineNum = 210;BA.debugLine="la=ListView1.SingleLineLayout.Label";
mostCurrent._la = mostCurrent._listview1.getSingleLineLayout().Label;
 //BA.debugLineNum = 211;BA.debugLine="la1=ListView1.TwoLinesLayout.Label";
mostCurrent._la1 = mostCurrent._listview1.getTwoLinesLayout().Label;
 //BA.debugLineNum = 212;BA.debugLine="la2=ListView1.TwoLinesLayout.SecondLabel";
mostCurrent._la2 = mostCurrent._listview1.getTwoLinesLayout().SecondLabel;
 //BA.debugLineNum = 214;BA.debugLine="kvs.Initialize(File.DirDefaultExternal, \"datastor";
mostCurrent._kvs._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore");
 //BA.debugLineNum = 215;BA.debugLine="kvs2.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs2._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_2");
 //BA.debugLineNum = 216;BA.debugLine="kvs3.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs3._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_3");
 //BA.debugLineNum = 217;BA.debugLine="kvs4.Initialize(File.DirDefaultExternal, \"datasto";
mostCurrent._kvs4._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_4");
 //BA.debugLineNum = 218;BA.debugLine="kvs4sub.Initialize(File.DirDefaultExternal, \"data";
mostCurrent._kvs4sub._initialize(processBA,anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal(),"datastore_sub_4");
 //BA.debugLineNum = 224;BA.debugLine="Mclist.Initialize";
mostCurrent._mclist.Initialize();
 //BA.debugLineNum = 226;BA.debugLine="Mclist.Clear";
mostCurrent._mclist.Clear();
 //BA.debugLineNum = 227;BA.debugLine="Mclist.Add(c1)";
mostCurrent._mclist.Add((Object)(_c1));
 //BA.debugLineNum = 228;BA.debugLine="Mclist.Add(c2)";
mostCurrent._mclist.Add((Object)(_c2));
 //BA.debugLineNum = 229;BA.debugLine="Mclist.Add(c3)";
mostCurrent._mclist.Add((Object)(_c3));
 //BA.debugLineNum = 230;BA.debugLine="Mclist.Add(c4)";
mostCurrent._mclist.Add((Object)(_c4));
 //BA.debugLineNum = 231;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 232;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 233;BA.debugLine="c3=mcl.md_lime_A700";
_c3 = mostCurrent._mcl.getmd_lime_A700();
 //BA.debugLineNum = 234;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 235;BA.debugLine="c5=mcl.md_deep_purple_A700";
_c5 = mostCurrent._mcl.getmd_deep_purple_A700();
 //BA.debugLineNum = 236;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 237;BA.debugLine="c7=mcl.md_indigo_A700";
_c7 = mostCurrent._mcl.getmd_indigo_A700();
 //BA.debugLineNum = 238;BA.debugLine="c8=mcl.md_blue_A700";
_c8 = mostCurrent._mcl.getmd_blue_A700();
 //BA.debugLineNum = 239;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 240;BA.debugLine="c10=mcl.md_grey_700";
_c10 = mostCurrent._mcl.getmd_grey_700();
 //BA.debugLineNum = 241;BA.debugLine="c11=mcl.md_green_A700";
_c11 = mostCurrent._mcl.getmd_green_A700();
 //BA.debugLineNum = 242;BA.debugLine="c12=mcl.md_light_green_A700";
_c12 = mostCurrent._mcl.getmd_light_green_A700();
 //BA.debugLineNum = 243;BA.debugLine="c13=mcl.md_yellow_A700";
_c13 = mostCurrent._mcl.getmd_yellow_A700();
 //BA.debugLineNum = 244;BA.debugLine="c14=mcl.md_cyan_A700";
_c14 = mostCurrent._mcl.getmd_cyan_A700();
 //BA.debugLineNum = 245;BA.debugLine="c15=mcl.md_blue_grey_700";
_c15 = mostCurrent._mcl.getmd_blue_grey_700();
 //BA.debugLineNum = 246;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 //BA.debugLineNum = 248;BA.debugLine="ra.setLowMemory";
mostCurrent._ra.setLowMemory();
 //BA.debugLineNum = 250;BA.debugLine="app_info";
_app_info();
 //BA.debugLineNum = 251;BA.debugLine="lab_set2";
_lab_set2();
 //BA.debugLineNum = 252;BA.debugLine="label_text";
_label_text();
 //BA.debugLineNum = 253;BA.debugLine="loading_norm";
_loading_norm();
 //BA.debugLineNum = 254;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 255;BA.debugLine="li2";
_li2();
 //BA.debugLineNum = 257;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 708;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 709;BA.debugLine="If KeyCode=KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 710;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 711;BA.debugLine="ToastMessageShow(\"BCT - Backround Service\",False";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("BCT - Backround Service"),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 713;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 714;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 268;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 270;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 259;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 260;BA.debugLine="app_info";
_app_info();
 //BA.debugLineNum = 261;BA.debugLine="lab_set2";
_lab_set2();
 //BA.debugLineNum = 262;BA.debugLine="label_text";
_label_text();
 //BA.debugLineNum = 263;BA.debugLine="loading_norm";
_loading_norm();
 //BA.debugLineNum = 264;BA.debugLine="store_check";
_store_check();
 //BA.debugLineNum = 265;BA.debugLine="li2";
_li2();
 //BA.debugLineNum = 266;BA.debugLine="End Sub";
return "";
}
public static String  _app_info() throws Exception{
int _i = 0;
int _form = 0;
 //BA.debugLineNum = 826;BA.debugLine="Sub app_info";
 //BA.debugLineNum = 827;BA.debugLine="lv2.Clear";
mostCurrent._lv2.Clear();
 //BA.debugLineNum = 828;BA.debugLine="list3.Clear";
mostCurrent._list3.Clear();
 //BA.debugLineNum = 829;BA.debugLine="datalist.Clear";
mostCurrent._datalist.Clear();
 //BA.debugLineNum = 831;BA.debugLine="list1=pak.GetInstalledPackages";
mostCurrent._list1 = _pak.GetInstalledPackages();
 //BA.debugLineNum = 833;BA.debugLine="Obj1.Target = Obj1.GetContext";
mostCurrent._obj1.Target = (Object)(mostCurrent._obj1.GetContext(processBA));
 //BA.debugLineNum = 834;BA.debugLine="Obj1.Target = Obj1.RunMethod(\"getPackageManager\")";
mostCurrent._obj1.Target = mostCurrent._obj1.RunMethod("getPackageManager");
 //BA.debugLineNum = 835;BA.debugLine="Obj2.Target = Obj1.RunMethod2(\"getInstalledPackag";
mostCurrent._obj2.Target = mostCurrent._obj1.RunMethod2("getInstalledPackages",BA.NumberToString(0),"java.lang.int");
 //BA.debugLineNum = 836;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 841;BA.debugLine="For i = 0 To size -1";
{
final int step9 = 1;
final int limit9 = (int) (_size-1);
for (_i = (int) (0) ; (step9 > 0 && _i <= limit9) || (step9 < 0 && _i >= limit9); _i = ((int)(0 + _i + step9)) ) {
 //BA.debugLineNum = 842;BA.debugLine="Obj3.Target = Obj2.RunMethod2(\"get\", i, \"java.la";
mostCurrent._obj3.Target = mostCurrent._obj2.RunMethod2("get",BA.NumberToString(_i),"java.lang.int");
 //BA.debugLineNum = 843;BA.debugLine="size = Obj2.RunMethod(\"size\")";
_size = (int)(BA.ObjectToNumber(mostCurrent._obj2.RunMethod("size")));
 //BA.debugLineNum = 845;BA.debugLine="Obj3.Target = Obj3.GetField(\"applicationInfo\") '";
mostCurrent._obj3.Target = mostCurrent._obj3.GetField("applicationInfo");
 //BA.debugLineNum = 846;BA.debugLine="flags = Obj3.GetField(\"flags\")";
_flags = (int)(BA.ObjectToNumber(mostCurrent._obj3.GetField("flags")));
 //BA.debugLineNum = 847;BA.debugLine="packName = Obj3.GetField(\"packageName\")";
mostCurrent._packname = BA.ObjectToString(mostCurrent._obj3.GetField("packageName"));
 //BA.debugLineNum = 849;BA.debugLine="If Bit.And(flags, 1)  = 0 Then";
if (anywheresoftware.b4a.keywords.Common.Bit.And(_flags,(int) (1))==0) { 
 //BA.debugLineNum = 852;BA.debugLine="args(0) = Obj3.Target";
mostCurrent._args[(int) (0)] = mostCurrent._obj3.Target;
 //BA.debugLineNum = 853;BA.debugLine="Types(0) = \"android.content.pm.ApplicationInfo\"";
mostCurrent._types[(int) (0)] = "android.content.pm.ApplicationInfo";
 //BA.debugLineNum = 854;BA.debugLine="name = Obj1.RunMethod4(\"getApplicationLabel\", a";
mostCurrent._name = BA.ObjectToString(mostCurrent._obj1.RunMethod4("getApplicationLabel",mostCurrent._args,mostCurrent._types));
 //BA.debugLineNum = 855;BA.debugLine="icon = Obj1.RunMethod4(\"getApplicationIcon\", ar";
mostCurrent._icon.setObject((android.graphics.drawable.BitmapDrawable)(mostCurrent._obj1.RunMethod4("getApplicationIcon",mostCurrent._args,mostCurrent._types)));
 //BA.debugLineNum = 857;BA.debugLine="list3.Add(packName)";
mostCurrent._list3.Add((Object)(mostCurrent._packname));
 //BA.debugLineNum = 860;BA.debugLine="phlis.Add(icon.Bitmap)";
mostCurrent._phlis.Add((Object)(mostCurrent._icon.getBitmap()));
 //BA.debugLineNum = 863;BA.debugLine="Dim form As Int";
_form = 0;
 //BA.debugLineNum = 864;BA.debugLine="form=File.Size(GetParentPath(GetSourceDir(GetAc";
_form = (int) (anywheresoftware.b4a.keywords.Common.File.Size(_getparentpath(_getsourcedir(_getactivitiesinfo(mostCurrent._packname))),_getfilename(_getsourcedir(_getactivitiesinfo(mostCurrent._packname)))));
 //BA.debugLineNum = 865;BA.debugLine="datalist.Add(form)";
mostCurrent._datalist.Add((Object)(_form));
 //BA.debugLineNum = 866;BA.debugLine="lv2.AddTwoLinesAndBitmap2(name,packName&\" - \"&F";
mostCurrent._lv2.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence(mostCurrent._name),BA.ObjectToCharSequence(mostCurrent._packname+" - "+_formatfilesize((float) (_form))),mostCurrent._icon.getBitmap(),(Object)(mostCurrent._packname));
 };
 }
};
 //BA.debugLineNum = 874;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 680;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 681;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 682;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
 //BA.debugLineNum = 491;BA.debugLine="Sub button2_click";
 //BA.debugLineNum = 493;BA.debugLine="End Sub";
return "";
}
public static String  _button3_click() throws Exception{
 //BA.debugLineNum = 717;BA.debugLine="Sub Button3_Click";
 //BA.debugLineNum = 718;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 719;BA.debugLine="End Sub";
return "";
}
public static String  _button4_click() throws Exception{
 //BA.debugLineNum = 534;BA.debugLine="Sub Button4_Click";
 //BA.debugLineNum = 535;BA.debugLine="CallSubDelayed(cool,\"c_start\")";
anywheresoftware.b4a.keywords.Common.CallSubDelayed(mostCurrent.activityBA,(Object)(mostCurrent._cool.getObject()),"c_start");
 //BA.debugLineNum = 536;BA.debugLine="End Sub";
return "";
}
public static String  _bytestofile(String _dir,String _filename,byte[] _data) throws Exception{
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _out = null;
 //BA.debugLineNum = 876;BA.debugLine="Sub BytesToFile (Dir As String, FileName As String";
 //BA.debugLineNum = 877;BA.debugLine="Dim out As OutputStream = File.OpenOutput(Dir, Fi";
_out = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_out = anywheresoftware.b4a.keywords.Common.File.OpenOutput(_dir,_filename,anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 878;BA.debugLine="out.WriteBytes(Data, 0, Data.Length)";
_out.WriteBytes(_data,(int) (0),_data.length);
 //BA.debugLineNum = 879;BA.debugLine="out.Close";
_out.Close();
 //BA.debugLineNum = 880;BA.debugLine="End Sub";
return "";
}
public static long  _calcsize(String _folder,boolean _recursive) throws Exception{
long _size1 = 0L;
String _f = "";
 //BA.debugLineNum = 638;BA.debugLine="Sub CalcSize(Folder As String, recursive As Boolea";
 //BA.debugLineNum = 639;BA.debugLine="Dim size1 As Long";
_size1 = 0L;
 //BA.debugLineNum = 640;BA.debugLine="For Each f As String In File.ListFiles(Folder)";
final anywheresoftware.b4a.BA.IterableList group2 = anywheresoftware.b4a.keywords.Common.File.ListFiles(_folder);
final int groupLen2 = group2.getSize();
for (int index2 = 0;index2 < groupLen2 ;index2++){
_f = BA.ObjectToString(group2.Get(index2));
 //BA.debugLineNum = 641;BA.debugLine="If recursive Then";
if (_recursive) { 
 //BA.debugLineNum = 642;BA.debugLine="If File.IsDirectory(Folder, f) Then";
if (anywheresoftware.b4a.keywords.Common.File.IsDirectory(_folder,_f)) { 
 //BA.debugLineNum = 643;BA.debugLine="size1 = size1 + CalcSize(File.Combine(Folder,";
_size1 = (long) (_size1+_calcsize(anywheresoftware.b4a.keywords.Common.File.Combine(_folder,_f),_recursive));
 };
 };
 //BA.debugLineNum = 646;BA.debugLine="size1 = size1 + File.Size(Folder, f)";
_size1 = (long) (_size1+anywheresoftware.b4a.keywords.Common.File.Size(_folder,_f));
 }
;
 //BA.debugLineNum = 648;BA.debugLine="Return size1";
if (true) return _size1;
 //BA.debugLineNum = 649;BA.debugLine="End Sub";
return 0L;
}
public static boolean  _cl_click() throws Exception{
int _res = 0;
 //BA.debugLineNum = 684;BA.debugLine="Sub cl_click As Boolean";
 //BA.debugLineNum = 695;BA.debugLine="Dim res As Int";
_res = 0;
 //BA.debugLineNum = 696;BA.debugLine="res=Msgbox2(cs.Initialize.Alignment(\"ALIGN_CENTER";
_res = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence(_cs.Initialize().Alignment(BA.getEnumFromString(android.text.Layout.Alignment.class,"ALIGN_CENTER")).Append(BA.ObjectToCharSequence(("pp wird geschlossen Service Notification läuft im Hintergrund Prozess, zum deaktivieren bitte 'Settings->Start/stop Notify Service'.!\",\"Bat-CaT beenden:"))).PopAll().getObject()),BA.ObjectToCharSequence(_cs.Initialize().Typeface(anywheresoftware.b4a.keywords.Common.Typeface.getFONTAWESOME()).Color((int) (0xff01ff20)).Size((int) (40)).PopAll().getObject()),"ja","Abbruch","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Power.png").getObject()),mostCurrent.activityBA);
 //BA.debugLineNum = 698;BA.debugLine="If res=DialogResponse.POSITIVE Then";
if (_res==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 699;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 //BA.debugLineNum = 701;BA.debugLine="If res=DialogResponse.CANCEL Then";
if (_res==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL) { 
 //BA.debugLineNum = 702;BA.debugLine="ToastMessageShow(\"zurück..\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("zurück.."),anywheresoftware.b4a.keywords.Common.False);
 };
 };
 //BA.debugLineNum = 705;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 706;BA.debugLine="End Sub";
return false;
}
public static String  _cli_click() throws Exception{
 //BA.debugLineNum = 894;BA.debugLine="Sub cli_click";
 //BA.debugLineNum = 896;BA.debugLine="StartActivity(cool)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._cool.getObject()));
 //BA.debugLineNum = 897;BA.debugLine="End Sub";
return "";
}
public static String  _device_batterychanged(int _level,int _scale,boolean _plugged,anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
int _val = 0;
int _hours = 0;
int _minutes = 0;
int _rst = 0;
int _status = 0;
 //BA.debugLineNum = 722;BA.debugLine="Sub device_BatteryChanged (level As Int, Scale As";
 //BA.debugLineNum = 723;BA.debugLine="li2";
_li2();
 //BA.debugLineNum = 725;BA.debugLine="Dim val,hours,minutes,rst As Int";
_val = 0;
_hours = 0;
_minutes = 0;
_rst = 0;
 //BA.debugLineNum = 726;BA.debugLine="Dim status As Int = Intent.GetExtra(\"voltage\")";
_status = (int)(BA.ObjectToNumber(_intent.GetExtra("voltage")));
 //BA.debugLineNum = 728;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","lvl.txt",BA.NumberToString(_level));
 //BA.debugLineNum = 729;BA.debugLine="File.WriteString(File.DirDefaultExternal&\"/mnt/ca";
anywheresoftware.b4a.keywords.Common.File.WriteString(anywheresoftware.b4a.keywords.Common.File.getDirDefaultExternal()+"/mnt/cache","volt.txt",mostCurrent._volt);
 //BA.debugLineNum = 730;BA.debugLine="pgWheel1.Text=level&\"%\"";
mostCurrent._pgwheel1.setText(BA.NumberToString(_level)+"%");
 //BA.debugLineNum = 733;BA.debugLine="If Plugged=True Then";
if (_plugged==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 734;BA.debugLine="pgWheel1.computeScroll";
mostCurrent._pgwheel1.computeScroll();
 //BA.debugLineNum = 735;BA.debugLine="pgWheel1.Progress=360/100*level";
mostCurrent._pgwheel1.setProgress((int) (360/(double)100*_level));
 //BA.debugLineNum = 736;BA.debugLine="pgWheel1.spin";
mostCurrent._pgwheel1.spin();
 //BA.debugLineNum = 738;BA.debugLine="If level=100 Then";
if (_level==100) { 
 //BA.debugLineNum = 739;BA.debugLine="pgWheel1.stopSpinning";
mostCurrent._pgwheel1.stopSpinning();
 //BA.debugLineNum = 740;BA.debugLine="pgWheel1.Progress=360";
mostCurrent._pgwheel1.setProgress((int) (360));
 //BA.debugLineNum = 741;BA.debugLine="Label1.SetTextSizeAnimated(1000,20)";
mostCurrent._label1.SetTextSizeAnimated((int) (1000),(float) (20));
 };
 //BA.debugLineNum = 744;BA.debugLine="val =status'-Scale-level '/60";
_val = _status;
 //BA.debugLineNum = 745;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 746;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 747;BA.debugLine="Label4.Text=\"noch: \"&hours&\"h,\"&minutes&\"min\"";
mostCurrent._label4.setText(BA.ObjectToCharSequence("noch: "+BA.NumberToString(_hours)+"h,"+BA.NumberToString(_minutes)+"min"));
 }else {
 //BA.debugLineNum = 749;BA.debugLine="pgWheel1.Progress=360 / Scale * level";
mostCurrent._pgwheel1.setProgress((int) (360/(double)_scale*_level));
 //BA.debugLineNum = 750;BA.debugLine="If level<=5 Then";
if (_level<=5) { 
 //BA.debugLineNum = 751;BA.debugLine="Label1.TextColor=Colors.Red";
mostCurrent._label1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 752;BA.debugLine="Label1.TextSize=15";
mostCurrent._label1.setTextSize((float) (15));
 //BA.debugLineNum = 753;BA.debugLine="Label1.Text=\"Akku laden!\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("Akku laden!"));
 //BA.debugLineNum = 754;BA.debugLine="low_power";
_low_power();
 };
 //BA.debugLineNum = 758;BA.debugLine="val =level+status  / 60";
_val = (int) (_level+_status/(double)60);
 //BA.debugLineNum = 759;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 760;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 761;BA.debugLine="Label4.Text= \"noch: \"&hours&\"h,\"&minutes&\"min\"";
mostCurrent._label4.setText(BA.ObjectToCharSequence("noch: "+BA.NumberToString(_hours)+"h,"+BA.NumberToString(_minutes)+"min"));
 };
 //BA.debugLineNum = 764;BA.debugLine="End Sub";
return "";
}
public static String  _device_devicestorageok(anywheresoftware.b4a.objects.IntentWrapper _intent) throws Exception{
 //BA.debugLineNum = 271;BA.debugLine="Sub  device_DeviceStorageOk (Intent As Intent)";
 //BA.debugLineNum = 272;BA.debugLine="Log(Intent.ExtrasToString)";
anywheresoftware.b4a.keywords.Common.Log(_intent.ExtrasToString());
 //BA.debugLineNum = 273;BA.debugLine="End Sub";
return "";
}
public static String  _fc_copydone(String _key,boolean _error) throws Exception{
 //BA.debugLineNum = 886;BA.debugLine="Sub fc_CopyDone(Key As String, Error As Boolean)";
 //BA.debugLineNum = 888;BA.debugLine="End Sub";
return "";
}
public static byte[]  _filetobytes(String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 882;BA.debugLine="Sub FileToBytes (Dir As String, FileName As String";
 //BA.debugLineNum = 883;BA.debugLine="Return Bit.InputStreamToBytes(File.OpenInput(Dir,";
if (true) return anywheresoftware.b4a.keywords.Common.Bit.InputStreamToBytes((java.io.InputStream)(anywheresoftware.b4a.keywords.Common.File.OpenInput(_dir,_filename).getObject()));
 //BA.debugLineNum = 884;BA.debugLine="End Sub";
return null;
}
public static String  _formatfilesize(float _bytes) throws Exception{
String[] _unit = null;
double _po = 0;
double _si = 0;
int _i = 0;
 //BA.debugLineNum = 651;BA.debugLine="Sub FormatFileSize(Bytes As Float) As String";
 //BA.debugLineNum = 653;BA.debugLine="Private Unit() As String = Array As String(\" Byte";
_unit = new String[]{" Byte"," KB"," MB"," GB"," TB"," PB"," EB"," ZB"," YB"};
 //BA.debugLineNum = 655;BA.debugLine="If Bytes = 0 Then";
if (_bytes==0) { 
 //BA.debugLineNum = 657;BA.debugLine="Return \"0 Bytes\"";
if (true) return "0 Bytes";
 }else {
 //BA.debugLineNum = 661;BA.debugLine="Private Po, Si As Double";
_po = 0;
_si = 0;
 //BA.debugLineNum = 662;BA.debugLine="Private I As Int";
_i = 0;
 //BA.debugLineNum = 664;BA.debugLine="Bytes = Abs(Bytes)";
_bytes = (float) (anywheresoftware.b4a.keywords.Common.Abs(_bytes));
 //BA.debugLineNum = 666;BA.debugLine="I = Floor(Logarithm(Bytes, 1024))";
_i = (int) (anywheresoftware.b4a.keywords.Common.Floor(anywheresoftware.b4a.keywords.Common.Logarithm(_bytes,1024)));
 //BA.debugLineNum = 667;BA.debugLine="Po = Power(1024, I)";
_po = anywheresoftware.b4a.keywords.Common.Power(1024,_i);
 //BA.debugLineNum = 668;BA.debugLine="Si = Bytes / Po";
_si = _bytes/(double)_po;
 //BA.debugLineNum = 670;BA.debugLine="Return NumberFormat(Si, 1, 2) & Unit(I)";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_si,(int) (1),(int) (2))+_unit[_i];
 };
 //BA.debugLineNum = 674;BA.debugLine="End Sub";
return "";
}
public static Object  _getactivitiesinfo(String _package) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 921;BA.debugLine="Sub GetActivitiesInfo(package As String) As Object";
 //BA.debugLineNum = 922;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 923;BA.debugLine="r.Target = r.GetContext";
_r.Target = (Object)(_r.GetContext(processBA));
 //BA.debugLineNum = 924;BA.debugLine="r.Target = r.RunMethod(\"getPackageManager\")";
_r.Target = _r.RunMethod("getPackageManager");
 //BA.debugLineNum = 925;BA.debugLine="r.Target = r.RunMethod3(\"getPackageInfo\", package";
_r.Target = _r.RunMethod3("getPackageInfo",_package,"java.lang.String",BA.NumberToString(0x00000001),"java.lang.int");
 //BA.debugLineNum = 926;BA.debugLine="Return r.GetField(\"applicationInfo\")";
if (true) return _r.GetField("applicationInfo");
 //BA.debugLineNum = 927;BA.debugLine="End Sub";
return null;
}
public static String  _getfilename(String _fullpath) throws Exception{
 //BA.debugLineNum = 890;BA.debugLine="Sub GetFileName(FullPath As String) As String";
 //BA.debugLineNum = 891;BA.debugLine="Return FullPath.SubString(FullPath.LastIndexOf(\"/";
if (true) return _fullpath.substring((int) (_fullpath.lastIndexOf("/")+1));
 //BA.debugLineNum = 892;BA.debugLine="End Sub";
return "";
}
public static String  _getparentpath(String _path) throws Exception{
String _path1 = "";
 //BA.debugLineNum = 902;BA.debugLine="Sub GetParentPath(Path As String) As String";
 //BA.debugLineNum = 903;BA.debugLine="Dim Path1 As String";
_path1 = "";
 //BA.debugLineNum = 904;BA.debugLine="If Path = \"/\" Then";
if ((_path).equals("/")) { 
 //BA.debugLineNum = 905;BA.debugLine="Return \"/\"";
if (true) return "/";
 };
 //BA.debugLineNum = 907;BA.debugLine="L = Path.LastIndexOf(\"/\")";
mostCurrent._l = BA.NumberToString(_path.lastIndexOf("/"));
 //BA.debugLineNum = 908;BA.debugLine="If L = Path.Length - 1 Then";
if ((mostCurrent._l).equals(BA.NumberToString(_path.length()-1))) { 
 //BA.debugLineNum = 910;BA.debugLine="Path1 = Path.SubString2(0,L)";
_path1 = _path.substring((int) (0),(int)(Double.parseDouble(mostCurrent._l)));
 }else {
 //BA.debugLineNum = 912;BA.debugLine="Path1 = Path";
_path1 = _path;
 };
 //BA.debugLineNum = 914;BA.debugLine="L = Path.LastIndexOf(\"/\")";
mostCurrent._l = BA.NumberToString(_path.lastIndexOf("/"));
 //BA.debugLineNum = 915;BA.debugLine="If L = 0 Then";
if ((mostCurrent._l).equals(BA.NumberToString(0))) { 
 //BA.debugLineNum = 916;BA.debugLine="L = 1";
mostCurrent._l = BA.NumberToString(1);
 };
 //BA.debugLineNum = 918;BA.debugLine="Return Path1.SubString2(0,L)";
if (true) return _path1.substring((int) (0),(int)(Double.parseDouble(mostCurrent._l)));
 //BA.debugLineNum = 919;BA.debugLine="End Sub";
return "";
}
public static String  _getsourcedir(Object _appinfo) throws Exception{
anywheresoftware.b4a.agraham.reflection.Reflection _r = null;
 //BA.debugLineNum = 929;BA.debugLine="Sub GetSourceDir(AppInfo As Object) As String";
 //BA.debugLineNum = 930;BA.debugLine="Try";
try { //BA.debugLineNum = 931;BA.debugLine="Dim r As Reflector";
_r = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 932;BA.debugLine="r.Target = AppInfo";
_r.Target = _appinfo;
 //BA.debugLineNum = 933;BA.debugLine="Return r.GetField(\"sourceDir\")";
if (true) return BA.ObjectToString(_r.GetField("sourceDir"));
 } 
       catch (Exception e6) {
			processBA.setLastException(e6); //BA.debugLineNum = 935;BA.debugLine="Return \"\"";
if (true) return "";
 };
 //BA.debugLineNum = 937;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 24;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private ProgressBar1 As ProgressBar";
mostCurrent._progressbar1 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private ra As OperatingSystem";
mostCurrent._ra = new com.rootsoft.oslibrary.OSLibrary();
 //BA.debugLineNum = 27;BA.debugLine="Dim device As PhoneEvents";
mostCurrent._device = new anywheresoftware.b4a.phone.PhoneEvents();
 //BA.debugLineNum = 29;BA.debugLine="Dim ct As CustomToast";
mostCurrent._ct = new com.rootsoft.customtoast.CustomToast();
 //BA.debugLineNum = 30;BA.debugLine="Private pgWheel1 As pgWheel";
mostCurrent._pgwheel1 = new de.donmanfred.pgWheel();
 //BA.debugLineNum = 31;BA.debugLine="Private Label1,Label2,Label3,Label4,lk As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label3 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._label4 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lk = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private ListView1,lv2,lvMenu As ListView";
mostCurrent._listview1 = new anywheresoftware.b4a.objects.ListViewWrapper();
mostCurrent._lv2 = new anywheresoftware.b4a.objects.ListViewWrapper();
mostCurrent._lvmenu = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim args(1) As Object";
mostCurrent._args = new Object[(int) (1)];
{
int d0 = mostCurrent._args.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._args[i0] = new Object();
}
}
;
 //BA.debugLineNum = 34;BA.debugLine="Dim Obj1, Obj2, Obj3 As Reflector";
mostCurrent._obj1 = new anywheresoftware.b4a.agraham.reflection.Reflection();
mostCurrent._obj2 = new anywheresoftware.b4a.agraham.reflection.Reflection();
mostCurrent._obj3 = new anywheresoftware.b4a.agraham.reflection.Reflection();
 //BA.debugLineNum = 35;BA.debugLine="Dim la,la1,la2 As Label";
mostCurrent._la = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._la1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._la2 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Dim ima As ImageView";
mostCurrent._ima = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Dim icon As BitmapDrawable";
mostCurrent._icon = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 38;BA.debugLine="Dim logo As Bitmap";
mostCurrent._logo = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Dim bat As Batut";
mostCurrent._bat = new com.batcat.batut();
 //BA.debugLineNum = 40;BA.debugLine="Private CheckBox1 As CheckBox";
mostCurrent._checkbox1 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private CheckBox2 As CheckBox";
mostCurrent._checkbox2 = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Dim volt,temp,level1 As String";
mostCurrent._volt = "";
mostCurrent._temp = "";
mostCurrent._level1 = "";
 //BA.debugLineNum = 43;BA.debugLine="Private Panel2,panel3,lip,setp,smlp As Panel";
mostCurrent._panel2 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._panel3 = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._lip = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._setp = new anywheresoftware.b4a.objects.PanelWrapper();
mostCurrent._smlp = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Dim kvs,kvs2,kvs3,kvs4,kvs4sub As KeyValueStore";
mostCurrent._kvs = new com.batcat.keyvaluestore();
mostCurrent._kvs2 = new com.batcat.keyvaluestore();
mostCurrent._kvs3 = new com.batcat.keyvaluestore();
mostCurrent._kvs4 = new com.batcat.keyvaluestore();
mostCurrent._kvs4sub = new com.batcat.keyvaluestore();
 //BA.debugLineNum = 46;BA.debugLine="Dim optmap As Map";
mostCurrent._optmap = new anywheresoftware.b4a.objects.collections.Map();
 //BA.debugLineNum = 47;BA.debugLine="Private Label5 As Label";
mostCurrent._label5 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 48;BA.debugLine="Private Label6 As Label";
mostCurrent._label6 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 50;BA.debugLine="Dim proc As List";
mostCurrent._proc = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 51;BA.debugLine="Private Panel1 As Panel";
mostCurrent._panel1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 52;BA.debugLine="Private showtip As MSShowTipsBuilder";
mostCurrent._showtip = new com.maximussoft.showtips.BuilderWrapper();
 //BA.debugLineNum = 53;BA.debugLine="Private slm As ClsSlidingSideBar";
mostCurrent._slm = new com.batcat.clsslidingsidebar();
 //BA.debugLineNum = 54;BA.debugLine="Dim cas As Int";
_cas = 0;
 //BA.debugLineNum = 55;BA.debugLine="Dim size,flags As Int";
_size = 0;
_flags = 0;
 //BA.debugLineNum = 56;BA.debugLine="Dim Types(1), name,packName,date,time,l,ramsize A";
mostCurrent._types = new String[(int) (1)];
java.util.Arrays.fill(mostCurrent._types,"");
mostCurrent._name = "";
mostCurrent._packname = "";
mostCurrent._date = "";
mostCurrent._time = "";
mostCurrent._l = "";
mostCurrent._ramsize = "";
 //BA.debugLineNum = 57;BA.debugLine="Dim list,list1,list2,list3,list4,list5,list6,phli";
mostCurrent._list = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list1 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list2 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list3 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list4 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list5 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._list6 = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._phlis = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._lis = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._setlist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._datalist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 59;BA.debugLine="Dim cd2,cd As CustomDialog2";
mostCurrent._cd2 = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
mostCurrent._cd = new anywheresoftware.b4a.agraham.dialogs.InputDialog.CustomDialog2();
 //BA.debugLineNum = 61;BA.debugLine="Dim FakeActionBar  As Panel";
mostCurrent._fakeactionbar = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 62;BA.debugLine="Dim sd,sdi As Bitmap";
mostCurrent._sd = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
mostCurrent._sdi = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 63;BA.debugLine="Private ACToolBarLight1 As ACToolBarLight";
mostCurrent._actoolbarlight1 = new de.amberhome.objects.appcompat.ACToolbarLightWrapper();
 //BA.debugLineNum = 64;BA.debugLine="Private ToolbarHelper As ACActionBar";
mostCurrent._toolbarhelper = new de.amberhome.objects.appcompat.ACActionBar();
 //BA.debugLineNum = 66;BA.debugLine="Private Button4 As ACButton";
mostCurrent._button4 = new de.amberhome.objects.appcompat.ACButtonWrapper();
 //BA.debugLineNum = 68;BA.debugLine="Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c1";
_c1 = 0;
_c2 = 0;
_c3 = 0;
_c4 = 0;
_c5 = 0;
_c6 = 0;
_c7 = 0;
_c8 = 0;
_c9 = 0;
_c10 = 0;
_c11 = 0;
_c12 = 0;
_c13 = 0;
_c14 = 0;
_c15 = 0;
 //BA.debugLineNum = 69;BA.debugLine="Dim mcl As MaterialColors";
mostCurrent._mcl = new com.tchart.materialcolors.MaterialColors();
 //BA.debugLineNum = 70;BA.debugLine="Dim Mclist,ramlist As List";
mostCurrent._mclist = new anywheresoftware.b4a.objects.collections.List();
mostCurrent._ramlist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _inputlist_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 898;BA.debugLine="Sub InputList_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 900;BA.debugLine="End Sub";
return "";
}
public static String  _lab_set2() throws Exception{
anywheresoftware.b4a.objects.LabelWrapper _la3 = null;
 //BA.debugLineNum = 496;BA.debugLine="Sub lab_set2";
 //BA.debugLineNum = 497;BA.debugLine="Dim la3 As Label";
_la3 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 498;BA.debugLine="ListView1.Invalidate";
mostCurrent._listview1.Invalidate();
 //BA.debugLineNum = 499;BA.debugLine="la=ListView1.TwoLinesAndBitmap.Label";
mostCurrent._la = mostCurrent._listview1.getTwoLinesAndBitmap().Label;
 //BA.debugLineNum = 500;BA.debugLine="la2=ListView1.TwoLinesAndBitmap.SecondLabel";
mostCurrent._la2 = mostCurrent._listview1.getTwoLinesAndBitmap().SecondLabel;
 //BA.debugLineNum = 501;BA.debugLine="la1=ListView1.TwoLinesLayout.Label";
mostCurrent._la1 = mostCurrent._listview1.getTwoLinesLayout().Label;
 //BA.debugLineNum = 502;BA.debugLine="la3=ListView1.TwoLinesLayout.SecondLabel";
_la3 = mostCurrent._listview1.getTwoLinesLayout().SecondLabel;
 //BA.debugLineNum = 503;BA.debugLine="la.TextSize=13";
mostCurrent._la.setTextSize((float) (13));
 //BA.debugLineNum = 504;BA.debugLine="la.TextColor=Colors.Black";
mostCurrent._la.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 505;BA.debugLine="la1.TextSize=13";
mostCurrent._la1.setTextSize((float) (13));
 //BA.debugLineNum = 506;BA.debugLine="la1.TextColor=Colors.Black";
mostCurrent._la1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 507;BA.debugLine="la2.TextSize=11";
mostCurrent._la2.setTextSize((float) (11));
 //BA.debugLineNum = 508;BA.debugLine="la2.TextColor=Colors.ARGB(150,255,255,255)";
mostCurrent._la2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (150),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 509;BA.debugLine="la3.TextColor=Colors.ARGB(100,255,255,255)";
_la3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (100),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 510;BA.debugLine="la3.TextSize=10";
_la3.setTextSize((float) (10));
 //BA.debugLineNum = 511;BA.debugLine="la1.TextSize=12";
mostCurrent._la1.setTextSize((float) (12));
 //BA.debugLineNum = 512;BA.debugLine="la1.TextColor=Colors.ARGB(180,255,255,255)";
mostCurrent._la1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (180),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 513;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.Height=48di";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (48)));
 //BA.debugLineNum = 514;BA.debugLine="ListView1.TwoLinesAndBitmap.ImageView.Width=48dip";
mostCurrent._listview1.getTwoLinesAndBitmap().ImageView.setWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (48)));
 //BA.debugLineNum = 515;BA.debugLine="ListView1.TwoLinesAndBitmap.ItemHeight=60dip";
mostCurrent._listview1.getTwoLinesAndBitmap().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 516;BA.debugLine="ListView1.SingleLineLayout.ItemHeight=60dip";
mostCurrent._listview1.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60)));
 //BA.debugLineNum = 517;BA.debugLine="ListView1.TwoLinesLayout.ItemHeight=50dip";
mostCurrent._listview1.getTwoLinesLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 518;BA.debugLine="End Sub";
return "";
}
public static String  _label_text() throws Exception{
 //BA.debugLineNum = 550;BA.debugLine="Sub label_text";
 //BA.debugLineNum = 551;BA.debugLine="lk.TextSize=13";
mostCurrent._lk.setTextSize((float) (13));
 //BA.debugLineNum = 552;BA.debugLine="lk.TextColor=Colors.Black";
mostCurrent._lk.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 553;BA.debugLine="lk.Text=\"Version: \"&pak.GetVersionName(\"com.batca";
mostCurrent._lk.setText(BA.ObjectToCharSequence("Version: "+_pak.GetVersionName("com.batcat")+", Integer: "+BA.NumberToString(_pak.GetVersionCode("com.batcat"))+". Coded in 'Basic' and 'Sun Java OpenSource' by D. Trojan, published by SuloMedia™. All Rights Reserved ©2017 "+_pak.GetApplicationLabel("com.batcat")));
 //BA.debugLineNum = 554;BA.debugLine="End Sub";
return "";
}
public static String  _label1_click() throws Exception{
 //BA.debugLineNum = 676;BA.debugLine="Sub Label1_Click";
 //BA.debugLineNum = 677;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 678;BA.debugLine="End Sub";
return "";
}
public static String  _label4_click() throws Exception{
 //BA.debugLineNum = 775;BA.debugLine="Sub Label4_Click";
 //BA.debugLineNum = 776;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 777;BA.debugLine="End Sub";
return "";
}
public static boolean  _li2() throws Exception{
int _math1 = 0;
int _ti = 0;
int _h = 0;
int _f = 0;
 //BA.debugLineNum = 610;BA.debugLine="Sub li2 As Boolean";
 //BA.debugLineNum = 611;BA.debugLine="list2.Clear";
mostCurrent._list2.Clear();
 //BA.debugLineNum = 612;BA.debugLine="list4.Clear";
mostCurrent._list4.Clear();
 //BA.debugLineNum = 613;BA.debugLine="list5.Clear";
mostCurrent._list5.Clear();
 //BA.debugLineNum = 614;BA.debugLine="list6.Clear";
mostCurrent._list6.Clear();
 //BA.debugLineNum = 615;BA.debugLine="proc.Clear";
mostCurrent._proc.Clear();
 //BA.debugLineNum = 616;BA.debugLine="ramlist.Clear";
mostCurrent._ramlist.Clear();
 //BA.debugLineNum = 617;BA.debugLine="li3";
_li3();
 //BA.debugLineNum = 618;BA.debugLine="proc=ra.getRecentTasks(99,0)";
mostCurrent._proc.setObject((java.util.List)(mostCurrent._ra.getRecentTasks((int) (99),(int) (0))));
 //BA.debugLineNum = 619;BA.debugLine="list2=ra.RunningServiceInfo(999,list4,list5,list6";
mostCurrent._list2.setObject((java.util.List)(mostCurrent._ra.RunningServiceInfo((int) (999),(java.util.List)(mostCurrent._list4.getObject()),(java.util.List)(mostCurrent._list5.getObject()),(java.util.List)(mostCurrent._list6.getObject()))));
 //BA.debugLineNum = 622;BA.debugLine="Dim math1 As Int";
_math1 = 0;
 //BA.debugLineNum = 623;BA.debugLine="math1=proc.Size+list4.Size*1000*1024*10";
_math1 = (int) (mostCurrent._proc.getSize()+mostCurrent._list4.getSize()*1000*1024*10);
 //BA.debugLineNum = 624;BA.debugLine="Dim ti As Int=proc.Size+list4.Size";
_ti = (int) (mostCurrent._proc.getSize()+mostCurrent._list4.getSize());
 //BA.debugLineNum = 626;BA.debugLine="For h = 0 To list4.size-1";
{
final int step13 = 1;
final int limit13 = (int) (mostCurrent._list4.getSize()-1);
for (_h = (int) (0) ; (step13 > 0 && _h <= limit13) || (step13 < 0 && _h >= limit13); _h = ((int)(0 + _h + step13)) ) {
 //BA.debugLineNum = 627;BA.debugLine="ramlist.Add(FormatFileSize(math1))";
mostCurrent._ramlist.Add((Object)(_formatfilesize((float) (_math1))));
 //BA.debugLineNum = 628;BA.debugLine="ProgressBar1.Progress=h";
mostCurrent._progressbar1.setProgress(_h);
 }
};
 //BA.debugLineNum = 630;BA.debugLine="For f = 0 To ramlist.Size-1";
{
final int step17 = 1;
final int limit17 = (int) (mostCurrent._ramlist.getSize()-1);
for (_f = (int) (0) ; (step17 > 0 && _f <= limit17) || (step17 < 0 && _f >= limit17); _f = ((int)(0 + _f + step17)) ) {
 //BA.debugLineNum = 632;BA.debugLine="Label5.Text=ti&\"% \"&ramlist.Get(f)";
mostCurrent._label5.setText(BA.ObjectToCharSequence(BA.NumberToString(_ti)+"% "+BA.ObjectToString(mostCurrent._ramlist.Get(_f))));
 }
};
 //BA.debugLineNum = 635;BA.debugLine="Return(True)";
if (true) return (anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 636;BA.debugLine="End Sub";
return false;
}
public static String  _li3() throws Exception{
int[] _batteryinfo = null;
int _v = 0;
anywheresoftware.b4a.objects.collections.List _piclist = null;
int _si = 0;
int _st = 0;
int _lz = 0;
int _az = 0;
int _g = 0;
 //BA.debugLineNum = 564;BA.debugLine="Sub li3";
 //BA.debugLineNum = 565;BA.debugLine="app_info";
_app_info();
 //BA.debugLineNum = 566;BA.debugLine="Dim batteryInfo(11) As Int";
_batteryinfo = new int[(int) (11)];
;
 //BA.debugLineNum = 567;BA.debugLine="batteryInfo = bat.BatteryInformation";
_batteryinfo = mostCurrent._bat._getbatteryinformation();
 //BA.debugLineNum = 568;BA.debugLine="For v = 0 To batteryInfo.Length - 1";
{
final int step4 = 1;
final int limit4 = (int) (_batteryinfo.length-1);
for (_v = (int) (0) ; (step4 > 0 && _v <= limit4) || (step4 < 0 && _v >= limit4); _v = ((int)(0 + _v + step4)) ) {
 //BA.debugLineNum = 569;BA.debugLine="Log(batteryInfo(v))";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_batteryinfo[_v]));
 }
};
 //BA.debugLineNum = 571;BA.debugLine="volt=bat.BatteryInformation(7)/1000";
mostCurrent._volt = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (7)]/(double)1000);
 //BA.debugLineNum = 572;BA.debugLine="temp=bat.BatteryInformation(6)/10";
mostCurrent._temp = BA.NumberToString(mostCurrent._bat._getbatteryinformation()[(int) (6)]/(double)10);
 //BA.debugLineNum = 573;BA.debugLine="Dim piclist As List";
_piclist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 574;BA.debugLine="piclist.Initialize";
_piclist.Initialize();
 //BA.debugLineNum = 575;BA.debugLine="sd=LoadBitmap(File.DirAssets,\"ic_sim_card_black_2";
mostCurrent._sd = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_sim_card_black_24dp.png");
 //BA.debugLineNum = 576;BA.debugLine="sdi=LoadBitmap(File.DirAssets,\"ic_sim_card_white_";
mostCurrent._sdi = anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"ic_sim_card_white_24dp.png");
 //BA.debugLineNum = 577;BA.debugLine="Dim si,st As Int";
_si = 0;
_st = 0;
 //BA.debugLineNum = 578;BA.debugLine="st=ra.TotalExternalMemorySize+ra.TotalInternalMem";
_st = (int) (mostCurrent._ra.getTotalExternalMemorySize()+mostCurrent._ra.getTotalInternalMemorySize());
 //BA.debugLineNum = 580;BA.debugLine="si=ra.AvailableExternalMemorySize+ra.AvailableInt";
_si = (int) (mostCurrent._ra.getAvailableExternalMemorySize()+mostCurrent._ra.getAvailableInternalMemorySize());
 //BA.debugLineNum = 582;BA.debugLine="ListView1.Clear";
mostCurrent._listview1.Clear();
 //BA.debugLineNum = 583;BA.debugLine="If ra.externalMemoryAvailable Then";
if (mostCurrent._ra.externalMemoryAvailable()) { 
 //BA.debugLineNum = 585;BA.debugLine="Dim lz,az As Int";
_lz = 0;
_az = 0;
 //BA.debugLineNum = 587;BA.debugLine="For Each g As Int  In datalist";
final anywheresoftware.b4a.BA.IterableList group19 = mostCurrent._datalist;
final int groupLen19 = group19.getSize();
for (int index19 = 0;index19 < groupLen19 ;index19++){
_g = (int)(BA.ObjectToNumber(group19.Get(index19)));
 //BA.debugLineNum = 589;BA.debugLine="lz=g";
_lz = _g;
 //BA.debugLineNum = 590;BA.debugLine="az=lz*1024*1024*10";
_az = (int) (_lz*1024*1024*10);
 }
;
 //BA.debugLineNum = 592;BA.debugLine="Log(\"SUB-> \"&FormatFileSize(az))";
anywheresoftware.b4a.keywords.Common.Log("SUB-> "+_formatfilesize((float) (_az)));
 //BA.debugLineNum = 593;BA.debugLine="ListView1.AddTwoLinesAndbitmap2(\"SD Memory(Total";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("SD Memory(Total): "+_formatfilesize((float) (mostCurrent._ra.getTotalInternalMemorySize()))),BA.ObjectToCharSequence(BA.NumberToString(mostCurrent._list3.getSize())+" apps installiert("+_formatfilesize((float) (_az))+") auf: "+_getparentpath(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/Android")),(android.graphics.Bitmap)(mostCurrent._sd.getObject()),(Object)(2));
 //BA.debugLineNum = 595;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(\"SD Used: \"&Form";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("SD Used: "+_formatfilesize((float) (_si-_st))),BA.ObjectToCharSequence(_getparentpath(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/Android")),(android.graphics.Bitmap)(mostCurrent._sd.getObject()),(Object)(3));
 //BA.debugLineNum = 597;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(\"SD Free: \"&Form";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("SD Free: "+_formatfilesize((float) (mostCurrent._ra.getAvailableExternalMemorySize()+mostCurrent._ra.getAvailableInternalMemorySize()))),BA.ObjectToCharSequence(_getparentpath(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/Android")),(android.graphics.Bitmap)(mostCurrent._sd.getObject()),(Object)(4));
 //BA.debugLineNum = 598;BA.debugLine="ListView1.AddTwoLinesAndbitmap2(\"SD (Intern): \"&";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("SD (Intern): "+_formatfilesize((float) (mostCurrent._ra.getTotalInternalMemorySize()))),BA.ObjectToCharSequence(_getparentpath(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/Android")),(android.graphics.Bitmap)(mostCurrent._sdi.getObject()),(Object)(5));
 }else {
 //BA.debugLineNum = 600;BA.debugLine="ListView1.AddTwoLinesAndbitmap2(\"SD Memory(Total";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("SD Memory(Total):"),BA.ObjectToCharSequence(_formatfilesize((float) (mostCurrent._ra.getTotalInternalMemorySize()+mostCurrent._ra.getTotalExternalMemorySize()))),(android.graphics.Bitmap)(mostCurrent._sd.getObject()),(Object)(2));
 //BA.debugLineNum = 601;BA.debugLine="ListView1.AddTwoLinesAndbitmap2(\"SD(Inten):\",For";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("SD(Inten):"),BA.ObjectToCharSequence(_formatfilesize((float) (mostCurrent._ra.getTotalInternalMemorySize()))),(android.graphics.Bitmap)(mostCurrent._sdi.getObject()),(Object)(5));
 //BA.debugLineNum = 602;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(\"SD total Free:\"";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("SD total Free:"),BA.ObjectToCharSequence(_formatfilesize((float) (mostCurrent._ra.getAvailableExternalMemorySize()+mostCurrent._ra.getAvailableInternalMemorySize()))),(android.graphics.Bitmap)(mostCurrent._sd.getObject()),(Object)(4));
 //BA.debugLineNum = 603;BA.debugLine="ListView1.AddTwoLinesAndBitmap2(\"SD total Used:\"";
mostCurrent._listview1.AddTwoLinesAndBitmap2(BA.ObjectToCharSequence("SD total Used:"),BA.ObjectToCharSequence(_formatfilesize((float) (_si-_st))),(android.graphics.Bitmap)(mostCurrent._sd.getObject()),(Object)(3));
 };
 //BA.debugLineNum = 606;BA.debugLine="End Sub";
return "";
}
public static String  _listview1_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 779;BA.debugLine="Sub listview1_ItemClick (Position As Int, value As";
 //BA.debugLineNum = 780;BA.debugLine="If Position=0 Then";
if (_position==0) { 
 //BA.debugLineNum = 781;BA.debugLine="rl_lo";
_rl_lo();
 };
 //BA.debugLineNum = 783;BA.debugLine="End Sub";
return "";
}
public static String  _loading_norm() throws Exception{
 //BA.debugLineNum = 785;BA.debugLine="Sub loading_norm";
 //BA.debugLineNum = 786;BA.debugLine="pgWheel1.CircleRadius=100";
mostCurrent._pgwheel1.setCircleRadius((int) (100));
 //BA.debugLineNum = 787;BA.debugLine="pgWheel1.FocusableInTouchMode=True";
mostCurrent._pgwheel1.setFocusableInTouchMode(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 791;BA.debugLine="pgWheel1.CircleColor= Colors.ARGB(0,255,255,255)";
mostCurrent._pgwheel1.setCircleColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 792;BA.debugLine="pgWheel1.ContourColor=Colors.ARGB(0,255,255,255)";
mostCurrent._pgwheel1.setContourColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 793;BA.debugLine="pgWheel1.BarColor= mcl.md_white_1000";
mostCurrent._pgwheel1.setBarColor(mostCurrent._mcl.getmd_white_1000());
 //BA.debugLineNum = 794;BA.debugLine="pgWheel1.ContourSize=1dip";
mostCurrent._pgwheel1.setContourSize(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 795;BA.debugLine="pgWheel1.FadingEdgeLength=2dip";
mostCurrent._pgwheel1.setFadingEdgeLength(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 796;BA.debugLine="pgWheel1.TextSize=45";
mostCurrent._pgwheel1.setTextSize((int) (45));
 //BA.debugLineNum = 797;BA.debugLine="pgWheel1.TextColor=Colors.White";
mostCurrent._pgwheel1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 798;BA.debugLine="pgWheel1.SpinSpeed=5";
mostCurrent._pgwheel1.setSpinSpeed((int) (5));
 //BA.debugLineNum = 799;BA.debugLine="pgWheel1.RimColor= Colors.Transparent'ARGB(255,25";
mostCurrent._pgwheel1.setRimColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 800;BA.debugLine="pgWheel1.RimWidth=20dip";
mostCurrent._pgwheel1.setRimWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 801;BA.debugLine="pgWheel1.BarWidth=15dip";
mostCurrent._pgwheel1.setBarWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (15)));
 //BA.debugLineNum = 802;BA.debugLine="pgWheel1.DelayMillis=1";
mostCurrent._pgwheel1.setDelayMillis((int) (1));
 //BA.debugLineNum = 803;BA.debugLine="pgWheel1.Clickable=False";
mostCurrent._pgwheel1.setClickable(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 805;BA.debugLine="End Sub";
return "";
}
public static String  _low_power() throws Exception{
 //BA.debugLineNum = 808;BA.debugLine="Sub low_power";
 //BA.debugLineNum = 809;BA.debugLine="pgWheel1.TextSize=45";
mostCurrent._pgwheel1.setTextSize((int) (45));
 //BA.debugLineNum = 810;BA.debugLine="pgWheel1.BarColor=Colors.Red";
mostCurrent._pgwheel1.setBarColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 811;BA.debugLine="pgWheel1.CircleColor= Colors.ARGB(40,255,255,255)";
mostCurrent._pgwheel1.setCircleColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (40),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 812;BA.debugLine="pgWheel1.ContourColor=Colors.red";
mostCurrent._pgwheel1.setContourColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 813;BA.debugLine="pgWheel1.ContourSize=1dip";
mostCurrent._pgwheel1.setContourSize(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (1)));
 //BA.debugLineNum = 814;BA.debugLine="pgWheel1.FadingEdgeLength=2dip";
mostCurrent._pgwheel1.setFadingEdgeLength(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)));
 //BA.debugLineNum = 815;BA.debugLine="pgWheel1.TextColor=Colors.Black";
mostCurrent._pgwheel1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 816;BA.debugLine="pgWheel1.SpinSpeed=2";
mostCurrent._pgwheel1.setSpinSpeed((int) (2));
 //BA.debugLineNum = 817;BA.debugLine="pgWheel1.RimColor= Colors.Transparent'ARGB(255,25";
mostCurrent._pgwheel1.setRimColor(anywheresoftware.b4a.keywords.Common.Colors.Transparent);
 //BA.debugLineNum = 818;BA.debugLine="pgWheel1.RimWidth=25dip";
mostCurrent._pgwheel1.setRimWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (25)));
 //BA.debugLineNum = 819;BA.debugLine="pgWheel1.BarWidth=20dip";
mostCurrent._pgwheel1.setBarWidth(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)));
 //BA.debugLineNum = 820;BA.debugLine="pgWheel1.DelayMillis=1";
mostCurrent._pgwheel1.setDelayMillis((int) (1));
 //BA.debugLineNum = 821;BA.debugLine="pgWheel1.Clickable=False";
mostCurrent._pgwheel1.setClickable(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 822;BA.debugLine="pgWheel1.Focusable=False";
mostCurrent._pgwheel1.setFocusable(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 824;BA.debugLine="End Sub";
return "";
}
public static String  _lvmenu_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 461;BA.debugLine="Sub lvMenu_ItemClick (Position As Int, Value As Ob";
 //BA.debugLineNum = 462;BA.debugLine="If Value = 0 Then";
if ((_value).equals((Object)(0))) { 
 //BA.debugLineNum = 463;BA.debugLine="opt_click";
_opt_click();
 //BA.debugLineNum = 464;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 };
 //BA.debugLineNum = 466;BA.debugLine="If Value=1 Then";
if ((_value).equals((Object)(1))) { 
 //BA.debugLineNum = 467;BA.debugLine="cl_click";
_cl_click();
 //BA.debugLineNum = 468;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 };
 //BA.debugLineNum = 470;BA.debugLine="If Value=2 Then";
if ((_value).equals((Object)(2))) { 
 //BA.debugLineNum = 471;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 472;BA.debugLine="i.Initialize( \"android.intent.action.POWER_USAGE";
_i.Initialize("android.intent.action.POWER_USAGE_SUMMARY","");
 //BA.debugLineNum = 473;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 //BA.debugLineNum = 474;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 476;BA.debugLine="If Value=3 Then";
if ((_value).equals((Object)(3))) { 
 //BA.debugLineNum = 477;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 478;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 };
 //BA.debugLineNum = 480;BA.debugLine="If Value=4 Then";
if ((_value).equals((Object)(4))) { 
 //BA.debugLineNum = 481;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 482;BA.debugLine="cl_click";
_cl_click();
 //BA.debugLineNum = 483;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 };
 //BA.debugLineNum = 485;BA.debugLine="If Value=5 Then";
if ((_value).equals((Object)(5))) { 
 //BA.debugLineNum = 486;BA.debugLine="pci_click";
_pci_click();
 //BA.debugLineNum = 487;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 };
 //BA.debugLineNum = 489;BA.debugLine="End Sub";
return "";
}
public static String  _menu_onfullyclosed() throws Exception{
 //BA.debugLineNum = 305;BA.debugLine="Sub Menu_onFullyClosed";
 //BA.debugLineNum = 306;BA.debugLine="smlp.SendToBack";
mostCurrent._smlp.SendToBack();
 //BA.debugLineNum = 307;BA.debugLine="lvMenu.SendToBack";
mostCurrent._lvmenu.SendToBack();
 //BA.debugLineNum = 308;BA.debugLine="Button4.BringToFront";
mostCurrent._button4.BringToFront();
 //BA.debugLineNum = 309;BA.debugLine="Button4.ButtonColor=mcl.md_light_green_A400";
mostCurrent._button4.setButtonColor(mostCurrent._mcl.getmd_light_green_A400());
 //BA.debugLineNum = 310;BA.debugLine="Button4.BringToFront";
mostCurrent._button4.BringToFront();
 //BA.debugLineNum = 311;BA.debugLine="pgWheel1.Focusable=True";
mostCurrent._pgwheel1.setFocusable(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 312;BA.debugLine="Button4.SetLayoutAnimated(200,20%x,55%y,60%x,105";
mostCurrent._button4.SetLayoutAnimated((int) (200),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (55),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),(int) (105));
 //BA.debugLineNum = 313;BA.debugLine="Label1.SetLayout(40%x,40%y,40%x,90dip)";
mostCurrent._label1.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90)));
 //BA.debugLineNum = 314;BA.debugLine="Button4.TextSize=15";
mostCurrent._button4.setTextSize((float) (15));
 //BA.debugLineNum = 315;BA.debugLine="End Sub";
return "";
}
public static String  _menu_onfullyopen() throws Exception{
 //BA.debugLineNum = 294;BA.debugLine="Sub Menu_onFullyOpen";
 //BA.debugLineNum = 295;BA.debugLine="lvMenu.BringToFront";
mostCurrent._lvmenu.BringToFront();
 //BA.debugLineNum = 296;BA.debugLine="Button4.SendToBack";
mostCurrent._button4.SendToBack();
 //BA.debugLineNum = 297;BA.debugLine="smlp.BringToFront";
mostCurrent._smlp.BringToFront();
 //BA.debugLineNum = 298;BA.debugLine="slm.Sidebar.BringToFront";
mostCurrent._slm._sidebar().BringToFront();
 //BA.debugLineNum = 299;BA.debugLine="pgWheel1.Focusable=False";
mostCurrent._pgwheel1.setFocusable(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 300;BA.debugLine="Button4.SetLayoutAnimated(200,0%x,55%y,20%x,105)";
mostCurrent._button4.SetLayoutAnimated((int) (200),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (55),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) (105));
 //BA.debugLineNum = 301;BA.debugLine="Button4.ButtonColor=mcl.md_cyan_A400";
mostCurrent._button4.setButtonColor(mostCurrent._mcl.getmd_cyan_A400());
 //BA.debugLineNum = 302;BA.debugLine="Label1.SetLayout(40%x,40%y,40%x,90dip)";
mostCurrent._label1.SetLayout(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (40),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90)));
 //BA.debugLineNum = 303;BA.debugLine="Button4.TextSize=10";
mostCurrent._button4.setTextSize((float) (10));
 //BA.debugLineNum = 304;BA.debugLine="End Sub";
return "";
}
public static String  _menu_onmove(boolean _isopening) throws Exception{
 //BA.debugLineNum = 317;BA.debugLine="Sub Menu_onMove (IsOpening As Boolean)";
 //BA.debugLineNum = 318;BA.debugLine="If IsOpening=True Then";
if (_isopening==anywheresoftware.b4a.keywords.Common.True) { 
 };
 //BA.debugLineNum = 322;BA.debugLine="End Sub";
return "";
}
public static String  _minutes_hours(int _ms) throws Exception{
int _val = 0;
int _hours = 0;
int _minutes = 0;
 //BA.debugLineNum = 767;BA.debugLine="Sub minutes_hours ( ms As Int ) As String";
 //BA.debugLineNum = 768;BA.debugLine="Dim val,hours,minutes As Int";
_val = 0;
_hours = 0;
_minutes = 0;
 //BA.debugLineNum = 769;BA.debugLine="val = ms";
_val = _ms;
 //BA.debugLineNum = 770;BA.debugLine="hours = Floor(val / 60)";
_hours = (int) (anywheresoftware.b4a.keywords.Common.Floor(_val/(double)60));
 //BA.debugLineNum = 771;BA.debugLine="minutes = val Mod 60";
_minutes = (int) (_val%60);
 //BA.debugLineNum = 772;BA.debugLine="Return NumberFormat(hours, 1, 0) & \":\" & NumberFo";
if (true) return anywheresoftware.b4a.keywords.Common.NumberFormat(_hours,(int) (1),(int) (0))+":"+anywheresoftware.b4a.keywords.Common.NumberFormat(_minutes,(int) (2),(int) (0));
 //BA.debugLineNum = 773;BA.debugLine="End Sub";
return "";
}
public static String  _opt_click() throws Exception{
 //BA.debugLineNum = 538;BA.debugLine="Sub opt_click";
 //BA.debugLineNum = 539;BA.debugLine="StartActivity(settings)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._settings.getObject()));
 //BA.debugLineNum = 540;BA.debugLine="End Sub";
return "";
}
public static String  _pci_click() throws Exception{
 //BA.debugLineNum = 556;BA.debugLine="Sub pci_click";
 //BA.debugLineNum = 557;BA.debugLine="cd.AddView(panel3,-1,350)";
mostCurrent._cd.AddView((android.view.View)(mostCurrent._panel3.getObject()),(int) (-1),(int) (350));
 //BA.debugLineNum = 558;BA.debugLine="cd.Show(\"About Batt-Cat: \",\"\",\"Got It\",\"\",LoadBit";
mostCurrent._cd.Show("About Batt-Cat: ","","Got It","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Rss.png").getObject()));
 //BA.debugLineNum = 559;BA.debugLine="If Not (cd2.Response=DialogResponse.CANCEL) Then";
if (anywheresoftware.b4a.keywords.Common.Not(mostCurrent._cd2.getResponse()==anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL)) { 
 //BA.debugLineNum = 560;BA.debugLine="ct.ShowBitmap(\"©2017 SuloMedia™\",30,Gravity.BOTT";
mostCurrent._ct.ShowBitmap(BA.ObjectToCharSequence("©2017 SuloMedia™"),(int) (30),anywheresoftware.b4a.keywords.Common.Gravity.BOTTOM,(int) (0),(int) (0),(android.graphics.Bitmap)(mostCurrent._logo.getObject()));
 };
 //BA.debugLineNum = 562;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
klo._process_globals();
cool._process_globals();
hw._process_globals();
starter._process_globals();
sys._process_globals();
settings._process_globals();
charts._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 19;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 20;BA.debugLine="Dim pak As PackageManager";
_pak = new anywheresoftware.b4a.phone.PackageManagerWrapper();
 //BA.debugLineNum = 21;BA.debugLine="Private cs As CSBuilder";
_cs = new anywheresoftware.b4a.objects.CSBuilder();
 //BA.debugLineNum = 22;BA.debugLine="End Sub";
return "";
}
public static String  _re_bound() throws Exception{
 //BA.debugLineNum = 457;BA.debugLine="Sub re_bound";
 //BA.debugLineNum = 458;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 459;BA.debugLine="End Sub";
return "";
}
public static String  _rl_lo() throws Exception{
 //BA.debugLineNum = 542;BA.debugLine="Sub rl_lo";
 //BA.debugLineNum = 543;BA.debugLine="cd2.Show(\"User App´s\",\"Check\",\"\",\"\",LoadBitmap(Fi";
mostCurrent._cd2.Show("User App´s","Check","","",mostCurrent.activityBA,(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Android.png").getObject()));
 //BA.debugLineNum = 544;BA.debugLine="If cd2.Response=DialogResponse.POSITIVE Then";
if (mostCurrent._cd2.getResponse()==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 545;BA.debugLine="ToastMessageShow(\"closed...\",False)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("closed..."),anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 547;BA.debugLine="Return";
if (true) return "";
 //BA.debugLineNum = 548;BA.debugLine="End Sub";
return "";
}
public static String  _show_2() throws Exception{
 //BA.debugLineNum = 285;BA.debugLine="Sub show_2";
 //BA.debugLineNum = 286;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 //BA.debugLineNum = 287;BA.debugLine="showtip.setDelay(400)";
mostCurrent._showtip.setDelay((int) (400));
 //BA.debugLineNum = 288;BA.debugLine="showtip.setCircleColor(Colors.ARGB(195,255,255,25";
mostCurrent._showtip.setCircleColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (195),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 289;BA.debugLine="showtip.setTitle(pak.GetApplicationLabel(\"com.bat";
mostCurrent._showtip.setTitle(_pak.GetApplicationLabel("com.batcat")).build();
 //BA.debugLineNum = 290;BA.debugLine="showtip.build.setDescription(\"Tippe hier um dein";
mostCurrent._showtip.build().setDescription("Tippe hier um dein Handy zu Boosten...");
 //BA.debugLineNum = 291;BA.debugLine="showtip.setTarget(Button4)";
mostCurrent._showtip.setTarget((android.view.View)(mostCurrent._button4.getObject()));
 //BA.debugLineNum = 292;BA.debugLine="showtip.show";
mostCurrent._showtip.show();
 //BA.debugLineNum = 293;BA.debugLine="End Sub";
return "";
}
public static String  _show_tip() throws Exception{
 //BA.debugLineNum = 274;BA.debugLine="Sub show_tip";
 //BA.debugLineNum = 275;BA.debugLine="slm.OpenSidebar";
mostCurrent._slm._opensidebar();
 //BA.debugLineNum = 276;BA.debugLine="showtip.setDelay(400)";
mostCurrent._showtip.setDelay((int) (400));
 //BA.debugLineNum = 277;BA.debugLine="showtip.setCircleColor(Colors.ARGB(195,255,255,2";
mostCurrent._showtip.setCircleColor(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (195),(int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 278;BA.debugLine="showtip.setTitle(pak.GetApplicationLabel(\"com.ba";
mostCurrent._showtip.setTitle(_pak.GetApplicationLabel("com.batcat")).build();
 //BA.debugLineNum = 279;BA.debugLine="showtip.build.setDescription(\"Swipe nach links z";
mostCurrent._showtip.build().setDescription("Swipe nach links zum öffnen und nach rechts zum schließen des Menü´s");
 //BA.debugLineNum = 280;BA.debugLine="showtip.setTarget(smlp).build";
mostCurrent._showtip.setTarget((android.view.View)(mostCurrent._smlp.getObject())).build();
 //BA.debugLineNum = 281;BA.debugLine="showtip.show";
mostCurrent._showtip.show();
 //BA.debugLineNum = 284;BA.debugLine="End Sub";
return "";
}
public static String  _slem_click() throws Exception{
 //BA.debugLineNum = 447;BA.debugLine="Sub slem_click";
 //BA.debugLineNum = 448;BA.debugLine="If slm.IsSidebarVisible Then";
if (mostCurrent._slm._issidebarvisible()) { 
 //BA.debugLineNum = 449;BA.debugLine="slm.CloseSidebar";
mostCurrent._slm._closesidebar();
 }else {
 //BA.debugLineNum = 452;BA.debugLine="slm.OpenSidebar";
mostCurrent._slm._opensidebar();
 };
 //BA.debugLineNum = 455;BA.debugLine="End Sub";
return "";
}
public static String  _slem1_click() throws Exception{
 //BA.debugLineNum = 443;BA.debugLine="Sub slem1_click";
 //BA.debugLineNum = 444;BA.debugLine="slem_click";
_slem_click();
 //BA.debugLineNum = 445;BA.debugLine="End Sub";
return "";
}
public static String  _st_click() throws Exception{
 //BA.debugLineNum = 529;BA.debugLine="Sub st_click";
 //BA.debugLineNum = 530;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 531;BA.debugLine="End Sub";
return "";
}
public static String  _stat_me() throws Exception{
 //BA.debugLineNum = 520;BA.debugLine="Sub stat_me";
 //BA.debugLineNum = 521;BA.debugLine="StartActivity(klo)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._klo.getObject()));
 //BA.debugLineNum = 522;BA.debugLine="End Sub";
return "";
}
public static String  _store_check() throws Exception{
 //BA.debugLineNum = 324;BA.debugLine="Sub store_check";
 //BA.debugLineNum = 325;BA.debugLine="c1=mcl.md_light_blue_A700";
_c1 = mostCurrent._mcl.getmd_light_blue_A700();
 //BA.debugLineNum = 326;BA.debugLine="c2=mcl.md_amber_A700";
_c2 = mostCurrent._mcl.getmd_amber_A700();
 //BA.debugLineNum = 327;BA.debugLine="c3=mcl.md_lime_A700";
_c3 = mostCurrent._mcl.getmd_lime_A700();
 //BA.debugLineNum = 328;BA.debugLine="c4=mcl.md_teal_A700";
_c4 = mostCurrent._mcl.getmd_teal_A700();
 //BA.debugLineNum = 329;BA.debugLine="c5=mcl.md_deep_purple_A700";
_c5 = mostCurrent._mcl.getmd_deep_purple_A700();
 //BA.debugLineNum = 330;BA.debugLine="c6=mcl.md_red_A700";
_c6 = mostCurrent._mcl.getmd_red_A700();
 //BA.debugLineNum = 331;BA.debugLine="c7=mcl.md_indigo_A700";
_c7 = mostCurrent._mcl.getmd_indigo_A700();
 //BA.debugLineNum = 332;BA.debugLine="c8=mcl.md_blue_A700";
_c8 = mostCurrent._mcl.getmd_blue_A700();
 //BA.debugLineNum = 333;BA.debugLine="c9=mcl.md_orange_A700";
_c9 = mostCurrent._mcl.getmd_orange_A700();
 //BA.debugLineNum = 334;BA.debugLine="c10=mcl.md_grey_700";
_c10 = mostCurrent._mcl.getmd_grey_700();
 //BA.debugLineNum = 335;BA.debugLine="c11=mcl.md_green_A700";
_c11 = mostCurrent._mcl.getmd_green_A700();
 //BA.debugLineNum = 336;BA.debugLine="c12=mcl.md_light_green_A700";
_c12 = mostCurrent._mcl.getmd_light_green_A700();
 //BA.debugLineNum = 337;BA.debugLine="c13=mcl.md_yellow_A700";
_c13 = mostCurrent._mcl.getmd_yellow_A700();
 //BA.debugLineNum = 338;BA.debugLine="c14=mcl.md_cyan_A700";
_c14 = mostCurrent._mcl.getmd_cyan_A700();
 //BA.debugLineNum = 339;BA.debugLine="c15=mcl.md_blue_grey_700";
_c15 = mostCurrent._mcl.getmd_blue_grey_700();
 //BA.debugLineNum = 340;BA.debugLine="If kvs4sub.ContainsKey(\"off\") Then";
if (mostCurrent._kvs4sub._containskey("off")) { 
 //BA.debugLineNum = 341;BA.debugLine="StopService(Starter)";
anywheresoftware.b4a.keywords.Common.StopService(mostCurrent.activityBA,(Object)(mostCurrent._starter.getObject()));
 }else {
 };
 //BA.debugLineNum = 345;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 346;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 347;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 }else {
 };
 //BA.debugLineNum = 352;BA.debugLine="If kvs4.ContainsKey(\"0\")Then";
if (mostCurrent._kvs4._containskey("0")) { 
 //BA.debugLineNum = 353;BA.debugLine="Log(\"AC_true->1\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->1");
 //BA.debugLineNum = 354;BA.debugLine="Activity.Color=c1";
mostCurrent._activity.setColor(_c1);
 };
 //BA.debugLineNum = 356;BA.debugLine="If kvs4.ContainsKey(\"1\")Then";
if (mostCurrent._kvs4._containskey("1")) { 
 //BA.debugLineNum = 357;BA.debugLine="Log(\"AC_true->2\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->2");
 //BA.debugLineNum = 358;BA.debugLine="Activity.Color=c2";
mostCurrent._activity.setColor(_c2);
 }else {
 };
 //BA.debugLineNum = 362;BA.debugLine="If kvs4.ContainsKey(\"2\")Then";
if (mostCurrent._kvs4._containskey("2")) { 
 //BA.debugLineNum = 363;BA.debugLine="Log(\"AC_true->3\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->3");
 //BA.debugLineNum = 364;BA.debugLine="Activity.Color=c3";
mostCurrent._activity.setColor(_c3);
 }else {
 };
 //BA.debugLineNum = 368;BA.debugLine="If kvs4.ContainsKey(\"3\")Then";
if (mostCurrent._kvs4._containskey("3")) { 
 //BA.debugLineNum = 369;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 370;BA.debugLine="Activity.Color=c4";
mostCurrent._activity.setColor(_c4);
 }else {
 };
 //BA.debugLineNum = 374;BA.debugLine="If kvs4.ContainsKey(\"4\")Then";
if (mostCurrent._kvs4._containskey("4")) { 
 //BA.debugLineNum = 375;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 376;BA.debugLine="Activity.Color=c5";
mostCurrent._activity.setColor(_c5);
 }else {
 };
 //BA.debugLineNum = 380;BA.debugLine="If kvs4.ContainsKey(\"5\")Then";
if (mostCurrent._kvs4._containskey("5")) { 
 //BA.debugLineNum = 381;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 382;BA.debugLine="Activity.Color=c6";
mostCurrent._activity.setColor(_c6);
 }else {
 };
 //BA.debugLineNum = 386;BA.debugLine="If kvs4.ContainsKey(\"6\")Then";
if (mostCurrent._kvs4._containskey("6")) { 
 //BA.debugLineNum = 387;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 388;BA.debugLine="Activity.Color=c7";
mostCurrent._activity.setColor(_c7);
 }else {
 };
 //BA.debugLineNum = 392;BA.debugLine="If kvs4.ContainsKey(\"7\")Then";
if (mostCurrent._kvs4._containskey("7")) { 
 //BA.debugLineNum = 393;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 394;BA.debugLine="Activity.Color=c8";
mostCurrent._activity.setColor(_c8);
 }else {
 };
 //BA.debugLineNum = 398;BA.debugLine="If kvs4.ContainsKey(\"8\")Then";
if (mostCurrent._kvs4._containskey("8")) { 
 //BA.debugLineNum = 399;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 400;BA.debugLine="Activity.Color=c9";
mostCurrent._activity.setColor(_c9);
 }else {
 };
 //BA.debugLineNum = 404;BA.debugLine="If kvs4.ContainsKey(\"9\")Then";
if (mostCurrent._kvs4._containskey("9")) { 
 //BA.debugLineNum = 405;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 406;BA.debugLine="Activity.Color=c10";
mostCurrent._activity.setColor(_c10);
 }else {
 };
 //BA.debugLineNum = 410;BA.debugLine="If kvs4.ContainsKey(\"10\")Then";
if (mostCurrent._kvs4._containskey("10")) { 
 //BA.debugLineNum = 411;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 412;BA.debugLine="Activity.Color=c11";
mostCurrent._activity.setColor(_c11);
 }else {
 };
 //BA.debugLineNum = 416;BA.debugLine="If kvs4.ContainsKey(\"11\")Then";
if (mostCurrent._kvs4._containskey("11")) { 
 //BA.debugLineNum = 417;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 418;BA.debugLine="Activity.Color=c12";
mostCurrent._activity.setColor(_c12);
 }else {
 };
 //BA.debugLineNum = 422;BA.debugLine="If kvs4.ContainsKey(\"12\")Then";
if (mostCurrent._kvs4._containskey("12")) { 
 //BA.debugLineNum = 423;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 424;BA.debugLine="Activity.Color=c13";
mostCurrent._activity.setColor(_c13);
 }else {
 };
 //BA.debugLineNum = 428;BA.debugLine="If kvs4.ContainsKey(\"13\")Then";
if (mostCurrent._kvs4._containskey("13")) { 
 //BA.debugLineNum = 429;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 430;BA.debugLine="Activity.Color=c14";
mostCurrent._activity.setColor(_c14);
 }else {
 };
 //BA.debugLineNum = 434;BA.debugLine="If kvs4.ContainsKey(\"14\")Then";
if (mostCurrent._kvs4._containskey("14")) { 
 //BA.debugLineNum = 435;BA.debugLine="Log(\"AC_true->4\")";
anywheresoftware.b4a.keywords.Common.Log("AC_true->4");
 //BA.debugLineNum = 436;BA.debugLine="Activity.Color=c15";
mostCurrent._activity.setColor(_c15);
 }else {
 };
 //BA.debugLineNum = 440;BA.debugLine="Activity.Invalidate";
mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 441;BA.debugLine="End Sub";
return "";
}
public static String  _sy_click() throws Exception{
 //BA.debugLineNum = 524;BA.debugLine="Sub sy_click";
 //BA.debugLineNum = 525;BA.debugLine="StartActivity(sys)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._sys.getObject()));
 //BA.debugLineNum = 527;BA.debugLine="End Sub";
return "";
}
//import com.google.android.gms.ads.identifier.AdvertisingIdClient;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;

	
}
