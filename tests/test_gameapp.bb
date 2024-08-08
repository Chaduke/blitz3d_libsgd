; test_gameapp.bb
; by Chaduke
; 20240806

; also tests startmenu.bb

Include "../engine/gameapp.bb"

ga.GameApp = CreateGameApp()
ga\debug = True 

While ga\loop
	BeginFrame ga
	
	EndFrame ga
Wend 
End 