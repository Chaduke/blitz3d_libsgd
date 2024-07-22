; runner_scene.bb
; by Chaduke
; 20240711 

; a special scene setup for an endless runner game 

Include "turtle.bb"

Type RunnerScene	
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

Function CreateRunnerScene.RunnerScene(g.GameApp,blankscene=False)
	Local b.RunnerScene = New RunnerScene
	
	SetCSMTextureSize 2048
	SetCSMSplitDistances 8,32,64,128
	b\directional_light = CreateDirectionalLight()
	SetLightShadowMappingEnabled b\directional_light,True 
	TurnEntity b\directional_light,-25,-15,0	
	b\ambient_red = 0.7
	b\ambient_green = 0.5
	b\ambient_blue = 0.6
	b\ambient_alpha = 0.5
	SetAmbientLightColor b\ambient_red,b\ambient_green,b\ambient_blue,b\ambient_alpha
	If blankscene = False Then 
		DisplayLoadingMessageWithTitle "Creating Sky",g
		b\sky_texture = LoadTexture("../engine/assets/textures/skybox/skybox_sunny.png",4,56)	
		SetEnvTexture b\sky_texture
		b\skybox = CreateSkybox(b\sky_texture)	
		b\sky_roughness = 0.0
		SetSkyboxRoughness b\skybox,b\sky_roughness
		SeedRnd MilliSecs()
		DisplayLoadingMessageWithTitle "Creating Terrain",g
		b\trn = New Terrain	
		SetTerrainDefaultsRunner b\trn			
		CreateTerrain b\trn		
		; DisplayLoadingMessageWithTitle "Creating Rock Fence",g	
		; GenerateRockFence b\trn,"../engine/assets/models/disc_golf/rock.glb"	
		DisplayLoadingMessageWithTitle "Adding Trees", g
		GenerateTreesRunner b\trn
		DisplayLoadingMessageWithTitle "Adding Grass",g
		GenerateGrass b\trn,1000,"../engine/assets/textures/foliage/grass1.png"
		GenerateGrass b\trn,1000,"../engine/assets/textures/foliage/weeds.png"
		
		DisplayLoadingMessageWithTitle "Spawning Turtles!",g		
		; GenerateTurtles b\trn,15,"../engine/assets/models/runner/turtle.glb"
		; load a water plane 
		water_plane = LoadModel("../engine/assets/models/runner/water_plane.glb")
		MoveEntity water_plane,128,1,128
		PlaceEntityOnTerrain g\pivot,b\trn,1	
	End If 	
	Return b												
End Function  

Function SetTerrainDefaultsRunner(t.Terrain)
	t\material_path$ = "../engine/assets/materials/Ground054_1K-JPG"
	t\width# = 256
	t\height# = 16
	t\depth# = 256
	t\start_offset# = 777
	t\offset_inc# = 0.02
	t\calc_normals = True	
	t\falloff_width = 20
	t\falloff_height = 0
End Function 

Dim treeMeshes(4)

Function GenerateTreesRunner(t.Terrain) 

	Local tw#=t\width
	Local td#=t\depth

	treeMeshes(0) = LoadMesh("../engine/assets/models/trees/palm_tree1.glb")
	SetMeshShadowCastingEnabled treeMeshes(0),True
	treeMeshes(1) = LoadMesh("../engine/assets/models/trees/palm_tree2.glb")
	SetMeshShadowCastingEnabled treeMeshes(1),True
	treeMeshes(2) = LoadMesh("../engine/assets/models/trees/palm_tree3.glb")
	SetMeshShadowCastingEnabled treeMeshes(2),True	
	treeMeshes(3) = LoadMesh("../engine/assets/models/trees/palm_tree4.glb")
	SetMeshShadowCastingEnabled treeMeshes(3),True
	treeMeshes(4) = LoadMesh("../engine/assets/models/trees/palm_tree5.glb")
	SetMeshShadowCastingEnabled treeMeshes(4),True	
	
	; randomly distribute trees on the terrain
	; keep trees between 15 and 30 units from edge 	
	
	Local border_close = 18
	Local border_far = 100
	
	For x# = border_close To tw-border_close Step 2							
		tx# = x			
		tz# = Rand(border_close,border_far)	
		ty# = GetTerrainHeight(tx,tz,t)
		rs# = Rnd(2) + 0.5
		AddOneTree("Tree #" + i,treeMeshes(Rand(0,4)),tx,ty,tz,rs)
		
		tz# = td-Rand(border_close,border_far)
		ty# = GetTerrainHeight(tx,tz,t)
		rs# = Rnd(2) + 0.5
		AddOneTree("Tree #" + i,treeMeshes(Rand(0,4)),tx,ty,tz,rs)			
	Next
	For z# = border_close To td-border_close Step 2			
		tx = Rand(border_close,border_far)
		tz# = z
		ty# = GetTerrainHeight(tx,tz,t)
		rs# = Rnd(2) + 0.5
		AddOneTree("Tree #" + i,treeMeshes(Rand(0,4)),tx,ty,tz,rs)							
			
		tx# = tw-Rand(border_close,border_far)
		ty# = GetTerrainHeight(tx,tz,t)
		rs# = Rnd(2) + 0.5
		AddOneTree("Tree #" + i,treeMeshes(Rand(0,4)),tx,ty,tz,rs)
	Next			
	
End Function