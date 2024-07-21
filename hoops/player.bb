; player.bb
; by Chaduke
; 20240721

; player for hoops game

Type Player
	Field model 
	Field pivot
	Field vel.Vec3
	Field acc.Vec3
	Field rot.Vec3
	Field collider	
	Field carrying
	Field jumping 
	Field shooting
End Type 

Function CreatePlayer.Player(bs.BasicScene)
	Local p.Player = New Player
	Local mesh = CreateCylinderMesh(1.7,0.5,16,GetCollisionMaterial())	
	SetMeshShadowCastingEnabled mesh,True 
	p\model = CreateModel(mesh)
	p\pivot = CreateModel(0)
	SetEntityParent p\model,p\pivot
	PlaceEntityOnTerrain p\pivot,bs\trn		
	MoveEntity p\pivot,0,1,-3
	p\vel = CreateVec3("Player Velocity")
	p\acc = CreateVec3("Player Acceleration",0,-0.005,0)
	p\rot = CreateVec3("Player Rotation")
	p\collider = CreateEllipsoidCollider(p\pivot,1,0.5,1.7)	
	Return p
End Function 

Function UpdatePlayer(p.Player,b.Basketball)	

	; get player keyboard input
	If IsKeyDown(KEY_S) Then 
		p\vel\z = -0.05
	ElseIf IsKeyDown(KEY_W)
		p\vel\z = 0.05
	Else 
	 p\vel\z = 0
	End If 
	
	If IsKeyDown(KEY_A) Then 
		p\vel\x = -0.05
	ElseIf IsKeyDown(KEY_D)
		p\vel\x = 0.05
	Else 
	 p\vel\x = 0
	End If 
	
	; jump
	If IsKeyHit(KEY_SPACE) Then 
		If p\jumping = False Then 
			p\vel\y = 0.15
			p\jumping = True 
		End If 
	End If 
	
	; get a runaway basketball
	If IsKeyHit(KEY_B) Then 
		p\carrying = True 
		SetEntityParent b\pivot,p\pivot 
		SetEntityPosition b\pivot,0,0.2,0.5
		; clear out all motion 
		b\acc\x = 0 : b\acc\y = 0 : b\acc\z = 0
		b\vel\x = 0 : b\vel\y = 0 : b\vel\z = 0
		b\rot\x = 0 : b\rot\y = 0 : b\rot\z = 0
	End If 	

	; mouse input
	TurnEntity p\pivot,0,-GetMouseVX() * 0.1,0	
	
	; shoot 
	If IsMouseButtonDown(0) Then 
		; we must be carrying the ball
		If p\carrying Then 
			; we must not already be shooting 
			If Not p\shooting Then 
				p\shooting = True 
			Else 
				; we are shooting, let's increase the ball power
				b\power = b\power + 0.002
				If b\power > 0.2 Then b\power = 0.2 					
			End If 		
		End If 
	Else 
	; see if we are shooting so we can release 
		If p\shooting Then 
			p\shooting = False
			p\carrying = False  			
			SetEntityParent b\pivot,0
			b\vel\x = p\vel\x : b\vel\y = p\vel\y + 0.1 + b\power : b\vel\z = p\vel\z + b\power
			b\acc\x = p\vel\x : b\acc\y = p\acc\y : b\acc\z = p\acc\z
			SetEntityRotation b\pivot,0,GetEntityRY(p\pivot),0
			b\rot\x = 5
			b\power = 0
		End If 
	End If 		

	; update movement 
	AddToVec3 p\vel,p\acc
	MoveEntityVec3 p\pivot,p\vel
	TurnEntityVec3 p\model,p\rot		
	
	; collision check and response 
	If GetCollisionCount(p\collider) > 0 Then 
		; handle collision from bottom
		If GetCollisionNX(p\collider,0) > 0 Then 		
			If p\vel\y < 0 Then p\vel\y = 0	
			p\jumping = False 
		End If 
		; handle bounce from top
		If GetCollisionNX(p\collider,0) < 0 Then 		
			If p\vel\y > 0 Then p\vel\y = -p\vel\y * 0.7		
		End If 		
	End If 
	
	; pick up ball when near 
	If p\carrying = False Then 
		If (EntityDistance(p\pivot,b\pivot) < 2 And IsMouseButtonDown(1)) Then 
			p\carrying = True 
			SetEntityParent b\pivot,p\pivot 
			SetEntityPosition b\pivot,0,0.2,0.5
			; clear out all motion 
			b\acc\x = 0 : b\acc\y = 0 : b\acc\z = 0
			b\vel\x = 0 : b\vel\y = 0 : b\vel\z = 0
			b\rot\x = 0 : b\rot\y = 0 : b\rot\z = 0
		End If 
	End If	
	
End Function 