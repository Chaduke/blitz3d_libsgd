CreateWindow 1280,720,"Light Parent Test",0
camera=CreatePerspectiveCamera()
MoveEntity camera,0,1,-3

cyl_material = LoadPrelitMaterial("sgd://misc/test-texture.png")
cyl_mesh = CreateCylinderMesh(1.77,0.33,32,cyl_material)
cyl_model = CreateModel(cyl_mesh)

player = LoadBonedModel("sgd://models/base_male_animated.glb",True)
SetEntityParent player,cyl_model

MoveEntity cyl_model,0,1.77/2,0
MoveEntity player,0,-1.77/2,0

light=CreatePointLight()
MoveEntity light,1,2,-2
SetEntityParent light,player
SetLightShadowMappingEnabled light,True

dl = CreateDirectionalLight()

ground_material = LoadPBRMaterial("sgd://materials/Bricks076C_1K-JPG")
ground_mesh = CreateBoxMesh(-16,-0.1,-16,16,0,16,ground_material)
ground_model = CreateModel(ground_mesh)

atime#=0.0
loop=True
v = True 
While loop 
	e=PollEvents()
	If e=1 Then loop = False
	If IsKeyHit(256) Then loop = False
	If IsKeyHit(32) Then 
		v = Not v
		SetEntityVisible cyl_model,v
	End If	
	AnimateModel player,1,atime,2,1
	atime = atime + 0.02
	RenderScene()
	Clear2D()
	Draw2DText "Press SPACE to toggle visiblity of collision cylinder",10,10
	Present()
Wend 
End 
