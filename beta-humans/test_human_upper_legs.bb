; test_human_upper_legs.bb
; by Chaduke
; 20240809

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "human_upper_leg.bb"

ga.GameApp = CreateGameApp("Human Upper Leg Tester","Chaduke's",1,False)
MoveEntity ga\pivot,0,1,0
e.Environment = CreateEnvironment(ga\gui_font)
ground = CreateGround()

For i = 0 To 3
	hul.HumanUpperLeg = CreateHumanUpperLeg("right")
	MoveEntity hul\hip_model,-1.5 + i,1,3
Next 	

ga\info$ = "Leg and hip, joint and tendon, twistem, turnem, stretch and bendem..."

While ga\loop
	BeginFrame ga
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	For hul.HumanUpperLeg= Each HumanUpperLeg
		UpdateHumanUpperLeg hul
	Next
	If (GUIDraggedCheck() = False And WidgetDraggedCheck()=False) Then 	SimpleNavigation ga
	UpdateEnvironment e,GUIDragCheck()	: DrawAllGUIs()
	EndFrame ga
Wend 
End