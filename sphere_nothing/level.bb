; level.bb
; by Chaduke
; 20240718 

Type Level
	Field width
	Field height
	Field startpos.Vec3
	Field endpos.Vec3
	Field checkpoints.Vec3[2]
	Field checkpoint_material	
	Field cells.Vec3[10000] 		
	Field cellcount
	Field ground_material
	Field start_material 
	Field end_material	
	Field env.Environment
End Type

Function ClearLevel(g.GameApp,p.Player)
	ClearScene()
	g\camera = CreatePerspectiveCamera()
	g\pivot = CreateModel(0)
	SetEntityParent g\camera,g\pivot 
	g\font = LoadFont("../engine/assets/fonts/bb.ttf",24)
	g\gui_font = LoadFont("../engine/assets/fonts/digital-7.ttf",16)	
	g\browser_font = LoadFont("../engine/assets/fonts/ds.ttf",14)
	g\start_menu = CreateStartMenu()	
	Local material = LoadPBRMaterial("../engine/assets/materials/Marble012_1K-JPG")
	Local mesh=CreateSphereMesh(0.5,16,16,material)
	SetMeshShadowCastingEnabled mesh,True
	p\model = CreateModel(mesh)	
	p\pivot = CreateModel(0)
	SetEntityParent p\model,p\pivot 	
	p\light = CreatePointLight()
	SetLightShadowMappingEnabled p\light,True 
	SetEntityParent p\light,p\pivot	
	MoveEntity p\light,0,3,0	
	SetLightRange p\light,10	
End Function 

Function BuildLevel(l.Level)
	l\ground_material = LoadPBRMaterial("../engine/assets/materials/Moss002_1K-JPG")
	Local ground_mesh=CreateSphereMesh(0.5,16,16,l\ground_material)
	
	l\start_material = LoadPBRMaterial("../engine/assets/materials/Tiles060_1K-JPG")
	start_mesh=CreateSphereMesh(0.5,16,16,l\start_material)

    l\end_material = LoadPBRMaterial("../engine/assets/materials/Tiles074_1K-JPG")
	end_mesh=CreateSphereMesh(0.5,16,16,l\end_material)
	
	l\checkpoint_material=LoadPBRMaterial("../engine/assets/materials/Tiles122_1K-JPG")
	checkpoint_mesh=CreateSphereMesh(0.5,16,16,l\checkpoint_material)	
	
	For i = 0 To l\cellcount - 1
		Local model = CreateModel(ground_mesh)
		MoveEntity model,l\cells[i]\x,l\cells[i]\y,l\cells[i]\z
		Local collider = CreateSphereCollider(model,1,0.49)
	Next 
	
	model = CreateModel(start_mesh)
	MoveEntity model,l\startpos\x,l\startpos\y,l\startpos\z
	collider = CreateSphereCollider(model,1,0.5)

	model = CreateModel(end_mesh)
	MoveEntity model,l\endpos\x,l\endpos\y,l\endpos\z
	collider = CreateSphereCollider(model,1,0.5)
	
	For i = 0 To 2 
		model = CreateModel(checkpoint_mesh)
		MoveEntity model,l\checkpoints[i]\x,l\checkpoints[i]\y,l\checkpoints[i]\z
		collider = CreateSphereCollider(model,1,0.5)	
	Next				
End Function  

Function LoadLevel.Level(path$)
	Local l.Level = New Level
	filein = ReadFile(path$)
	Local loop = True
	Local current_line = 0
	Local current_checkpoint = 0
	Local widest = 0
	Local current_cell = 0
	While loop
		Local line$ = ReadLine(filein)
		If line$="" Then
			loop = False
		Else 
			; process line$ 
			If Len(line$) > widest Then widest = Len(line$)
			For i = 1 To Len(line$) 
				Local char$ = Mid(line$,i,1)
				If char$ = "o" Then 
					If current_cell < 10001 Then 				
						l\cells[current_cell] = CreateVec3("Cell " + current_cell,i-1,-current_line,0)
						current_cell = current_cell + 1
					End If
				End If
				
				If char$ = "s" Then 					
					l\startpos = CreateVec3("Level Start",i-1,-current_line,0)									
				End If 

				If char$ = "e" Then 					
					l\endpos = CreateVec3("Level End",i-1,-current_line,0)
				End If 	
				
				If char$ = "c" Then 	
					If current_checkpoint < 3 Then 				
						l\checkpoints[current_checkpoint] = CreateVec3("Level Checkpoint " + current_checkpoint,i-1,-current_line,0)
						current_checkpoint = current_checkpoint + 1
					End If 	
				End If 				
			Next 	
			current_line = current_line + 1	
		End If				
	Wend 	
	CloseFile filein
	l\width = widest
	l\height = current_line
	l\cellcount = current_cell
	Return l
End Function 