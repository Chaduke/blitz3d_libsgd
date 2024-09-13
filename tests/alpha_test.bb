; alpha_test.bb

Include "../engine/gameapp.bb"

g.GameApp = CreateGameApp()
g\game_state = GAME_STATE_STORY

light = CreateDirectionalLight()
SetLightShadowsEnabled light,True 
TurnEntity light,-35,0,0

test_cube_mesh = LoadMesh("alpha_test.glb")
SetMeshShadowsEnabled test_cube_mesh,True 
test_cube = CreateModel(test_cube_mesh)

plane_material = LoadPBRMaterial("sgd://materials/Bricks076C_1K-JPG")
plane_mesh = CreateBoxMesh(-16,-0.1,-16,16,0,16,plane_material)
TFormMeshTexCoords plane_mesh,16,16,0,0
plane = CreateModel(plane_mesh)

MoveEntity g\camera,0,1,-5
While g\loop
	BeginFrame g
	TurnEntity test_cube,0,2,0
	EndFrame g
Wend 
End 