; scene.bb
; by Chaduke
; 20240517

; sets up the scene step by step 
; and reports progress to the screen 
; using 2D overlay text

Include "noise.bb" ; for perlin noise function
Include "vec3.bb" ; for various Vec3 math functions
Include "terrain.bb"
Include "sprites.bb"
Include "trees.bb"
Include "platforms.bb"
Include "static_objects.bb"
Include "npc.bb"
Include "dialogue.bb"

Global generate_scene = True 
Global generate_terrain = True
Global trn.Terrain

Global add_trees=True
Global numtrees=100
Global add_rock_fence=True ; "rock fence" surrounding terrain

Global add_grass=False
Global numsprites = 1000

Global add_platforms = False
Global numplatforms = 2

Global add_npcs = True

; fonts 
Global bradybunch

; sounds 
Global song1_music
Global blip_sound

Function DisplayLoadingMessage(msg$)
	Clear2D()
	Draw2DText msg$,10,10
	PollEvents()
	RenderScene()
	Present()
End Function	

Function GenerateScene()	
	ClearScene()	
	
	; load fonts
	bradybunch = LoadFont("assets/fonts/Brady Bunch.ttf",24)
	Set2DFont bradybunch
	
	DisplayLoadingMessage "Loading sounds and music..."	
	; load sounds 

	blip_sound = LoadSound("assets/sounds/blip.wav")
	
	; song1_music = LoadSound("assets/music/song1.mp3")
	
	trn.Terrain = New Terrain
	SetTerrainDefaults trn
	
	DisplayLoadingMessage "Adding Camera..."
	; add a camera
	camera = CreatePerspectiveCamera()
	pivot = CreateModel(0)
	SetCameraFar camera, Sqr(trn\width * trn\depth) * 3
	MoveEntity camera,trn\width/2,trn\height/2,trn\depth/2
	
	DisplayLoadingMessage "Setting up scene and skybox"	
	; setup the scene	
	env = Load2DTexture("sgd://envmaps/sunnysky-cube.png", 4, 56)
	SetEnvTexture env
	skybox = CreateSkybox(env)	
	SetSkyboxRoughness skybox, .3
	
	DisplayLoadingMessage "Adding Lights"	
	; add lights
	light = CreateDirectionalLight()	
	TurnEntity light,-30,0,0	
	SetAmbientLightColor 1,1,1,0.2	
		
	If generate_terrain Then CreateTerrain trn
	
	If add_grass Then GenerateGrass(trn,numsprites)	; sprites.bb
	If add_trees Then GenerateTrees(trn,numtrees)	; trees.bb
	If add_rock_fence Then GenerateRockFence(trn)	; static_objects.bb
	
	If add_npcs Then AddNPCs() ; npc.bb
	
	If add_platforms Then AddPlatforms(trn) ; platforms.bb
		
	DisplayLoadingMessage "Starting Main Loop..."	
End Function

Function SaveScene(bank,slot)
	; Open a file to write to 
	fileout = WriteFile("levels/bank" + bank + "slot" + slot + ".lvl")
   ; count the platforms 
	count = 0
	For p.Platform = Each Platform		
		count = count + 1
	Next
	WriteInt fileout,count
		
	; now write the data for each platform
	For p.Platform = Each Platform
		WriteString fileout,p\name$
		WriteFloat fileout,p\cyl\height
		WriteFloat fileout,p\cyl\radius 
		WriteFloat fileout,GetEntityX(p\cyl\model)
		WriteFloat fileout,GetEntityY(p\cyl\model)
		WriteFloat fileout,GetEntityZ(p\cyl\model)
	Next  
	; Close the file  
	CloseFile fileout
End Function

Function LoadScene(bank,slot)
	; clear all platforms
	Delete Each Platform
	Delete Each Cylinder
	Delete Each Player
	add_plaforms=False
	GenerateScene()	
	SetupEditMode()
	
	filein = ReadFile("levels/bank" + bank + "slot" + slot + ".lvl")
	
	; get number of platforms
	count = ReadInt(filein)
	For i = 1 To count
		Local name$ = ReadString$(filein)	
		Local height# = ReadFloat(filein)
		Local radius# = ReadFloat(filein)
		Local ex# = ReadFloat(filein)
		Local ey# = ReadFloat(filein)
		Local ez# = ReadFloat(filein)	
		AddOnePlatform(name$,height,radius,ex,ey,ez)	
	Next	
	CloseFile filein
End Function 