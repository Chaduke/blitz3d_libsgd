; water.bb
; by Chaduke
; 20240808

Type Water 
	Field mesh
	Field material 
	Field model
	; Field vertices(3)
End Type 

Function CreateWater.Water()
	Local w.Water = New Water	
		w\mesh = CreateMesh(0,0)
		AddVertex w\mesh,-0.5,0.0,0.5,0,1,0,0,0
		AddVertex w\mesh,0.5,0.0,0.5,0,1,0,1,0
		AddVertex w\mesh,0.5,0.0,-0.5,0,1,0,1,1
		AddVertex w\mesh,-0.5,0.0,-0.5,0,1,0,0,1		
		w\material = CreatePBRMaterial()
		Local collider_texture = Load2DTexture("../engine/assets/textures/misc/water_texture.png",4,18)
		SetMaterialTexture w\material,"albedoTexture",collider_texture
		SetMaterialBlendMode w\material,3
		Local surface = CreateSurface(w\mesh,w\material,2)
		AddTriangle surface,0,3,2
		AddTriangle surface,0,2,1		
		w\model = CreateModel(w\mesh)		
	Return w
End Function 

Function UpdateWater(w.Water)
	; TFormMeshTexCoords w\mesh,8,8,0,0
End Function 