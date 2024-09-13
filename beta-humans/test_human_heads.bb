; test_human_heads.bb
; by Chaduke
; 20240809

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "human_head.bb"

ga.GameApp = CreateGameApp("Human Head Tester","Chaduke's",1,False)
MoveEntity ga\pivot,0,1,0
e.Environment = CreateEnvironment(ga\gui_font)
ground = CreateGround()

For i = 0 To 3
	hh.HumanHead = CreateHumanHead()
	MoveEntity hh\model,-1.5 + i,1,3
Next 	

ga\info$ = "A selection of bouncy human heads created never severed..."

While ga\loop
	BeginFrame ga
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	For hh.HumanHead = Each HumanHead 
		UpdateHumanHead hh
	Next
	If (GUIDraggedCheck() = False And WidgetDraggedCheck()=False) Then 	SimpleNavigation ga
	UpdateEnvironment e,GUIDragCheck()	: DrawAllGUIs()
	EndFrame ga
Wend 
End
