; crash_bug.bb
; by Chaduke
; 20240718

Const KEY_ESCAPE = 256

Type Ball
	Field model	
	Field turn_speed#
End Type 

Function CreateBall.Ball(material)
	Local b.Ball = New Ball	
	size# = Rnd(0.2,1.0)
	Local mesh = CreateSphereMesh(size,16,16,material)
	SetMeshShadowCastingEnabled mesh,True
	b\model = CreateModel(mesh)
	MoveEntity b\model,Rnd(-14,14),size,Rnd(-10,14)
	b\turn_speed# = Rnd(-5,5)
	Return b
End Function 

CreateWindow 1280,720,"Crash Bug",0

; create environment 
env_texture = LoadTexture("sgd://envmaps/sunnysky-cube.png",4,56)
SetEnvTexture env_texture
skybox = CreateSkybox(env_texture)

light = CreateDirectionalLight()
SetLightShadowMappingEnabled light,True 
TurnEntity light,-25,45,0
SetAmbientLightColor 0.5,0.4,0.5,0.4

camera = CreatePerspectiveCamera()
MoveEntity camera,0,0.2,0

ground_material = LoadPBRMaterial("sgd://materials/Gravel023_1K-JPG")
ground_mesh = CreateBoxMesh(-16,-0.1,-16,16,0,16,ground_material)
TFormMeshTexCoords ground_mesh,16,16,0,0
ground_model = CreateModel(ground_mesh)

numballs = 3
SeedRnd MilliSecs()

Dim ball_materials(2)
ball_materials(0) = LoadPBRMaterial("sgd://materials/PavingStones131_1K-JPG")
ball_materials(1) = LoadPBRMaterial("sgd://materials/Fabric048_1K-JPG")
ball_materials(2) = LoadPBRMaterial("sgd://materials/Tiles019_1K-JPG")

For i = 1 To numballs
	r = Rand(0,2)
	b.Ball = CreateBall(ball_materials(r))
Next	

SetMouseCursorMode 3

loop = True 
While loop
	e = PollEvents()
	If e = 1 Then loop = False 
	If (IsKeyDown(KEY_ESCAPE)) Then loop = False 
	
	TurnEntity camera,0,-GetMouseVX() * 0.1,0
	; add a ball
	If IsMouseButtonDown(0) Then
		If Not MouseLeftDown Then 
			MouseLeftDown = True 
			r = Rand(0,2)
			b.Ball = CreateBall(ball_materials(r))
		End If 	
	Else 
		MouseLeftDown = False
	End If 	
		
	bc = 0
	For b.Ball = Each Ball
		bc = bc + 1
		TurnEntity b\model,0,b\turn_speed,0
	Next
			
	; delete a ball at random
	If IsMouseButtonDown(1) Then 
		If Not MouseRightDown
			MouseRightDown = True  	
			r = Rand(0,bc-1)
			b.Ball = First Ball
			i = 0
			While i < r 
				b = After b
				i = i + 1
			Wend 
			; If b <> Null Then ; checking for null first eliminates the error 
				DestroyEntity b\model ; when not in debug mode this just crashes with no error message
				Delete b
			; End If 	
		End If 	
	Else 
		MouseRightDown = False 
	End If 	
	
	RenderScene()
	Clear2D()
	Set2DTextColor 1,0,0,1
	Draw2DText "Ball Count : " + bc,5,5
	Draw2DText "Left Mouse to Add, Right Mouse to Delete",5,25
	Draw2DText "FPS : " + GetFPS(),5,GetWindowHeight() - 20
	Present()
Wend 
End 