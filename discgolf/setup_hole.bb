; setup_hole.bb

Function SetupHole(course,hole)
	
	ClearScene()
	Delete Each Tree
	Delete Each SolidTree	
	; If NPCs Then Delete Each NPC
		
	SetCSMTextureSize 2048
	SetCSMSplitDistances 8,32,64,128	
	; No shadows after last distance, so set same as far clip if you want shadows everywhere.
			
	; load fonts
	tahoma_font = LoadFont("c:\windows\fonts\tahomabd.ttf",120)
	constan_font = LoadFont("c:\windows\fonts\constan.ttf",26)	
	
	Set2DFont constan_font 
	DisplayLoadingMessage "Loading Hole " + hole + ", Please Wait...",loading_screen 
	
	; create a camera
	pivot = CreateModel(O)
	camera=CreatePerspectiveCamera()
	SetEntityParent camera,pivot
	
	; setup collision material
	collision_material = CreatePBRMaterial()
	collision_texture = LoadTexture("../engine/assets/textures/misc/yellow_grid.png",3,20)
	SetMaterialTexture collision_material,"albedoTexture",collision_texture
	SetMaterialFloat collision_material,"roughnessFactor1f",0.2
	SetMaterialBlendMode collision_material,2	
	
	disc_collision_mesh =  CreateCylinderMesh(0.2,0.2,16,collision_material)	
	disc_collision_model = CreateModel(disc_collision_mesh)	
	disc_mesh = LoadMesh("../engine/assets/models/disc_golf/disc.glb")
	SetMeshShadowCastingEnabled disc_mesh,True
	disc_pivot = CreateModel(0)
	disc_model=CreateModel(disc_mesh)		
	SetEntityParent disc_collision_model,disc_pivot
	SetEntityParent disc_model,disc_pivot
	
	; load collision trees 
	tree_mesh = LoadMesh("../engine/assets/models/trees/tree1.glb")
	SetMeshShadowCastingEnabled tree_mesh,True
	
	; setup basket
	basket_mesh=LoadMesh("../engine/assets/models/disc_golf/basket.glb")
	SetMeshShadowCastingEnabled basket_mesh,True
	basket= CreateModel(basket_mesh)
	
	basket_collision_mesh =  CreateCylinderMesh(1.5,0.5,16,collision_material)
	basket_collision_model = CreateModel(basket_collision_mesh)
	SetEntityParent basket_collision_model,basket
	SetEntityPosition basket_collision_model,0,0.8,0
	
	; setup teepad
	teepad = LoadModel("../engine/assets/models/disc_golf/teepad.glb")	
	
	; sound 
	chains1 = LoadSound("../engine/assets/audio/disc_golf/chains1.wav")
	hit = LoadSound("../engine/assets/audio/disc_golf/hit.wav")
	nature = LoadSound("../engine/assets/audio/disc_golf/nature.mp3")
	birdie_sound = LoadSound("../engine/assets/audio/disc_golf/birdie.wav")
	eagle_sound = LoadSound("../engine/assets/audio/disc_golf/eagle.wav")
	
	PlaySound nature
	
	; hide collision meshes
	If debug = False Then SetEntityVisible basket_collision_model,False
	If debug = False Then SetEntityVisible disc_collision_model,False

	Select course 
		; Beginner's Course - 0
		Case 0 
			course_par = 36
			Select hole
				; Course 0, Hole 1	 
				Case 1
					; very basic hole to learn throwing technique
					hole_name$ = "Let's Go!"
					hole_par = 3					
					teepad_x = 16
					teepad_z = 8
					basket_x = 8
					basket_z = 120
					ob_padding = 3
					sky_texture = LoadTexture("../engine/assets/textures/skybox/skyboxsun25degtest.png",4,56)
					sky_roughness = 0.2
					light_angle = -25
					ambient# = 0.1
					trn\material_path = "../engine/assets/materials/Ground054_1K-JPG"					
					trn\width = 32
					trn\height = 8
					trn\depth = 128
					trn\start_offset = 1111
					trn\offset_inc = 0.02
					trn\calc_normals = True
					trees = 10
					solidtrees = 25
					rockfence = False
					treefence = True 
					grasses = 2000					
				; course 0, Hole 2				
				Case 2
					; a hole to demonstrate the hyzer
					; lets go around a tree
					hole_name$ = "The Hyzer Shot"
					hole_par = 3					
					teepad_x = 32
					teepad_z = 8
					basket_x = 16
					basket_z = 120
					ob_padding = 2
					sky_texture = LoadTexture("../engine/assets/textures/skybox/skyboxsun5deg.png",4,56)
					sky_roughness = 0.2
					light_angle = -5
					ambient# = 0.3
					trn\material_path = "../engine/assets/materials/Ground037_1K-JPG"				
					trn\width = 64
					trn\height = 8
					trn\depth = 128
					trn\start_offset = 2222
					trn\offset_inc = 0.03
					trn\calc_normals = True
					trees = 1
					solidtrees = 50
					rockfence = False	
					treefence = True
					grasses = 10000
				Case 3
					; a hole to demonstrate the anhyzer
					; go around a tree or something
					hole_name$ = "The Anhyzer Shot"
					hole_par = 5					
					teepad_x = 64
					teepad_z = 8
					basket_x = 20
					basket_z = 240
					ob_padding = 5
					sky_texture = LoadTexture("../engine/assets/textures/skybox/skyboxsun45deg.png",4,56)
					sky_roughness = 0.2
					light_angle = -45
					ambient# = 0.3
					trn\material_path = "../engine/assets/materials/Ground020_1K-JPG"				
					trn\width = 128
					trn\height = 16
					trn\depth = 256
					trn\start_offset = 3333
					trn\offset_inc = 0.03
					trn\calc_normals = True
					solidtrees = 100
					trees = 0
					rockfence = True	
					treefence = False
					grasses = 0
				Case 4
					; a hole to demonstrate the flex shot 
					hole_name$ = "The Flex Shot"
					hole_par = 4					
					teepad_x = 64
					teepad_z = 8
					basket_x = 64
					basket_z = 120
					ob_padding = 2
					sky_texture = LoadTexture("../engine/assets/textures/skybox/skyboxsun5deg2.png",4,56)
					sky_roughness = 0.2
					light_angle = -5
					ambient# = 0.1
					trn\material_path = "../engine/assets/materials/Ground075_1K-JPG"				
					trn\width = 128
					trn\height = 5
					trn\depth = 128
					trn\start_offset = 4444
					trn\offset_inc = 0.01
					trn\calc_normals = True
					trees = 5
					solidtrees = 30
					rockfence = False	
					treefence = True
					grasses = 500
				Case 5
					; a hole to demonstrate the flex shot 
					hole_name$ = "The Elevated Basket"
					hole_par = 3					
					teepad_x = 40
					teepad_z = 8
					basket_x = 20
					basket_z = 120
					ob_padding = 2
					sky_texture = LoadTexture("sgd://envmaps/nightsky-cube.png",4,56)
					sky_roughness = 0
					light_angle = -65
					ambient# = 0.1
					trn\material_path = "sgd://materials/Gravel023_1K-JPG"					
					trn\width = 64
					trn\height = 5
					trn\depth = 128
					trn\start_offset = 5555
					trn\offset_inc = 0.01
					trn\calc_normals = True
					trees = 0
					solidtrees=45
					rockfence = True
					treefence = False
					grasses = 100
				Case 6
					; throw around a water tower
					hole_name$ = "The Water Tower"
					hole_par = 4					
					teepad_x = 64
					teepad_z = 8
					basket_x = 64
					basket_z = 120
					ob_padding = 2
					sky_texture = LoadTexture("sgd://envmaps/grimmnight-cube.jpg",4,56)
					sky_roughness = 0.0
					light_angle = -60
					ambient# = 0.1
					trn\material_path = "sgd://materials/PavingStones119_1K-JPG"					
					trn\width = 128
					trn\height = 5
					trn\depth = 128
					trn\start_offset = 6666
					trn\offset_inc = 0.01
					trn\calc_normals = True
					trees = 1
					solidtrees = 30
					rockfence = False	
					treefence = True
					grasses = 500	
				Case 7
					; over a lake
					hole_name$ = "Over Water"
					hole_par = 4					
					teepad_x = 64
					teepad_z = 8
					basket_x = 64
					basket_z = 120
					ob_padding = 2
					sky_texture = LoadTexture("sgd://envmaps/stormy-cube.jpg",4,56)
					sky_roughness = 0.2
					light_angle = -5
					ambient# = 0.1
					trn\material_path = "sgd://materials/PavingStones131_1K-JPG"				
					trn\width = 128
					trn\height = 5
					trn\depth = 128
					trn\start_offset = 7777
					trn\offset_inc = 0.01
					trn\calc_normals = True
					trees = 1
					solidtrees = 50
					rockfence = False	
					treefence = True
					grasses = 500	
				Case 8
					; basket on an island
					hole_name$ = "The Island"
					hole_par = 5					
					teepad_x = 64
					teepad_z = 8
					basket_x = 128
					basket_z = 250
					ob_padding = 2
					sky_texture = LoadTexture("sgd://envmaps/sunnysky-cube.png",4,56)
					sky_roughness = 0.2
					light_angle = -15
					ambient# = 0.3
					trn\material_path = "../engine/assets/materials/Ground075_1K-JPG"				
					trn\width = 256
					trn\height = 40
					trn\depth = 256
					trn\start_offset = 8888
					trn\offset_inc = 0.04
					trn\calc_normals = True
					trees = 0
					solidtrees = 50
					rockfence = False	
					treefence = False
					grasses = 500	
				Case 9
					; a basket on the side of a mountain
					hole_name$ = "The Mountain"
					hole_par = 5					
					teepad_x = 64
					teepad_z = 8
					basket_x = 64
					basket_z = 250
					ob_padding = 2
					sky_texture = LoadTexture("../engine/assets/textures/skybox/skyboxsun45deg.png",4,56)
					sky_roughness = 0.2
					light_angle = -45
					ambient# = 0.1
					trn\material_path = "../engine/assets/materials/Grass001_1K-JPG"					
					trn\width = 256
					trn\height = 32
					trn\depth = 256
					trn\start_offset = 9999
					trn\offset_inc = 0.05
					trn\calc_normals = True
					trees = 0
					solidtrees = 50
					rockfence = True
					treefence = False
					grasses = 500																																									
			End Select 
	End Select 
	
	DisplayLoadingMessage "Loading Hole " + hole + ", Please Wait...",loading_screen 
	
	; create sky environment	
	SetEnvTexture sky_texture
	skybox = CreateSkybox(sky_texture)
	SetSkyboxRoughness skybox,sky_roughness	
	
	DisplayLoadingMessage "Loading Hole " + hole + ", Please Wait...",loading_screen 
	
	CreateTerrain trn
	If rockfence Then GenerateRockFence(trn,"../engine/assets/models/disc_golf/rock.glb")
	
	If trees > 0 Then GenerateTrees trn,trees,treefence	
	If solidtrees > 0 Then GenerateSolidTrees(trn,solidtrees,collision_material)
				
	If grasses > 0 Then 		
		GenerateGrass trn,grasses,"../engine/assets/textures/foliage/grass1.png"
		GenerateGrass trn,grasses/2,"../engine/assets/textures/foliage/weeds.png"
	End If		

	DisplayLoadingMessage "Loading Hole " + hole + ", Please Wait...",loading_screen 
	
	; create lights
	dl = CreateDirectionalLight()
	SetLightShadowMappingEnabled dl,True
	TurnEntity dl,light_angle,0,0
	SetAmbientLightColor 1,1,1,ambient#
	
	; teepad_light = CreatePointLight()	
	; SetLightShadowMappingEnabled teepad_light,True
	; basket_light = CreatePointLight()	
	; SetLightShadowMappingEnabled basket_light,True	
	
	PlaceEntityOnTerrain disc_pivot,trn,0,False,False,teepad_x,teepad_z-2
	PlaceEntityOnTerrain teepad,trn,0,False,False,teepad_x,teepad_z
	; PlaceEntityOnTerrain teepad_light,trn,3,False,False,teepad_x,teepad_z
	PlaceEntityOnTerrain basket,trn,0,False,False,basket_x,basket_z
	; PlaceEntityOnTerrain basket_light,trn,3,False,False,basket_x,basket_z
	If NPCs Then AddNPCs()
	birdie = LoadModel("../engine/assets/models/disc_golf/birdie.glb")
	PlaceEntityOnTerrain birdie,trn,1.55,False,False,basket_x,basket_z	
	SetEntityVisible birdie,False
	eagle = LoadModel("../engine/assets/models/disc_golf/eagle.glb")
	PlaceEntityOnTerrain eagle,trn,1.55,False,False,basket_x,basket_z	
	SetEntityVisible eagle,False
	minidisc = LoadModel("../engine/assets/models/disc_golf/minidisc.glb")
	PlaceEntityOnTerrain minidisc,trn,0.1,False,False,teepad_x-2,teepad_z		
	hole_distance = Distance2D(teepad_x,teepad_z,basket_x,basket_z)	
	Set2DTextColor 1,1,1,1
End Function 
