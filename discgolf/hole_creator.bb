; hole_creator.bb
; by Chaduke
; 20240705 

; a disc golf hole creator using a combo of procedural techniques 
; like randomness, noise and setting constraints
Include "../engine/testapp.bb"

t.TestApp = CreateTestApp()
b.BasicScene = CreateBasicScene(True)

tw = 80: td = 150

p.Polygon = CreatePolygon()
Const numtrees = 400
Dim trees.Point(numtrees)

generated = False 

While t\loop 
	BeginFrame t 
	If Not generated Then 
		If IsMouseButtonHit(0) Then 
			pt.Point = CreatePoint(GetMouseX(),GetMouseY())
			AddPolygonPoint p,pt
		End If 	
		If IsMouseButtonHit(1) Then 
			p.Polygon = CreatePolygon()
		End If 
	
		If IsKeyHit(KEY_T) Then 
			; add trees 
			For i = 0 To numtrees
				lp = True 
				While lp 
					x = Rand(0,tw) 
					y = Rand(0,td)
					Local temp_point.Point = CreatePoint(x,y)
					If PointCollidedPolygon(temp_point,p) = False Then lp = False 
				Wend			
				trees(i) = temp_point 
			Next 
			
			b\sky_texture = Load2DTexture("../engine/assets/textures/skybox/skybox_sunny.png",4,56)	
			SetEnvTexture b\sky_texture
			b\skybox = CreateSkybox(b\sky_texture)	
			b\sky_roughness = 0.2
			SetSkyboxRoughness b\skybox,b\sky_roughness
			SeedRnd MilliSecs()
			b\trn = New Terrain	
			SetTerrainDefaults b\trn	
			b\trn\width = tw
			b\trn\depth = td
			CreateTerrain b\trn					
			tree_mesh = LoadMesh("../engine/assets/models/trees/tree1.glb")
			SetMeshShadowsEnabled tree_mesh,True
			 			
			For i = 0 To numtrees
				model = CreateModel(tree_mesh)
				; SetEntityScale model,10,10,10
				PlaceEntityOnTerrain model,b\trn,0,False,False,trees(i)\x,trees(i)\y	
			Next 
			GenerateGrass b\trn,1000,"../engine/assets/textures/foliage/grass1.png"
			GenerateGrass b\trn,1000,"../engine/assets/textures/foliage/weeds.png"		
			PlaceEntityOnTerrain b\pivot,b\trn,1,False,True 			
			generated = True 	
			SetMouseCursorMode 3
		End If 		
	
		If IsKeyHit(KEY_X) Then 
			Delete Each Polygon
			Delete Each Point 
			p.Polygon = CreatePolygon()
		End If		
		EndFrame t
		
		; draw the terrain rectangle 
		Set2DFillColor 0,1,0,1
		Draw2DRect 0,0,tw,td
		; draw polygons 
		Set2DFillColor 1,1,0,1
		Set2DOutlineColor 1,0,0,1
		For pl.Polygon = Each Polygon 		
			DrawPolygon pl 
		Next 	
		; draw trees 
		If generated Then 
			Set2DFillColor 0.1,0.4,0.2,1
			For i = 0 To numtrees 
				Draw2DOval trees(i)\x-5,trees(i)\y-5,trees(i)\x+5,trees(i)\y+5
			Next 	
		End If 	
		Draw2DText "Terrain Width : " + tw,5,5
		Draw2DText "Terrain Depth : " + td,5,25
	Else 
		ThirdPersonMouseInputEditor b\camera,b\pivot 
		KeyboardInputFirstPerson b\pivot 
		EndFrame t
	End If 	
	Present()
Wend 
End 