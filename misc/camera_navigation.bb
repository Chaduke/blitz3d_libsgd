; camera_navigation.bb
; by Chaduke
; updated on 20240519 
; To support LibSGD 0.06.1

; this demonstrates how to create a camera 
; movement and rotation input system 
; that mirrors the way Unreal Editor works

; use WASD or Arrow Keys to move

; While holding down LEFT mouse button:
; turn left and right with mouse X-Axis
; move forward and backward with mouse Y-Axis

; While holding down RIGHT mouse button:
; look around in all directions with mouse
; combine this with movement keys
; and you have a basic "flymode" camera

; While holding down BOTH buttons:
; X-axis strafes left and right
; Y-axis move up and down

; I also added in a wandering Larry Lemon
; He picks a random location and walks to it

; create window and scene
CreateWindow(1920,1080,"Camera Navigation Example",256)
CreateScene()

; create camera and light
camera = CreatePerspectiveCamera()
MoveEntity camera,0,3,0
SetCameraFar camera,128
light = CreateDirectionalLight()
TurnEntity light,-30,0,0

; create environment and skybox
env = LoadTexture("sgd://envmaps/sunnysky-cube.png", 4, 56)
SetSceneEnvTexture env
skybox = CreateSkybox(env)
SetSkyboxRoughness skybox,.3

; create ground
material = LoadPBRMaterial("sgd://materials/PavingStones065_1K-JPG")
mesh = CreateBoxMesh(-64, -1, -68, 64, 0, 64,material)
TransformMeshTexCoords mesh, 32,32,0,0
ground = CreateModel(mesh)

; add some grass
grass_material = LoadImage("sgd://misc/grass1.png",1) 
SetImageBlendMode grass_material,2

SeedRnd(MilliSecs())
Const n=10000
For i=0 To n
	sprite = CreateSprite(grass_material)	
	TurnEntity sprite,0,Rand(360),0 
	MoveEntity sprite,Rand(-50,50),0.5,Rand(-50,50)	
Next

; add a wandering Lawrence Lemon
larry = LoadBonedModel("sgd://models/larry_walk.glb",True)
larry_light = CreatePointLight()
SetEntityParent larry_light, larry
MoveEntity larry_light,0.5,4,2
SetLightCastsShadow larry_light,True
SetLightRange larry_light,20
larry_light_front = CreatePointLight()
SetEntityParent larry_light_front, larry
MoveEntity larry_light_front,-0.5,1,-2


t#=0.0 ; animation time
destx# = 0.0
destz# = 0.0

; cam movement variables
cam_turn_speed# = 0.2
cam_move_speed_keyboard# = 0.2
cam_move_speed_mouse# = 0.02

loop=True
While(loop) 
	e = PollEvents()
	If e=1 Then loop=False ; window close clicked
	
	; keyboard input
	If KeyHit(256) Then loop=False ; escape	
	If (KeyDown(87) Or KeyDown(265)) Then MoveEntity camera, 0, 0, cam_move_speed_keyboard ; w or up
	If (KeyDown(83) Or KeyDown(264)) Then MoveEntity camera, 0, 0, -cam_move_speed_keyboard ; s or down 
   If (KeyDown(65) Or KeyDown(263)) Then MoveEntity camera, -cam_move_speed_keyboard, 0, 0 ; a or left
	If (KeyDown(68) Or KeyDown(262)) Then MoveEntity camera, cam_move_speed_keyboard, 0, 0 ; d or right	
		
	; mouse input
	If MouseButtonDown(0)
		SetMouseCursorMode 3 
		If MouseButtonDown(1)
			MoveEntity camera, MouseVX() * cam_move_speed_mouse, -MouseVY() * cam_move_speed_mouse, 0	
		Else 
			MoveEntity camera, 0, 0, -MouseVY() * cam_move_speed_mouse
			TurnEntity camera, 0, -MouseVX() * cam_turn_speed, 0			
		EndIf		
	ElseIf MouseButtonDown(1)
		SetMouseCursorMode 3
		TurnEntity camera, -MouseVY() * cam_turn_speed, -MouseVX() * cam_turn_speed, 0	
	Else 
		SetMouseCursorMode 1
	End If
	
	; prevent camera from going under ground
	If EntityY(camera) < 0.5 Then SetEntityPosition camera, EntityX(camera), 0.5, EntityZ(camera)	
	
	; prevent looking too far downwards
	If EntityRX(camera) < -70 Then SetEntityRotation camera, -70, EntityRY(camera), 0
	
	; prevent looking too far upward
	If EntityRX(camera) > 70 SetEntityRotation camera, 70, EntityRY(camera), 0	
	
	; prevent camera from rolling
	If (EntityRZ(camera) < 0 Or EntityRZ(camera) > 0) Then SetEntityRotation camera, EntityRX(camera), EntityRY(camera), 0
	
	; update Larry
	t=t+0.02
	AnimateModel larry,0,t,2
	MoveEntity larry,0,0,-0.035
	; check if Larry has found his destination
	If (Abs(EntityX(larry) - destx#) < 10 And Abs(EntityZ(larry) - destz#) < 10)
		; pick a new destination
		destx# = Rand(-64,64)
		destz# = Rand(-64,64)
		; face that destination
		dz# = destz#-EntityZ(larry)
		dx# = destx#-EntityX(larry)
		angle# = ATan2(dz#,dx#)+90
		SetEntityRotation larry,0,angle#,0		
	End If	
	
	RenderScene()
	; draw 2D Overlay
	Clear2D()
	Set2DTextColor 1,1,0,1
	Draw2DText "Larry's Location " + EntityX(larry) + "," + EntityZ(larry),10,10
	Draw2DText "Larry's Destination " + destx + "," + destz,10,40
	Draw2DText "Larry's Y Rotation " + EntityRY(larry),10,70
	Present()
Wend
End