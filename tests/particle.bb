Type Particle 
	Field sprite 
	Field acc.Vec3 ; acceleration 
	Field vel.Vec3 ; velocity	
	Field age,lifespan	
End Type 

Function CreateParticle.Particle(image,t.Terrain)	
	Local p.Particle = New Particle 
	p\sprite = CreateSprite(image)
	PlaceEntityOnTerrain(p\sprite,t,1,True)
	Local sc# = Rnd(0.03,0.07)
	SetEntityScale p\sprite,sc,sc,sc
	p\acc = CreateVec3("Particle Acceleration",0,-0.01,0)
	p\vel = CreateVec3("Particle Velocity",Rnd(-0.08,0.08),0.8,Rnd(-0.08,0.08))
	p\age = 0
	p\lifespan = Rand(200,1000)	
	Return p
End Function 

Function UpdateParticle(p.Particle,t.Terrain)
	AddToVec3 p\vel,p\acc
	MoveEntity p\sprite,p\vel\x,p\vel\y,p\vel\z
	If GetEntityY(p\sprite) < GetTerrainHeight(GetEntityX(p\sprite),GetEntityZ(p\sprite),t) + 0.9 Then 
		DestroyEntity p\sprite
		Delete p
	End If 	
End Function 

