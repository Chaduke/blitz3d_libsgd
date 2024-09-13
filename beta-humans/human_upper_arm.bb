; human_upper_arm.bb
; by Chaduke
; 20240809

; consulting reference the human upper arm looks to be approx 15cm in diameter
; with a ball at the shoulder its max, then tapering off
; lets go for a sphere / cylinder combo to start
; the length of the upper arm appears to be around 25cm

Type HumanUpperArm
	Field shoulder_radius#
	Field height# 
	Field radius# 	
	Field red#
	Field green#
	Field blue#
	Field alpha#	
	Field material
	Field mesh 
	Field shoulder_mesh	
	Field model
	Field shoulder_model
End Type 

Function CreateHumanUpperArm.HumanUpperArm(side$)
	Local hua.HumanUpperArm = New HumanUpperArm
	hua\shoulder_radius = 0.07 + Rnd(-0.02,0.02)
	hua\height = 0.25 + Rnd(-0.06,0.06)
	hua\radius = 0.04 + Rnd(-0.02,0.02)	
	hua\red = Rnd(0.5,1.0)
	hua\green = Rnd(0.1,0.5)
	hua\blue = Rnd(0.2,0.6)
	hua\alpha = Rnd(0.8,1.0)
	hua\material = CreatePBRMaterial()
	SetMaterialVector4f hua\material,"albedoColor4f",hua\red,hua\green,hua\blue,hua\alpha
	SetMaterialFloat hua\material,"roughnessFactor1f",Rnd(1)
	SetMaterialBlendMode hua\material,1
	SetMaterialCullMode hua\material,3
	hua\shoulder_mesh = CreateSphereMesh(hua\shoulder_radius,16,16,hua\material)
	SetMeshShadowsEnabled hua\shoulder_mesh,True
	hua\shoulder_model = CreateModel(hua\shoulder_mesh)
	hua\mesh = CreateCylinderMesh(hua\height,hua\radius,8,hua\material)
	SetMeshShadowsEnabled hua\mesh,True
	hua\model = CreateModel(hua\mesh)	
	SetEntityParent hua\model,hua\shoulder_model
	TurnEntity hua\model,0,0,90
	If side$ = "left" Then SetEntityPosition hua\model,hua\height/2,0,0
	If side$ = "right" Then SetEntityPosition hua\model,-hua\height/2,0,0
	Return hua
End Function 

Function MirrorHumanUpperArm.HumanUpperArm(h.HumanUpperArm)
	Local hua.HumanUpperArm = New HumanUpperArm
	hua\shoulder_radius = h\shoulder_radius
	hua\height = h\height
	hua\radius = h\radius
	hua\red = h\red
	hua\green = h\green
	hua\blue = h\blue
	hua\alpha = h\alpha
	hua\material = h\material	
	hua\shoulder_mesh = CreateSphereMesh(hua\shoulder_radius,16,16,hua\material)
	SetMeshShadowsEnabled hua\shoulder_mesh,True
	hua\shoulder_model = CreateModel(hua\shoulder_mesh)
	hua\mesh = CreateCylinderMesh(hua\height,hua\radius,8,hua\material)
	SetMeshShadowsEnabled hua\mesh,True
	hua\model = CreateModel(hua\mesh)	
	SetEntityParent hua\model,hua\shoulder_model
	TurnEntity hua\model,0,0,90
	If GetEntityX(h\model) < 0 Then 
		SetEntityPosition hua\model,hua\height/2,0,0
	Else 
		SetEntityPosition hua\model,-hua\height/2,0,0
	End If 	
	Return hua
End Function 

Function UpdateHumanUpperArm(hua.HumanUpperArm)
	TurnEntity hua\shoulder_model,0,1,0
End Function 