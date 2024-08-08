; debug_window.bb
; by Chaduke
; 20240724 


Type DebugWindow
	Field gui.GUIWindow
	Field playerx.Widget
	Field playery.Widget
	Field playerz.Widget
End Type 

Function CreateDebugWindow.DebugWindow(g.GameApp)
	Local d.DebugWindow = New DebugWindow
		gui = CreateGUIWindow(g\gui_font,)
	Return d
End Function 