package com.batcat.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_2{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="Panel1.SetLeftAndRight(1,-1)"[2/General script]
views.get("panel1").vw.setLeft((int)(1d));
views.get("panel1").vw.setWidth((int)(0-1d - (1d)));
//BA.debugLineNum = 3;BA.debugLine="ListView1.SetLeftAndRight(1,-1)"[2/General script]
views.get("listview1").vw.setLeft((int)(1d));
views.get("listview1").vw.setWidth((int)(0-1d - (1d)));

}
}