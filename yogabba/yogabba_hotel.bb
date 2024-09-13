Include "../engine/gameapp.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "../engine/navigation.bb"
Include "../engine/assets/prefabs/bubble_maker/bubble_maker.bb"
Include "../engine/assets/prefabs/ferris_wheel/ferris_wheel.bb"
Include "../engine/assets/prefabs/fountain/fountain.bb"

; create window and scene
ga.GameApp = CreateGameApp("Yo Gabba Gabba Hotel","Louis Dore's")
env.Environment = CreateEnvironment(ga\gui_font)
trn.Terrain = New Terrain 
trn\material_path$ = "../engine/assets/materials/Grass001_1K-JPG"
trn\width = 128
trn\height = 8
trn\depth = 128
trn\falloff_width = 16
trn\falloff_height = 0
trn\start_offset# = 867
trn\offset_inc# = 0.01
trn\calc_normals = True
CreateTerrain trn

; add models
; main building 
building_model = LoadModel("../engine/assets/models/theme_park/building.glb")
MoveEntity building_model,6,4.8,7.8

; fans
exercise_fan = LoadModel("../engine/assets/models/theme_park/ceiling_fan.glb")
MoveEntity exercise_fan,15.8,7.3,17

pool_fan = LoadModel("../engine/assets/models/theme_park/ceiling_fan_pool.glb")
MoveEntity pool_fan,33.8,16.2,25.4

; foofa = LoadModel("../engine/assets/models/disc_golf/foofa.glb")
; TurnEntity foofa,0,90,0
; MoveEntity foofa,0,0,5

; pool_building 
pool_building = LoadModel("../engine/assets/models/theme_park/pool_building.glb")
MoveEntity pool_building,33.5,4.8,25.8

; pool speaker
pool_speaker = LoadModel("../engine/assets/models/theme_park/speaker.glb")
MoveEntity pool_speaker,34.4,5.5,37.4

; outdoor pools
outdoor_pool_mesh = LoadMesh("../engine/assets/models/theme_park/outdoor_pool.glb")

outdoor_pool = CreateModel(outdoor_pool_mesh)
MoveEntity outdoor_pool,55.2,5.2,55
TurnEntity outdoor_pool,0,180,0

outdoor_pool2 = CreateModel(outdoor_pool_mesh)
MoveEntity outdoor_pool2,12.4,5.2,55.2
TurnEntity outdoor_pool2,0,180,0

; BOUNCE ROOMS 
bounce_room_mesh = LoadMesh("../engine/assets/models/theme_park/bounce_room.glb")
For x = 1 To 8
	m = CreateModel(bounce_room_mesh)
	MoveEntity m,x * 15,0,100
Next

; slides
slide_mesh = LoadMesh("../engine/assets/models/theme_park/slide.glb")
For x = 1 To 5
	m = CreateModel(slide_mesh)
	m2 = CreateModel(slide_mesh)
	MoveEntity m,x * 20 + 50,0,130
	MoveEntity m2,x * 20 + 50,0,150
Next

; waterfalls 
waterfall = LoadModel("../engine/assets/models/theme_park/waterfall.glb")
MoveEntity waterfall,42.2,5,4.4

; ferris wheel 
ferris_wheel.FerrisWheel = CreateFerrisWheel()
MoveEntity ferris_wheel\base_pivot,80,8,200

; race track
; grass for racetrack 

; race_track_ground = CreateModel(grass_mesh)
; MoveEntity race_track_ground,128,0,0
; race_track = LoadModel("../engine/assets/models/theme_park/race_track.glb")
; MoveEntity race_track,128,0,0

; swings 
swings_mesh = LoadMesh("../engine/assets/models/theme_park/swings.glb")
SetMeshShadowsEnabled swings_mesh,True
swings1 = CreateModel(swings_mesh)
PlaceEntityOnTerrain(swings1,trn,0,False,False,22,160)
swings2 = CreateModel(swings_mesh)
PlaceEntityOnTerrain(swings2,trn,0,False,False,40,160)
swings3 = CreateModel(swings_mesh)
PlaceEntityOnTerrain(swings3,trn,0,False,False,22,170)
swings4 = CreateModel(swings_mesh)
PlaceEntityOnTerrain(swings4,trn,0,False,False,40,170)

; desk
desk_mesh = LoadMesh("../engine/assets/models/theme_park/desk.glb")
SetMeshShadowsEnabled desk_mesh,True
desk = CreateModel(desk_mesh)

; bubble maker 
bubble_maker.BubbleMaker = CreateBubbleMaker()

SetEntityParent bubble_maker\model,desk
MoveEntity bubble_maker\model,0,0.75,0
MoveEntity desk,5,5.2,43

; snack machine 
snack_mesh = LoadMesh("../engine/assets/models/theme_park/snack_machine.glb")
SetMeshShadowsEnabled snack_mesh,True
snack_machine = CreateModel(snack_mesh)
MoveEntity snack_machine,35.8,4.8,4.2

; beverage machine 
bev_mesh = LoadMesh("../engine/assets/models/theme_park/bev_machine.glb")
SetMeshShadowsEnabled bev_mesh,True
bev_machine = CreateModel(bev_mesh)
MoveEntity bev_machine,37.5,4.8,4.2

; fountain 
fountain1.Fountain = CreateFountain()
PlaceEntityOnTerrain fountain1\model,trn,0,False,False,2.6,26.4
TurnEntity fountain1\model,0,180,0

; twitter logo
twitter = LoadModel("../engine/assets/models/theme_park/twitter_logo.glb")
MoveEntity twitter,12.4,8.2,-7.4
TurnEntity twitter,0,90,0

t#=0.0
loop=True

bubble_maker_on = False
ceiling_fans_on = False 
ferris_wheel_on = True

switch = LoadSound("../engine/assets/audio/misc/switch.wav")
bubble_sound = LoadSound("../engine/assets/audio/misc/bubble_motor.mp3")
snack_beep = LoadSound("../engine/assets/audio/misc/snack_beep.mp3")
snack_chime = LoadSound("../engine/assets/audio/misc/snack_chime.mp3")
drink_beep = LoadSound("../engine/assets/audio/misc/drink_beep.mp3")
drink_chime = LoadSound("../engine/assets/audio/misc/drink_chime.mp3")

MoveEntity ga\pivot,20,6,-5
TurnEntity ga\pivot,0,90,0

ga\info$ = "Welcome to Yo Gabba Gabba Hotel!!"

While ga\loop
	
	BeginFrame ga	
	
	If ferris_wheel_on Then UpdateFerrisWheel ferris_wheel
	If bubble_maker_on Then UpdateBubbleMaker bubble_maker
	
	If IsKeyHit(KEY_1) Then BoardPassenger(ferris_wheel,ga\pivot,10)
	If IsKeyHit(KEY_2) Then RemovePassenger(ferris_wheel,ga\pivot,trn)

	If IsKeyHit(66) Then 
		If bubble_maker_on = True Then 
			bubble_maker_on = False 
			PlaySound switch
		Else 
			bubble_maker_on = True				
			PlaySound bubble_sound
		End If 
	End If 
	
	If IsKeyHit(71) ToggleRunning fountain1	
	UpdateFountains trn
	
	If IsKeyHit(70) Then 
		If ferris_wheel_on = True Then ferris_wheel_on = False Else ferris_wheel_on = True
	End If 
	
	If IsKeyHit(320) Then 	
		justin = LoadSound("justin_time_song.mp3")		
		PlaySound justin
	End If	
	
	If IsKeyHit(321) Then	
		poolie = LoadSound("poolie_ball_song.mp3")		
		PlaySound poolie
	End If	
	
	If IsKeyHit(322) Then 	
		ilike = LoadSound("i_like_to_dance.mp3")			
		PlaySound ilike
	End If	
	
	If IsKeyHit(323) Then PlaySound snack_beep
	If IsKeyHit(324) Then PlaySound snack_chime
	If IsKeyHit(325) Then PlaySound drink_beep
	If IsKeyHit(326) Then PlaySound drink_chime


	; turn fans 	
	If ceiling_fans_on Then 	
		TurnEntity exercise_fan,0,10,0
		TurnEntity pool_fan,0,-10,0	
	End If 
	
	If IsKeyHit(67) Then 
		If ceiling_fans_on Then 
			ceiling_fans_on = False 
			PlaySound switch
		Else 
			ceiling_fans_on = True
			PlaySound switch
		End If 
	End If 		
	
	NavigationMode ga\pivot,trn	
	MoveEntityKeyboard twitter
	
	EndFrame ga
Wend
End