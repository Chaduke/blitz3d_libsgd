; ferris_wheel.bb
; by Chaduke 
; 20240703 

; an attempt at a "prefab"
; basically a collection of models and behavior scripts
; that can be imported into a scene and with minimal effort 
; operate independently as designed

Type FerrisWheel
	Field wheel_model 	
	Field support_model
	Field carriages[15]	
	Field wheel_pivot
	Field base_pivot 
End Type

Function CreateFerrisWheel.FerrisWheel()
	Local fw.FerrisWheel = New FerrisWheel 
	Local wheel_mesh = LoadMesh("../engine/assets/prefabs/ferris_wheel/ferris_wheel_wheel.glb")
	SetMeshShadowCastingEnabled wheel_mesh,True 
	fw\wheel_model = CreateModel(wheel_mesh)
	
	fw\wheel_pivot = CreateModel(0)	
	fw\base_pivot = CreateModel(0)
	SetEntityParent fw\wheel_pivot,fw\base_pivot 
	SetEntityParent fw\wheel_model,fw\wheel_pivot	
	
	Local support_mesh = LoadMesh("../engine/assets/prefabs/ferris_wheel/ferris_wheel_support.glb")
	SetMeshShadowCastingEnabled support_mesh,True 
	fw\support_model = CreateModel(support_mesh)	
	SetEntityParent fw\support_model,fw\base_pivot 
	MoveEntity fw\support_model,0,-22.5,0	
	
	Local carriage_mesh = LoadMesh("../engine/assets/prefabs/ferris_wheel/ferris_wheel_carriage.glb")
	SetMeshShadowCastingEnabled support_mesh,True 
	
	For i = 0 To 15 
		fw\carriages[i] = CreateModel(carriage_mesh)
		SetEntityParent fw\carriages[i],fw\wheel_pivot
		TurnEntity fw\carriages[i],0,0,22.5 * i
		MoveEntity fw\carriages[i],0,20,0
	Next 	
	
	MoveEntity fw\base_pivot,0,22.5,0
	Return fw	
End Function  

Function UpdateFerrisWheel(f.FerrisWheel)
	TurnEntity f\wheel_pivot,0,0,0.2
	For i = 0 To 15 
		SetEntityRotation f\carriages[i],0,0,-GetEntityRZ(f\wheel_pivot)
	Next 	
End Function 

Function BoardPassenger(f.FerrisWheel,passenger,carriage_number)
	SetEntityParent passenger,f\carriages[carriage_number]
	SetEntityPosition passenger,0,0,0
	SetEntityRotation passenger,0,180,0
	MoveEntity passenger,0,-0.7,2
	TurnEntity passenger,-10,0,0
End Function 

Function RemovePassenger(f.FerrisWheel,passenger,t.Terrain)
	SetEntityParent passenger,0
	PlaceEntityOnTerrain passenger,t,1,False,True
	AimEntityAtEntity passenger,f\wheel_pivot,0
End Function 

Function RemovePassengerGround(f.FerrisWheel,passenger,ground)
	SetEntityParent passenger,0
	SetEntityPosition passenger,GetEntityX(ground),0,GetEntityZ(ground)	
	AimEntityAtEntity passenger,f\wheel_pivot,0
End Function 

