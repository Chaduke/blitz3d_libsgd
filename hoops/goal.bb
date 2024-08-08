; goal.bb
; by Chaduke 
; 20240725

; a 3D basketball goal for hoops

Type Goal
	Field pivot
	Field view_model	
	Field backboard_collider_model	
	Field backboard_collider
End Type 

Function CreateGoal.Goal(x#,y#,z#)
	Local g.Goal = New Goal
		g\pivot = CreateModel(0)
		g\view_model = LoadModel("../engine/assets/models/hoops/basketball_goal.glb")
		SetEntityParent g\view_model,g\pivot
		Local backboard_collider_mesh = CreateBoxMesh(-0.92,-0.61,-0.1,0.92,0.61,0.1,GetCollisionMaterial())
		g\backboard_collider_model = CreateModel(backboard_collider_mesh)
		g\backboard_collider = CreateMeshCollider(g\backboard_collider_model,0,backboard_collider_mesh)
		SetEntityParent g\backboard_collider_model,g\pivot
		MoveEntity g\backboard_collider_model,0,3.35,-0.65	
		MoveEntity g\pivot,x,y,z	
	Return g
End Function 

Function DrawColliders(g.Goal)
	
End Function 

Function MoveGoal(g.Goal)
	; If IsKeyDown(KEY_UP) Then g\pos\y = g\pos\y - 5
	; If IsKeyDown(KEY_DOWN) Then g\pos\y = g\pos\y + 5
	; If IsKeyDown(KEY_LEFT) Then g\pos\x = g\pos\x - 5
	; If IsKeyDown(KEY_RIGHT) Then g\pos\x = g\pos\x + 5
End Function 