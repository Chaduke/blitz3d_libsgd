; test_basic_scene.bb
; by Chaduke
; 20240807 

Include "../engine/gameapp.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/trees.bb"
Include "../engine/sprites.bb"
Include "../engine/navigation.bb"
Include "../engine/basic_scene.bb"

ga.GameApp = CreateGameApp("Basic Scene Test", "Chaduke's")
ga\game_state = GAME_STATE_STORY
ga\debug = True 

bs.BasicScene = CreateBasicScene(ga)

While ga\loop
	BeginFrame ga
	NavigationMode ga\pivot,bs\trn
	EndFrame ga
Wend 
End 