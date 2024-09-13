; test_human_lower_arms.bb
; by Chaduke
; 20240809

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "human_upper_arm.bb"
Include "human_lower_arm.bb"

ga.GameApp = CreateGameApp("Human Arm Tester","Chaduke's",1,False)
MoveEntity ga\pivot,0,1,0
e.Environment = CreateEnvironment(ga\gui_font)
ground = CreateGround()

Type ArmBase
	Field pivot
	Field right_upper.HumanUpperArm
	Field right_lower.HumanLowerArm
	Field left_upper.HumanUpperArm
	Field left_Lower.HumanLowerArm
End Type 

Function CreateArmBase.ArmBase()
	Local ab.ArmBase = New ArmBase 
	Local pivot_material = GetCollisionMaterial()
	SetMaterialCullMode pivot_material,0
	Local pivot_mesh = CreateSphereMesh(0.24,16,16,pivot_material)
	SetMeshShadowsEnabled pivot_mesh,True 
	ab\pivot = CreateModel(pivot_mesh)
	
	ab\right_upper = CreateHumanUpperArm("right")
	ab\left_upper = MirrorHumanUpperArm(ab\right_upper)
	SetEntityParent ab\right_upper\shoulder_model,ab\pivot	
	SetEntityParent ab\left_upper\shoulder_model,ab\pivot	
	MoveEntity ab\right_upper\shoulder_model,-0.24,0,0
	MoveEntity ab\left_upper\shoulder_model,0.24,0,0
	
	ab\right_lower = CreateHumanLowerArm("right")
	SetEntityParent ab\right_lower\elbow_model, ab\right_upper\model	
	MoveEntity ab\right_lower\elbow_model,0,-ab\right_upper\height/2,0	
	
	ab\left_lower = MirrorHumanLowerArm(ab\right_lower)
	SetEntityParent ab\left_lower\elbow_model, ab\left_upper\model	
	MoveEntity ab\left_lower\elbow_model,0,ab\left_upper\height/2,0	
	Return ab
	
End Function 

ab1.ArmBase = CreateArmBase()
MoveEntity ab1\pivot,0,1,2

ga\info$ = "A simple human arm, two parts, infinite complexity..."

While ga\loop
	BeginFrame ga
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	For ab.ArmBase = Each ArmBase
		; TurnEntity ab1\pivot,0,0.5,0
		SetEntityRotation ab\right_upper\shoulder_model,Sin(angle) * 45,0,-65
		SetEntityRotation ab\right_lower\elbow_model,Sin(angle) * 22,0,-22
		SetEntityRotation ab\left_upper\shoulder_model,Sin(angle) * -45,0,65	
		SetEntityRotation ab\left_lower\elbow_model,Sin(angle) * 22,0,22			
	Next
	angle = angle + 3
	If (GUIDraggedCheck() = False And WidgetDraggedCheck()=False) Then 	SimpleNavigation ga
	UpdateEnvironment e,GUIDragCheck()	: DrawAllGUIs()
	EndFrame ga
Wend 
End