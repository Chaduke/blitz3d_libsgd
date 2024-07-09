; player.bb
; by Chaduke
; 20240518

; Contains all functionality related to a player

; here are the names and frame counts of each animation sequence for base_male_animated.glb

; sequence# | frames |	description 
; ---------	 ------   -----------
; 0				 72 	 	death
; 1				 117		freehang_climb
; 2 			 142		hanging_idle	
; 3 			 60 		idle	
; 4     		 27 	   jump
; 5 		    42			left_shimmy
; 6 			 43	 		right_shimmy		 		
; 7 			 17			run_fast
; 8				 21 		run_left
; 9 			 21			run_right
; 10 			 23 		run_slow
; 11			 226      shuffle
; 12 			 36 		standard_walk
; 13 			 32 		walk_left
; 14 		    29			walk_right

Type Player
	; the cylinder is the collision model
	; it contains a height, radius, and model
	Field pivot
	Field cyl.Cylinder
	Field collision_mesh
	Field view_model ; this is the boned animated model
	Field acc.Vec3 ; represents the player's current acceleration
	Field vel.Vec3 ; represents the player's current velocity
	Field speed.Vec3 ; this is the amount velocity is set to when using the keyboard
	Field pos.Vec3
	Field turn_speed#
	Field sprint_speed#
	Field airborne
	Field jumps 
	Field h# ; player height off the ground
	Field hdiff# ; difference between h and the terrain or landed platform
	Field landed_platform.Platform	
	Field anim
	Field anim_time#
	Field face_forward ; this is to rotate the player model 180 degrees 	
	Field light 
	Field start_y_high# ; this is to determine fall distance
	Field start_y_low# ; this is to determine jump height
	Field highest_jump#
	Field lowest_fall#
	Field health#
	Field stamina#
	Field dead#
	Field shimmying 
	Field climbing
	Field jumping 		
	Field gravity#
	Field friction#
	Field landing_sound 
	Field ow1_sound 
	Field ow2_sound
	Field jump_grunt_sound 
	Field death_sound
End Type	

; Include "../engine/testing.bb" : Include "platforms.bb" : Global ta.TestApp : Global bs.BasicScene : TestPlayer()

Function TestPlayer()
	ta = CreateTestApp()
	bs = CreateBasicScene()
	p.Player = CreatePlayer(bs\trn)
	AddPlatforms bs\trn
	While ta\loop 
		BeginFrame ta
		UpdatePlayer p,bs\trn,bs\camera,bs\pivot
		EndFrame ta
	Wend 
	End 
End Function 

; check collsions with player and platform
; if collided return the platform
; if not return null
Function PlayerPlatformCollision.Platform(p.Player)
	Local pf.Platform = First Platform
	While pf <> Null
		If CylindersCollided(p\cyl,pf\cyl) Then Return pf
		pf = After pf
	Wend 
	Return pf
End Function 

Function CreatePlayer.Player(t.Terrain)
	Local p.Player=New Player
	p\pivot = CreateModel(0)
	p\cyl=New Cylinder
	p\cyl\height=1.77
	p\cyl\radius=0.33
	Local collision_mesh_material = LoadPrelitMaterial("sgd://misc/test-texture.png")
	p\collision_mesh = CreateCylinderMesh(p\cyl\height,p\cyl\radius,16,collision_mesh_material)
	p\cyl\model = CreateModel(p\collision_mesh)	
	p\view_model = LoadBonedModel("../engine/assets/models/misc/base_male_animated.glb",True)
	TurnEntity p\view_model, 0,180,0
	MoveEntity p\view_model,0,-p\cyl\height/2,0
	SetEntityParent p\cyl\model,p\pivot
	SetEntityParent p\view_model,p\pivot
	; move to the middle of the level	
	
	MoveEntity p\pivot,t\width/2,GetTerrainHeight(t\width/2,t\depth/2,t) + p\cyl\height/2,t\depth/2	
	p\acc = New Vec3	
	SetVec3 p\acc,"Player Acceleration"
	p\vel = New Vec3
	SetVec3 p\vel,"Player Velocity"
	p\speed = New Vec3
	SetVec3 p\speed,"Player Speed",0.05,0.1,0.08 ; x=strafe speed | y=jump strength | z=forward/backward speed
	p\pos = New Vec3
	SetVec3 p\pos,"Player Position"
	p\turn_speed# = 0.1	
	p\sprint_speed# = 0.1	
	p\airborne = False
	p\jumps = 0
	p\h = 0.0
	p\hdiff = 0.0
	p\landed_platform = Null	
	p\anim = 1
	p\anim_time#=0.0
	p\face_forward=False	
	; p\light=CreatePointLight()	
	; SetEntityParent p\light,p\view_model
	; SetLightShadowMappingEnabled p\light,True		 
	; MoveEntity p\light,0,5,1
	; SetLightRange p\light,10	
	p\start_y_high=0
	p\start_y_low=0
	; load these from storage at some point
	; once we create player profiles
	p\highest_jump=0
	p\lowest_fall=0 
	p\health = 1.0
	p\stamina = 1.0
	p\dead = False
	SetEntityVisible p\cyl\model, False ; hide the collsion mesh
	p\shimmying = False
	p\climbing = False
	p\jumping = False
	p\gravity# = -0.0004
	p\friction# = 0.02
	
	p\landing_sound = LoadSound("../engine/assets/audio/platformer/landing.wav")
	p\ow1_sound = LoadSound ("../engine/assets/audio/platformer/ow1.wav")
	p\ow2_sound = LoadSound ("../engine/assets/audio/platformer/ow2.wav")
	p\death_sound = LoadSound ("../engine/assets/audio/platformer/death.wav")
	p\jump_grunt_sound = LoadSound("../engine/assets/audio/platformer/jump_grunt.wav")
	CenterPlayerOnTerrain p,t
	
	SetMouseCursorMode 3
	SetMouseZ -5
	Return p
	
End Function 

; make sure we are not outside the terrain limits
Function KeepPlayerInsideLevel(p.Player,t.Terrain)	
	If p\pos\x > t\width-1 Then p\pos\x=t\width-1
	If p\pos\x < 1 Then p\pos\x=1
	If p\pos\z > t\depth-1 Then p\pos\z=t\depth-1
	If p\pos\z < 1 Then p\pos\z=1			
End Function	

Function CenterPlayerOnTerrain(p.Player,t.Terrain)	
	Local px# = t\width / 2
	Local pz# = t\depth / 2
	Local py# = GetTerrainHeight(px,pz,t)
	SetEntityPosition p\pivot,px,py,pz	
End Function	

Function UpdatePlayer(p.Player,t.Terrain,cam,piv)
   ; all of this is based on the player's 
	; pivot, which is p\pivot
	; the collision model p\cyl\model is parented to it	
	; defined when we called CreatePlayer()
	; by default it's hidden 
	; the player's animated view_model is parented to to the pivot as well	
	; this way when we hide the collision model it doesnt hide anything parented to it
	
	; if we are not idle, dead, shimmying or climbing then face the proper direction 
	If (p\anim <> 3 And p\anim<>0 And p\shimmying=False And p\climbing=False) Then 
		If p\face_forward Then  
			SetEntityRotation p\pivot,0,GetEntityRY(piv),0	
		Else 
			SetEntityRotation p\pivot,0,GetEntityRY(piv)-180,0
		End If
	End If	
			
	; move the player according to velocity vector
	MoveEntity p\pivot,p\vel\x,p\vel\y,p\vel\z	
	
	; set the position Vec3 called p\pos
	; this is our starting point
	; afterwards we check to see if anything needs to adjusted
	; and we make those changes to the pos Vec3 
	; then at the end of the loop we update p\pivot 
	; based on what's in p\pos
			 
	SetVec3 p\pos,"Player Position",GetEntityX(p\pivot),GetEntityY(p\pivot),GetEntityZ(p\pivot)	
			
	; get the height at our current x / z position
	If p\landed_platform<>Null Then 
		; height at the top of the platform
		p\h = GetEntityY(p\landed_platform\cyl\model) + p\landed_platform\cyl\height/2	
	Else 	
		p\h# = GetTerrainHeight(p\pos\x,p\pos\z,t)
	End If	
					
	; get height difference of our current position and the ground 
	p\hdiff# = (p\pos\y - p\cyl\height/2) - p\h	
	; if were above the ground we're airborne 
	If p\hdiff > 0  Then 
		p\airborne = True
		; at this moment we need to update our starting height
		; if it's higher than start_y_high then we update start_y_high
		; if it's lower than start_y_low then we update start_y_low		
		If p\pos\y > p\start_y_high Then p\start_y_high = p\pos\y
		If p\pos\y < p\start_y_low Then p\start_y_low = p\pos\y
	Else 
		; were not above the terrain
		; check if we were already airborne 
		If p\airborne Then 
			; we hit the ground				
			; we can play a hit the ground sound here
			; before we can do this though
			; we need to determine how far we fell from
			; this is to determine fall damage
			; but also to prevent the landing sound 
			; from playing repeatedly
			; running around on a sloped surface will cause us 
			; to go airborne and then land in quick succession
			
			end_y_high# = p\start_y_high - p\pos\y ; this is fall distance
			end_y_low# = p\start_y_low - p\pos\y ; this is jump height
			
			If (end_y_high > 2 Or end_y_low > 2) Then 
				PlaySound p\landing_sound
				; this is where we need to add fall damage
				; based on the value of end_y_high#
				If end_y_high > 30 Then 
					p\health = p\health - ((end_y_high-10) * 0.01)
					If end_y_high > 50 Then 
						PlaySound p\ow2_sound
					Else 
						PlaySound p\ow1_sound
					End If		
				End If					
			End If	
		End If	
		; regardless we are not airborne
		p\airborne = False
		p\jumping = False
		p\acc\y = 0
		p\vel\y = 0
		p\start_y_high = 0
		p\start_y_low = 0
		p\jumps=0
		; make sure we are not below the terrain
		p\pos\y = p\pos\y - p\hdiff
	End If			
	
	; add friction 
	If Not p\airborne
		If p\vel\z > 0 Then p\vel\z = p\vel\z - p\friction
		If p\vel\z < 0 Then p\vel\z = p\vel\z + p\friction			
		If Abs(p\vel\z) < 0.02 p\vel\z = 0 ; zero out low speed
		If p\vel\x > 0 Then p\vel\x = p\vel\x - p\friction
		If p\vel\x < 0 Then p\vel\x = p\vel\x + p\friction			
		If Abs(p\vel\x) < 0.02 p\vel\x = 0 ; zero out low speed
	End If
	
	; make sure we don't go out of bounds	
	KeepPlayerInsideLevel(p,t)
	
	; we don't have to be airborne to collide with platforms 
	; so let's do a collision check 
	
	; **********************
	; START COLLISION CHECKS 
	; **********************
		
	p\landed_platform = PlayerPlatformCollision(p)
	If p\landed_platform <> Null Then 
		p\jumping = False
		; we hit a platform
		; now we need to see where the collision took place
		; and do a collision response		
		Local px# = GetEntityX(p\pivot) ; player x
		Local pz# = GetEntityZ(p\pivot) ; player z
		Local plx# = GetEntityX(p\landed_platform\cyl\model) ; collided platform x
		Local plz# = GetEntityZ(p\landed_platform\cyl\model) ; collided platform z
		Local pr# = p\cyl\radius ; player collision cylinder radius 		
		Local plr# = p\landed_platform\cyl\radius ; landed platform collision cylinder radius 		
		Local distXZ# = Distance2D(px,pz,plx,plz) ; distance between player and platform horizontally
		Local rsum# = pr + plr ; sum of player and platform radiuses
		Local xzdiff# = rsum - distXZ ; how far we are into the platform horizontally
		
		Local player_top# = p\pos\y + p\cyl\height/2 ; the top of the players head
		Local platform_bottom# = GetEntityY(p\landed_platform\cyl\model) - p\landed_platform\cyl\height/2	; the bottom of the collided platform
		Local player_bottom# = p\pos\y - p\cyl\height/2 ; the bottom of the player's feet
		Local platform_top# = GetEntityY(p\landed_platform\cyl\model) + p\landed_platform\cyl\height/2	; the top landing surface of the collided platform
		Local head_into_platform# = player_top# - platform_bottom# ; distance our head is above the bottom of the platform
		Local feet_into_platform# = platform_top# - player_bottom# ; distance our feet are below the top of the platform
		
		If xzdiff < 1 Then 
			; we are likely just on the outside edge
			If Not p\climbing Then 
				If feet_into_platform > 1 Then 
					p\shimmying = True	
					MoveEntity p\view_model,0,0,p\landed_platform\cyl\radius+0.12	
					SetMouseZ -(p\landed_platform\cyl\radius + 8)
					p\airborne = False
				End If	
			End If	
		Else 
			; we are further in, let's check if we're hitting our head on the bottom
			If head_into_platform# < 1 Then 
				p\vel\y = -p\vel\y * 0.2 ; bounce a little
				p\acc\y = 0
				; prevent from sticking into the platform 					
				p\pos\y = p\pos\y-(head_into_platform#+0.1)
				; this should also cause damage
				p\health = p\health - 0.1
				; also play a bad ouch
				PlaySound p\ow2_sound
				p\landed_platform = Null
			End If
		End If					
		
		If (p\shimmying) Then 		
			p\pos\x = plx
			p\pos\y = platform_top - p\cyl\height * 0.85
			p\pos\z = plz			
		End If	
		
		If (p\climbing) Then 			
			p\pos\y = platform_top - p\cyl\height * 0.85				
		End If	
		
	End If	
	
	; implement gravity	
	If p\airborne Then						
	 	p\acc\y = p\acc\y + p\gravity			
	End If				
	
	; add accleration to velocity
	AddToVec3(p\vel,p\acc)	
	
	; update the model position based on corrected values
	SetEntityPosition p\pivot,p\pos\x,p\pos\y,p\pos\z	
	
	; check if we are dead
	If p\health < 0 Then 
		If p\dead = False Then 
			p\dead = True
			PlaySound p\death_sound
			p\anim = 0 
			p\anim_time = 0
			p\health = 0
			p\stamina = 0
		End If		
	End If	
	
	; if we are idle regain some health and stamina
	If p\anim=3 Then 
		p\stamina=p\stamina+0.01
		If p\stamina > 1.0	Then p\stamina = 1.0
		p\health=p\health+0.0001
		If p\health > 1.0	Then p\health = 1.0
	End If
	
	If p\climbing
		If p\anim_time > (117 * 0.0333) Then 
			p\climbing = False
			p\anim=3
			p\vel\z = p\speed\z
		End If	
	End If		
		
	; animate the view model 	
	If p\anim=4 Then 
		; jumping needs to be stopped and started
		; 0.44 is the mid air look
		; stay there as long as we're above the ground
		If (p\anim_time > 0.44 And p\hdiff > 3) Then p\anim_time = 0.44
		AnimateModel p\view_model,p\anim,p\anim_time,1,1
	Else 
		If p\dead Or p\climbing Then 
			AnimateModel p\view_model,p\anim,p\anim_time,1,1
		Else 
			AnimateModel p\view_model,p\anim,p\anim_time,2,1
		End If
	End If			
	p\anim_time=p\anim_time+0.02			
	GetPlayerInput p,cam,piv
End Function 

Function GetPlayerInput(p.Player,cam,piv)
	If (p\airborne=False And p\dead=False And p\shimmying=False And p\climbing=False) Then			
		If (IsKeyDown(340) Or IsKeyDown(344)) Then ; SHIFT to sprint 
			If p\stamina > 0.0 Then 
				sprint#=p\sprint_speed 
				p\stamina = p\stamina - 0.001
				If p\stamina < 0 Then 
					p\stamina = 0
					sprint=0.0
				End If	
			End If	
		Else 
			sprint#=0.0 
		End If
		; strafe is prioritized first
		; this way we cant add forward motion in mid-strafe
      If (IsKeyDown(65) Or IsKeyDown(263)) Then ; a or left
			If p\face_forward Then 			
				p\vel\x = -(p\speed\x + sprint) 
				; walk left animation
				If sprint > 0 Then p\anim = 8 Else p\anim=13 				
			Else 
				p\vel\x = p\speed\x + sprint 
				; walk right animation 
				If sprint > 0 Then p\anim = 9 Else p\anim=14
			End If				
		ElseIf (IsKeyDown(68) Or IsKeyDown(262)) Then ; d or right
			If p\face_forward Then 
				p\vel\x = p\speed\x + sprint
				; walk right animation 
				If sprint > 0 Then p\anim = 9 Else p\anim=14
			Else 
				p\vel\x = -(p\speed\x + sprint)
				; walk left animation 
				If sprint > 0 Then p\anim = 8 Else p\anim=13 
			End If	
		ElseIf	 (IsKeyDown(87) Or IsKeyDown(265)) Then 
			p\vel\z = p\speed\z + sprint ; w or up
			If sprint = 0 Then p\anim = 10 Else p\anim=7
			p\face_forward = True
		ElseIf (IsKeyDown(83) Or IsKeyDown(264)) Then 
			p\vel\z = p\speed\z + sprint ; s or down	
			If sprint = 0 Then p\anim = 10 Else p\anim=7
			p\face_forward = False
		Else 
			p\anim = 3 ; idle
		End If		
	End If	
	
	If p\shimmying Then 
		If (IsKeyDown(68) Or IsKeyDown(262)) Then ; d or right
			TurnEntity p\pivot,0,0.12,0
			p\anim = 6
		ElseIf (IsKeyDown(65) Or IsKeyDown(263)) Then ; a or left
			TurnEntity p\pivot,0,-0.12,0
			p\anim = 5
		ElseIf (IsKeyDown(83) Or IsKeyDown(264)) Then 
			; drop down off this platform
			p\shimmying = False
			SetEntityPosition p\pivot, GetEntityX(p\view_model), GetEntityY(p\view_model), GetEntityZ(p\view_model)
			MoveEntity p\pivot,0,0,-1			
			SetEntityPosition p\view_model,0,-p\cyl\height/2,0			
			p\landed_platform = Null
		Else 	
			p\anim = 2
		End If	
	End If			
	
	If IsKeyDown(32) Then ; SPACE is held down 
		If p\jumping = True Then 
			; this means we are still holding it down 
			If p\vel\y > 0 Then 
				; give a little boost
				p\vel\y = p\vel\y + 0.01
			End If	
		End If		
	End If 
	
	; to revive just press jump	
	If (IsKeyHit(32) And p\dead) Then 
		p\dead=False
		p\health = 1.0
		p\stamina = 1.0
	End If	
		
	If (p\dead=False And p\climbing=False) Then 
		; jump button SPACE
		If IsKeyHit(32) Then	
			If p\shimmying Then 
				; exit shimmy mode
				p\shimmying = False	
				p\climbing = True
				p\anim = 1
				p\anim_time = 0.0
				SetEntityPosition p\pivot, GetEntityX(p\view_model), GetEntityY(p\view_model), GetEntityZ(p\view_model)				
				SetEntityPosition p\view_model,0,-p\cyl\height/2,0
			Else 						
				; check if velocity is close to zero
				; if so the timing is right 
				; and we allow another jump
				If p\vel\y > -0.1 And p\vel\y < 0.1 Then  
					p\jumps = p\jumps + 1
					p\vel\y = p\vel\y + (p\speed\y * p\jumps)				
					p\airborne = True
					p\anim = 4
					p\anim_time = 0.0
					PlaySound p\jump_grunt_sound
					p\jumping = True 
				End If	
			End If			
		End If		
	End If 	
	
	; mouse input
	ThirdPersonMouseInputGame p,cam,piv
End Function 	

Function ThirdPersonMouseInputGame(p.Player,cam,piv)	
	SetEntityPosition piv,GetEntityX(p\pivot),GetEntityY(p\pivot),GetEntityZ(p\pivot)
	; adjust for mousewheel
	SetEntityPosition cam,0,0,GetMouseZ()	
	; moving the mouse left and right 
	; rotates the pivot clockwise and counter clockwise
	TurnEntity piv, 0, -GetMouseVX() * p\turn_speed, 0
	; moving the mouse up and down
	; changes the pivots pitch
	; when the player moves forward 
	; the pivot should follow 
	; and rotate behind the player using a lerp
	; we need to work on this
	TurnEntity piv, -GetMouseVY() * p\turn_speed, 0, 0	
	CorrectEntityRoll(piv)
	ClampEntityPitch(piv,0,-70)
End Function