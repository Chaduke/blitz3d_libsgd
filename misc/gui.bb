; gui.bb
; by Chaduke 
; 20240525

; a simple gui system for Blitz3D 
; using the LibSGD 2D overlay

Include "../platformer/noise.bb"
Include "../platformer/vec3.bb"
Include "../platformer/terrain.bb"
Include "../platformer/navigation.bb"
Include "../platformer/static_objects.bb"
Include "../platformer/trees.bb"

; key constants 
Const KEY_ESCAPE = 256
Const KEY_F1 = 290

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
End Type

Type Widget
	Field gui_id ; what gui window it belongs to 
	Field title$	
	Field widget_type ; 0=float 1=int 2=string
	Field value$	
End Type 

Global sunlight 

TestGUI()
End

Function AddWidget(gui_id=1,title$="New Value",widget_type=0,value$="0.0")
	w.Widget = New Widget
	w\gui_id = gui_id
	w\title$ = title$
	w\widget_type = widget_type
	If widget_type = 1 Then w\value$ = Int(value$) Else w\value$ = value$	
End Function 

Function SetWidgetValue(title$,value$)
	For w.Widget = Each Widget
		If w\title$ = title$ Then w\value$ = value$
	Next	
End Function 

Function GetNewGUIID()
	Local g.GUI = First GUI
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

Function CreateGUI.GUI(title$="New GUI Window",width#=320,height#=240,posx#=0,posy#=0,visible=True,selected=False)
	Local g.GUI = New GUI
	g\id = GetNewGUIID()
	g\title$ = title$
	g\l = posx 
	g\t = posy
	g\r = posx + width
	g\b = posy + height	
	g\v = visible
	g\s = selected	
	Return g
End Function

Function DrawGUI(g.GUI)
	Local alpha# = 1.0
	If g\v Then 
		; draw the main rect	
		Set2DFillColor 0.1,0.2,0.4,alpha#
		Set2DOutlineColor 0.3,0.5,6,alpha#
		Draw2DRect g\l,g\t,g\r,g\b		
		; draw a rect for the title bar
		; if selected change the fill color
		Set2DFillColor 0.13,0.24,0.24,alpha#
		Draw2DRect g\l,g\t,g\r,g\t + 30
		; draw the window title text
		Set2DTextColor 1,1,0,alpha#
		; Local tw# = Get2DTextWidth(g\title$) / 2 ; text width / 2
		; Local gw# = (g\r - g\l) / 2 ; gui width / 2
		; Local c# = gw + g\l ; center
		
		Draw2DText g\title$, g\l + 5, g\t + 8 
		; draw the gui id in the upper left corner		
		; Draw2DText g\id,g\l + 2,g\t + 2 
		; draw the GUI Widgets 
		yoffset = 0		
		For w.Widget = Each Widget
			If w\gui_id = g\id Then 
				; draw the widget				
				If w\widget_type < 2 Then	
					Set2DFillColor 0.1,0.1,0.1,alpha#		
					Draw2DRect g\l + 2,g\t + 32 + yoffset*22,g\l + 70,g\t + 32 + yoffset*22 + 22	
					Set2DTextColor 1,1,1,alpha#	
					Draw2DText w\value$,g\l + 6,g\t + 36 + yoffset*22
					Draw2DText w\title$,g\l + 76,g\t + 36 + yoffset*22
				Else 
					Set2DTextColor 0,1,0,alpha#
					Draw2DText w\title$ + " " + w\value$,g\l + 2,g\t + 34 + yoffset*22	
				End If	
				yoffset = yoffset + 1
			End If
		Next		
	End If	
End Function 

Function MouseCollidedRect(l,t,r,b)
	Local c = False
	If (MouseX() > l And MouseX() < r And MouseY() > t And MouseY() < b) Then c = True
	Return c 	
End Function 

Function TestGUI()
	; CreateWindow 1366,768,"GUI Tests",6
	CreateWindow 1920,1080,"GUI Tests",1
	CreateScene()
	
	sunlight = CreateDirectionalLight()
	TurnEntity sunlight,-25,0,0
	
	; create sky environment 
	sky_texture = LoadTexture("../platformer/assets/images/skyboxsun25degtest.png",4,56)
	SetSceneEnvTexture sky_texture
	skybox = CreateSkybox(sky_texture)
	SetSkyboxRoughness skybox,0.2	
	
	t.Terrain = New Terrain
	t\material = LoadPBRMaterial("../platformer/assets/materials/Ground054_1K-JPG")
	t\texscale = 16
	t\width = 512
	t\height = 32
	t\depth = 512
	t\start_offset = 1111
	t\offset_inc = 0.03
	t\calc_normals = True
	CreateTerrain t	
	GenerateRockFence("../platformer/assets/models/rock.glb",t)
	GenerateTrees t,10000	
	camera = CreatePerspectiveCamera()
	PlaceEntityOnTerrain camera,t	
	
	myGUI.GUI = CreateGUI("Environment Settings",200,150,10,10)
	AddWidget myGUI\id,"Sunlight Angles",2,""
	AddWidget myGUI\id,"Rotation X",1,EntityRX(sunlight)
	AddWidget myGUI\id,"Rotation Y",1,EntityRY(sunlight)
	AddWidget myGUI\id,"Rotation Z",1,EntityRZ(sunlight)
	
	Set2DOutlineWidth 2
	Set2DLineWidth 1
	Set2DOutlineEnabled True
	Set2DFillEnabled True
	
	dragging = False
	gui_visible = True
	loop = True 
	While loop 
		e = PollEvents()
		If e = 1 Then loop = False
		If KeyHit(KEY_ESCAPE) Then loop = False	
		If KeyHit(KEY_F1) Then If gui_visible = False Then gui_visible = True Else gui_visible = False
			
		If dragging = False Then 
			NavigationMode(t)
		End If
		
		; left button down
		If MouseButtonDown(0) Then 
			If dragging = False Then 
				For g.GUI = Each GUI
					If MouseCollidedRect(g\l,g\t,g\r,g\t+30) Then 
						If g\s Then g\s = False Else g\s = True ; toggle selection 
						dragging = True
						dragged_widget$ = g\title$
					End If	
					If MouseCollidedRect(g\l,g\t+30,g\r,g\b) Then					
						; we are dragging inside the window
						; find the widget
						yoffset = 0		
						For w.Widget = Each Widget
							If w\gui_id = g\id Then									
								If w\widget_type < 2 Then								
									If MouseCollidedRect(g\l + 76,g\t + 36 + yoffset*22,g\l + 76 + Get2DTextWidth(w\title$),g\t + 36 + yoffset*22 + 22) Then 
										dragged_widget$ = w\title$
										dragging = True		
										oldx = MouseX()							
									End If								
								End If	
								yoffset = yoffset + 1
							End If
						Next					
					End If	
				Next
			Else 
				; we are dragging the GUI or a widget
				; Draw2DText "Currently Adjusting " + dragged_widget$,600,10	
				Select dragged_widget$
					Case "Rotation X" 
						newx = MouseX() - oldx
						newx = newx + EntityRX(sunlight)
						SetEntityRotation sunlight,newx,EntityRY(sunlight),EntityRZ(sunlight)
						oldx = MouseX()
						SetWidgetValue "Rotation X",newx
					Case "Rotation Y" 
						newx = MouseX() - oldx
						newx = newx + EntityRY(sunlight)
						SetEntityRotation sunlight,EntityRX(sunlight),newx,EntityRZ(sunlight)
						oldx = MouseX()
						SetWidgetValue "Rotation Y",newx	
					Case "Rotation Z" 
						newx = MouseX() - oldx
						newx = newx + EntityRZ(sunlight)
						SetEntityRotation sunlight,EntityRX(sunlight),EntityRY(sunlight),newx
						oldx = MouseX()
						SetWidgetValue "Rotation Z",newx							
				End Select							
			End If	
		Else 
			dragging = False
			For g.GUI = Each GUI
				g\s = False
			Next	
		End If
				
		RenderScene()	
		Clear2D()	
		If gui_visible = True Then
			For g.GUI = Each GUI
				If g\s Then
					Local ghw# = ((g\r - g\l) / 2) ; gui half width
					Local gh# = g\b - g\t ; gui height
					If (MouseX() - ghw > 0 And MouseX() + ghw < WindowWidth() And MouseY() - 7 > 0 And MouseY() - 7 + gh < WindowHeight()) Then 
						g\l = MouseX() - ghw			
						g\r = MouseX() + ghw			
						g\t = MouseY() - 7
						g\b = MouseY() - 7 + gh
					End If	
				End If				
				DrawGUI g
			Next 	
		End If 
		Draw2DText FPS(),5,WindowHeight() - 24	
		Present()
	Wend 
End Function