; camera_tests.bb
; by Chaduke 
; 20240720 

CreateWindow 1280,720,"Camera Tests",0

camera = CreatePerspectiveCamera()
light = CreateDirectionalLight()
SetLightShadowsEnabled light,True 
TurnEntity light,-25,-35,0
SetAmbientLightColor 1,1,1,0.1

sphere_material = LoadPBRMaterial("sgd://materials/Bricks076C_1K-JPG")
sphere_mesh = CreateSphereMesh(0.5,16,16,sphere_material)
SetMeshShadowsEnabled sphere_mesh,True 
sphere_model = CreateModel(sphere_mesh)
SetMouseZ -150

Type Spinner
	Field model
	Field speed#
End Type 

camspeed# = 0.1
SetMouseCursorMode 3

While ( Not IsKeyHit(256) ) And ( Not PollEvents() )

	; move camera 
	If (IsKeyDown(340) Or IsKeyDown(344)) Then sprint#=0.3 Else sprint#=0.0 ; SHIFT to sprint
	If (IsKeyDown(87) Or IsKeyDown(265)) Then MoveEntity camera, 0, camspeed + sprint#,0 ; w or up
	If (IsKeyDown(83) Or IsKeyDown(264)) Then MoveEntity camera, 0, -camspeed + sprint#,0 ; s or down 
   	If (IsKeyDown(65) Or IsKeyDown(263)) Then MoveEntity camera, -(camspeed + sprint#), 0, 0 ; a or left
	If (IsKeyDown(68) Or IsKeyDown(262)) Then MoveEntity camera, camspeed+sprint#, 0, 0 ; d or right
	
	x# = GetMouseX() : y# = GetMouseY() : z# = GetMouseZ()
	nx# = (x/GetWindowWidth()-0.5) * 2 : ny# = -(y/GetWindowHeight()-0.5) * 2 ; normalized x and y
	hx# = 12.8 : hy# = 24 ; seems to be the "hotspot" numbers for x and y 
	xfac# = Abs(GetMouseZ()) / hx# : yfac# = Abs(GetMouseZ()) / hy# ; divide camera distance by this number
	camx# = GetEntityX(camera) : camy# = GetEntityY(camera) ; get camera location 
	SetEntityPosition camera,camx,camy,GetMouseZ() * 0.1 ; use mousewheel to zoom in and out
	SetEntityPosition sphere_model,nx# * xfac + camx,ny# * yfac + camy,0 ; position "cursor" adjusted for camera location 
	
	; drop a spinning sphere
	If IsMouseButtonHit(0) Then 
		s.Spinner = New Spinner 
		s\model = CreateModel(sphere_mesh) 
		SetEntityPosition s\model,GetEntityX(sphere_model),GetEntityY(sphere_model),0
		s\speed# = Rnd(-5,5)
	End If 	
	
	; spin the spheres
	For s.Spinner = Each Spinner
		TurnEntity s\model,0,s\speed,0
	Next 	
	
	RenderScene()
	Clear2D()
	
	; Draw2DText "Mouse X,Y,Z : " + Int(x) + "," + Int(y) + "," + Int(z),5,5	
	; Draw2DText "Normalized X,Y : " + nx + "," + ny,5,25
	Draw2DText "Mouse Left Click to Drop a Sphere, Use WASD or arrow keys to move camera (Hold Shift to boost speed), Use Mousewheel to Zoom",5,5
	Draw2DText "FPS : " + GetFPS() + " | Camera Location : " + camx# + "," + camy#, 5,GetWindowHeight() - 20
	Present()
Wend 
End 