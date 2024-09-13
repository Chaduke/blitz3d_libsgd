CreateWindow(1280, 720, "Primitives", 2)

env = Load2DTexture("sgd://envmaps/sunnysky-cube.png", 4, 56)
SetEnvTexture env

Local skybox = CreateSkybox(env)
SetSkyboxTexture skybox,env
SetSkyboxRoughness skybox, .3

Local light0 = CreatePointLight()
SetLightRange light0, 50

Local material = LoadPBRMaterial("sgd://materials/Fabric050_1K-JPG")

Local r# = .5, z=3.5
Local mesh0 = CreateSphereMesh(r, 96, 48, material)
Local model0 = CreateModel(mesh0)
MoveEntity model0, -2.5, 0, z

Local r2#=r*.7071
Local mesh1 = CreateBoxMesh(-r2, -r2, -r2, r2, r2, r2, material)
Local model1 = CreateModel(mesh1)
SetModelMesh model1, mesh1
MoveEntity model1, -1.25, 0, z

Local mesh2 = CreateCylinderMesh(r * 2, r / 2, 96, material)
Local model2 = CreateModel(mesh2)
MoveEntity model2, 0, 0, z

Local mesh3 = CreateConeMesh(r * 2, r / 2, 96, material)
Local model3 = CreateModel(mesh3)
MoveEntity model3, 1.25, 0, z

Local mesh4 = CreateTorusMesh(r * .75, r * .25, 96, 48, material)
Local model4 = CreateModel(mesh4)
MoveEntity model4, 2.5, 0, z

While (PollEvents() And 1) <> 1

	TurnEntity model0, .3, .4, .5
	TurnEntity model1, .3, .4, .5
	TurnEntity model2, .3, .4, .5
	TurnEntity model3, .3, .4, .5
	TurnEntity model4, .3, .4, .5
	
	RenderScene()
	
	Present()
Wend