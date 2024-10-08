; gameapp.bb
; by Chaduke
; 20240703 
; a GameApp type to handle generic globals and functions at the very top level of most games

Include "../engine/constants.bb"
Include "../engine/vec3.bb"
Include "../engine/math2d.bb" 
Include "../engine/math3d.bb" 
Include "../engine/text_helpers.bb"
Include "../engine/start_menu.bb"

Type GameApp
	Field title$
	Field subtitle$
	Field debug ; false during normal gameplay
	Field ww ; window width
	Field wh ; window height
	Field game_state ; see constants.bb
	Field npcs ; enable / disable npcs
	Field loop	
	Field font
	Field gui_font	
	Field browser_font
	Field loading_screen
	Field camera
	Field pivot 
	Field editor_loaded
	Field options_loaded
	Field story_loaded 
	Field tutorial_loaded
	Field start_menu.StartMenu
	Field show_start_menu
	Field info$
End Type 

Function CreateGameApp.GameApp(title$="Game App",subtitle$="Programmer's",fullscreen=1,show_start_menu=True)
	Local g.GameApp = New GameApp
	g\debug = True
	g\ww = GetDesktopWidth()
	g\wh = GetDesktopHeight()
	g\title$ = title$
	g\subtitle$ = subtitle$
	If fullscreen = 0 Then 	
		CreateWindow 1280,720,g\subtitle$ + " " + g\title$,fullscreen
	Else 
		CreateWindow g\ww,g\wh,g\subtitle$ + " " + g\title$,fullscreen
	End If 
			
	g\loading_screen = LoadImage("../engine/assets/textures/backgrounds/background.png",1)
	g\game_state = GAME_STATE_STORY
	g\npcs = True 
	g\loop = True 	
	g\font = LoadFont("../engine/assets/fonts/ds.ttf",24)
	g\gui_font = LoadFont("../engine/assets/fonts/ds.ttf",16)	
	g\browser_font = LoadFont("../engine/assets/fonts/ds.ttf",14)	
	g\pivot = CreateModel(0)
	g\camera=CreatePerspectiveCamera()
	SetEntityParent g\camera,g\pivot	
	g\editor_loaded = False
	g\options_loaded = False
	g\story_loaded = False
	g\tutorial_loaded = False
	g\start_menu = CreateStartMenu()
	g\show_start_menu = show_start_menu
	g\info$ = "This is an informational message!"
	Return g
End Function 

Function BeginFrame(g.GameApp)	
	e = PollEvents()
	If e = 1 Then g\loop = False
	If IsKeyHit(KEY_ESCAPE) Then 		
		If g\game_state=GAME_STATE_QUIT Then 
			g\game_state=GAME_STATE_PREVIOUS 
		Else 
			GAME_STATE_PREVIOUS = g\game_state
			If g\show_start_menu Then g\game_state=GAME_STATE_MENU Else g\game_state=GAME_STATE_QUIT
		End If				
	End If 				
	If (IsKeyHit(KEY_Y) And g\game_state = GAME_STATE_QUIT) Then g\loop = False		
	If IsKeyHit(KEY_P) Then 
		If g\game_state=GAME_STATE_PAUSED Then 
			g\game_state=GAME_STATE_STORY 
		Else 
			If g\game_state = GAME_STATE_STORY Then g\game_state=GAME_STATE_PAUSED
		End If 	
	End If 		
	If g\game_state = GAME_STATE_MENU Then DisplayStartMenu g	 		
End Function

Function EndFrame(g.GameApp,call_present=True,call_clear2d=True,call_render=True)			
	If call_render Then RenderScene()	
	If call_clear2d Then Clear2D()	
	If g\game_state = GAME_STATE_QUIT Then 
		DrawTitle g,True
		Set2DTextColor 1,1,0,1
		DisplayTextCenter "Quit? Y to confirm | ESC to Cancel",g\font
	ElseIf g\game_state = GAME_STATE_PAUSED Then 		
		DisplayTextCenter "Game Paused (P to toggle)",g\font
	End If 	
	
	DisplayTextCenter g\info$,g\font,-GetWindowHeight() / 2
	
	If g\debug Then 
		Set2DTextColor 1,0,0,1 
		Set2DFont g\gui_font	
		msg$ = "FPS : " + Floor(GetFPS())
		; msg$ = msg$ + " RPS : " + GetRPS()
		msg$ = msg$ + " MouseZ " + GetMouseZ()
		Draw2DText msg$,5,GetWindowHeight() - 20
	End If 	
	If call_present Then Present()	
End Function

Function DrawTitle(g.GameApp,center_image=False)
	Local w = (GetWindowWidth() / 2)
	Local h = (GetWindowHeight() /2)
	
	If center_image = True Then 
		; Draw2DImage g\loading_screen,w,h,1	
	Else 
		; Draw2DImage g\loading_screen,0,0,1	
	End If 
			
	; Set2DTextColor 0.72,.24,.24,1
	Set2DTextColor .15,0.19,0.26,1
	DisplayTextCenter g\subtitle$,g\start_menu\subtitle_font,-282,-222
	DisplayTextCenter g\title$,g\start_menu\title_font,-253,-3	
	Set2DTextColor .90,.85,.19,1
	DisplayTextCenter g\subtitle$,g\start_menu\subtitle_font,-280,-220
	DisplayTextCenter g\title$,g\start_menu\title_font,-250,0	
End Function 

Function DisplayLoadingMessageWithTitle(msg$,g.GameApp)	
	Clear2D()	
	DrawTitle g	
	Set2DTextColor 1,1,0,1
	DisplayTextCenter msg$,g\font
	PollEvents()
	RenderScene()
	Present()
End Function

Function SimpleNavigation(g.GameApp)
	Local e = g\pivot
	Local camspeed# = 0.2
	Local move_speed# = 0.02
	Local turn_speed# = 0.2
	
	If (IsKeyDown(340) Or IsKeyDown(344)) Then sprint#=0.3 Else sprint#=0.0 ; SHIFT to sprint
	If (IsKeyDown(87) Or IsKeyDown(265)) Then MoveEntity e, 0, 0, camspeed + sprint ; w or up
	If (IsKeyDown(83) Or IsKeyDown(264)) Then MoveEntity e, 0, 0, -(camspeed + sprint) ; s or down 
   	If (IsKeyDown(65) Or IsKeyDown(263)) Then MoveEntity e, -(camspeed + sprint), 0, 0 ; a or left
	If (IsKeyDown(68) Or IsKeyDown(262)) Then MoveEntity e, camspeed+sprint, 0, 0 ; d or right
	
	If (IsKeyDown(81) Or IsKeyDown(266)) Then MoveEntity e, 0, camspeed + sprint, 0 ; Q or Pageup
	If (IsKeyDown(69) Or IsKeyDown(267)) Then MoveEntity e, 0, -(camspeed+sprint), 0 ; E or Pagedown
	
	If IsMouseButtonDown(0) ; left mouse button down
		SetMouseCursorMode 3 ; hide and lock the cursor
		If IsMouseButtonDown(1) ; right mouse button down (both are held down now)
			MoveEntity e, GetMouseVX() * move_speed, -GetMouseVY() * move_speed, 0	
		Else ; only left mouse button is down			
			MoveEntity e, 0, 0, -GetMouseVY() * move_speed
			TurnEntity e, 0, -GetMouseVX() * turn_speed, 0		
		EndIf		
	ElseIf IsMouseButtonDown(1) ; only right mouse button is down
		SetMouseCursorMode 3
		TurnEntity e, -GetMouseVY() * turn_speed, -GetMouseVX() * turn_speed, 0	
	Else 
		SetMouseCursorMode 1 ; set the mouse cursor to normal
	End If	
	If (GetEntityRZ(e) < 0 Or GetEntityRZ(e) > 0) Then SetEntityRotation e, GetEntityRX(e), GetEntityRY(e), 0
End Function 