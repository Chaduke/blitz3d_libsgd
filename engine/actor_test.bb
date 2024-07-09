Local loop = True
a2.Actor = First Actor  

While loop
	If (a2 <> a And a2\actor_path$ = a\actor_path$) Then 
		If a\actor_type = 0 Then a\mesh = a2\mesh
		If a\actor_type = 1 Then a\image = a2\image
		If a\actor_type = 6 Then a\model = a2\model
		If a\actor_type = 7 Then a\sound = a2\sound
		loop = False 
	Else 
		a2 = After a2
	End If 
				
	If a2 = Null Then 
		loop = False
		If a\actor_type = 0 Then 
			a\mesh = LoadMesh(a\actor_path$)
			SetMeshShadowCastingEnabled a\mesh,True 				
		End If 
					
		If a\actor_type = 1 Then 
			a\image = LoadImage(a\actor_path$,1)
			SetImageBlendMode a\image,3
			SetImageSpriteViewMode a\image,1						
		End If 
		
		If a\actor_type = 6 Then a\model = LoadBonedModel(a\actor_path$,True)
		If a\actor_type = 7 Then a\sound = LoadSound(a\actor_path$)					
	End If 		
Wend 	

If a\actor_type = 0 Then a\model = CreateModel(a\mesh)
If a\actor_type = 1 Then a\sprite = CreateSprite(a\image)
If a\actor_type = 2 Then a\point_light = CreatePointLight() 
If a\actor_type = 3 Then a\spot_light = CreateSpotLight()
If a\actor_type = 4 Then a\collision_cylinder = CreateCylinder()
If a\actor_type = 5 Then a\collision_aabb = CreateAABB()