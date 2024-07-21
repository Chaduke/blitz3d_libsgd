; trees.bb
; by Chaduke
; 20240531

; this code contains everything related to trees 
; at this point this is basic funtionality to add static objects and place them in a scene

Global current_tree.Tree

Type Tree
	Field name$
	Field model	
	Field scale#	
End Type	

Type SolidTree
	Field name$
	Field model	
	Field leaf_cyl.Cylinder 
	Field trunk_cyl.Cylinder
End Type	

Dim treeMeshes(4)

; make sure we are not outside the terrain limits
Function KeepTreeInsideLevel(t.Terrain)
	
	; get our terrain values	
	Local tw = t\width
	Local td = t\depth
	
	; check for zero trees
	If current_tree = Null Then 
		my_palm_tree = LoadMesh("../engine/assets/models/trees/my_palm_tree.glb")
		AddOneTree "Our First Palm Tree",my_palm_tree,tw/2,GetTerrainHeight(tw/2,td/2,t),td/2,1
		current_tree = First Tree
	End If 	
	
	; get our current location 
	Local tlx# = GetEntityX(current_tree\model)
	Local tly# = GetEntityY(current_tree\model)		
	Local tlz# = GetEntityZ(current_tree\model)	
	
	; correct if outside the terrain
	If tlx<2 Then tlx = 2
	If tlx>tw-2 Then tlx = tw-2
	If tlz<2 Then tlz = 2
	If tlz>td-2 Then tlz = td-2
	
	; keep tree within reasonable terrain height limits
	Local th# = GetTerrainHeight(tlx,tlz,t)		
	If tly > th + 0.2 Then tly = th + 0.2
	If tly < th - 0.2 Then tly = th - 0.2
	
	SetEntityPosition current_tree\model,tlx,tly,tlz
	SetEntityPosition pivot,tlx,tly + 2,tlz
End Function

Function EditTrees(t.Terrain)

	ThirdPersonMouseInputEditor(camera,pivot)
	ClampEntityPitch(pivot,70,-70)
	KeepTreeInsideLevel(t)
	KeyboardInputThirdPerson(current_tree\model,0.05)	
		
	; left bracket or Z
	If (IsKeyHit(91) Or IsKeyHit(90)) Then		
		; switch to previous 
		current_tree = Before(current_tree)
		; check if null
		If current_tree = Null Then 
			; switch to last 
			current_tree = Last Tree
		End If		
	End If
		
	; right bracket or C
	If (IsKeyHit(93) Or IsKeyHit(67)) Then		
		; switch to next
		current_tree = After(current_tree)
		; check if null
		If current_tree = Null Then 
			; switch to first
			current_tree = First Tree
		End If		
	End If	
	
	; numpad minus or left alt scales down 
	If (IsKeyHit(333) Or IsKeyHit(342)) Then 
		current_tree\scale# = current_tree\scale - 0.1
		If current_tree\scale < 0.1 Then current_tree\scale = 0.1
		SetEntityScale current_tree\model, current_tree\scale#,current_tree\scale#,current_tree\scale#
	End If	
	
	; numpad plus or SPACE scales up
	If (IsKeyHit(334) Or IsKeyHit(32)) Then 
		current_tree\scale# = current_tree\scale + 0.1
		If current_tree\scale# > 3 Then current_tree\scale# = 3
		SetEntityScale current_tree\model, current_tree\scale#,current_tree\scale#,current_tree\scale#
	End If	
	
	; INSERT or left mouse adds a new tree
	If (IsKeyHit(260) Or IsMouseButtonHit(0)) Then 
		Local count = 0
		Local i.Tree
		For i.Tree = Each Tree
			count = count + 1
		Next 	
		my_palm_tree = LoadMesh("../engine/assets/models/trees/my_palm_tree.glb")
		Local n$ = "Tree Number " + (count + 1)
		AddOneTree n$, my_palm_tree, GetEntityX(pivot)+1, GetEntityY(pivot), GetEntityZ(pivot),1
		current_tree = Last Tree
	End If
	
	; DELETE or Right Mouse deletes the current tree
	If (IsKeyHit(261) Or IsMouseButtonHit(1)) Then 
		DestroyEntity current_tree\model 
		Delete current_tree	
		; check for zero trees		
		current_tree = Last Tree
		If current_tree = Null Then 
			my_palm_tree = LoadMesh("../engine/assets/models/trees/my_palm_tree.glb")
			; get our terrain values			
			Local tw = t\width
			Local td = t\depth

			AddOneTree "Our First Palm Tree",my_palm_tree,tw/2,GetTerrainHeight(tw/2,td/2,t),td/2,1
			current_tree = First Tree
		End If	
	End If
	
	Draw2DText "Current Tree Name : " + current_tree\name$,10,30
	
End Function 

Function AddOneTree(name$,mesh,x#,y#,z#,scale#)
	t.Tree = New Tree
	t\name$ = name$
	t\model = CreateModel(mesh)
	SetEntityPosition t\model,x,y,z	
	SetEntityScale t\model,scale,scale,scale
	t\scale# = scale
	TurnEntity t\model,0,Rand(360),0
End Function 

Function AddOneSolidTree(name$,mesh,t.Terrain,x#,z#,material,debug=False)
	st.SolidTree = New SolidTree
	st\name$ = name$
	st\model = CreateModel(mesh)	
	; SetEntityScale t\model,scale#,scale#,scale#
	; t\scale# = scale
	TurnEntity st\model,0,Rand(360),0
	st\trunk_cyl = New Cylinder
	st\trunk_cyl\height = 3  
	st\trunk_cyl\radius = .25
	trunk_collider_mesh =  CreateCylinderMesh(st\trunk_cyl\height,st\trunk_cyl\radius,8,material)
	st\trunk_cyl\model = CreateModel(trunk_collider_mesh)		
	SetEntityParent st\trunk_cyl\model,st\model
	MoveEntity st\trunk_cyl\model,0,1.5,0	
	st\leaf_cyl = New Cylinder
	st\leaf_cyl\height = 3.5
	st\leaf_cyl\radius = 2
	leaf_collider_mesh =  CreateCylinderMesh(st\leaf_cyl\height,st\leaf_cyl\radius,8,material)		
	st\leaf_cyl\model = CreateModel(leaf_collider_mesh)	
	SetEntityParent st\leaf_cyl\model,st\model	
	MoveEntity st\leaf_cyl\model,0,4.75,0
	Local y# = GetTerrainHeight(x,z,t)
	SetEntityPosition st\model,x#,y#,z#
	If debug = False Then SetEntityVisible st\trunk_cyl\model,False
	If debug = False Then SetEntityVisible st\leaf_cyl\model,False	
End Function 


Function GenerateSolidTrees(t.Terrain,numtrees,material)
	tree_mesh = LoadMesh("../engine/assets/models/trees/tree1.glb")
	SetMeshShadowCastingEnabled tree_mesh,True
	For i = 0 To numtrees 
		Local tx# = Rand(2,t\width-2)
		Local tz# = Rand(2,t\depth-2)		
		AddOneSolidTree "Collider Tree " + i,tree_mesh,t,tx,tz,material	
	Next
End Function 

Function GenerateTrees(t.Terrain,numtrees,plus_fence = False) 

	Local tw=t\width
	Local td=t\depth

	treeMeshes(0) = LoadMesh("../engine/assets/models/trees/low_poly.glb")
	SetMeshShadowCastingEnabled treeMeshes(0),True
	treeMeshes(1) = LoadMesh("../engine/assets/models/trees/low_poly.glb")
	SetMeshShadowCastingEnabled treeMeshes(1),True
	treeMeshes(2) = LoadMesh("../engine/assets/models/trees/low_poly.glb")
	SetMeshShadowCastingEnabled treeMeshes(2),True	
	; randomly distribute trees on the terrain
	For i=0 To numtrees			
		tx# = Rand(2,tw-2)
		tz# = Rand(2,td-2)	
		ty# = GetTerrainHeight(tx,tz,t) - 0.3		
		rs# = Rnd(2) + 0.5		
		
		AddOneTree("Tree #" + i,treeMeshes(Rand(0,2)),tx,ty,tz,rs)		
	Next
	
	If plus_fence Then
		; create a "tree fence" around the outside edges of the terrain	
		
		For x = 0 To tw									
			tx = x			
			tz = 2		
			ty# = GetTerrainHeight(tx,tz,t)
			rs# = Rnd(2) + 0.5
			AddOneTree("Tree #" + i,treeMeshes(Rand(0,2)),tx,ty,tz,rs)
			
			tz = td-2
			ty# = GetTerrainHeight(tx,tz,t)
			rs# = Rnd(2) + 0.5
			AddOneTree("Tree #" + i,treeMeshes(Rand(0,2)),tx,ty,tz,rs)			
		Next
		For z = 0 To td				
			tx = 2
			tz = z
			ty# = GetTerrainHeight(tx,tz,t)
			rs# = Rnd(2) + 0.5
			AddOneTree("Tree #" + i,treeMeshes(Rand(0,2)),tx,ty,tz,rs)							
				
			tx = tw-2
			ty# = GetTerrainHeight(tx,tz,t)
			rs# = Rnd(2) + 0.5
			AddOneTree("Tree #" + i,treeMeshes(Rand(0,2)),tx,ty,tz,rs)
		Next			
	End If	
End Function