; gui.bb
; by Chaduke 
; 20240525

; a simple gui system for Blitz3D 
; using the LibSGD 2D overlay

; a custom type for an individual GUI "Window"

Global debug = False

; TEST GUI
Const numspheres = 10 : Const step_value = 360 / numspheres 

; Include "../engine/math2d.bb" : GUITest3D() : End
; Include "../engine/math2d.bb" : GUITest2D() : End 

Type GUIWindow
	Field id
	Field title$
	Field l# ; left
	Field t# ; top 
	Field r# ; right
	Field b# ; bottom	
	Field v ; visible flag
	Field s ; selected flag	
	Field dragged 	
	Field font 	
	Field fonty
	Field vs ; vertical spacing 
	Field drag_start_x
	Field drag_start_y	
End Type

Type Widget
	Field gui_id ; what gui window it belongs to 
	Field title$	
	Field widget_type ; 0=float 1=int 2=string
	Field val_string$	
	Field val_float#
	Field textboxwidth; textbox width	
	Field dragged 
	Field drag_start_x#
	Field val_start#
	Field val_low# 
	Field val_high# 
	Field clicked 
	Field click_handled
End Type 

Function ShowHideGUIWindow(g.GUIWindow)
	If g\v = False Then 
		g\v = True 
	Else 
		g\v = False
	End If 	
End Function 

Function GUISizeAdjust(g.GUIWindow)
	Local th = g\vs + g\vs / 2
	For w.Widget = Each Widget
		If w\gui_id = g\id Then 
			Local ww# = w\textboxwidth + GetTextWidth(g\font,w\title$) + 15
			Local tw# = GetTextWidth(g\font,w\title$) + 15 + GetTextWidth(g\font,w\val_string$)
			If tw > ww Then ww = tw
			If g\r - g\l < ww Then g\r = g\l + ww	
			th = th + g\vs
		End If 		
	Next	
	If th > g\b - g\t Then g\b = g\t + th		
End Function 

Function AddWidget.Widget(g.GUIWindow,title$="New Value",widget_type=0,value$="0.0",vl#=-180,vh#=180,tbw=50)
	Local w.Widget = New Widget
	w\gui_id = g\id
	w\title$ = title$
	w\widget_type = widget_type
	w\val_float# = Float(value$)
	w\val_string$ = value$	
	w\val_low = vl
	w\val_high = vh
	w\textboxwidth = tbw
	GUISizeAdjust g
	Return w
End Function 

Function GetNewGUIID()
	Local g.GUIWindow = First GUIWindow
	If g = Null Then Return 0	
	Local r=0
	Local loop=True 
	While loop
		r = r + 1
		g = After g
		If g = Null Then loop = False
	Wend 
	Return r
End Function 

Function CreateGUI.GUIWindow(font,title$="New GUI Window",posx=0,posy=0,fonty=0)
	Local g.GUIWindow = New GUIWindow
	g\id = GetNewGUIID()
	g\title$ = title$	
	g\l = posx 
	g\t = posy
	g\r = posx + GetTextWidth(font,title$) + 30
	g\b = posy + GetFontHeight(font) + 6
	g\v = True
	g\s = False
	g\font = font
	g\fonty = fonty
	g\vs = GetFontHeight(font) + 6 - g\fonty ; vertical spacing
	Return g
End Function

Function DrawGUI(g.GUIWindow)
	Local alpha# = 1.0
	Local fh# = GetFontHeight(g\font)	
	If g\v Then 
		; draw the main rect	
		Set2DFillColor 0.1,0.2,0.4,alpha#
		Set2DOutlineColor 0.3,0.5,6,alpha#
		Draw2DRect g\l,g\t,g\r,g\b
				
		; draw a rect for the title bar
		; if selected change the fill color
		Set2DFillColor 0.13,0.24,0.24,alpha#
		Draw2DRect g\l,g\t,g\r,g\t + g\vs
		
		; draw the window title text
		Set2DFont g\font
		Set2DTextColor 1,1,0,alpha#		
		Draw2DText g\title$, g\l + 4, g\t + 4
				
		; draw the gui id	
		Draw2DText g\id,g\r - GetTextWidth(g\font,g\id) - 4,g\t + 4
		
		; draw the GUI Widgets 
		yoffset = 0		
		For w.Widget = Each Widget
			If w\gui_id = g\id Then 
				; draw the widget				 
				Local wl# = g\l + 4 ; widget left side 
				; size of gui header = vs + 4, vertical widget spacing = font height + 4
				Local wt# = g\t + g\vs + 4 + yoffset * g\vs ; widget top
				Local wr# = g\r
				Local wb# = wt# + g\vs				
				If w\widget_type < 2 Then	
					Set2DFillColor 0.1,0.2,0.3,alpha#						
					; draw the textbox 					
					Draw2DRect wl,wt,wl + w\textboxwidth,wt + g\vs
					Set2DTextColor 1,1,1,alpha#	
					; draw the value 
					If w\widget_type = 0 Then Draw2DText w\val_float#,wl + 4,wt + 4 
					If w\widget_type = 1 Then Draw2DText Int(w\val_float#),wl + 4,wt + 4 										
					; draw the title 
					Draw2DText w\title$,wl + w\textboxwidth + 6,wt + 5
				Else 
					Set2DTextColor 0,1,0,alpha#
					Draw2DText w\title$ + " " + w\val_string$,wl,wt + 4
				End If	
				yoffset = yoffset + 1
			End If
		Next		
	End If	
End Function

Function GUIMouseDraggedAdjust(g.GUIWindow)
	Local gw = g\r - g\l 
	g\l = GetMouseX() - g\	drag_start_x
	g\r = g\l + gw
	Local gh = g\b - g\t
	g\t = GetMouseY() - g\drag_start_y
	g\b = g\t + gh
End Function 

Function GUIDroppedBoundsCheck(g.GUIWindow)
	Local gw = g\r - g\l 
	Local gh = g\b - g\t
	
	If g\l < 0 Then 
		g\l = 0
		g\r = g\l + gw
	End If
	
	If g\r > GetWindowWidth() Then 
		g\r = GetWindowWidth()
		g\l = g\r - gw
	End If 

	If g\t < 0 Then 
		g\t = 0
		g\b = g\t + gh
	End If 	
	
	If g\b > GetWindowHeight() Then 
		g\b = GetWindowHeight()
		g\t = g\b - gh
	End If	
End Function 

Function GUIDroppedCollisionCheck(g.GUIWindow)
	For og.GUIWindow = Each GUIWindow 
		If (og <> g And RectsCollided(og\l,og\t,og\r,og\b,g\l,g\t,g\r,g\b)) Then 
			Local c = GetCollisionType(og\l,og\t,og\r,og\b,g\l,g\t,g\r,g\b)	
			Local gw# = g\r - g\l	
			Local gh# = g\b - g\t 
			If c = 1 Then 
				local d# = g\r - og\l				 
				g\l = g\l - d 
				g\r = g\l + gw
			End If 	
			If c = 2 Then 
				d = g\b - og\t
				g\t = g\t - d
				g\b = g\t + gh
			End If 				
			If c = 3 Then 
				d = og\r - g\l
				g\l = g\l + d
				g\r = g\l + gw				
			End If 	
			If c = 4 Then 
				d = og\b - g\t
				g\t = g\t + d
				g\b = g\t + gh								
			End If					
		End If					
	Next 	
	GUIDroppedBoundsCheck g
End Function 

Function GUIHoverCheck()
	; check if the mouse is over a GUI
	For g.GUIWindow = Each GUIWindow	
		Set2DFont g\font 	
		If MouseCollidedRect(g\l,g\t,g\r,g\b) Then 
			Draw2DText "Mouse is over GUI ID - " + g\id + " - " + g\title$,g\l,g\b + 2
			If MouseCollidedRect(g\l,g\t,g\r,g\t + g\vs) Then 
				Draw2DText "Mouse is over the title bar",g\l,g\b + g\vs
			Else 
				; mouse is over a widget 										
				yoffset = 0
				For w.Widget = Each Widget	
					If w\gui_id = g\id Then 
						; widget belongs to this gui
						Local wl# = g\l + 2 ; widget left side 					
						Local wt# = g\t + g\vs + yoffset * g\vs ; widget top
						Local wr# = g\r
						Local wb# = wt# + g\vs		
						; check if mouse is over this widget, if so report info 		
						If MouseCollidedRect(wl,wt,wr,wb) Then 
							Select w\widget_type
								Case 0
									wtp$ = "Float"
								Case 1 
									wtp$ = "Int"
								Case 2 
									wtp$ = "String"
							End Select						
							Draw2DText "Mouse is over widget - Title : " + w\title$ + " - Type : " + wtp$,g\l,g\b + g\vs							
						End If
						yoffset = yoffset + 1	; increment for each widget on the current GUI 
					End If 				
				Next 
			End If
		End If 	
	Next		
End Function 

Function GUIDraggedCheck()
	Local r = False 
	For g.GUIWindow = Each GUIWindow
		If g\dragged Then r = True 
	Next 	
	Return r
End Function 

Function WidgetDraggedCheck()
	Local r = False 
	For w.Widget = Each Widget 
		If w\dragged Then r = True		
	Next
	Return r 
End Function 

Function ClearAllDragged()
	For g.GUIWindow = Each GUIWindow
		If g\dragged Then 
			GUIDroppedCollisionCheck g
			g\dragged = False
		End If 	 
	Next 	
	For w.Widget = Each Widget 
		w\dragged = False 	
		w\clicked = False 
	Next
End Function 

Function ClearClickedWidgets()
	For w.Widget = Each Widget 			
		w\clicked = False 
	Next
End Function 

Function GUIDragCheck()
	; are we already dragging something		
	Local gdc = GUIDraggedCheck()
	Local wdc = WidgetDraggedCheck()
	If (IsMouseButtonDown(0)) Then
		If (gdc = False And wdc = False) Then 
			; nothing is being dragged 
			; loop through the guis 
			For g.GUIWindow = Each GUIWindow 
				; check if we are over the current GUI 
				If (MouseCollidedRect(g\l,g\t,g\r,g\b) And g\v) Then 
					; we are over the current GUI
					; check if we are over the title bar
					If MouseCollidedRect(g\l,g\t,g\r,g\t+g\vs) Then
						; we are over the title bar, mouse is down 
						; we should be dragging this GUI 
						g\dragged = True 
						g\drag_start_x = GetMouseX() - g\l
						g\drag_start_y = GetMouseY() - g\t 
					Else 
						; we are over a widget 
						; loop though the widgets and find the one
						yoffset = 0 ; adjust for each widget on this gui 
						For w.Widget = Each Widget 
							; make sure the widget belongs to the GUI first 
							If w\gui_id = g\id Then 
								; this one is part of the current GUI 
								; check if mouse is over it 
								Local wl# = g\l + 2 ; widget left side 					
								Local wt# = g\t + g\vs + yoffset * g\vs ; widget top
								Local wr# = g\r
								Local wb# = wt# + g\vs		
								If MouseCollidedRect(wl,wt,wr,wb) Then 
									; we are over this widget, set it to dragged			
									If w\widget_type < 2 Then
										w\dragged = True 
										w\drag_start_x = GetMouseX()										
										w\val_start# = w\val_float#
									Else 
										; it's a text type, treat it as clicked 
										If w\clicked = False Then
											w\clicked = True 
											w\click_handled = False											
										End If 	
									End If 	
								End If 	
								yoffset = yoffset + 1 ; increment for the next one 
							End If
						Next 
					End If 						
				End If					
			Next 								
		Else If gdc Then 
			If debug Then Draw2DText "A GUI is being dragged",10,GetWindowHeight() - 50
			; handle dragging the GUI	
			; find the gui thats being dragged 
			For g.GUIWindow = Each GUIWindow 
				If g\dragged Then GUIMouseDraggedAdjust g 
			Next 							 	
		Else If wdc Then 
			If debug Then Draw2DText "A Widget is being dragged",10,GetWindowHeight() - 50	
			; handle dragging the widget 
			For w.Widget = Each Widget 
				If w\dragged Then
					If w\widget_type = 0 Then 
						v# = ((GetMouseX() - w\drag_start_x) * 0.01) + w\val_start#	
					Else If w\widget_type = 1 Then 
						v# = (GetMouseX() - w\drag_start_x) + w\val_start#	
					End If 		
					If v > w\val_high Then v = w\val_high 
					If v < w\val_low Then v = w\val_low
					w\val_float# = v							
				End If 
			Next 	
		End If	
	Else 
		; mouse is not pressed 
		If (gdc Or wdc) Then 
			ClearAllDragged()	
			gdc = False
			wdc = False
		End If 
		ClearClickedWidgets()						
	End If 
	If (gdc Or wdc) Then 
		Return True 
	Else
		Return False
	End If 		
End Function 

Function DrawAllGUIs()
	Set2DOutlineWidth 1
	Set2DLineWidth 1
	Set2DOutlineEnabled True
	Set2DFillEnabled True	

	For g.GUIWindow = Each GUIWindow
		If g\v Then DrawGUI g
	Next 		
End Function 

Type TestShape 
	Field name$
	; 0 = oval, 1 = rect  
	Field shape_type 
	Field posx# 
	Field posy# 
	Field width# 
	Field height# 
End Type 

Function DrawAllTestShapes()
	Set2DFillColor 1,1,1,1
	For t.TestShape = Each TestShape 
		If t\shape_type = 0 Then 
			Draw2DOval t\posx,t\posy,t\posx+t\width,t\posy+t\height
		Else If t\shape_type = 1 Then 
			Draw2DRect t\posx,t\posy,t\posx+t\width,t\posy+t\height
		End If 
	Next 		
End Function 

Function GUITest2D()	
	 
	CreateWindow 1280,720,"GUI Test 2D",0
	
	font1 = LoadFont("c:\windows\fonts\CascadiaMono.ttf",14)
	font2 = LoadFont("c:\windows\fonts\consola.ttf",22)
	
	; creation parameters are :
	; font, title, posx, posy, fonty
	; some fonts require an adjustment of vertical spacing 
	; which is the last parameter called fonty in this case -3
	; if it looks like your font is being displayed too low 
	; set fonty to something negative	
	; width and height of the gui are automatically 
	; adjusted to fit the widgets as they are added
	
	ovals.GUIWindow = CreateGUI(font1,"Ovals",200,10,-3)
	
	; widget creation parameters are :
	; gui, title, type, value, value_low,value_height,textboxwidth
	; types are 0=float 1=int 2=string
	
	ovals_name_label.Widget = AddWidget(ovals,"Oval Name",2,"")
	
	ovals_location_label.Widget = AddWidget(ovals,"Location",2,"")
	ovals_x.Widget = AddWidget(ovals,"X",1,100,0,GetWindowWidth())
	ovals_y.Widget = AddWidget(ovals,"Y",1,100,0,GetWindowHeight())	
	
	ovals_size_label.Widget = AddWidget(ovals,"Size",2,"")	
	
	ovals_width.Widget = AddWidget(ovals,"Width",1,10,2,200)
	ovals_height.Widget = AddWidget(ovals,"Height",1,10,2,200)	
	
	ovals_generate.Widget = AddWidget(ovals,"Generate",2,"")
	ovals_random.Widget = AddWidget(ovals,"Random",2,"")	
			
	rects.GUIWindow = CreateGUI(font1,"Rects",400,10,-3)
	
	rects_location_label.Widget = AddWidget(rects,"Location",2,"")
	rects_x.Widget = AddWidget(rects,"X",1,0,0,GetWindowWidth())
	rects_y.Widget = AddWidget(rects,"Y",1,0,0,GetWindowHeight())	
	
	rects_size_label.Widget = AddWidget(rects,"Size",2,"")	
	
	rects_width.Widget = AddWidget(rects,"Width",1,10,2,200)
	rects_height.Widget = AddWidget(rects,"Height",1,10,2,200)	
	
	rects_test.Widget = AddWidget(rects,"Test",0,0.02,0.01,0.05)
	
	rects_generate.Widget = AddWidget(rects,"Generate",2,"")
	rects_random.Widget = AddWidget(rects,"Random",2,"")	
		
	loop = True
	shape_count = 0
	While loop 
		e = PollEvents()
		If (e = 1 Or IsKeyHit(256)) Then loop = False
		RenderScene()
		Clear2D()
		If (ovals_generate\clicked And ovals_generate\click_handled = False) Then 
			; handle the click 
			; generate an oval 
			t.TestShape = New TestShape
			t\name$ = "Shape #" + shape_count
			t\shape_type = 0
			t\posx = ovals_x\val_float
			t\posy = ovals_y\val_float
			t\width = ovals_width\val_float
			t\height = ovals_height\val_float			
			ovals_generate\click_handled = True 	
			shape_count = shape_count + 1		
		End If
		
		If (ovals_random\clicked And ovals_random\click_handled = False) Then 			
			t.TestShape = New TestShape
			t\name$ = "Random Shape #" + shape_count
			t\shape_type = 0
			ovals_x\val_float = Rand(ovals_x\val_low,ovals_x\val_high)
			t\posx = ovals_x\val_float
			ovals_y\val_float = Rand(ovals_y\val_low,ovals_y\val_high)
			t\posy = ovals_y\val_float
			ovals_width\val_float = Rand(ovals_width\val_low,ovals_width\val_high)
			t\width = ovals_width\val_float
			ovals_height\val_float = Rand(ovals_height\val_low,ovals_height\val_high)
			t\height = ovals_height\val_float			
			ovals_random\click_handled = True 	
			shape_count = shape_count + 1		
		End If
		
		If (rects_generate\clicked And rects_generate\click_handled = False) Then 
			; handle the click 
			; generate rect
			t.TestShape = New TestShape
			t\name$ = "Shape #" + shape_count
			t\shape_type = 1
			t\posx = rects_x\val_float#
			t\posy = rects_y\val_float#
			t\width = rects_width\val_float#
			t\height = rects_height\val_float#			
			rects_generate\click_handled = True 	
			shape_count = shape_count + 1		
		End If
		
		If (rects_random\clicked And rects_random\click_handled = False) Then 			
			t.TestShape = New TestShape
			t\name$ = "Random Shape #" + shape_count
			t\shape_type = 1
			rects_x\val_float = Rand(rects_x\val_low,rects_x\val_high)
			t\posx = rects_x\val_float
			rects_y\val_float = Rand(rects_y\val_low,rects_y\val_high)
			t\posy = rects_y\val_float
			rects_width\val_float = Rand(rects_width\val_low,rects_width\val_high)
			t\width = rects_width\val_float
			rects_height\val_float = Rand(rects_height\val_low,rects_height\val_high)
			t\height = rects_height\val_float			
			rects_random\click_handled = True 	
			shape_count = shape_count + 1		
		End If
		
		DrawAllTestShapes()		
		DrawAllGUIs()
		If debug Then GUIHoverCheck()
		gdc = GUIDragCheck()
		; Draw2DText "GUI Drag Check " + gdc,10,GetWindowHeight()-100
		If gdc = False Then 
			
		End If	
		Draw2DText "Shape Count : " + shape_count,5,5	
		Present()
	Wend	
End Function 

Function GUITest3D()
	; CreateWindow 1366,768,"GUI Tests",6
	CreateWindow 1920,1080,"GUI Tests",1
		
	SetCSMTextureSize 2048
	SetCSMSplitDistances 8,32,64,128	
	
	camera = CreatePerspectiveCamera()
	MoveEntity camera,0,1,0
	
	sunlight = CreateDirectionalLight()
	TurnEntity sunlight,-25,0,0
	SetLightShadowsEnabled sunlight,True 	
	
	font = LoadFont("c:\windows\fonts\consola.ttf",18)

	; create sky environment 
	sky_texture = LoadCubeTexture("sgd://envmaps/sunnysky-cube.png",4,56)
	SetEnvTexture sky_texture
	skybox = CreateSkybox(sky_texture)
	SetSkyboxRoughness skybox,0.2	
	
	; create ground 
	ground_material = LoadPBRMaterial("sgd://materials/Ground037_1K-JPG")	
	ground_mesh = CreateBoxMesh(-64,-0.1,-64,64,0,64,ground_material)
	TFormMeshTexCoords ground_mesh,64,64,0,0
	ground_model = CreateModel(ground_mesh)
	
	; create a circle of spheres 	
	radius = 10 ; circle radius 
	sphere_material = LoadPBRMaterial("sgd://materials/Fabric048_1K-JPG")	
	sphere_mesh = CreateSphereMesh(1,32,32,sphere_material)
	SetMeshShadowsEnabled sphere_mesh,True 
	
	For angle = 0 To 360 Step 36
		sphere_model = CreateModel(sphere_mesh)
		TurnEntity sphere_model,0,angle,0
		MoveEntity sphere_model,0,1,radius
	Next 	
	
	env.GUIWindow = CreateGUI(font,"Environment Settings",10,10,-3)
		
	; widget creation parameters are :
	; gui, title, type, val_string$, val_low,val_high,textboxwidth
	; types are 0=float 1=int 2=string
	
	env_sun_label.Widget = AddWidget(env,"Location",2,"")	
	env_sun_rotx.Widget = AddWidget(env,"Rotation X",0,GetEntityRX(sunlight),-180,180,70)
	env_sun_roty.Widget = AddWidget(env,"Rotation Y",0,GetEntityRY(sunlight),-180,180,70)
		
	Set2DOutlineWidth 1
	Set2DLineWidth 1
	Set2DOutlineEnabled True
	Set2DFillEnabled True
	
	loop = True 
	While loop 
		e = PollEvents()
		If e = 1 Then loop = False
		If IsKeyHit(256) Then loop = False	
		If IsKeyHit(290) Then If env\v = False Then env\v = True Else env\v = False
		
		If debug Then GUIHoverCheck()	
		If GUIDragCheck()=False  Then 
			If IsKeyDown(87) Then MoveEntity camera,0,0,0.2
			If IsKeyDown(83) Then MoveEntity camera,0,0,-0.2
			If IsKeyDown(65) Then TurnEntity camera,0,2,0
			If IsKeyDown(68) Then TurnEntity camera,0,-2,0			
		End If		
		DrawAllGUIs()
		SetEntityRotation sunlight,env_sun_rotx\val_float#,env_sun_roty\val_float#,0		
		RenderScene()	
		Clear2D()			
		
		Draw2DText GetFPS(),5,GetWindowHeight() - 24	
		Present()
	Wend 
End Function