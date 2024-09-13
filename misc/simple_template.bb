; create window and scene
CreateWindow(1920,1080,"Yo Gabba Gabba Hotel",1)
CreateScene()

; create camera and light
camera = CreatePerspectiveCamera()
MoveEntity camera,10,1,-3
light = CreateDirectionalLight()
TurnEntity light,-60,0,0

point_light = CreatePointLight()
MoveEntity point_light,0,3,0
SetLightRange point_light,10
SetLightCastsShadow point_light,True

SetSceneAmbientLightColor 1,1,1,0.1

; create environment and skybox
env = Load2DTexture("sgd://envmaps/sunnysky-cube.png", 4, 56)
SetSceneEnvTexture env
skybox = CreateSkybox(env)
SetSkyboxRoughness skybox,.3

; create ground
material = LoadPBRMaterial("../platformer/assets/materials/Concrete031_1K-JPG")
mesh = CreateBoxMesh(-64, -4.26, -64, 64, -3.26, 64,material)
TransformMeshTexCoords mesh, 8,8,0,0
ground = CreateModel(mesh)

; add models
building_model = LoadModel("building.glb")
fan = LoadModel("ceiling_fan.glb")
fan_pool = LoadModel("ceiling_fan_pool.glb")

MoveEntity fan_pool,22.2,4.3,9.2
MoveEntity fan,10,2,7

foofa = LoadModel("foofa.glb")
TurnEntity foofa,0,90,0

; pool_building 
pool_building = LoadModel("pool_building.glb")
MoveEntity pool_building,22.2,0,9.2

; pool speaker
pool_speaker = LoadModel("speaker.glb")
MoveEntity pool_speaker,22.2,0,20

Global cam_turn_speed# = 0.2
Global cam_move_speed_keyboard# = 0.2
Global cam_move_speed_mouse# = 0.02
Global sprint# = 0.0

Function StandardMouseInput(e)
	; Mouse Input					
	If MouseButtonDown(0) ; left mouse button down
		SetMouseCursorMode 3 ; hide and lock the cursor
		If MouseButtonDown(1) ; right mouse button down (both are held down now)
			MoveEntity e, MouseVX() * cam_move_speed_mouse, -MouseVY() * cam_move_speed_mouse, 0	
		Else ; only left mouse button is down
			MoveEntity e, 0, 0, -MouseVY() * cam_move_speed_mouse
			TurnEntity e, 0, -MouseVX() * cam_turn_speed, 0			
		EndIf		
	ElseIf MouseButtonDown(1) ; only right mouse button is down
		SetMouseCursorMode 3
		TurnEntity e, -MouseVY() * cam_turn_speed, -MouseVX() * cam_turn_speed, 0	
	Else 
		SetMouseCursorMode 1 ; set the mouse cursor to normal
	End If	
End Function

Function CorrectEntityRoll(e)
	; prevent entity from rolling
	If (EntityRZ(e) < 0 Or EntityRZ(e) > 0) Then SetEntityRotation e, EntityRX(e), EntityRY(e), 0			
End Function

Function KeyBoardInputFirstPerson(e)	
	If (KeyDown(340) Or KeyDown(344)) Then sprint=0.3 Else sprint=0.0 ; SHIFT to sprint
	If (KeyDown(87) Or KeyDown(265)) Then MoveEntity e, 0, 0, cam_move_speed_keyboard + sprint ; w or up
	If (KeyDown(83) Or KeyDown(264)) Then MoveEntity e, 0, 0, -(cam_move_speed_keyboard + sprint) ; s or down 
   	If (KeyDown(65) Or KeyDown(263)) Then MoveEntity e, -(cam_move_speed_keyboard + sprint), 0, 0 ; a or left
	If (KeyDown(68) Or KeyDown(262)) Then MoveEntity e, cam_move_speed_keyboard+sprint, 0, 0 ; d or right
	
	; add support for moving cam up / down with the keys Q / E or Pageup / Pagedown	
	If (KeyDown(81) Or KeyDown(266)) Then MoveEntity e, 0, cam_move_speed_keyboard + sprint, 0 ; Q or Pageup
	If (KeyDown(69) Or KeyDown(267)) Then MoveEntity e, 0, -(cam_move_speed_keyboard+sprint), 0 ; E or Pagedown
End Function 

Function MoveEntityKeyboard(e)
	If (KeyDown(73)) Then MoveEntity e, 0, 0, cam_move_speed_keyboard + sprint ; w or up
	If (KeyDown(75)) Then MoveEntity e, 0, 0, -(cam_move_speed_keyboard + sprint) ; s or down 
   	If (KeyDown(74)) Then MoveEntity e, -(cam_move_speed_keyboard + sprint), 0, 0 ; a or left
	If (KeyDown(76)) Then MoveEntity e, cam_move_speed_keyboard+sprint, 0, 0 ; d or right
End Function 

loop=True
While(loop) 
	e = PollEvents()
	If e=1 Then loop=False ; window close clicked
	If KeyHit(256) Then loop = False	 
	If KeyHit(320) Then 
		StopAudio current_song
		current_song = LoadSound("justin_time_song.mp3")		
		PlaySound current_song 
	End If	
	If KeyHit(321) Then 
		StopAudio current_song
		current_song = LoadSound("poolie_ball_song.mp3")		
		PlaySound current_song 
	End If	

	TurnEntity fan,0,10,0
	TurnEntity fan_pool,0,-10,0
	StandardMouseInput(camera)
	KeyBoardInputFirstPerson(camera)
	CorrectEntityRoll(camera)
	MoveEntityKeyboard fan_pool
	RenderScene()	
	Clear2D()
	Draw2DText "Fan Location " + EntityX(fan_pool) + " " + EntityY(fan_pool) + " " + EntityZ(fan_pool),10,10
		
	Present()
Wend
End