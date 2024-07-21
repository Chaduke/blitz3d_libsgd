; camera_tests.bb
; by Chaduke 
; 20240720 

CreateWindow 1280,720,"Camera Tests",0

camera = CreatePerspectiveCamera()
light = CreateDirectionalLight()
SetLightShadowMappingEnabled light,True 
TurnEntity light,-25,-35,0
SetAmbientLightColor 1,1,1,0.1

box_material = LoadPBRMaterial("sgd://materials/Bricks076C_1K-JPG")
box_mesh = CreateBoxMesh(-0.5,0,-0.5,0.5,1,0.5,box_material)
SetMeshShadowCastingEnabled box_mesh,True 
box_model = CreateModel(box_mesh)
SetMouseZ -20
While ( Not IsKeyHit(256) ) And ( Not PollEvents() )
	SetEntityPosition camera,0,0.5,GetMouseZ() * 0.1
	RenderScene()
	Present()
Wend 
End 

