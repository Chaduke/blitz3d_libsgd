; human_torso.bb
; by Chaduke
; 20240809

; according to reference the torso height including the top of the legs is 
; 152cm (start of neck) - 84cm(taint high) = 68cm
; lets go 68cm +- 15cm (around the length of a hand)
; to start we'll go half for the width and also half for the depth
; so 34cm +- 8cm
; colors will be chosen to begin at full alpha with the following parameters
; red 0.5 - 1.0 | green 0.1 to 0.5 | blue 0.2 to 0.6 


Type HumanTorso
	Field height# 
	Field width# 
	Field depth# 
	Field red#
	Field green#
	Field blue#
	Field alpha#	
	Field material
	Field mesh 
	Field model
End Type 

Function CreateHumanTorso.HumanTorso()
	Local ht.HumanTorso = New HumanTorso
	ht\height = 0.68 + Rnd(-0.15,0.15)
	ht\width = 0.34 + Rnd(-0.08,0.08)
	ht\depth = 0.34 + Rnd(-0.08,0.08)
	ht\red = Rnd(0.5,1.0)
	ht\green = Rnd(0.1,0.5)
	ht\blue = Rnd(0.2,0.6)
	ht\alpha = Rnd(0.8,1.0)
	ht\material = CreatePBRMaterial()
	SetMaterialVector4f ht\material,"albedoColor4f",ht\red,ht\green,ht\blue,ht\alpha
	SetMaterialFloat ht\material,"roughnessFactor1f",Rnd(1)
	SetMaterialBlendMode ht\material,1
	SetMaterialCullMode ht\material,3
	ht\mesh = CreateBoxMesh(-ht\width/2,-ht\height/2,-ht\depth/2,ht\width/2,ht\height/2,ht\depth/2,ht\material)
	SetMeshShadowsEnabled ht\mesh,True
	ht\model = CreateModel(ht\mesh)		
	Return ht
End Function 

Function UpdateHumanTorso(ht.HumanTorso)
	TurnEntity ht\model,0,1,0
End Function 