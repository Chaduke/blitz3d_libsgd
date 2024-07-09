; gui_with_palettes.bb
; by Chaduke 
; 20240525

; a simple gui system for Blitz3D 
; using the LibSGD 2D overlay

Include "palettes.bb"

Const LEFT_BRACKET = 91
Const RIGHT_BRACKET = 93
Const ESCAPE = 256


; a custom type for an individual GUI "Window"
Type GUI
	Field id
	Field title$
	Field l# ; left
	Field t# ; top 
	Field r# ; right
	Field b# ; bottom	
	Field v ; visible flag
	Field s ; selected flag
	Field text$
End Type

Type FloatWidget
	Field gui_id ; what gui window it belongs to 
	Field title$
	Field index ; where it appears on the gui window
	Field value#	
End Type 

Function CreateGUI.GUI(title$="New GUI Window",width#=320,height#=240,posx#=200,posy#=200,visible=True,selected=False)
	Local g.GUI = New GUI
	g\title$ = title$
	g\l = posx 
	g\t = posy
	g\r = posx + width
	g\b = posy + height	
	g\v = visible
	g\s = selected
	g\text$ = "This is a new GUI"
	Return g
End Function

Function DrawGUI(g.GUI,p.palette)
	If g\v Then 
		; draw the main rect	
		Set2DFillColorHex GetPaletteColor(p,2)
		Set2DOutlineColorHex GetPaletteColor(p,3)
		Draw2DRect g\l,g\t,g\r,g\b		
		; draw a rect for the title bar
		; if selected change the fill color
		If g\s Then Set2DFillColorHex GetPaletteColor(p,4)			
		Draw2DRect g\l,g\t,g\r,g\t + 30
		; draw the window title text
		Set2DTextColorHex GetPaletteColor(p,1)
		Local tw# = Get2DTextWidth(g\title$) / 2 ; text width / 2
		Local gw# = (g\r - g\l) / 2 ; gui width / 2
		Local c# = gw + g\l ; center
		Draw2DText g\title$, c - tw, g\t + 7 		
		; draw the GUI Text
		Draw2DText g\text$,g\l + 5, g\t + 35		
	End If	
End Function 

Function MouseCollidedRect(l,t,r,b)
	Local c = False
	If (MouseX() > l And MouseX() < r And MouseY() > t And MouseY() < b) Then c = True
	Return c 	
End Function 

CreateWindow 1366,768,"GUI Tests",6
CreateScene()

myGUI.GUI = CreateGUI()
p.palette = GetPalette("palette9")
SetPalette p
current_palette = 9

Set2DOutlineWidth 2
Set2DLineWidth 1
Set2DOutlineEnabled True
Set2DFillEnabled True

loop = True 
While loop 
	e = PollEvents()
	If e = 1 Then loop = False
	If KeyHit(ESCAPE) Then loop = False	
	
	If KeyHit(LEFT_BRACKET) 
		current_palette = current_palette - 1
		If current_palette < 1 Then current_palette = 9
		name$ = "palette" + current_palette
		p = GetPalette(name$)
		SetPalette p
	End If
	
	If KeyHit(RIGHT_BRACKET) 
		current_palette = current_palette + 1
		If current_palette > 9 Then current_palette = 1
		name$ = "palette" + current_palette
		p = GetPalette(name$)
		SetPalette p
	End If
	
	; left button hit
	If MouseButtonHit(0) Then 
		For g.GUI = Each GUI
			If MouseCollidedRect(g\l,g\t,g\r,g\t+30) Then 
				If g\s Then g\s = False Else g\s = True ; toggle selection 
			End If	
		Next
	End If
			
	RenderScene()
	Clear2D()
	Draw2DText "Use the Left and Right Bracket Keys to cycle through different color palettes", 10,10
	Draw2DText "Current Palette : " + name$,10,30
	For g.GUI = Each GUI
		If g\s Then
			Local ghw# = ((g\r - g\l) / 2) ; gui half width
			Local gh# = g\b - g\t ; gui height
			g\l = MouseX() - ghw
			g\r = MouseX() + ghw
			g\t = MouseY() - 7
			g\b = MouseY() - 7 + gh
		End If				
		DrawGUI g,p
	Next 	
	Present()
Wend 
End 