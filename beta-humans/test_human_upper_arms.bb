; test_human_upper_arms.bb
; by Chaduke
; 20240809

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "human_upper_arm.bb"

ga.GameApp = CreateGameApp("Human Upper Arm Tester","Chaduke's",1,False)
MoveEntity ga\pivot,0,1,0
e.Environment = CreateEnvironment(ga\gui_font)
ground = CreateGround()

For i = 0 To 3
	hua.HumanUpperArm = CreateHumanUpperArm("right")
	MoveEntity hua\shoulder_model,-1.5 + i,1,3
Next 	

ga\info$ = "A dazzling sequence of human upper arms to delight your senses oh great one..."

While ga\loop
	BeginFrame ga
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	For hua.HumanUpperArm= Each HumanUpperArm
		UpdateHumanUpperArm hua
	Next
	If (GUIDraggedCheck() = False And WidgetDraggedCheck()=False) Then 	SimpleNavigation ga
	UpdateEnvironment e,GUIDragCheck()	: DrawAllGUIs()
	EndFrame ga
Wend 
End