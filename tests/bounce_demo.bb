; bounce_demo.bb
; by Chaduke
; 20240718

; the goal is to create 4 different balls with different elasticity

Const KEY_ESCAPE = 256

Type Ball
	Field model	
	Field collider 	
	Field vel#
	Field acc#	
	Field damping#
End Type 

Function CreateBall.Ball(mesh,damping#,x)
	Local b.Ball = New Ball		
	b\model = CreateModel(mesh)
	b\collider = CreateSphereCollider(b\model,1,0.5)
	b\vel = 0
	b\acc = -0.001	
	b\damping = damping
	MoveEntity b\model,x,8,6
	Return b
End Function

CreateWindow 1280,720,"Bounce Demo",0

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
TurnEntity camera,5,0,0

ground_material = LoadPBRMaterial("sgd://materials/Gravel023_1K-JPG")
ground_mesh = CreateBoxMesh(-16,-0.1,-16,16,0,16,ground_material)
TFormMeshTexCoords ground_mesh,16,16,0,0
ground_model = CreateModel(ground_mesh)
ground_collider = CreateMeshCollider(ground_model,0,ground_mesh) ; Ground is collider type 0

Dim materials(3)

materials(0) = LoadPBRMaterial("sgd://materials/PavingStones131_1K-JPG")
materials(1) = LoadPBRMaterial("sgd://materials/Fabric048_1K-JPG")
materials(2) = LoadPBRMaterial("sgd://materials/Tiles019_1K-JPG")
materials(3) = LoadPBRMaterial("sgd://materials/PaintedWood009C_1K-JPG")

Const numballs = 4
Dim balls.Ball(numballs-1)

For i = 1 To numballs	
	mesh = CreateSphereMesh(0.5,16,16,materials(i-1))
	SetMeshShadowCastingEnabled mesh,True
	balls(i-1) = CreateBall(mesh,Float(i)/5,(i * 2) - 5) 
Next	

EnableCollisions 1,0,1

loop = True 
While loop
	e = PollEvents()
	If e = 1 Then loop = False 
	If (IsKeyDown(KEY_ESCAPE)) Then loop = False 	
	
	For i = 0 To numballs-1
		balls(i)\vel = balls(i)\vel + balls(i)\acc ; add acceleration (gravity) to velocity
		MoveEntity balls(i)\model,0,balls(i)\vel,0
		If GetCollisionCount(balls(i)\collider) > 0 Then 
			; bounce has occured 
			If balls(i)\vel < 0 Then balls(i)\vel = -balls(i)\vel * balls(i)\damping
		End If 	
	Next 
	
	UpdateColliders()
	
	RenderScene()
	Clear2D()	
	Draw2DText "FPS : " + GetFPS(),5,GetWindowHeight() - 20
	Present()
Wend 
End 



