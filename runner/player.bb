; player.bb
; by Chaduke
; 20240710 

; player for endless runner game 

Type Player
	Field model
	Field pivot
	Field vel.Vec3	
	Field acc.Vec3
	Field collider
	Field collider_model
	Field anim
	Field anim_time#
	Field light
	Field jumping,running,sliding,dying 
	Field health	
	Field apex_reached
End Type 

Function CreatePlayer.Player()
	Local p.Player = New Player
	p\model = LoadBonedModel("../engine/assets/models/runner/aj_animated.glb",True)
	Local mesh= GetModelMesh(p\model)
	SetMeshShadowCastingEnabled mesh,True
	p\pivot = CreateModel(0)
	SetEntityParent p\model,p\pivot 	
	p\vel = CreateVec3("Player Velocity",0,0,-0.1)
	p\acc = CreateVec3("Player Acceleration",0,0,0)
	p\collider = CreateEllipsoidCollider(p\model,0,0.57,.91)
	p\running = True 
	p\anim = 1 ; running
	p\anim_time = 0.0	
	; p\light = CreatePointLight()
	; SetLightShadowMappingEnabled p\light,True 
	; SetEntityParent p\light,p\pivot	
	; MoveEntity p\light,0,3,0	
	; SetLightRange p\light,10
	Return p
End Function 

Function UpdatePlayer(p.Player,t.Terrain)

	; Update player motion 
	AddToVec3 p\vel,p\acc
	MoveEntity p\pivot,p\vel\x,p\vel\y,p\vel\z
	
	; turn when we come to an edge
	If GetEntityZ(p\pivot) < 15 Then 
		SetEntityPosition p\pivot,GetEntityX(p\pivot),GetEntityY(p\pivot),16
		TurnEntity p\pivot,0,90,0		
	End If 	
	If GetEntityZ(p\pivot) > 240 Then 
		SetEntityPosition p\pivot,GetEntityX(p\pivot),GetEntityY(p\pivot),239
		TurnEntity p\pivot,0,90,0		
	End If
	If GetEntityX(p\pivot) < 15 Then 
		SetEntityPosition p\pivot,16,GetEntityY(p\pivot),GetEntityZ(p\pivot)
		TurnEntity p\pivot,0,90,0		
	End If 
	If GetEntityX(p\pivot) > 240 Then 
		SetEntityPosition p\pivot,239,GetEntityY(p\pivot),GetEntityZ(p\pivot)
		TurnEntity p\pivot,0,90,0		
	End If 	
	
	If p\sliding MoveEntity p\model,0,0,0.065
	
	; running or sliding
	If (p\jumping = False) Then 
		; in these cases don't let us get above or below the terrain
		ClampEntityAboveTerrain p\pivot,t,0	
	Else 
		; we're jumping, just make sure we stay above
		KeepEntityAboveTerrain p\pivot,t,0			
		; compensate for the animation horizontal movement
		If p\apex_reached = False Then MoveEntity p\model,0,0,0.075		
	End If 		
		
	; jump	
	If (IsKeyDown(KEY_UP) Or IsMouseButtonDown(0)) Then 		
		If (p\running And p\jumping = False) Then 			
			p\running = False
			p\jumping = True  
			p\acc\y = -0.004
			p\vel\y = 0.1
			p\anim=0	
			p\anim_time = 0.0			
		Else 
			; a little extra jump boost
			If p\apex_reached = False Then p\vel\y = p\vel\y + 0.001 
		End If 			
	End If 
	
	; get our height off the ground for calculations
	terrain_height# = GetTerrainHeight(GetEntityX(p\pivot),GetEntityZ(p\pivot),t)	
	hdiff# = 	GetEntityY(p\pivot) - terrain_height

	; slide			
	If (IsKeyDown(KEY_DOWN) Or IsMouseButtonDown(1)) Then 
		If (p\running Or (p\jumping And hdiff < 0.1)) Then			
			p\anim=2	
			p\anim_time = 0.0
			p\sliding = True 
			p\running = False
			p\jumping = False
			p\vel\y = 0 ; zero out velocity
			p\acc\y = 0 ; zero out gravity
			p\apex_reached = False
			SetEntityPosition p\model,0,0,0					
		End If 
	End If 
	
	; if jumping ends go back to running
	If p\jumping Then 
		; check if we have reached apex
		If (p\anim_time > 11 * 0.0333 And p\apex_reached = False) Then  
			p\anim_time = 11 * 0.0333
			p\apex_reached = True 	
		End If 			
		If p\anim_time > 28 * 0.0333	Then 	
			p\anim = 1
			p\anim_time = 0.0
			SetEntityPosition p\model,0,0,0	
			p\vel\y = 0 ; zero out velocity
			p\acc\y = 0 ; zero out gravity
			p\apex_reached = False
			p\jumping = False
			p\running = True 
		End If 	
	End If 	
	
	; if sliding ends go back to running	
	If (p\sliding And p\anim_time > 47 * 0.0333) Then 
		p\anim = 1
		p\anim_time = 0.0		
		SetEntityPosition p\model,0,0,0	
		p\sliding = False
		p\running = True 
	End If 	
		
	AnimateModel p\model,p\anim,p\anim_time,2,1.0
	; if we're jumping and reached apex 
	; don't advance the animation time unless we're close to the ground
	If (p\jumping And p\apex_reached) Then 
		; check our distance from the ground 		
		If hdiff# < 0.2 Then 
			p\anim_time = p\anim_time + 0.02
			; compensate for animation
			MoveEntity p\model,0,0,0.075
		End If 			
	Else 
		p\anim_time = p\anim_time + 0.02
	End If 
			
End Function 

Function EndFrameCustom(g.GameApp,p.Player)			
	RenderScene()
	Clear2D()	
	If g\quit Then 
		DrawTitle g
		Set2DTextColor 1,0,0,1
		DisplayTextCenter "Quit? Y to confirm | ESC to Cancel",g\font
	End If 		
	Set2DFont g\gui_font	
	Draw2DText "Player Position : " + GetEntityX(p\pivot) + "," + GetEntityY(p\pivot) + "," + GetEntityZ(p\pivot),5,5
	Draw2DText "Player Model Position : " + GetEntityX(p\model) + "," + GetEntityY(p\model) + "," + GetEntityZ(p\model),5,25
	Draw2DText "Pivot Position : " + GetEntityX(g\pivot) + "," + GetEntityY(g\pivot) + "," + GetEntityZ(g\pivot),5,45
	Draw2DText "Camera Position : " + GetEntityX(g\camera) + "," + GetEntityY(g\camera) + "," + GetEntityZ(g\camera),5,65
 
	msg$ = "FPS : " + Floor(GetFPS())
	msg$ = msg$ + " | RPS : " + GetRPS()
	msg$ = msg$ + " | MouseZ " + GetMouseZ()
	Draw2DText msg$,5,GetWindowHeight() - 20
	Present()	
End Function	