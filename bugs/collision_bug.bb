; collision_question.bb
; by Chaduke
; 20240718

Const KEY_ESCAPE = 256
Const KEY_LEFT = 263
Const KEY_RIGHT = 262
Const KEY_SPACE = 32

Type Vec3	
	Field x#
	Field y#
	Field z#
End Type

Function CreateVec3.Vec3(x#=0,y#=0,z#=0)
	Local v.Vec3 = New Vec3	
	v\x# = x# 
	v\y# = y#
	v\z# = z#
	Return v
End Function 

Function AddToVec3(v1.Vec3,v2.Vec3)
	v1\x = v1\x + v2\x
	v1\y = v1\y + v2\y
	v1\z = v1\z + v2\z
End Function 

Function MoveEntityVec3(e,v.Vec3)
	MoveEntity e,v\x,v\y,v\z
End Function 

Function TurnEntityVec3(e,v.Vec3)
	TurnEntity e,v\x,v\y,v\z
End Function 

Type Player 
	Field model	
	Field pivot 
	Field collider 	
	Field vel.Vec3
	Field acc.Vec3	
	Field rot.Vec3
	Field damping#
	Field dead
	Field reviving
End Type 

Function CreatePlayer.Player()
	Local p.Player = New Player	
	Local material = LoadPBRMaterial("sgd://materials/Fabric048_1K-JPG")
	Local mesh = CreateSphereMesh(0.5,16,16,material)
	SetMeshShadowCastingEnabled mesh,True 
	p\model = CreateModel(mesh)
	p\pivot = CreateModel(0)
	SetEntityParent p\model,p\pivot 
	MoveEntity p\pivot,0,5,0
	p\collider = CreateSphereCollider(p\pivot,1,0.5)
	p\vel = CreateVec3(0,0,0)
	p\acc = CreateVec3(0,-0.001,0)
	p\rot = CreateVec3(0,0,0)
	p\damping = 0.5	
	p\dead = False
	p\reviving = False
	Return p
End Function

Function UpdatePlayer(p.Player)

	If p\dead = False Then 		

	   c = GetCollisionCount(p\collider)
	
		If IsKeyDown(KEY_LEFT) Then 
			p\vel\x = -0.03
			p\rot\z = -3
			If c > 0 Then p\acc\y = 0.001 Else p\acc\y = -0.001
		ElseIf IsKeyDown(KEY_RIGHT) Then 
			p\vel\x = 0.03
			p\rot\z = 3			
			If c > 0 Then p\acc\y = 0.001 Else p\acc\y = -0.001
		Else 
			p\vel\x = 0
			p\rot\z = 0
			p\acc\y = -0.001
		End If 
				 	
		AddToVec3 p\vel,p\acc
		MoveEntity p\pivot,p\vel\x,p\vel\y,p\vel\z
		TurnEntity p\model,p\rot\x,p\rot\y,p\rot\z
		If c > 0 Then 
			If p\vel\y < 0 Then p\vel\y = -p\vel\y * p\damping
		End If 
		
		If GetEntityY(p\pivot) < -5 Then p\dead = True 
	Else 
		If IsKeyDown(KEY_SPACE) Then	p\reviving = True 
	End If 		
End Function 

Type Position
	Field x#
	Field y#	
End Type 

position_count = 0
position_max = 180 ; 3 seconds 

CreateWindow 1280,720,"Collision Question",0

ground_material = LoadPBRMaterial("sgd://materials/PavingStones131_1K-JPG")
ground_mesh = CreateSphereMesh(0.5,16,16,ground_material)

For x = -5 To 5 
	ground_model = CreateModel(ground_mesh)
	ground_collider = CreateSphereCollider(ground_model,0,0.5)
	MoveEntity ground_model,x,0,0
Next 	

p.Player = CreatePlayer()

camera = CreatePerspectiveCamera()
light = CreateDirectionalLight()
SetLightShadowMappingEnabled light,True 
TurnEntity light,-25,-35,0
SetAmbientLightColor 1,1,1,0.1

MoveEntity camera,0,0,-10
EnableCollisions 1,0,2

loop = True 

While loop
	e = PollEvents()
	If e = 1 Then loop = False 
	If (IsKeyDown(KEY_ESCAPE)) Then loop = False 	
	
	UpdatePlayer p	
	
	If p\dead = False Then 
		If position_count > position_max Then 
			Delete First Position
			position_count = position_count - 1
		End If 	 
		pos.Position = New Position 
		pos\x = GetEntityX(p\pivot)
		pos\y = GetEntityY(p\pivot)
		position_count = position_count + 1
	Else 
		If p\reviving Then 
			; rewind positions 
			pos.Position = Last Position 
			If pos <> Null Then 
				SetEntityPosition p\pivot,pos\x,pos\y,0
				Delete pos
				position_count = position_count - 1
			Else 
				p\reviving = False 
				p\dead = False 
			End If
		End If 
	End If
	 		 		
	UpdateColliders()
	
	RenderScene()
	Clear2D()	
	If p\dead Then 
		Draw2DText "You died, press SPACE to restart",5,5	
	Else 
		Draw2DText "Use left and right arrow to move",5,5
	End If 
				
	Draw2DText "FPS : " + GetFPS(),5,GetWindowHeight() - 20
	Present()
Wend 
End 