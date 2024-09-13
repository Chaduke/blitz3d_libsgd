; test_particles.bb
; by Chaduke
; 20240817


Include "particle.bb"
Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/navigation.bb"

ga.GameApp = CreateGameApp("Test Particles", "Louis",1,False)	
e.Environment = CreateEnvironment(ga\gui_font)
trn.Terrain = New Terrain
SetTerrainDefaults trn
CreateTerrain trn
PlaceEntityOnTerrain ga\pivot,trn,1
MoveEntity ga\pivot,0,0,-5
Const num_particles = 500
raindrop = LoadImage("../engine/assets/textures/misc/raindrop2.png",1)
fountain = LoadModel("../engine/assets/models/theme_park/fountain.glb")
PlaceEntityOnTerrain fountain,trn,0

fountain_sound = LoadSound("../engine/assets/audio/misc/fountain.mp3")
SetAudioLooping fountain_sound,True
PlaySound fountain_sound 

ga\info$ = "This is a particle test in the form of a fountain!"

While ga\loop
	BeginFrame ga
	c = 0
	For p.Particle = Each Particle
		UpdateParticle p,trn
		c = c + 1
	Next
	If c < num_particles Then 
		p.Particle = CreateParticle(raindrop,trn)
		p.Particle = CreateParticle(raindrop,trn)
		p.Particle = CreateParticle(raindrop,trn)
	End If 
	NavigationMode ga\pivot,trn
	Draw2DText "Particles: " + c,5,5
	EndFrame ga
Wend 
End 
	
	
