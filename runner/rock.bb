; rock.bb
; by Chaduke 
; 20240722 

Type Rock 
	Field pivot 
	Field model 
	Field collider	
End Type 

Function CreateRock.Rock(mesh)
	Local r.Rock = New Rock
	r\model = CreateModel(mesh)
	r\pivot = CreateModel(0)
	SetEntityParent r\model,r\pivot 
	r\collider = CreateMeshCollider(r\pivot,1,mesh)	
	Return r
End Function  

Function GenerateRocks(t.Terrain,density)
	Local mesh = LoadMesh("../engine/assets/models/runner/rock_small.glb")
	SetMeshShadowCastingEnabled mesh,True 
	Local r.Rock
	For i = 0 To density
		r=CreateRock(mesh)	
		PlaceEntityOnTerrain r\pivot,t,0,False,False,Rand(20,t\width-20),16
		TurnEntity r\pivot,0,Rand(0,360),0
		r=CreateRock(mesh)	
		PlaceEntityOnTerrain r\pivot,t,0,False,False,Rand(20,t\width-20),239
		TurnEntity r\pivot,0,Rand(0,360),0
		PlaceEntityOnTerrain r\pivot,t,0,False,False,16,Rand(20,t\depth-20)
		TurnEntity r\pivot,0,Rand(0,360),0
		r=CreateRock(mesh)	
		PlaceEntityOnTerrain r\pivot,t,0,False,False,239,Rand(20,t\depth-20)
		TurnEntity r\pivot,0,Rand(0,360),0

	Next 		
End Function 