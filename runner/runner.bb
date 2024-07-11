; runner.bb
; by Chaduke
; 20240710 

; a quick endless runner for fun!
; "Turtle Hurdler"

Include "../engine/gameapp.bb"
Include "player.bb"
Include "runner_scene.bb"

ga.GameApp = CreateGameApp("Turtle Hurdler","Chaduke's")
rs.RunnerScene = CreateRunnerScene(ga,False)
p.Player = CreatePlayer() 
ga\debug = False
If ga\debug = False Then SetEntityParent ga\pivot,p\pivot 
PlaceEntityOnTerrain p\pivot,rs\trn,0,False,False,16,16
SetEntityPosition p\model,0,0,0
If ga\debug = False Then 
	SetEntityPosition ga\pivot,0,0.75,0
	TurnEntity ga\pivot,-10,-100,0
	SetMouseZ -10	
End If 

While ga\loop
	BeginFrame ga	
	If ga\debug Then 
		UnrealMouseInput ga\pivot	
	Else 			
		SetEntityPosition ga\camera,5,0,GetMouseZ()		
	End If 	
	UpdatePlayer p,rs\trn	
	EndFrameCustom ga,p	
Wend
End 