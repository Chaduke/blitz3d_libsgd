; test_human_torsos.bb
; by Chaduke
; 20240809

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "human_torso.bb"

ga.GameApp = CreateGameApp("Human Torso Tester","Chaduke's",1,False)
MoveEntity ga\pivot,0,1,0
e.Environment = CreateEnvironment(ga\gui_font)
ground = CreateGround()

For i = 0 To 3
	ht.HumanTorso = CreateHumanTorso()
	MoveEntity ht\model,-1.5 + i,1.5,3
Next 	

ga\info$ = "A selection of HUUUMAN torsos for your viewing pleasure sir or madam..."

While ga\loop
	BeginFrame ga
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	For ht.HumanTorso = Each HumanTorso 
		UpdateHumanTorso ht
	Next
	If (GUIDraggedCheck() = False And WidgetDraggedCheck()=False) Then 	SimpleNavigation ga
	UpdateEnvironment e,GUIDragCheck()	: DrawAllGUIs()
	EndFrame ga
Wend 
End
