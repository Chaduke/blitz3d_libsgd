; human_neck.bb
; by Chaduke
; 20240809

; according to reference the neck appears to be about 10cm 
; moving upwards into the bottom of the head, perhaps a little more in the back
; in the front ending around the bottom of the chin
; the neck needs to extend into the torso and also the head
; so we need a few cm extra to ensure this
; of course we will use a cylinder to begin
; lets go for 14cm +- 6cm on height
; radius will be 6cm +- 3cm 

Type HumanNeck
	Field height# 
	Field radius# 	
	Field red#
	Field green#
	Field blue#
	Field alpha#	
	Field material
	Field mesh 
	Field model
End Type 

Function CreateHumanNeck.HumanNeck()
	Local hn.HumanNeck = New HumanNeck
	hn\height = 0.14 + Rnd(-0.06,0.06)
	hn\radius = 0.06 + Rnd(-0.03,0.03)	
	hn\red = Rnd(0.5,1.0)
	hn\green = Rnd(0.1,0.5)
	hn\blue = Rnd(0.2,0.6)
	hn\alpha = Rnd(0.8,1.0)
	hn\material = CreatePBRMaterial()
	SetMaterialVector4f hn\material,"albedoColor4f",hn\red,hn\green,hn\blue,hn\alpha
	SetMaterialFloat hn\material,"roughnessFactor1f",Rnd(1)
	SetMaterialBlendMode hn\material,1
	SetMaterialCullMode hn\material,3
	hn\mesh = CreateCylinderMesh(hn\height,hn\radius,8,hn\material)
	SetMeshShadowsEnabled hn\mesh,True
	hn\model = CreateModel(hn\mesh)		
	Return hn
End Function 

Function UpdateHumanNeck(hn.HumanNeck)
	TurnEntity hn\model,0,1,0
End Function 