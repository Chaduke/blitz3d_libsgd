; human_lower_arm.bb
; by Chaduke
; 20240809

; for the lower arm to begin let's just create a smaller scaled version of the upper

Type HumanLowerArm
	Field elbow_radius#
	Field height# 
	Field radius# 	
	Field red#
	Field green#
	Field blue#
	Field alpha#	
	Field material
	Field mesh 
	Field elbow_mesh	
	Field model
	Field elbow_model
End Type 

Function CreateHumanLowerArm.HumanLowerArm(side$)
	Local hla.HumanLowerArm = New HumanLowerArm
	hla\elbow_radius = 0.05 + Rnd(-0.01,0.01)
	hla\height = 0.22 + Rnd(-0.04,0.04)
	hla\radius = 0.03 + Rnd(-0.01,0.01)	
	hla\red = Rnd(0.5,1.0)
	hla\green = Rnd(0.1,0.5)
	hla\blue = Rnd(0.2,0.6)
	hla\alpha = Rnd(0.8,1.0)
	hla\material = CreatePBRMaterial()
	SetMaterialVector4f hla\material,"albedoColor4f",hla\red,hla\green,hla\blue,hla\alpha
	SetMaterialFloat hla\material,"roughnessFactor1f",Rnd(1)
	SetMaterialBlendMode hla\material,1
	SetMaterialCullMode hla\material,3
	hla\elbow_mesh = CreateSphereMesh(hla\elbow_radius,16,16,hla\material)
	SetMeshShadowsEnabled hla\elbow_mesh,True
	hla\elbow_model = CreateModel(hla\elbow_mesh)
	hla\mesh = CreateCylinderMesh(hla\height,hla\radius,8,hla\material)
	SetMeshShadowsEnabled hla\mesh,True
	hla\model = CreateModel(hla\mesh)	
	SetEntityParent hla\model,hla\elbow_model	
	If side$ = "left" Then SetEntityPosition hla\model,0,hla\height/2,0
	If side$ = "right" Then SetEntityPosition hla\model,0,-hla\height/2,0
	Return hla
End Function 

Function MirrorHumanLowerArm.HumanLowerArm(h.HumanLowerArm)
	Local hla.HumanLowerArm = New HumanLowerArm
	hla\elbow_radius = h\elbow_radius
	hla\height = h\height
	hla\radius = h\radius
	hla\red = h\red
	hla\green = h\green
	hla\blue = h\blue
	hla\alpha = h\alpha
	hla\material = h\material	
	hla\elbow_mesh = CreateSphereMesh(hla\elbow_radius,16,16,hla\material)
	SetMeshShadowsEnabled hla\elbow_mesh,True
	hla\elbow_model = CreateModel(hla\elbow_mesh)
	hla\mesh = CreateCylinderMesh(hla\height,hla\radius,8,hla\material)
	SetMeshShadowsEnabled hla\mesh,True
	hla\model = CreateModel(hla\mesh)	
	SetEntityParent hla\model,hla\elbow_model	
	If GetEntityX(h\model) < 0 Then 
		SetEntityPosition hla\model,0,hla\height/2,0
	Else 
		SetEntityPosition hla\model,0,-hla\height/2,0
	End If 	
	Return hla
End Function 

Function UpdateHumanLowerArm(hla.HumanLowerArm)
	TurnEntity hla\elbow_model,0,1,0
End Function 