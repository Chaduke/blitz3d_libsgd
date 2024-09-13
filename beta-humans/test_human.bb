; test_human.bb
; by Chaduke
; 20240809

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/navigation.bb"
Include "human.bb"

ga.GameApp = CreateGameApp("Human Tester","Chaduke's",1,False)
MoveEntity ga\pivot,0,1,0
e.Environment = CreateEnvironment(ga\gui_font)
ground = CreateGround()

SeedRnd MilliSecs()
For i = 0 To 3
	h.Human = CreateHuman()
	MoveEntity h\torso\model,-1.5 + i,1,3
Next 	

ga\info$ = "A small array of most lovely humans for you to do with what you will master..."

While ga\loop
	BeginFrame ga
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	For h.Human = Each Human
		TurnEntity h\torso\model,0,1,0
	Next
	If (GUIDraggedCheck() = False And WidgetDraggedCheck()=False) Then 	SimpleNavigation ga
	UpdateEnvironment e,GUIDragCheck()	: DrawAllGUIs()
	EndFrame ga
Wend 
End