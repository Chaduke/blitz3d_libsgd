; turtle.bb
; by Chaduke 
; 20240711

; turtle character for runner / platformer

Type Turtle
	Field view_model
	Field pivot 
	Field collision_model
	Field collider
	Field acc.Vec3
	Field vel.Vec3	
	Field walking,idle
	Field seq,time#,timeStep#	
	Field destx# 
	Field destz#	
End Type

Function CreateTurtle.Turtle(parent)
	Local t.Turtle = New Turtle 
	t\view_model = LoadModel("../engine/assets/models/runner/turtle.glb")
	SetMeshShadowsEnabled GetModelMesh(t\view_model),True 
	t\pivot  = CreateModel(0)
	SetEntityParent t\view_model,t\pivot 		
	Local collision_mesh = CreateSphereMesh(1.6,8,8,GetCollisionMaterial())
	t\collision_model = CreateModel(collision_mesh)
	SetEntityParent t\collision_model,t\pivot
	SetEntityPosition t\collision_model,0,.8,0
	SetEntityParent t\pivot,parent 
	SetEntityPosition t\pivot,Rnd(-64,64),0,Rnd(-128,128)
	
	t\collider = CreateEllipsoidCollider(t\collision_model,2,1.6, 1.5)
	t\acc = CreateVec3("Turtle Acceleration",0,-0.006,0) ; Y value is gravity
	t\vel = CreateVec3("Turtle Velocity",0,0,-0.01) ; Z value is run speed
	t\walking = False
	t\idle = True 
	t\seq = 0 ; idle
	t\time = 0.0
	t\timeStep = 0.02
	Return t		
End Function 	

Function UpdateTurtle(t.Turtle)	
	If t\walking Then 
		AddToVec3 t\vel,t\acc
		MoveEntityVec3 t\pivot,t\vel
		If (distance2D(GetEntityX(t\pivot),GetEntityZ(t\pivot),t\destx#,t\destz#) < 5)
			t\walking = False
			t\idle = True 
			t\seq = 0
			t\time = 0.0		
		End If	
	End If 	
	
	AnimateModel t\view_model,t\seq,t\time,2,1
	t\time=t\time + t\timeStep
	
	If (t\idle And t\time > 0.0333  * 250) Then
		t\idle = False
		t\walking = True 
		t\seq = 1 ; walking 
		t\time = 0.0
		; pick a new destination		
		t\destx# = GetEntityX(GetEntityParent(t\pivot)) + Rand(-64,64)
		t\destz# = GetEntityZ(GetEntityParent(t\pivot)) + Rand(-128,128)
		; face that destination		
		EntityFaceLocation(t\pivot,t\destx,t\destz)	; navigation.bb		
	End If 
	
End Function 