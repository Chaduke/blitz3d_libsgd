; collision_tests.bb
; by Chaduke 
; 20240711

Include "../engine/testapp.bb"
Include "../engine/assets/prefabs/ferris_wheel/ferris_wheel.bb"
; Include "../runner/turtle.bb"
Include "../engine/environment.bb"

; animations are 

; 0 - dancing
; 1 - falling
; 2 - idle
; 3 - jumping
; 4 - running 
; 5 - sliding 

Type Player
	Field view_model
	Field pivot 
	Field collision_model
	Field collider
	Field acc.Vec3
	Field vel.Vec3	
	Field dancing,falling,idle,jumping,running,sliding		
	Field nseq ; new animation sequence
	Field seq0,time0#,time0Step#	;base animation
	Field seq1,time1#,time1Step#	;blend target
	Field blend#,blendStep#
End Type 

Function CreatePlayer.Player()
	DisplayLoadingMessage "Creating Player..."
	Local p.Player = New Player
	p\pivot = CreateModel(0)
	p\view_model = LoadBonedModel("../engine/assets/models/runner/aj_animated.glb",True)
	SetMeshShadowsEnabled GetModelMesh(p\view_model),True	
	SetEntityParent p\view_model,p\pivot 	
	Local collision_mesh = CreateSphereMesh(0.5,16,16,GetCollisionMaterial())	
	p\collision_model = CreateModel(collision_mesh)
	SetEntityParent p\collision_model,p\pivot	
	MoveEntity p\pivot,0,0.5,0 ; prevent player from falling thru the ground on game start
	MoveEntity p\view_model,0,-0.5,0
	p\collider = CreateSphereCollider(p\pivot,1,0.5)
	p\acc = CreateVec3("Player Acceleration",0,-0.006,0) ; Y value is gravity
	p\vel = CreateVec3("Player Velocity",0,0,-0.095) ; Z value is run speed
	p\jumping = False
	p\idle = True 
	p\nseq = 2 ; idle		
	Return p
End Function 

; Init
t.TestApp = CreateTestApp(True,"Collision Tests")
e.Environment = CreateEnvironment(t\gui_font)

ground1 = CreateGround()
ground1_collider = CreateMeshCollider(ground1,0,GetModelMesh(ground1))
AddTrees ground1
AddRocks ground1
; AddGrass ground1
; AddTurtles ground1
MoveEntity ground1,0,0,256

ground2 = CreateGround()
ground2_collider = CreateMeshCollider(ground2,0,GetModelMesh(ground2))
AddTrees ground2
AddRocks ground2
; AddGrass ground2
; AddTurtles ground2

ground3 = CreateGround()
ground3_collider = CreateMeshCollider(ground3,0,GetModelMesh(ground3))
AddTrees ground3
AddRocks ground3
; AddGrass ground3
; AddTurtles ground3
MoveEntity ground3,0,0,-256

p.Player = CreatePlayer()
SetEntityVisible p\collision_model,False
f.FerrisWheel = CreateFerrisWheel()
MoveEntity f\base_pivot,0,0,200
EnableCollisions 1,0,2

SetMouseZ -8

While t\loop
	BeginFrame t	
	
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	If e\gui\v Then 
		SetMouseCursorMode 1 
		SetEntityPosition t\camera,0,0,GetMouseZ()
		UpdateEnvironment e,GUIDragCheck()		
		DrawAllGUIs()
	Else 
		SetMouseCursorMode 3
		ThirdPersonMouseInputEditor t\camera,t\pivot,1,0.02			
	End If 
		
	If (p\running Or p\jumping) Then 
		AddToVec3 p\vel,p\acc ; add acceleration to velocity
		MoveEntityVec3 p\pivot,p\vel	; move player according to its velocity
		SetEntityPosition t\pivot,GetEntityX(p\pivot),GetEntityY(p\pivot),GetEntityZ(p\pivot)
		If IsMouseButtonDown(0) = False Then 			
			SetEntityRotation p\pivot,0,GetEntityRY(t\pivot)-180,0	
		End If 	
	End If 	

	; in order to create an endless ground to run on	
	; test the distance of player z location with ground 1,2 and 3
	; if > 384 or < -384 move the ground 768 units
		
	distance1# = GetEntityZ(p\pivot) - GetEntityZ(ground1) 
	If 	distance1 < -384 Then MoveEntity ground1,0,0,-768
	If 	distance1 > 384 Then MoveEntity ground1,0,0,768

	distance2# = GetEntityZ(p\pivot) - GetEntityZ(ground2)
	If 	distance2 < -384 Then MoveEntity ground2,0,0,-768
	If 	distance2 > 384 Then MoveEntity ground2,0,0,768
	
	distance3# = GetEntityZ(p\pivot) - GetEntityZ(ground3) 
	If 	distance3 < -384 Then MoveEntity ground3,0,0,-768
	If 	distance3 > 384 Then MoveEntity ground3,0,0,768
				
	UpdateColliders()	
	c = GetCollisionCount(p\collider)
	
	If c > 0 Then 
		p\jumping = False
		p\vel\y = 0	
		p\running = True 		
	End If 
	
	; start / stop running
	If IsKeyHit(KEY_SPACE) Then 
		If p\running = False Then 
			p\running = True 
			p\idle = False
			p\nseq = 4
			SetEntityRotation p\pivot,0,GetEntityRY(t\pivot)-180,0			
		Else 
			p\running = False
			p\idle = True
			p\nseq = 2
		End If 
	End If 	
	
	; start / stop dancing 			
	If IsKeyHit(KEY_D) Then 
		If p\dancing = True Then 
			p\dancing = False
			p\idle = True 
			p\nseq = 2
		Else 
			p\dancing = True
			p\idle = False
			p\running = False		
			p\nseq = 0
		End If 
	End If 					
	; jump	
	If IsMouseButtonHit(1) Then 
		If p\jumping = False Then 
			p\jumping = True 
			p\running = False
			p\vel\y = 0.2 ; jump power		
			p\nseq = 3	
		End If 
	End If 		
		
	If p\jumping Then
		; find out in anim_time exceeds the mid jump pose 
		; if so set it there until we are close enough to the ground 
		; to do landing prep, then blend into a run 		
		If (p\time0 > 0.0333 * 12) Then 
			If GetEntityY(p\pivot) > 1 Then 
				p\time0 = 0.0333 * 12
			Else 
				p\nseq = 4
			End If		
		End If	
	End If 		
	UpdateFerrisWheel f			
	ProcessPlayerAnimation p
	
	; For tt.Turtle = Each Turtle 
	; 	UpdateTurtle tt
	; Next
	 
	CustomEndFrame t	

	; collider stuff	
	Draw2DText "Player Collider Count : " + c,5,5
	If c > 0 Then 
		Draw2DText "Player Y Collision Coord : " + GetCollisionY(p\collider,0),5,25
	End If 	
	Draw2DText "Distances 1,2, and 3 : " + Floor(distance1) + " | " + Floor(distance2) + " | " + Floor(distance3),5,45
	Draw2DText "Player Pivot RX,RY,RZ : " + Floor(GetEntityRX(p\pivot)) + " | " + Floor(GetEntityRY(p\pivot)) + " | " + Floor(GetEntityRZ(p\pivot)),5,65
	Draw2DText "Camera Pivot RX,RY,RZ : " + Floor(GetEntityRX(t\pivot)) + " | " + Floor(GetEntityRY(t\pivot)) + " | " + Floor(GetEntityRZ(t\pivot)),5,85
	Draw2DText "Camera RX,RY,RZ : " + Floor(GetEntityRX(t\camera)) + " | " + Floor(GetEntityRY(t\camera)) + " | " + Floor(GetEntityRZ(t\camera)),5,105
	Present()
Wend 

Function ProcessPlayerAnimation(p.Player) 

	;Change to new anim
	If p\blendStep = 0 And p\seq0 <> p\nseq		
		p\time0Step = 0
		p\seq1 = p\nseq
		p\time1 = 0
		p\time1Step = 0.02
		p\blendStep = .05
	End If 	
	
	;Update blend state
	p\blend = p\blend + p\blendStep
	If p\blend >= 1
		p\seq0 = p\seq1
		p\time0  = p\time1
		p\time0Step = p\time1Step
		p\time1 = 0
		p\time1Step = 0
		p\blend = 0
		p\blendStep = 0
	EndIf
	
	;Set base animation
	p\time0 = p\time0 + p\time0Step
	AnimateModel p\view_model,p\seq0,p\time0,2,1		
	
	;Appy blend
	p\time1 = p\time1 + p\time1Step
	AnimateModel p\view_model,p\seq1,p\time1,2,p\blend	
	
End Function 

Function CustomEndFrame(t.TestApp)			
	RenderScene()
	Clear2D()		
	Set2DTextColor 1,1,0,1
	If t\quit Then DisplayTextCenter "Quit? Y to confirm | ESC to Cancel",t\font	
	Set2DFont t\gui_font	
	msg$ = "FPS : " + Floor(GetFPS())
	msg$ = msg$ + " | RPS : " + GetRPS()
	msg$ = msg$ + " | MouseZ " + GetMouseZ()
	Draw2DText msg$,5,GetWindowHeight() - 20		
End Function

Function AddGrassClump(ground,tree,images[5])
	; create a clump of grass / weeds around it 
	For j = 0 To 18	 
		Local r# = Rnd(1)
		If r# > 0.97 Then 
			i = 5
		Else If r# > 0.94 Then 
			i = 4	
		Else If r# > 0.9 Then 
			i = 3	
		Else If r# > 0.7 Then  	
			i = 1
		Else If r# > 0.3 Then 
			i = 2
		Else 
			i = 0
		End If		
		sprite = CreateSprite(images[i])		
		SetEntityParent sprite,ground		
		SetEntityPosition sprite,GetEntityX(tree),0,GetEntityZ(tree)
		SetEntityRotation sprite,0,Rnd(360),0
		Local sc# = Rnd(1) + 1
		SetEntityScale sprite,sc,sc,sc
		MoveEntity sprite,0,0.4,Rnd(0.5,1.5)
	Next 
End Function 

Function AddTrees(ground)
	DisplayLoadingMessage "Adding Trees ..."
	Local tree_mesh = LoadMesh("../engine/assets/models/trees/tree1.glb")
	SetMeshShadowsEnabled tree_mesh,True
	Local birch_mesh = LoadMesh("../engine/assets/models/trees/birch_tree1.glb")
	SetMeshShadowsEnabled birch_mesh,True
	
	Local images[5]	
	images[0] = LoadImage("../engine/assets/textures/foliage/grass1.png",1)
	images[1] = LoadImage("../engine/assets/textures/foliage/grass2.png",1)
	images[2] = LoadImage("../engine/assets/textures/foliage/weeds.png",1)
	images[3] = LoadImage("../engine/assets/textures/foliage/daisies.png",1)
	images[4] = LoadImage("../engine/assets/textures/foliage/wildflower_blue.png",1)
	images[5] = LoadImage("../engine/assets/textures/foliage/wildflower_red.png",1)
	
	; For i = 0 To 3
	; 	SetImageSpriteViewMode images[i],3
	; Next 	
	
	Local tree,sprite
	For i = 0 To 80
		If Rnd(1) > 0.5 Then 
			tree = CreateModel(tree_mesh)
		Else 
			tree = CreateModel(birch_mesh)
		End If 
		SetEntityParent tree,ground
		Local sc# = Rnd(10) + 0.5
		ScaleEntity tree,sc,sc,sc		
		SetEntityPosition tree,Rand(-10,-120),0,Rand(-128,128)
		SetEntityRotation tree,0,Rand(360),0	
		AddGrassClump ground,tree,images	
	Next 	
	
	For i = 0 To 80
		If Rnd(1) > 0.5 Then 
			tree = CreateModel(tree_mesh)
		Else 
			tree = CreateModel(birch_mesh)
		End If 
		SetEntityParent tree,ground
		sc# = Rnd(10) + 0.5
		ScaleEntity tree,sc,sc,sc		
		SetEntityPosition tree,Rand(10,120),0,Rand(-128,128)
		SetEntityRotation tree,0,Rand(360),0	
		AddGrassClump ground,tree,images	
	Next  
End Function

Function AddTurtles(ground)
	DisplayLoadingMessage "Adding Turtles..."		
	; Local t.Turtle
	Local i
	For i = 0 To 2
		; t = CreateTurtle(ground)			
		; SetEntityPosition turtle,Rnd(-64,64),0,Rnd(-128,128)
		; rs# = Rnd(1) + 0.5
		; SetEntityScale turtle,rs,rs,rs
		; TurnEntity turtle,0,Rand(360),0		
	Next		
End Function  

Function AddRocks(ground)
	DisplayLoadingMessage "Adding Rocks..."	
	Local rock_mesh=LoadMesh("../engine/assets/models/runner/rock.glb")	
	SetMeshShadowsEnabled rock_mesh,True
	
	Local rock
	For z = -128 To 128 Step 4			
		rock = CreateModel(rock_mesh)	
		SetEntityParent rock,ground	
		SetEntityPosition rock,Rand(-64,-128),0,z
		rs# = Rnd(3) + 0.5
		SetEntityScale rock,rs,rs,rs
		TurnEntity rock,0,Rand(360),0						
		rock = CreateModel(rock_mesh)	
		SetEntityParent rock,ground		
		SetEntityPosition rock,Rand(64,128),0,z
		rs# = Rnd(3) + 0.5
		SetEntityScale rock,rs,rs,rs
		TurnEntity rock,0,Rand(360),0
	Next	
End Function 

Function AddGrass(ground)
	DisplayLoadingMessage "Adding Grass..."
	
	Local foliage[2] 
	foliage[0] = LoadImage("../engine/assets/textures/foliage/grass1.png",1)
	foliage[1] = LoadImage("../engine/assets/textures/foliage/weeds.png",1)
	foliage[2] = LoadImage("../engine/assets/textures/foliage/grass2.png",1)

	Local i
	Local sprite 
	For i = 0 To 1000
		sprite = CreateSprite(foliage[Rand(0,2)])		
		SetEntityParent sprite,ground
		SetEntityPosition sprite,Rnd(-64,64),0.4,Rnd(-128,128)		
	Next 			
End Function 