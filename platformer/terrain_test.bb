; terrain_test.bb
; by Chaduke 
; 20240614

; new terrain testing to make it more modular
; adding in a terrain type

Include "../engine/vec3.bb"
Include "../engine/noise.bb" 
Include "../engine/terrain.bb"
Include "../engine/navigation.bb"

CreateWindow 1280,720,"Terrain Test",0

; create sky environment 
sky_texture = LoadCubeTexture("../engine/assets/textures/skybox/skyboxsun25degtest.png",4,56)
SetEnvTexture sky_texture
skybox = CreateSkybox(sky_texture)
SetSkyboxRoughness skybox,0.2

; camera
camera = CreatePerspectiveCamera()
; MoveEntity camera,width/2,20,depth/2

; lights 
light = CreateDirectionalLight()
TurnEntity light,-60,0,0
SetAmbientLightColor 1,1,1,0.5

; terrain
t.Terrain = New Terrain
SetTerrainDefaults t
CreateTerrain t
PlaceEntityOnTerrain camera,t,1,False,True	

loop = True 
While loop 
	e = PollEvents()
	If e = 1 Then loop = False 
	If IsKeyHit(256) Then loop = False
	If IsKeyHit(32) Then PlaceEntityOnTerrain camera,t,1,False,True
	NavigationMode camera,t
	RenderScene()	
	Clear2D()	
	Present()
Wend 
End