﻿Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region
#Extends: android.support.v7.app.AppCompatActivity
Sub Process_Globals
	Dim t1 As Timer 

End Sub

Sub Globals
	Dim op As OperatingSystem
	Private Label1 As Label
	Dim count As Int=0
	Dim args(1) As Object
	Dim Obj1, Obj2, Obj3 As Reflector
	Dim Types(1), name,packName,date,time As String
'	Dim cache,stats,stats2 As Long
    Dim icon As BitmapDrawable
	Dim pak As PackageManager
	Dim list1,list2,list3,clist,piclist,phlis,list4,list5,list6,lis,list7,list8,list9,apklist,reslist,aclist,apclist,filelist As List 
	Dim catdel As CacheCleaner
	Dim cat As Cache
	'Dim dir1 As String =File.DirDefaultExternal&"/mnt/cache/store"
	Private lw2 As ListView
	Dim l1,l2,l3 As Label 
	Dim AnimSet As AnimationSet
	Dim ph As PhoneEvents
	Dim size,flags,count As Int
	Dim ffiles,ffolders As List
'	Private ion As Object
	'Dim cas As String
	Private dialog As CustomDialog2
	Dim diapan As Panel
	Dim dial As Label
	Dim dill As List
	Dim ActivityManager1 As ActivityManager
	Dim RunningTaskInfos() As RunningTaskInfo

	Dim paths As Map
	Dim panel1 As Panel 
	Dim storage As env
	Dim de As String = File.DirRootExternal
	Dim mtc As Matcher = Regex.Matcher("(/|\\)[^(/|\\)]*(/|\\)",de)
	Dim extsdcard As String = de
	Dim nativeMe As JavaObject
	Private Panel2 As Panel

	Private ListView1 As ListView
	Private ImageView1 As ImageView
	Private pg As NumberProgressBar
	Dim ffil,ffold As List
	'Dim root As RuntimePermissions
	Dim rot As String 
	Private pgWheel1 As pgWheel
	Dim andro,bat,desk,work As Bitmap
'	Private c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15 As Int
	Private mcl As MaterialColors
End Sub 

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("5")
	Activity.Title=pak.GetApplicationLabel("com.batcat")&" - "&pak.GetVersionName("com.batcat")
	op.Initialize("op")
	list1.Initialize
	list2.Initialize
	list3.Initialize
	list4.Initialize
	list5.Initialize
	list6.Initialize
	list7.Initialize
	list8.Initialize
	list9.Initialize
	reslist.Initialize
	apklist.Initialize
	clist.Initialize
	aclist.Initialize
	filelist.Initialize
	apclist.Initialize
	ph.Initialize("ph")
	piclist.Initialize
	lis.Initialize

	'ml.Initialize
	l1.Initialize("l1")
	l2.Initialize("l2")
	AnimSet.Initialize(True)
	phlis.Initialize
	diapan.Initialize("diapan")
	dial.Initialize("dial") 
	dill.Initialize
	lw2.Initialize("lw2")
	ffiles.Initialize
	ph.Initialize("ph")
	'Activity.Background=c1
		If FirstTime Then 
		
		If File.Exists(File.DirDefaultExternal&"/mnt/cache","ressize.txt") Then
			ToastMessageShow("core ready...!",False)
			File.WriteList(File.DirDefaultExternal&"/mnt/cache","sv.txt",list4)
			File.WriteList(File.DirDefaultExternal&"/mnt/cache","fn.txt",list1)
			'ListView1.Clear
			Else
	File.MakeDir(File.DirDefaultExternal, "mnt/cache")
	File.MakeDir(File.DirDefaultExternal, "mnt/cache/store")
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","sv.txt",list4)
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","fn.txt",list1)
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","apk1.txt",list7)
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","apk2.txt",list8)
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","apk3.txt",list9)
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","pn1.txt",lis)
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","apsize.txt",clist)
	File.WriteList(File.DirDefaultExternal&"/mnt/cache","ressize.txt",reslist)
			File.WriteString(File.DirDefaultExternal&"/mnt/cache","cclist.txt","")
	ToastMessageShow("Files ready! "&date&", "&time,False)
	End If 
	End If 
	'#######Timer Settings##############
	t1.Initialize("t1",1000)
	t1.Enabled=False
	
	l2=ListView1.TwoLinesLayout.Label
	l2.TextSize=11
	l2.TextColor=Colors.Black
	l3=ListView1.TwoLinesLayout.SecondLabel
	l3.TextColor=Colors.White
	l3.TextSize=10
	ListView1.TwoLinesLayout.ItemHeight=25
	
    ffolders.Initialize
    'root = File.DirRootExternal
	'sdint = File.DirDefaultExternal
	'nat_me

	dial.TextSize=15
	dial.TextColor=Colors.White
	dialog.AddView(diapan,350,350)
	diapan.AddView(lw2,3,3,-1,-1)
	Dim la,la1,lwa1,lwa2 As Label
	la.Initialize("la")
	la1.Initialize("la1")
	lwa1.Initialize("lwa1")
	lwa2.Initialize("lwa2")
	
	lwa1=lw2.SingleLineLayout.Label
	lwa1.TextColor=Colors.White
	lwa1.TextSize=13
	lw2.SingleLineLayout.ItemHeight=70
	
	la=ListView1.TwoLinesAndBitmap.SecondLabel
	la.TextSize=12
	la.TextColor=Colors.ARGB(130,254,254,255)
	la1=ListView1.TwoLinesAndBitmap.Label
	la1.TextSize=14
	la1.TextColor=Colors.ARGB(199,255,255,255)
	ListView1.TwoLinesAndBitmap.ItemHeight=120
	
	'#######################Storage Lolipop########################
	paths = storage.Initialize
	nativeMe.InitializeContext
	'#########################End LP Storage#######################
'
	'#########################CLS Storage##########################
	Panel2.Visible=False
	'#########################End CLS Storage######################
	ffil.Initialize
	ffold.Initialize
	'Activity.Color=Colors.ARGB(150,30,124,235)
	Activity.SetColorAnimated(12000,mcl.md_amber_500,mcl.md_lime_700)
	Label1.SetTextColorAnimated(9000,Colors.White)
	'clist.Add(root.GetSafeDirDefaultExternal(""))
	For h = 0 To clist.Size-1
		Log(clist.Get(h))
	Next
	andro=LoadBitmap(File.DirAssets,"ic_autorenew_black_48dp.png")
	bat=LoadBitmap(File.DirAssets,"ic_data_usage_black_48dp.png")
	desk=LoadBitmap(File.DirAssets, "ic_battery_alert_black_48dp.png")
	work=LoadBitmap(File.DirAssets, "ic_delete_black_48dp.png")
	
	GetDeviceId
	c_start
	loading_norm
	clean_start
End Sub

Sub Activity_Resume
	t1.Enabled=True
	c_start
	loading_norm
	clean_start
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	Activity.Finish
End Sub

Sub Activity_KeyPress (KeyCode As Int) As Boolean 'Return True to consume the event
	If KeyCode=KeyCodes.KEYCODE_BACK Then
		Activity.Finish
	End If
	Return(True)
End Sub

Sub c_start
	app_info
	pgWheel1.Visible=False
	dill.Clear
	reslist.Clear
	piclist.Clear
	filelist.Clear
	apclist.Clear
	list2.Clear
	list4.Clear
	phlis.Clear
	piclist.Add(andro)
	piclist.Add(bat)
	piclist.Add(desk)
	li4
	cat_start
End Sub

Sub GetDeviceId As String
	Dim api As Int
	Dim r As Reflector
	api = r.GetStaticField("android.os.Build$VERSION", "SDK_INT")
	If api < 18 Then
		'Old device
		If File.Exists(File.DirInternal, "__id") Then
			Return File.ReadString(File.DirInternal, "__id")
			c_start
		Else
			c_start
			Dim id As Int
			id = Rnd(0x10000000, 0x7FFFFFFF)
			File.WriteString(File.DirInternal, "__id", id)
			Return id
		End If
		Log(api)
	Else
		'New device
		Return r.GetStaticField("android.os.Build", "SERIAL")
		

		
		storage_check
	End If
End Sub

Sub storage_check
	For i = 0 To paths.Size-1
		Log(paths.GetKeyAt(i)&"="&paths.GetValueAt(i))
	Next

	Log ("DirRootExternal = "&de)
	
	If mtc.Find = True Then
		Dim mnt As String = mtc.Group(0)
   
		Log ("mount point = "& mnt)
		Dim dirs As List = File.ListFiles(mnt)
		For Each f As String In dirs
			If storage.isExternalStorageRemovable(mnt&f) Then
				Log ("Device = "& f&":"&mnt&f&" is removable")
				If File.ListFiles(mnt&f).IsInitialized Then
					Log("probably ExtSDCard: "&mnt&f)
					extsdcard = mnt&f
				Else
					'Log("Problem reading "&mnt&f)
				End If
			Else
				Log ("Device = "& f&":"&mnt&f&" is NOT removable")
			End If
		Next
	End If
	Activity.SetColorAnimated(1000,Colors.ARGB(100,30,124,235),Colors.ARGB(190,35,140,7))
	c_start
End Sub 

Sub cat_start
	count=count+1
	
	'cat.Initialize(25,50*1024*1024,dir1)
	clean_start
	t1.Enabled=True
	t1_Tick
End Sub



Sub logcat_LogCatData(Buffer() As Byte, Length As Int)
	Dim data As String
	data = BytesToString(Buffer,0,Length,"UTF-8")
	'lw2.AddSingleLine(data)
	Log(data)
End Sub



Sub li4
	
	list2=op.RunningServiceInfo(999,list4,list5,list6)
	For u = 0 To list2.Size-1
		Log(list2.Get(u))
	Next
End Sub



Sub close
		If Not(apklist.size=0) Then
				Dim df As String  
		df=apklist.size
		Label1.Text=op.formatSize(cat.FreeMemory)&" RAM free! "&list4.Size&" -Backround Processes closed."&df&" Files and Trash Data cleared"
		Else
		Label1.Text=op.formatSize(cat.FreeMemory)&" RAM free! "&list4.Size&" Backround Processes killed...!"
	
		End If 	
		
		
	delayed_t2
End Sub

Sub del_quest
	pgWheel1.Visible=False
	ImageView1.Bitmap=LoadBitmap(File.DirAssets,"Accept128.png")
		Label1.Text= "clear RAM and close.."
	
		real_delete
	
		ListView1.Clear
End Sub

Sub deleting_files
	dill.Clear

	Dim dd As String
	Dim ens As String
	For i = 0 To  list3.Size-1
		dd=list3.Get(i)
		ens=GetParentPath(GetSourceDir(GetActivitiesInfo(dd)))
		ReadDir(ens,True)
	Next
	
	For d = 0 To ffold.Size-1
		'bc=ffold.Get(d)
	
	Next
End Sub


Sub real_delete
	pgWheel1.Progress=360
	RunningTaskInfos=ActivityManager1.GetRunningTasks
	Log("RunningTaskInfos.Length="&RunningTaskInfos.Length)

	For Each RunningTaskInfo1 As RunningTaskInfo In RunningTaskInfos
		
		'op.killBackgroundProcesses(RunningTaskInfo1.GetPackageName)
		Label1.Text=RunningTaskInfo1.GetApplicationName&"  - "&RunningTaskInfo1.GetPackageName
		'Log(RunningTaskInfo1.GetPackageName)
	Next
		'cat.CloseAndClearDiskCache
	
	
	close
End Sub
Sub loading_norm
	pgWheel1.CircleRadius=100
	pgWheel1.FocusableInTouchMode=False
	pgWheel1.CircleColor= Colors.ARGB(90,255,255,255)
	pgWheel1.ContourColor=Colors.ARGB(255,255,255,255)
	pgWheel1.BarColor= Colors.ARGB(220,255,255,255)
	pgWheel1.ContourSize=2dip
	pgWheel1.FadingEdgeLength=3dip
	pgWheel1.TextSize=45
	pgWheel1.TextColor=Colors.Black
	pgWheel1.SpinSpeed=10
	pgWheel1.RimColor= Colors.ARGB(100,11,170,242)
	pgWheel1.RimWidth=25dip
	pgWheel1.BarWidth=15dip
	pgWheel1.DelayMillis=0.6
	pgWheel1.Clickable=False
	'pgWheel1.RimShader=LoadBitmap(File.DirAssets,"header.png")
End Sub
Sub ph_DeviceStorageOk (Intent As Intent)
	Log(Intent.ExtrasToString)
End Sub

Sub t1_Tick
	andro=LoadBitmap(File.DirAssets,"ic_autorenew_black_48dp.png")
	bat=LoadBitmap(File.DirAssets,"ic_data_usage_black_48dp.png")
	desk=LoadBitmap(File.DirAssets, "ic_battery_alert_black_48dp.png")
	work=LoadBitmap(File.DirAssets, "ic_delete_black_48dp.png")
	pgWheel1.Visible=True
	pgWheel1.SpinSpeed=100
	pgWheel1.spin
	ListView1.Visible=True 
	pg.SetColorAnimated(12000,Colors.Transparent,mcl.md_lime_A700)
	ImageView1.Bitmap=andro
	count=count+1
	pg.incrementProgressBy(count)
	If count>0 Then 
		Label1.Text="check Battery.."
	End If
	If count > 1 Then 
		Label1.Text="check Battery.."
	End If
	If count > 2 Then
		Label1.Text="check Battery.."
		'ListView1.AddTwoLinesAndBitmap("Sytem Apps:",list1.Size &" gefunden.", LoadBitmap(File.DirAssets,"Android.png"))
	End If
	If count > 3 Then
		'pg.Progress=28
		Label1.Text="check System.."
		ListView1.AddTwoLinesAndBitmap("System Prozesse:",list2.Size &" gefunden.", andro)
	End If
	If count > 4 Then
		'pg.Progress=29
		ListView1.Clear
		Label1.Text="check System.."
		'ListView1.AddTwoLinesAndBitmap("System Prozesse:",list2.Size &" gefunden.", desk)
	End If
	If count > 5 Then
		'pg.Progress=32
		pgWheel1.SpinSpeed=200
		ImageView1.Bitmap=bat
		Label1.Text="clear Cache System.."
		'Label1.Text=op.formatSize(cat.DiskFree) &" App Daten gefunden.."
	End If
	If count > 6 Then
		Label1.Text="defrag File System.."
	End If
	If count > 7 Then
		Label1.Text="search SDCard System.."
			ImageView1.Bitmap=andro
		'For t = 0 To piclist.Size-1
				'ListView1.AddTwoLinesAndBitmap(GetFileName(GetSourceDir(GetActivitiesInfo(packName))),GetParentPath(GetSourceDir(GetActivitiesInfo(packName))),piclist.Get(t))
	
		
	End If
	If count > 8 Then
		Label1.Text="clear Cache System.."
		ImageView1.Bitmap=desk
				Label1.Text="check "&op.formatSize(op.AvailableMemory)
	End If
	If count > 9 Then  
		pgWheel1.SpinSpeed=100
		ImageView1.Bitmap=andro
		'ListView1.Clear
	End If
	If count > 10 Then  
		ImageView1.Bitmap=desk
				Label1.Text="Running Process: "&  list3.Size
		'ListView1.AddTwoLinesAndBitmap("Benutzer Apps:",list3.Size &" gefunden.", LoadBitmap(File.DirAssets,"Android.png"))
	End If
	If count > 11 Then  
		ImageView1.Bitmap=bat
	End If
	If count > 12 Then
		If Not (filelist.Size=0) Then
			ImageView1.Bitmap=andro
			
		Else
			ImageView1.Bitmap=desk
			Label1.Text="Clear!"
		End If
		
		pgWheel1.stopSpinning
		'ListView1.Clear
		'ListView1.AddTwoLinesAndBitmap("Boost Phone","cleaning "&op.formatSize(list4.Size+apklist.Size+list1.size),LoadBitmap(File.DirAssets,"Battery.png"))
	End If
	If count > 13 Then
		Label1.Text=op.formatSize(cat.FreeMemory)
			If Not (filelist.Size=1) Then
			pgWheel1.Progress=180
			ImageView1.Bitmap=andro
				Label1.Text=nativeMe.RunMethod("getOSCodename", Null)
			ImageView1.Bitmap=bat
			Else
			ImageView1.Bitmap=andro
			
			ImageView1.Bitmap=desk
			End If
		'ListView1.Clear
		'ListView1.AddTwoLinesAndBitmap(list4.Size&" Process killed",list1.Size&" Apps optmiert",LoadBitmap(File.DirAssets,"Tag.png"))
	End If
	If count > 14 Then
		pgWheel1.Progress=360
		CallSub(Me,"del_quest")
	End If
End Sub

Sub delayed_t2
		pg.Progress=100
		If count = 15 Then 
			catdel.clearCache
		t1.Enabled=False  
		ToastMessageShow(op.formatSize(cat.FreeMemory)&" free",False)
		Activity.Finish
	End If
End Sub

Sub pg_onProgressChange(current As Int, maxvalue As Int)
	andro.Initialize(File.DirAssets,"Android.png")
	bat.Initialize(File.DirAssets,"Battery.png")
	desk.Initialize(File.DirAssets, "Chart.png")
	maxvalue=100
	If current=count+1 Then 
	For Each tr As String In reslist
		Log(tr)
		ImageView1.Bitmap=andro
		Label1.Text=tr
	Next
	End If
	If current=100 Then
		ImageView1.Bitmap=LoadBitmap(File.DirAssets,"Accept128.png")
	End If
End Sub
Sub clean_start
	pg.incrementProgressBy(0)
	pg.ProgressTextColor = Colors.Black
	pg.ReachedBarColor = Colors.ARGB(185,255,255,255)
	pg.UnreachedBarColor = Colors.Transparent
	pg.UnreachedBarHeight = 25dip
	pg.ReachedBarHeight = 20dip
	pg.ProgressTextSize=20dip
	pg.Max=100
	pg.Width=100%x
	pg.Left=1dip
	pg.Prefix = "%"
	pg.Suffix = "..."
End Sub


Sub ReadDir(folder As String, recursive As Boolean)
	'Log("ReadDir("&folder&")")
	Dim lst As List = File.ListFiles(folder)
	For i = 0 To lst.Size - 1
		If File.IsDirectory(folder,lst.Get(i)) Then
			Dim v As String
			v = folder&"/"&lst.Get(i)
			'Log("v="&v)
			ffold.Add(v.SubString(rot.Length+1))
			If recursive Then
				ReadDir(v,recursive)
			End If
		Else
			ffil.Add(folder&"/"&lst.Get(i))
		End If
	Next
	'Log(ffolders.Size&" Ordner / "&ffiles.Size&" Dateien")
End Sub

Sub getdir(dir As String, recursive As Boolean) As Long
	Dim si As String
	Dim siz As Long
	For Each f As String In File.ListFiles(dir)
		If recursive Then
			If File.IsDirectory(dir, f) Then
				si = dir
				Log(si)
				siz = siz + getdir(File.Combine(dir, f),recursive)
			End If
		End If
		siz = siz + File.Size(dir, f)
	Next
	Return siz
End Sub
'Sub ListFolders(dir As String)
'	Dim list_files As List
'	Dim lista_folders As List
'	lista_folders.Initialize
'	list_files=File.ListFiles(dir)
'	For i= 0 To list_files.Size -1
'		If File.IsDirectory(dir, list_files.Get(i))=True Then
'			lista_folders.Add(list_files.Get(i))
'		End If
'	Next
'	Return lista_folders
'End Sub

Sub app_info
	
	list1=pak.GetInstalledPackages
	'Log(list1.Get(i))
	Obj1.Target = Obj1.GetContext
	Obj1.Target = Obj1.RunMethod("getPackageManager") ' PackageManager
	Obj2.Target = Obj1.RunMethod2("getInstalledPackages", 0, "java.lang.int") ' List<PackageInfo>
	size = Obj2.RunMethod("size")
		
	'packName = Obj3.GetField("packageName")
	' name = Obj1.RunMethod4("getApplicationLabel", args, Types)
	' icon = Obj1.RunMethod4("getApplicationIcon", args, Types)
	For i = 0 To size -1
		Obj3.Target = Obj2.RunMethod2("get", i, "java.lang.int") ' PackageInfo
		size = Obj2.RunMethod("size")
 		
		Obj3.Target = Obj3.GetField("applicationInfo") ' ApplicationInfo
		flags = Obj3.GetField("flags")
		packName = Obj3.GetField("packageName")
	
		If Bit.And(flags, 1)  = 0 Then
          
			'app is not in the system image
			args(0) = Obj3.Target
			Types(0) = "android.content.pm.ApplicationInfo"
			name = Obj1.RunMethod4("getApplicationLabel", args, Types)
			icon = Obj1.RunMethod4("getApplicationIcon", args, Types)

			phlis.Add(icon.Bitmap)
			list3.Add(packName)
		End If
	Next
	
End Sub

Sub object_to_byte(obj As Object)As Byte()
	Dim ser As B4XSerializator
	Return ser.ConvertObjectToBytes(obj)
End Sub

Sub byte_to_object(data() As Byte)As Object
	Dim ser As B4XSerializator
	Return ser.ConvertBytesToObject(data)
End Sub


Sub fc_CopyDone(Key As String, Error As Boolean)
	'Log(Key&":"&DateTime.Date(DateTime.Now))
End Sub

Sub fc_PutDone(key As String,Error As Boolean)
	'Log(key&":"&DateTime.Date(DateTime.Now))
End Sub

Sub fc2_PutDone(key As String,Error As Boolean)
	'Log(key&":"&DateTime.Date(DateTime.Now))
End Sub

Sub GetParentPath(Path As String) As String
	Dim L As String
	Dim Path1 As String
	If Path = "/" Then
		Return "/"
	End If
	L = Path.LastIndexOf("/")
	If L = Path.Length - 1 Then
		'Strip the last slash
		Path1 = Path.SubString2(0,L)
	Else
		Path1 = Path
	End If
	L = Path.LastIndexOf("/")
	If L = 0 Then
		L = 1
	End If
	Return Path1.SubString2(0,L)
End Sub

Sub GetActivitiesInfo(package As String) As Object
	Dim r As Reflector
	r.Target = r.GetContext
	r.Target = r.RunMethod("getPackageManager")
	r.Target = r.RunMethod3("getPackageInfo", package, "java.lang.String", 0x00000001, "java.lang.int")
	Return r.GetField("applicationInfo")
End Sub

Sub GetSourceDir(AppInfo As Object) As String
	Try
		Dim r As Reflector
		r.Target = AppInfo
		Return r.GetField("sourceDir")
	Catch
		Return ""
	End Try
End Sub

Sub CalcSize(Folder As String, recursive As Boolean) As Long
	Dim size1 As Long
	For Each f As String In File.ListFiles(Folder)
		If recursive Then
			If File.IsDirectory(Folder, f) Then
				size1 = size1 + CalcSize(File.Combine(Folder, f),recursive)
			End If
		End If
		size1 = size1 + File.Size(Folder, f)
	Next
	Return size1
End Sub

Sub file_CopyDone
	Log("copy done!")
	
End Sub

#If Java

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
//import com.google.android.gms.ads.identifier.AdvertisingIdClient;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


  //private final Context context;
  private TelephonyManager tm; // = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
  private String initialVal = "";

  public int RINGER_MODE_SILENT = 0;
  public int RINGER_MODE_NORMAL = 1;
  public int RINGER_MODE_VIBRATE = 2;

  /**
   * The constant LOGTAG.
   */
  //private static final String LOGTAG = "EasyDeviceInfo";





  /**
   * Instantiates a new Easy device info.
   *
   * @param context the context
   */
//  public EasyDeviceInfo(Context context) {
//    this.context = context;
//    tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//    initialVal = "na";
//  }

  /**
   * Gets library version.
   *
   * @return the library version
   */
//  public String getLibraryVersion() {
//    String version = "1.1.9";
//    int versionCode = 11;
//    return version + "-" + versionCode;
//  }

  /**
   * Gets android id.
   *
   * @return the android id
   */
  public String getAndroidID() {
    String result = initialVal;
    try {
      result = Settings.Secure.getString(BA.applicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  
  
   /**
   * Gets model.
   *
   * @return the model
   */
  public String getModel() {
    String result = initialVal;
    try {
      result = Build.MODEL;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  } 


  /**
   * Gets build brand.
   *
   * @return the build brand
   */
  public String getBuildBrand() {
    String result = initialVal;
    try {
      result = Build.BRAND;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  }

  /**
   * Gets build host.
   *
   * @return the build host
   */
  public String getBuildHost() {
    String result = initialVal;
    try {
      result = Build.HOST;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets build tags.
   *
   * @return the build tags
   */
  public String getBuildTags() {
    String result = initialVal;
    try {
      result = Build.TAGS;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  
  
  /**
   * Gets build time.
   *
   * @return the build time
   */
  public long getBuildTime() {
    long result = 0;
    try {
      result = Build.TIME;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }  
  
  /**
   * Gets build user.
   *
   * @return the build user
   */
  public String getBuildUser() {
    String result = initialVal;
    try {
      result = Build.USER;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  /**
   * Gets build version release.
   *
   * @return the build version release
   */
  public String getBuildVersionRelease() {
    String result = initialVal;
    try {
      result = Build.VERSION.RELEASE;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
    /**
   * Gets screen display id.
   *
   * @return the screen display id
   */
  public String getScreenDisplayID() {
    String result = initialVal;
    try {
      WindowManager wm = (WindowManager) BA.applicationContext.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      result = String.valueOf(display.getDisplayId());
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  /**
   * Gets build version codename.
   *
   * @return the build version codename
   */
  public String getBuildVersionCodename() {
    String result = initialVal;
    try {
      result = Build.VERSION.CODENAME;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  
  /**
   * Gets build version incremental.
   *
   * @return the build version incremental
   */
  public String getBuildVersionIncremental() {
    String result = initialVal;
    try {
      result = Build.VERSION.INCREMENTAL;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  

  /**
   * Gets build version sdk.
   *
   * @return the build version sdk
   */
  public int getBuildVersionSDK() {
    int result = 0;
    try {
      result = Build.VERSION.SDK_INT;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
  

  /**
   * Gets build id.
   *
   * @return the build id
   */
  public String getBuildID() {
    String result = initialVal;
    try {
      result = Build.ID;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
        
  /**
   * Is Device rooted boolean
   *
   * @return the boolean
   */
  public boolean isDeviceRooted() {
    String su = "su";
    String[] locations = {
        "/sbin/", "/system/bin/", "/system/xbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
        "/data/local/xbin/", "/data/local/bin/", "/data/local/"
    };
    for (String location : locations) {
      if (new File(location + su).exists()) {
        return true;
      }
    }
    return false;
  }
  

  /**
   * Get supported abis string [ ].
   *
   * @return the string [ ]
   */
  public String[] getSupportedABIS() {
    String[] result = new String[] { "-" };
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        result = Build.SUPPORTED_ABIS;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length == 0) {
      result = new String[] { "-" };
    }
    return result;
  }
  


  /**
   * Gets string supported abis.
   *
   * @return the string supported abis
   */
  public String getStringSupportedABIS() {
    String result = initialVal;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        String[] supportedABIS = Build.SUPPORTED_ABIS;

        StringBuilder supportedABIString = new StringBuilder();
        if (supportedABIS.length > 0) {
          for (String abis : supportedABIS) {
            supportedABIString.append(abis).append("_");
          }
          supportedABIString.deleteCharAt(supportedABIString.lastIndexOf("_"));
        } else {
          supportedABIString.append(initialVal);
        }
        result = supportedABIString.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  }  
  
  
   /**
   * Gets string supported 32 bit abis.
   *
   * @return the string supported 32 bit abis
   */
  public String getStringSupported32bitABIS() {
    String result = initialVal;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        String[] supportedABIS = Build.SUPPORTED_32_BIT_ABIS;

        StringBuilder supportedABIString = new StringBuilder();
        if (supportedABIS.length > 0) {
          for (String abis : supportedABIS) {
            supportedABIString.append(abis).append("_");
          }
          supportedABIString.deleteCharAt(supportedABIString.lastIndexOf("_"));
        } else {
          supportedABIString.append(initialVal);
        }

        result = supportedABIString.toString();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }

    return handleIllegalCharacterInResult(result);
  } 
  
  
  /**
   * Gets manufacturer.
   *
   * @return the manufacturer
   */
  public String getManufacturer() {
    String result = initialVal;
    try {
      result = Build.MANUFACTURER;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  }  
  
  
  
   /**
   * Gets resolution.
   *
   * @return the resolution
   */
  public String getResolution() {
    String result = initialVal;
    try {
      WindowManager wm = (WindowManager) BA.applicationContext.getSystemService(Context.WINDOW_SERVICE);

      Display display = wm.getDefaultDisplay();

      DisplayMetrics metrics = new DisplayMetrics();
      display.getMetrics(metrics);
      result = metrics.heightPixels + "x" + metrics.widthPixels;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return result;
  } 
  

  /**
   * Gets carrier.
   *
   * @return the carrier
   */
  public String getCarrier() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      if (tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
        result = tm.getNetworkOperatorName().toLowerCase(Locale.getDefault());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  }
  
  
  /**
   * Gets device.
   *
   * @return the device
   */
  public String getDevice() {
    String result = initialVal;
    try {
      result = Build.DEVICE;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets bootloader.
   *
   * @return the bootloader
   */
  public String getBootloader() {
    String result = initialVal;
    try {
      result = Build.BOOTLOADER;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets board.
   *
   * @return the board
   */
  public String getBoard() {
    String result = initialVal;
    try {
      result = Build.BOARD;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets display version.
   *
   * @return the display version
   */
  public String getDisplayVersion() {
    String result = initialVal;
    try {
      result = Build.DISPLAY;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
   /**
   * Gets language.
   *
   * @return the language
   */
  public String getLanguage() {
    String result = initialVal;
    try {
      result = Locale.getDefault().getLanguage();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets country.
   *
   * @return the country
   */
  public String getCountry() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
        result = tm.getSimCountryIso().toLowerCase(Locale.getDefault());
      } else {
        Locale locale = Locale.getDefault();
        result = locale.getCountry().toLowerCase(locale);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  } 
  
  
  
  /**
   * Gets battery percentage
   *
   * @return the battery percentage
   */
  public int getBatteryPercentage() {
    int percentage = 0;
    Intent batteryStatus = getBatteryStatusIntent();
    if (batteryStatus != null) {
      int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
      int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
      percentage = (int) ((level / (float) scale) * 100);
    }

    return percentage;
  }

  /**
   * Is device charging boolean.
   *
   * @return is battery charging boolean
   */
  public boolean isDeviceCharging() {
    Intent batteryStatus = getBatteryStatusIntent();
    int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
    return (status == BatteryManager.BATTERY_STATUS_CHARGING
        || status == BatteryManager.BATTERY_STATUS_FULL);
  }

  /**
   * Is device charging usb boolean.
   *
   * @return is battery charging via USB boolean
   */
  public boolean isDeviceChargingUSB() {
    Intent batteryStatus = getBatteryStatusIntent();
    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
    return (chargePlug == BatteryManager.BATTERY_PLUGGED_USB);
  }

  /**
   * Is device charging ac boolean.
   *
   * @return is battery charging via AC boolean
   */
  public boolean isDeviceChargingAC() {
    Intent batteryStatus = getBatteryStatusIntent();
    int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
    return (chargePlug == BatteryManager.BATTERY_PLUGGED_AC);
  }  
  
  
   /**
   * Gets network type.
   *
   * @return the network type
   */
  public String getNetworkType() {
    int networkStatePermission =
        BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);

    String result = initialVal;

    if (networkStatePermission == PackageManager.PERMISSION_GRANTED) {
      try {
        ConnectivityManager cm =
            (ConnectivityManager) BA.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null) {
          result = "Unknown";
        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
            || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX) {
          result = "Wifi/WifiMax";
        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
          TelephonyManager manager =
              (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
          if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            switch (manager.getNetworkType()) {

              // Unknown
              case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                result = "Cellular - Unknown";
                break;
              // Cellular Data–2G
              case TelephonyManager.NETWORK_TYPE_EDGE:
              case TelephonyManager.NETWORK_TYPE_GPRS:
              case TelephonyManager.NETWORK_TYPE_CDMA:
              case TelephonyManager.NETWORK_TYPE_IDEN:
              case TelephonyManager.NETWORK_TYPE_1xRTT:
                result = "Cellular - 2G";
                break;
              // Cellular Data–3G
              case TelephonyManager.NETWORK_TYPE_UMTS:
              case TelephonyManager.NETWORK_TYPE_HSDPA:
              case TelephonyManager.NETWORK_TYPE_HSPA:
              case TelephonyManager.NETWORK_TYPE_HSPAP:
              case TelephonyManager.NETWORK_TYPE_HSUPA:
              case TelephonyManager.NETWORK_TYPE_EVDO_0:
              case TelephonyManager.NETWORK_TYPE_EVDO_A:
              case TelephonyManager.NETWORK_TYPE_EVDO_B:
                result = "Cellular - 3G";
                break;
              // Cellular Data–4G
              case TelephonyManager.NETWORK_TYPE_LTE:
                result = "Cellular - 4G";
                break;
              // Cellular Data–Unknown Generation
              default:
                result = "Cellular - Unknown Generation";
                break;
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return handleIllegalCharacterInResult(result);
  } 
  
  
  
  /**
   * Gets os codename.
   *
   * @return the os codename
   */
  public String getOSCodename() {
    String codename = initialVal;
    switch (Build.VERSION.SDK_INT) {
      case Build.VERSION_CODES.BASE:
        codename = "First Android Version. Yay !";
        break;
      case Build.VERSION_CODES.BASE_1_1:
        codename = "Base Android 1.1";
        break;
      case Build.VERSION_CODES.CUPCAKE:
        codename = "Cupcake";
        break;
      case Build.VERSION_CODES.DONUT:
        codename = "Donut";
        break;
      case Build.VERSION_CODES.ECLAIR:
      case Build.VERSION_CODES.ECLAIR_0_1:
      case Build.VERSION_CODES.ECLAIR_MR1:

        codename = "Eclair";
        break;
      case Build.VERSION_CODES.FROYO:
        codename = "Froyo";
        break;
      case Build.VERSION_CODES.GINGERBREAD:
      case Build.VERSION_CODES.GINGERBREAD_MR1:
        codename = "Gingerbread";
        break;
      case Build.VERSION_CODES.HONEYCOMB:
      case Build.VERSION_CODES.HONEYCOMB_MR1:
      case Build.VERSION_CODES.HONEYCOMB_MR2:
        codename = "Honeycomb";
        break;
      case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
      case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
        codename = "Ice Cream Sandwich";
        break;
      case Build.VERSION_CODES.JELLY_BEAN:
      case Build.VERSION_CODES.JELLY_BEAN_MR1:
      case Build.VERSION_CODES.JELLY_BEAN_MR2:
        codename = "Jelly Bean";
        break;
      case Build.VERSION_CODES.KITKAT:
        codename = "Kitkat";
        break;
      case Build.VERSION_CODES.KITKAT_WATCH:
        codename = "Kitkat Watch";
        break;
      case Build.VERSION_CODES.LOLLIPOP:
      case Build.VERSION_CODES.LOLLIPOP_MR1:
        codename = "Lollipop";
        break;
      case Build.VERSION_CODES.M:
        codename = "Marshmallow";
        break;
    }
    return codename;
  }  
  
  
  /**
   * Gets os version.
   *
   * @return the os version
   */
  public String getOSVersion() {
    String result = initialVal;
    try {
      result = Build.VERSION.RELEASE;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  


  /**
   * Gets wifi mac.
   *
   * @return the wifi mac
   */
  @SuppressWarnings("MissingPermission") public String getWifiMAC() {
    String result = initialVal;
    try {

      if (BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)
          == PackageManager.PERMISSION_GRANTED) {

        WifiManager wm = (WifiManager) BA.applicationContext.getSystemService(Context.WIFI_SERVICE);
        result = wm.getConnectionInfo().getMacAddress();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }
  
  
  /**
   * Gets imei.
   *
   * @return the imei
   */
  public String getIMEI() {
    tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    String result = initialVal;
    boolean hasReadPhoneStatePermission =
        BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE)
            == PackageManager.PERMISSION_GRANTED;
    try {
      if (hasReadPhoneStatePermission) result = tm.getDeviceId();
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets imsi.
   *
   * @return the imsi
   */
  public String getIMSI() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    boolean hasReadPhoneStatePermission =
        BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE)
            == PackageManager.PERMISSION_GRANTED;
    try {
      if (hasReadPhoneStatePermission) result = tm.getSubscriberId();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets serial.
   *
   * @return the serial
   */
  public String getSerial() {
    String result = initialVal;
    try {
      result = Build.SERIAL;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets sim serial.
   *
   * @return the sim serial
   */
  public String getSIMSerial() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      result = tm.getSimSerialNumber();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets gsfid.
   *
   * @return the gsfid
   */
  public String getGSFID() {
    final Uri URI = Uri.parse("content://com.google.android.gsf.gservices");
    final String ID_KEY = "android_id";

    String[] params = { ID_KEY };
    Cursor c = BA.applicationContext.getContentResolver().query(URI, null, null, params, null);

    if (c == null) {
      return initialVal;
    } else if (!c.moveToFirst() || c.getColumnCount() < 2) {
      c.close();
      return initialVal;
    }

    try {
      String gsfID = Long.toHexString(Long.parseLong(c.getString(1)));
      c.close();
      return gsfID;
    } catch (NumberFormatException e) {
      c.close();
      return initialVal;
    }
  }

  /**
   * Gets bluetooth mac.
   *
   * @return the bluetooth mac
   */
  @SuppressWarnings("MissingPermission") public String getBluetoothMAC() {
    String result = initialVal;
    try {
      if (BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.BLUETOOTH)
          == PackageManager.PERMISSION_GRANTED) {
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        result = bta.getAddress();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets psuedo unique id.
   *
   * @return the psuedo unique id
   */
  public String getPsuedoUniqueID() {
    // If all else fails, if the user does have lower than API 9 (lower
    // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
    // returns 'null', then simply the ID returned will be solely based
    // off their Android device information. This is where the collisions
    // can happen.
    // Try not to use DISPLAY, HOST or ID - these items could change.
    // If there are collisions, there will be overlapping data
    String devIDShort = "35" +
        (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      devIDShort += (Build.SUPPORTED_ABIS[0].length() % 10);
    } else {
      devIDShort += (Build.CPU_ABI.length() % 10);
    }

    devIDShort +=
        (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length()
            % 10) + (Build.PRODUCT.length() % 10);

    // Only devices with API >= 9 have android.os.Build.SERIAL
    // http://developer.android.com/reference/android/os/Build.html#SERIAL
    // If a user upgrades software or roots their phone, there will be a duplicate entry
    String serial;
    try {
      serial = Build.class.getField("SERIAL").get(null).toString();

      // Go ahead and return the serial for api => 9
      return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
    } catch (Exception e) {
      // String needs to be initialized
      serial = "ESYDV000"; // some value
    }

    // Finally, combine the values we have found by using the UUID class to create a unique identifier
    return new UUID(devIDShort.hashCode(), serial.hashCode()).toString();
  }  
  
  
  /**
   * Gets phone no.
   *
   * @return the phone no
   */
  public String getPhoneNo() {
    String result = initialVal;
	tm = (TelephonyManager) BA.applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
    try {
      if (tm.getLine1Number() != null) {
        result = tm.getLine1Number();
        if (result.equals("")) {
          result = initialVal;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets product.
   *
   * @return the product
   */
  public String getProduct() {
    String result = initialVal;
    try {
      result = Build.PRODUCT;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  /**
   * Gets fingerprint.
   *
   * @return the fingerprint
   */
  public String getFingerprint() {
    String result = initialVal;
    try {
      result = Build.FINGERPRINT;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets hardware.
   *
   * @return the hardware
   */
  public String getHardware() {
    String result = initialVal;
    try {
      result = Build.HARDWARE;
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets radio ver.
   *
   * @return the radio ver
   */
  public String getRadioVer() {
    String result = initialVal;
    try {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        result = Build.getRadioVersion();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets ip address.
   *
   * @param useIPv4 the use i pv 4
   * @return the ip address
   */
  public String getIPAddress(boolean useIPv4) {
    String result = initialVal;
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface intf : interfaces) {
        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
        for (InetAddress addr : addrs) {
          if (!addr.isLoopbackAddress()) {
            String sAddr = addr.getHostAddress().toUpperCase();
            boolean isIPv4 = addr instanceof Inet4Address;
            if (useIPv4) {
              if (isIPv4) result = sAddr;
            } else {
              if (!isIPv4) {
                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                result = delim < 0 ? sAddr : sAddr.substring(0, delim);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
  /**
   * Gets ua.
   *
   * @return the ua
   */
  public String getUA() {
    final String system_ua = System.getProperty("http.agent");
    String result = system_ua;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        result = new WebView(BA.applicationContext).getSettings().getDefaultUserAgent(BA.applicationContext) +
            "__" + system_ua;
      } else {
        result = new WebView(BA.applicationContext).getSettings().getUserAgentString() +
            "__" + system_ua;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() < 0 || result == null) {
      result = system_ua;
    }
    return result;
  }

  /**
   * Get lat long double [ ].
   *
   * @return the double [ ]
   */
  @SuppressWarnings("MissingPermission") @TargetApi(Build.VERSION_CODES.M)
  public double[] getLatLong() {
    boolean hasFineLocationPermission =
        BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED ? true : false;
    boolean isGPSEnabled, isNetworkEnabled;

    double[] gps = new double[2];
    gps[0] = 0;
    gps[1] = 0;
    if (hasFineLocationPermission) {
      try {
        LocationManager lm = (LocationManager) BA.applicationContext.getSystemService(Context.LOCATION_SERVICE);

        isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, final_loc = null;

        if (isGPSEnabled) {
          gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (isNetworkEnabled) {
          net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (gps_loc != null && net_loc != null) {
          if (gps_loc.getAccuracy() >= net_loc.getAccuracy()) {
            final_loc = gps_loc;
          } else {
            final_loc = net_loc;
          }
        } else {
          if (gps_loc != null) {
            final_loc = gps_loc;
          } else if (net_loc != null) {
            final_loc = net_loc;
          } else {
            // GPS and Network both are null so try passive
            final_loc = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
          }
        }

        if (final_loc != null) {
          gps[0] = final_loc.getLatitude();
          gps[1] = final_loc.getLongitude();
        }

        return gps;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return gps;
  }  
  
  
  /**
   * Gets time.
   *
   * @return the time
   */
  public long getTime() {
    return System.currentTimeMillis();
  }

  /**
   * Gets formatted time.
   *
   * @return the formatted time
   */
  public String getFormatedTime() {

    long millis = System.currentTimeMillis();
    int sec = (int) (millis / 1000) % 60;
    int min = (int) ((millis / (1000 * 60)) % 60);
    int hr = (int) ((millis / (1000 * 60 * 60)) % 24);

    return String.format("%02d:%02d:%02d", hr, min, sec);
  }  
  
  
  /**
   * Gets app name.
   *
   * @return the app name
   */
  public String getAppName() {
    String result;
    final PackageManager pm = BA.applicationContext.getPackageManager();
    ApplicationInfo ai;
    try {
      ai = pm.getApplicationInfo(BA.applicationContext.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      ai = null;
      e.printStackTrace();
    }
    result = (String) (ai != null ? pm.getApplicationLabel(ai) : initialVal);
    return result;
  }

  /**
   * Gets app version.
   *
   * @return the app version
   */
  public String getAppVersion() {
    String result = initialVal;
    try {
      result = BA.applicationContext.getPackageManager().getPackageInfo(BA.applicationContext.getPackageName(), 0).versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  /**
   * Gets app version code.
   *
   * @return the app version code
   */
  public String getAppVersionCode() {
    String result = initialVal;
    try {
      result = String.valueOf(
          BA.applicationContext.getPackageManager().getPackageInfo(BA.applicationContext.getPackageName(), 0).versionCode);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets activity name.
   *
   * @return the activity name
   */
  public String getActivityName() {
    String result = initialVal;
    try {
      result = BA.applicationContext.getClass().getSimpleName();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.length() == 0) {
      result = initialVal;
    }
    return result;
  }  
  
  
   /**
   * Gets package name.
   *
   * @return the package name
   */
  public String getPackageName() {
    String result = initialVal;
    try {
      result = BA.applicationContext.getPackageName();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  }

  /**
   * Gets store.
   *
   * @return the store
   */
  public String getStore() {
    String result = initialVal;
    if (Build.VERSION.SDK_INT >= 3) {
      try {
        result = BA.applicationContext.getPackageManager().getInstallerPackageName(BA.applicationContext.getPackageName());
      } catch (Exception e) {
        //Log.i(LOGTAG, "Can't get Installer package");
      }
    }
    if (result == null || result.length() == 0) {
      result = initialVal;
    }
    return result;
  } 
  
  /**
   * Gets density.
   *
   * @return the density
   */
  public String getDensity() {
    String densityStr = initialVal;
    final int density = BA.applicationContext.getResources().getDisplayMetrics().densityDpi;
    switch (density) {
      case DisplayMetrics.DENSITY_LOW:
        densityStr = "LDPI";
        break;
      case DisplayMetrics.DENSITY_MEDIUM:
        densityStr = "MDPI";
        break;
      case DisplayMetrics.DENSITY_TV:
        densityStr = "TVDPI";
        break;
      case DisplayMetrics.DENSITY_HIGH:
        densityStr = "HDPI";
        break;
      case DisplayMetrics.DENSITY_XHIGH:
        densityStr = "XHDPI";
        break;
      case DisplayMetrics.DENSITY_400:
        densityStr = "XMHDPI";
        break;
      case DisplayMetrics.DENSITY_XXHIGH:
        densityStr = "XXHDPI";
        break;
      case DisplayMetrics.DENSITY_XXXHIGH:
        densityStr = "XXXHDPI";
        break;
    }
    return densityStr;
  }  
  
  /**
   * Get accounts string [ ].
   *
   * @return the string [ ]
   */
  @SuppressWarnings("MissingPermission") public String[] getAccounts() {
    try {

      if (BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.GET_ACCOUNTS)
          == PackageManager.PERMISSION_GRANTED) {
        Account[] accounts = AccountManager.get(BA.applicationContext).getAccountsByType("com.google");
        String[] result = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
          result[i] = accounts[i].name;
        }
        return result;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }  
  

  /**
   * Is network available boolean.
   *
   * @return the boolean
   */
  public boolean isNetworkAvailable() {
    if (BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
        == PackageManager.PERMISSION_GRANTED
        && BA.applicationContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        == PackageManager.PERMISSION_GRANTED) {
      ConnectivityManager cm = (ConnectivityManager) BA.applicationContext.getApplicationContext()
          .getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = cm.getActiveNetworkInfo();
      return netInfo != null && netInfo.isConnected();
    }
    return false;
  }

  /**
   * Is running on emulator boolean.
   *
   * @return the boolean
   */
  public static boolean isRunningOnEmulator() {
    return Build.BRAND.contains("generic")
        || Build.DEVICE.contains("generic")
        || Build.PRODUCT.contains("sdk")
        || Build.HARDWARE.contains("goldfish")
        || Build.MANUFACTURER.contains("Genymotion")
        || Build.PRODUCT.contains("vbox86p")
        || Build.DEVICE.contains("vbox86p")
        || Build.HARDWARE.contains("vbox86");
  }  
  


  /**
   * Is wifi enabled
   *
   * @return the boolean
   */
  public boolean isWifiEnabled() {
    boolean wifiState = false;

    WifiManager wifiManager = (WifiManager) BA.applicationContext.getSystemService(Context.WIFI_SERVICE);
    if (wifiManager != null) {
      wifiState = wifiManager.isWifiEnabled() ? true : false;
    }
    return wifiState;
  }

  /**
   * Gets Device Ringer Mode
   *
   * @return Device Ringer Mode
   */
  public int getDeviceRingerMode() {
    int ringerMode = RINGER_MODE_NORMAL;
    AudioManager audioManager = (AudioManager) BA.applicationContext.getSystemService(Context.AUDIO_SERVICE);
    switch (audioManager.getRingerMode()) {
      case AudioManager.RINGER_MODE_NORMAL:
        ringerMode = RINGER_MODE_NORMAL;
        break;
      case AudioManager.RINGER_MODE_SILENT:
        ringerMode = RINGER_MODE_SILENT;
        break;
      case AudioManager.RINGER_MODE_VIBRATE:
        ringerMode = RINGER_MODE_VIBRATE;
    }
    return ringerMode;
  }


  private Intent getBatteryStatusIntent() {
    IntentFilter batFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    return BA.applicationContext.registerReceiver(null, batFilter);
  }


  private String handleIllegalCharacterInResult(String result) {
    if (result.indexOf(" ") > 0) {
      result = result.replaceAll(" ", "_");
    }
    return result;
  }

  /**
   * Gets app cache
   *
   * @return Installed app cache
   */
 





#End If
' File cache = getCacheDir();
'        File appDir = new File(cache.getParent());
'        If (appDir.exists()) {
'            String[] children = appDir.list();
'            For (String s : children) {
'                If (!s.equals("lib")) {
'                    deleteDir(new File(appDir, s));
'                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
'                }
'            }
'        }