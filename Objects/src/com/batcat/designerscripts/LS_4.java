package com.batcat.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_4{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
views.get("panel1").vw.setLeft((int)(0d));
views.get("panel1").vw.setWidth((int)(0-1d - (0d)));
//BA.debugLineNum = 4;BA.debugLine="Panel1.SetTopAndBottom(0,-1)"[4/General script]
views.get("panel1").vw.setTop((int)(0d));
views.get("panel1").vw.setHeight((int)(0-1d - (0d)));
//BA.debugLineNum = 5;BA.debugLine="ListView1.SetLeftAndRight(0,-1)"[4/General script]
views.get("listview1").vw.setLeft((int)(0d));
views.get("listview1").vw.setWidth((int)(0-1d - (0d)));
//BA.debugLineNum = 6;BA.debugLine="ListView1.SetTopAndBottom(1,-1)"[4/General script]
views.get("listview1").vw.setTop((int)(1d));
views.get("listview1").vw.setHeight((int)(0-1d - (1d)));

}
}