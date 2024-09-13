; human_upper_leg.bb
; by Chaduke
; 20240809

; consulting reference the human upper Leg looks to be approx 15cm in diameter
; with a ball at the hip its max, then tapering off
; lets go for a sphere / cylinder combo to start
; the length of the upper Leg appears to be around 25cm

Type HumanUpperLeg
	Field hip_radius#
	Field height# 
	Field radius# 	
	Field red#
	Field green#
	Field blue#
	Field alpha#	
	Field material
	Field mesh 
	Field hip_mesh	
	Field model
	Field hip_model
End Type 

Function CreateHumanUpperLeg.HumanUpperLeg(side$)
	Local hul.HumanUpperLeg = New HumanUpperLeg
	hul\hip_radius = 0.07 + Rnd(-0.02,0.02)
	hul\height = 0.25 + Rnd(-0.06,0.06)
	hul\radius = 0.04 + Rnd(-0.02,0.02)	
	hul\red = Rnd(0.5,1.0)
	hul\green = Rnd(0.1,0.5)
	hul\blue = Rnd(0.2,0.6)
	hul\alpha = Rnd(0.8,1.0)
	hul\material = CreatePBRMaterial()
	SetMaterialVector4f hul\material,"albedoColor4f",hul\red,hul\green,hul\blue,hul\alpha
	SetMaterialFloat hul\material,"roughnessFactor1f",Rnd(1)
	SetMaterialBlendMode hul\material,1
	SetMaterialCullMode hul\material,3
	hul\hip_mesh = CreateSphereMesh(hul\hip_radius,16,16,hul\material)
	SetMeshShadowsEnabled hul\hip_mesh,True
	hul\hip_model = CreateModel(hul\hip_mesh)
	hul\mesh = CreateCylinderMesh(hul\height,hul\radius,8,hul\material)
	SetMeshShadowsEnabled hul\mesh,True
	hul\model = CreateModel(hul\mesh)	
	SetEntityParent hul\model,hul\hip_model	
	If side$ = "left" Then SetEntityPosition hul\model,0,hul\height/2,0
	If side$ = "right" Then SetEntityPosition hul\model,0,-hul\height/2,0
	Return hul
End Function 

Function MirrorHumanUpperLeg.HumanUpperLeg(h.HumanUpperLeg)
	Local hul.HumanUpperLeg = New HumanUpperLeg
	hul\hip_radius = h\hip_radius
	hul\height = h\height
	hul\radius = h\radius
	hul\red = h\red
	hul\green = h\green
	hul\blue = h\blue
	hul\alpha = h\alpha
	hul\material = h\material	
	hul\hip_mesh = CreateSphereMesh(hul\hip_radius,16,16,hul\material)
	SetMeshShadowsEnabled hul\hip_mesh,True
	hul\hip_model = CreateModel(hul\hip_mesh)
	hul\mesh = CreateCylinderMesh(hul\height,hul\radius,8,hul\material)
	SetMeshShadowsEnabled hul\mesh,True
	hul\model = CreateModel(hul\mesh)	
	SetEntityParent hul\model,hul\hip_model	
	If GetEntityX(h\model) < 0 Then 
		SetEntityPosition hul\model,0,hul\height/2,0
	Else 
		SetEntityPosition hul\model,0,-hul\height/2,0
	End If 	
	Return hul
End Function 

Function UpdateHumanUpperLeg(hul.HumanUpperLeg)
	TurnEntity hul\hip_model,0,1,0
End Function 