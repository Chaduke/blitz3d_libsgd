; basic_scene.bb
; by Chaduke
; 20240704

; a quick scene that includes terrain, static models and sprites 

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
	Field grass
	Field camera_mode 	
End Type

Function CreateBasicScene.BasicScene(g.GameApp,trees=True,rockfence=True,grass=True)
	Local b.BasicScene = New BasicScene
	
	SetCSMTextureSize 2048
	SetCSMSplitDistances 8,32,64,128
	b\directional_light = CreateDirectionalLight()
	SetLightShadowsEnabled b\directional_light,True 
	TurnEntity b\directional_light,-25,-15,0	
	b\ambient_red = 0.7
	b\ambient_green = 0.5
	b\ambient_blue = 1.0
	b\ambient_alpha = 0.2
	SetAmbientLightColor b\ambient_red,b\ambient_green,b\ambient_blue,b\ambient_alpha	
	
	DisplayLoadingMessageWithTitle "Creating Sky",g
	b\sky_texture = Load2DTexture("../engine/assets/textures/skybox/skybox_sunny.png",4,56)	
	SetEnvTexture b\sky_texture
	b\skybox = CreateSkybox(b\sky_texture)	
	b\sky_roughness = 0.2
	SetSkyboxRoughness b\skybox,b\sky_roughness
	SeedRnd MilliSecs()
	DisplayLoadingMessageWithTitle "Creating Terrain",g
   b\trn = New Terrain	
	SetTerrainDefaults b\trn			
	CreateTerrain b\trn		
	
	If rockfence Then 
		DisplayLoadingMessageWithTitle "Creating Rock Fence",g	
		GenerateRockFence b\trn,"../engine/assets/models/runner/rock.glb"	
	End If 
	
	If trees Then 	
		DisplayLoadingMessageWithTitle "Adding Trees", g
		GenerateTrees b\trn,2000
	End If 
	
	If grass Then 	
		DisplayLoadingMessageWithTitle "Adding Grass",g
		GenerateGrass b\trn,2000,"../engine/assets/textures/foliage/grass1.png"
		GenerateGrass b\trn,1000,"../engine/assets/textures/foliage/grass2.png"
		GenerateGrass b\trn,2000,"../engine/assets/textures/foliage/weeds.png"	
		GenerateGrass b\trn,200,"../engine/assets/textures/foliage/daisies.png"
		GenerateGrass b\trn,200,"../engine/assets/textures/foliage/wildflower_blue.png"	
		GenerateGrass b\trn,200,"../engine/assets/textures/foliage/wildflower_red.png"	
	End If 
		
	PlaceEntityOnTerrain g\pivot,b\trn,1	
	 	
	Return b												
End Function 