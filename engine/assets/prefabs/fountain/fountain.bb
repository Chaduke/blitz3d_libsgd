; fountain.bb
; by Chaduke
; 20240817

; a fountain prefab

Type Fountain
	Field model 
	Field raindrop_image	
	Field sound 
	Field kinowelt_sound 
	Field chime_sound
	Field running
End Type 

Function CreateFountain.Fountain()
	Local f.Fountain = New Fountain 
	Local mesh = LoadMesh("../engine/assets/models/theme_park/fountain.glb")
	SetMeshShadowsEnabled mesh,True
	f\model = CreateModel(mesh)	
	f\raindrop_image = LoadImage("../engine/assets/textures/misc/raindrop2.png",1)	
	SetImageBlendMode f\raindrop_image,3	
	f\sound = LoadSound("../engine/assets/audio/misc/fountain.mp3")
	f\kinowelt_sound = LoadSound("../engine/assets/audio/misc/kinowelt.mp3")
	f\chime_sound = LoadSound("../engine/assets/audio/misc/fountain_chime.mp3")
	f\running = False
	Return f	
End Function 

Function UpdateFountain(f.Fountain,t.Terrain)
	If f\running Then 
		CreateDrop(f\raindrop_image,f,t) 
		CreateDrop(f\raindrop_image,f,t) 
		CreateDrop(f\raindrop_image,f,t) 
	End If	
	UpdateDrops(t)
End Function 

Function UpdateFountains(t.Terrain)
	For f.Fountain = Each Fountain
		UpdateFountain f,t
	Next 	
End Function 

Type Drop
	Field sprite 
	Field acc.Vec3 ; acceleration 
	Field vel.Vec3 ; velocity	
End Type 
	
Function CreateDrop.Drop(image,f.Fountain,t.Terrain)	
	Local d.Drop = New Drop
	d\sprite = CreateSprite(image)	
	PlaceEntityOnTerrain d\sprite,t,1,False,False,GetEntityX(f\model),GetEntityZ(f\model)
	Local sc# = Rnd(0.03,0.07)
	SetEntityScale d\sprite,sc,sc,sc
	d\acc = CreateVec3("Particle Acceleration",0,-0.01,0)
	d\vel = CreateVec3("Particle Velocity",Rnd(-0.08,0.08),0.8,Rnd(-0.08,0.08))	
	Return d
End Function

Function UpdateDrop(d.Drop,t.Terrain)
	AddToVec3 d\vel,d\acc
	MoveEntity d\sprite,d\vel\x,d\vel\y,d\vel\z
	If GetEntityY(d\sprite) < GetTerrainHeight(GetEntityX(d\sprite),GetEntityZ(d\sprite),t) + 0.9 Then 
		DestroyEntity d\sprite
		Delete d
	End If 	
End Function 

Function UpdateDrops(t.Terrain)
	For d.Drop = Each Drop
		UpdateDrop(d,t)
	Next 
End Function 	

Function ToggleRunning(f.Fountain)
	If f\running = False Then 
		f\running = True 
		PlaySound f\sound
		PlaySound f\kinowelt_sound
	Else 
		f\running = False
		StopAudio f\sound
		StopAudio f\kinowelt_sound
		PlaySound f\chime_sound
	End If 		
End Function 

	
	
