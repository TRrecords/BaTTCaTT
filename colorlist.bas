Type=StaticCode
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules. 
	Private MColor As MaterialColors
	Dim gcf,c,c1,c2,c3 As Int
	Dim clist As List
End Sub


Sub color_id(cid As MaterialColors) As Int
	
	clist.Initialize
	clist=cid
	Return color_id(cid) 
End Sub 