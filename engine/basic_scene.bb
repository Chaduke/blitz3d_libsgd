; basic_scene.bb
; by Chaduke
; 20240704

Type BasicScene	
	Field directional_light	
	Field ambient_red# 
	Field ambient_green#
	Field ambient_blue# 
	Field ambient_alpha# 	
	Field sky_texture
	Field sky_roughness
	Field skybox 	
	Field trn.Terrain	
   Field trees	
	Field rockfence
	Field grasses	
	Field camera_mode 	
End Type

Function CreateBasicScene.BasicScene(g.GameApp)
	Local b.BasicScene = New BasicScene
	
	SetCSMTextureSize 2048
	SetCSMSplitDistances 8,32,64,128
	b\directional_light = CreateDirectionalLight()
	SetLightShadowMappingEnabled b\directional_light,True 
	TurnEntity b\directional_light,-25,-15,0	
	b\ambient_red = 0.7
	b\ambient_green = 0.5
	b\ambient_blue = 1.0
	b\ambient_alpha = 0.2
	SetAmbientLightColor b\ambient_red,b\ambient_green,b\ambient_blue,b\ambient_alpha	
	
	DisplayLoadingMessageWithTitle "Creating Sky",g
	b\sky_texture = LoadTexture("../engine/assets/textures/skybox/skybox_sunny.png",4,56)	
	SetEnvTexture b\sky_texture
	b\skybox = CreateSkybox(b\sky_texture)	
	b\sky_roughness = 0.2
	SetSkyboxRoughness b\skybox,b\sky_roughness
	SeedRnd MilliSecs()
	DisplayLoadingMessageWithTitle "Creating Terrain",g
   b\trn = New Terrain	
	SetTerrainDefaults b\trn			
	CreateTerrain b\trn		
	DisplayLoadingMessageWithTitle "Creating Rock Fence",g	
	GenerateRockFence b\trn,"../engine/assets/models/runner/rock.glb"	
	DisplayLoadingMessageWithTitle "Adding Trees", g
	GenerateTrees b\trn,20
	DisplayLoadingMessageWithTitle "Adding Grass",g
	GenerateGrass b\trn,100,"../engine/assets/textures/foliage/grass1.png"
	GenerateGrass b\trn,100,"../engine/assets/textures/foliage/weeds.png"		
	PlaceEntityOnTerrain g\pivot,b\trn,1	
	 	
	Return b												
End Function 