; terrain_test.bb
; by Chaduke 
; 20240614

; new terrain testing to make it more modular
; adding in a terrain type

Include "vec3.bb"
Include "noise.bb" 
Include "terrain.bb"
Include "navigation.bb"

CreateWindow 1280,720,"Terrain Test",0
CreateScene()

; create sky environment 
sky_texture = Load2DTexture("assets\images\skyboxsun25degtest.png",4,56)
SetSceneEnvTexture sky_texture
skybox = CreateSkybox(sky_texture)
SetSkyboxRoughness skybox,0.2

; camera
camera = CreatePerspectiveCamera()
; MoveEntity camera,width/2,20,depth/2

; lights 
light = CreateDirectionalLight()
TurnEntity light,-60,0,0
SetSceneAmbientLightColor 1,1,1,0.5

; terrain
t.Terrain = New Terrain
SetTerrainDefaults t
CreateTerrain t
PlaceEntityOnTerrain camera,t,1,False,True	

loop = True 
While loop 
	e = PollEvents()
	If e = 1 Then loop = False 
	If KeyHit(256) Then loop = False
	If KeyHit(32) Then PlaceEntityOnTerrain camera,t,1,False,True
	NavigationMode t
	RenderScene()	
	Clear2D()	
	Present()
Wend 
End