; human_head.bb
; by Chaduke
; 20240809

; the human head appears to be 22cm tall
; for the type being we'll use a sphere of 11cm radius
; with a variance of +- 4cm
; later on we can experiment with better shapes 

Type HumanHead	
	Field radius# 	
	Field red#
	Field green#
	Field blue#
	Field alpha#	
	Field material
	Field mesh 
	Field model
End Type 

Function CreateHumanHead.HumanHead()
	Local hh.HumanHead = New HumanHead	
	hh\radius = 0.22 + Rnd(-0.04,0.04)	
	hh\red = Rnd(0.5,1.0)
	hh\green = Rnd(0.1,0.5)
	hh\blue = Rnd(0.2,0.6)
	hh\alpha = Rnd(0.8,1.0)
	hh\material = CreatePBRMaterial()
	SetMaterialVector4f hh\material,"albedoColor4f",hh\red,hh\green,hh\blue,hh\alpha
	SetMaterialFloat hh\material,"roughnessFactor1f",Rnd(1)
	SetMaterialBlendMode hh\material,1
	SetMaterialCullMode hh\material,3
	hh\mesh = CreateSphereMesh(hh\radius,16,16,hh\material)
	SetMeshShadowsEnabled hh\mesh,True
	hh\model = CreateModel(hh\mesh)		
	Return hh
End Function 

Function UpdateHumanHead(hh.HumanHead)
	TurnEntity hh\model,0,1,0
End Function 

Type Eyeball
	Field radius# 	
	Field red#
	Field green#
	Field blue#
	Field alpha#	
	Field material
	Field mesh 
	Field model
End Type 

Function CreateEyeball.Eyeball()
	Local e.Eyeball = New Eyeball
	e\radius = 0.22 + Rnd(-0.04,0.04)	
	e\red = Rnd(0.5,1.0)
	e\green = Rnd(0.1,0.5)
	e\blue = Rnd(0.2,0.6)
	e\alpha = Rnd(0.8,1.0)
	e\material = CreatePBRMaterial()	
	e\mesh = CreateSphereMesh(e\radius,16,16,e\material)
	SetMeshShadowsEnabled e\mesh,True
	e\model = CreateModel(e\mesh)		
	Return e
End Function 

