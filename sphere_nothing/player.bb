; player.bb
; by Chaduke
; 20240717

; player for sphere game

Type Player
	Field model
	Field pivot
	Field vel.Vec3	
	Field acc.Vec3
	Field rot.Vec3
	Field collider	
	Field light 
	Field health		
	Field speed#
	Field damping#
	Field checkpointx#
	Field checkpointy#
	Field dead
	Field reviving
	Field position_count
	Field position_max
	Field level_finished 
	Field advancing
	Field current_level
	Field jumping 
	Field jump_strength
End Type 

Type Position
	Field x#
	Field y#	
End Type 

Function CreatePlayer.Player()
	Local p.Player = New Player		
	Local material = LoadPBRMaterial("../engine/assets/materials/Marble012_1K-JPG")
	Local mesh=CreateSphereMesh(0.5,16,16,material)
	SetMeshShadowsEnabled mesh,True
	p\model = CreateModel(mesh)	
	p\pivot = CreateModel(0)
	SetEntityParent p\model,p\pivot 	
	p\vel = CreateVec3("Player Velocity",0,0,0)
	p\speed = 0.03
	p\acc = CreateVec3("Player Acceleration",0,-0.001,0)
	p\rot = CreateVec3("Player Rotation",0,0,0)
	p\collider = CreateSphereCollider(p\pivot,0,0.49)
	p\light = CreatePointLight()
	SetLightShadowsEnabled p\light,True 
	SetEntityParent p\light,p\pivot	
	MoveEntity p\light,0,3,0	
	SetLightRange p\light,10	
	p\position_count = 0
	p\position_max = 180
	p\damping = 0.5
	p\dead = False 
	p\reviving = False 
	p\level_finished = False 
	p\advancing = False 
	p\current_level = 0
	p\jumping = False
	p\jump_strength# = 0.1
	Return p
End Function

Function UpdatePlayer(p.Player,l.Level)
	Local pos.Position 
	
	If (p\dead = False And p\level_finished=False) Then
		; update position recording  
		If p\position_count > p\position_max Then 
			Delete First Position
			p\position_count = p\position_count - 1
		End If 
			 
		pos = New Position 
		pos\x = GetEntityX(p\pivot)
		pos\y = GetEntityY(p\pivot)
		p\position_count = p\position_count + 1	
		
		; get input
		Local c = GetCollisionCount(p\collider) 	
	   
		If (IsKeyDown(KEY_D) Or IsKeyDown(KEY_RIGHT)) Then 
			p\vel\x = p\speed
			p\rot\z = 3
			If c > 0 Then p\acc\y = 0.001 Else p\acc\y = -0.001
		ElseIf (IsKeyDown(KEY_A) Or IsKeyDown(KEY_LEFT)) Then 
			p\vel\x= -p\speed
			p\rot\z = -3
			If c > 0 Then p\acc\y = 0.001 Else p\acc\y = -0.001
		Else 
			p\vel\x = 0
			p\rot\z = 0
			p\acc\y = -0.001
		End If 		
		
		; jump!
		If (IsKeyHit(KEY_W) Or IsKeyHit(KEY_UP)) Then 
			If p\jumping = False Then 
				p\vel\y = p\jump_strength
				p\jumping = True
			End If 	
		End If 	
		
		AddToVec3 p\vel,p\acc
		MoveEntityVec3 p\pivot,p\vel
		TurnEntityVec3 p\model,p\rot	
		
		If c > 0 Then 
			If p\jumping = True 
				If (p\vel\y < p\jump_strength - p\acc\y) Then p\jumping = False
			End If 	
			If p\vel\y < 0 Then p\vel\y = -p\vel\y * p\damping
			if GetCollisionNY(p\collider, 0) < 0 then 
				if p\vel\y > 0 then p\vel\y = -p\vel\y * p\damping
			end if 	
			; check checkpoints
			For i = 0 To 2
				If Distance2D(GetEntityX(p\pivot),GetEntityY(p\pivot),l\checkpoints[i]\x,l\checkpoints[i]\y) < 1 Then 
					p\checkpointx = l\checkpoints[i]\x
					p\checkpointy = l\checkpoints[i]\y 
					p\vel\y = p\vel\y * 3
				End If 
			Next 
			; check for end of level
			If Distance2D(GetEntityX(p\pivot),GetEntityY(p\pivot),l\endpos\x,l\endpos\y) < 1 Then 
				; handle end of level
				p\level_finished = True 
			End If
		End If ; end if there are collisions occuring 

		If (GetEntityY(p\pivot) < -l\height And p\dead = False) Then p\dead = True 	
	Else ; 
		If p\reviving Then 
			; rewind positions 
			pos = Last Position 
			If pos <> Null Then 
				SetEntityPosition p\pivot,pos\x,pos\y,0
				Delete pos
				p\position_count = p\position_count - 1
			Else 
				p\reviving = False 
				p\dead = False 
			End If
		Else 
			If IsKeyDown(KEY_SPACE) Then 
				If p\dead Then 
					p\reviving = True
				Else 
					p\level_finished = False 
					p\advancing = True
				End If
			End If 							
		End If 					 
	End If ; end if dead or alive			
End Function 

Function CamFollow(follower,followed,speed#=0.03)
	; first get the 3D distance between the two
	Local frx# = GetEntityX(follower)
	Local fry# = GetEntityY(follower)
	Local frz# = GetEntityZ(follower)
	Local fdx# = GetEntityX(followed)
	Local fdy# = GetEntityY(followed)
	Local fdz# = GetEntityZ(followed)
		
	Local xd# = fdx - frx 
	Local yd# = fdy - fry
	Local zd# = fdz - frz	
		
	Local incx# = xd# * speed
	Local incy# = yd# * speed
	Local incz# = zd# * speed
	
	SetEntityPosition follower,frx + incx,fry + incy,GetEntityZ(follower)
End Function 