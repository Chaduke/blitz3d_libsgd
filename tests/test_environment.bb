; test_environment.bb
; by Chaduke 
; 20240719

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "../engine/assets/prefabs/bubble_maker/bubble_maker.bb"

g.GameApp = CreateGameApp()
MoveEntity g\pivot,0,2,0
e.Environment = CreateEnvironment(g\gui_font)
ground = CreateGround()
b.BubbleMaker = CreateBubbleMaker()
MoveEntity b\model,-2,0,5

tree_mesh = LoadMesh("../engine/assets/models/trees/tree1.glb")
SetMeshShadowCastingEnabled tree_mesh,True 
tree_model = CreateModel(tree_mesh)
MoveEntity tree_model,0,0,8

While g\loop
	BeginFrame g
	If (GUIDraggedCheck()=False And WidgetDraggedCheck()=False) Then UnrealMouseInput g\pivot	
	UpdateEnvironment e
	UpdateBubbleMaker b
	EndFrame g
Wend 
End 

Function UnrealMouseInput(e,move_speed#=0.02,turn_speed#=0.2)						
	If IsMouseButtonDown(0) ; left mouse button down
		SetMouseCursorMode 3 ; hide and lock the cursor
		If IsMouseButtonDown(1) ; right mouse button down (both are held down now)
			MoveEntity e, GetMouseVX() * move_speed, -GetMouseVY() * move_speed, 0	
		Else ; only left mouse button is down
			If value_adjust Then 
				; adjust a slider type value
			Else					
				MoveEntity e, 0, 0, -GetMouseVY() * move_speed
				TurnEntity e, 0, -GetMouseVX() * turn_speed, 0	
			End If			
		EndIf		
	ElseIf IsMouseButtonDown(1) ; only right mouse button is down
		SetMouseCursorMode 3
		TurnEntity e, -GetMouseVY() * turn_speed, -GetMouseVX() * turn_speed, 0	
	Else 
		SetMouseCursorMode 1 ; set the mouse cursor to normal
	End If	
	If (GetEntityRZ(e) < 0 Or GetEntityRZ(e) > 0) Then SetEntityRotation e, GetEntityRX(e), GetEntityRY(e), 0	
End Function