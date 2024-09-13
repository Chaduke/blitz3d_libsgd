; 3D_Game_Template_v1.bb
; by Chaduke
; 20240604

; 3D Game Template
; INITIAL SETUP

; CONSTANTS

; game states 
Const GAME_STATE_START_MENU = 0
Const GAME_STATE_STORY = 1
Const GAME_STATE_SANDBOX = 2
Const GAME_STATE_TUTORIAL = 3
Const GAME_STATE_OPTIONS = 4

; keys 
Const KEY_ESCAPE = 256
Const KEY_Y = 89

; GLOBALS 
Global cam_turn_speed# = 0.2
Global cam_move_speed_mouse# = 0.02
Global ground_size = 64
Global game_state = GAME_STATE_START_MENU

; initialize a game window and a scene
CreateWindow 1280,720,"3D Game",0

; create a camera
pivot = CreateModel(O)
camera=CreatePerspectiveCamera()
SetEntityParent camera,pivot
MoveEntity pivot,0,1.5,-4
TurnEntity camera,-10,0,0

; load fonts
Global tahoma_font = LoadFont("c:\windows\fonts\tahomabd.ttf",120)
Global constan_font = LoadFont("c:\windows\fonts\constan.ttf",26)

Set2DFont constan_font

LoadingMessage "Loading Please Wait",constan_font

; create sky environment 
sky_texture = Load2DTexture("C:\Users\chadu\OneDrive\Projects\blitz3d\engine\assets\textures\skybox\skyboxsun25degtest.png",4,56)
SetEnvTexture sky_texture
skybox = CreateSkybox(sky_texture)

SetSkyboxRoughness skybox,0.2

LoadingMessage "Loading Please Wait",constan_font

; create lights
dl = CreateDirectionalLight()
TurnEntity dl,-30,0,0
SetAmbientLightColor 1,1,1,0.2

light = CreatePointLight()
MoveEntity light, 0,3,0
SetLightShadowsEnabled light,True

LoadingMessage "Loading Please Wait",constan_font

; create ground
ground_material = LoadPBRMaterial("d:/blender/pbr_materials/Concrete031_1K-JPG")
ground_mesh = CreateBoxMesh(-ground_size,-0.1,-ground_size,ground_size,0,ground_size,ground_material)
TFormMeshTexCoords ground_mesh,16,16,0,0
ground_model = CreateModel(ground_mesh)

; create buildings
large_building1 = LoadModel("assets/models/Large Building1.glb")
MoveEntity large_building1,-15,0,30
ScaleEntity large_building1,5,5,5
large_building2 = LoadModel("assets/models/Large Building2.glb")
MoveEntity large_building2,-5,0,30
ScaleEntity large_building2,5,5,5
large_building3 = LoadModel("assets/models/Large Building3.glb")
MoveEntity large_building3,5,0,30
ScaleEntity large_building3,5,5,5
large_building4 = LoadModel("assets/models/Large Building4.glb")
MoveEntity large_building4,15,0,30
ScaleEntity large_building4,5,5,5

; MAIN LOOP

loop=True
quit = False

While loop 
	; get user input 
	e=PollEvents()	
	
	If (e = 1 Or IsKeyHit(KEY_ESCAPE)) Then 
		If quit = False Then quit = True Else quit = False		
	End If
	
	If (quit = True And IsKeyHit(KEY_Y)) Then loop = False
	
	; create branches based on game state
	Select game_state 
		Case 0				
		; main menu
		If quit = False Then 			
			Set2DTextColor 0,0,0,1
			DisplayTextCenter "3D Game",tahoma_font,-252,-2
			Set2DTextColor 0.5,1,0,1
			DisplayTextCenter "3D Game",tahoma_font,-250
			
			If (MouseCollidedRect(400,GetWindowHeight() / 2 - 60,GetWindowWidth()-400,GetWindowHeight() / 2 - 30)) Then
				If IsMouseButtonHit(0) Then game_state = GAME_STATE_STORY				 
				Set2DTextColor 1,1,0,1 
			Else 
				Set2DTextColor 1,1,1,1
			End If			
			DisplayTextCenter "Story Mode",constan_font,-60
			
			If (MouseCollidedRect(400,GetWindowHeight() / 2 - 30,GetWindowWidth()-400,GetWindowHeight() / 2)) Then 
				If IsMouseButtonHit(0) Then game_state = GAME_STATE_SANDBOX
				Set2DTextColor 1,1,0,1 
			Else 
				Set2DTextColor 1,1,1,1
			End If 			
			DisplayTextCenter "Sandbox Mode",constan_font,-30
			
			If (MouseCollidedRect(400,GetWindowHeight() / 2,GetWindowWidth()-400,GetWindowHeight() / 2 + 30)) Then 
				If IsMouseButtonHit(0) Then game_state = GAME_STATE_TUTORIAL
				Set2DTextColor 1,1,0,1 
			Else 
				Set2DTextColor 1,1,1,1				
			End If			
			DisplayTextCenter "Tutorial",constan_font
			
			If (MouseCollidedRect(400,GetWindowHeight() / 2 + 30,GetWindowWidth()-400,GetWindowHeight() / 2 + 60)) Then 
				If IsMouseButtonHit(0) Then game_state = GAME_STATE_OPTIONS
				Set2DTextColor 1,1,0,1 
			Else 
				Set2DTextColor 1,1,1,1
			End If 
			DisplayTextCenter "Options",constan_font,30			
		End If	
		
		Case 1
		; story mode		
		Case 2		
		; sandbox mode
		StandardMouseInput(pivot)
		Case 3 
		; tutorial
		Case 4		
		; options
	End Select 	
	 
	
	; update objects and data structures based on user input
	
	; perform corrections / implement constraints / apply game rules
	
	; render 
	RenderScene()
		
	; draw 2D overlay	
	Clear2D()
	If quit = True Then
		DisplayTextCenter "Really Quit ?? (Y to Confirm, Escape to Cancel)",constan_font
	End If	

	Present()
Wend 
End 

; HELPER FUNCTIONS
Function DisplayTextCenter(msg$,font,offsetY#=0,offsetX#=0)
	Local centerX# = GetWindowWidth()/2
	Local centerY# = GetWindowHeight()/2
	Local mhw# = GetTextWidth(font,msg$) / 2 ; message half width
	Set2DFont font	
	Draw2DText msg$,centerX#-mhw#+offsetX,centerY# + offsetY
End Function 

Function LoadingMessage(msg$,font)
	PollEvents()
	RenderScene()
	Clear2D()
	DisplayTextCenter(msg$,font)
	Present()
End Function 

Function MouseCollidedRect%(l,t,r,b) ; left, right, top, bottom
	Local c% = False
	If (GetMouseX() > l And GetMouseX() < r And GetMouseY() > t And GetMouseY() < b) Then c = True
	Return c 	
End Function 

Function StandardMouseInput(e)
	; Mouse Input					
	If IsMouseButtonDown(0) ; left mouse button down
		SetMouseCursorMode 3 ; hide and lock the cursor
		If IsMouseButtonDown(1) ; right mouse button down (both are held down now)
			MoveEntity e, GetMouseVX() * cam_move_speed_mouse, -GetMouseVY() * cam_move_speed_mouse, 0	
		Else ; only left mouse button is down
			MoveEntity e, 0, 0, -GetMouseVY() * cam_move_speed_mouse
			TurnEntity e, 0, -GetMouseVX() * cam_turn_speed, 0			
		EndIf		
	ElseIf IsMouseButtonDown(1) ; only right mouse button is down
		SetMouseCursorMode 3
		TurnEntity e, -GetMouseVY() * cam_turn_speed, -GetMouseVX() * cam_turn_speed, 0	
	Else 
		SetMouseCursorMode 1 ; set the mouse cursor to normal
	End If	
	If (GetEntityRZ(e) < 0 Or GetEntityRZ(e) > 0) Then SetEntityRotation e, GetEntityRX(e), GetEntityRY(e), 0	
	If GetEntityY(e) < 0.5 Then SetEntityPosition e,GetEntityX(e),0.5,GetEntityZ(e)
End Function