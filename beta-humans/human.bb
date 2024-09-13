; human.bb
; by Chaduke
; 20240809

; we will create custom types for each body part
; then combine them all into a custom human type

Include "human_torso.bb"
Include "human_neck.bb"
Include "human_head.bb"
Include "human_upper_arm.bb"
Include "human_lower_arm.bb"

Type Human
	Field pivot
	Field torso.HumanTorso
	Field neck.HumanNeck
	Field head.HumanHead
	Field left_upper_arm.HumanUpperArm
	Field right_upper_arm.HumanUpperArm
	Field left_lower_arm.HumanLowerArm
	Field right_lower_arm.HumanLowerArm
	Field destx#
	Field destz#
	Field angle
End Type

Function CreateHuman.Human()
	Local h.Human = New Human
		h\pivot = CreateModel(0)
		h\torso = CreateHumanTorso()
		SetEntityParent h\torso\model,h\pivot			
		
		; position arms
		h\right_upper_arm = CreateHumanUpperArm("right")
		h\left_upper_arm = MirrorHumanUpperArm(h\right_upper_arm)
		SetEntityParent h\right_upper_arm\shoulder_model,h\pivot	
		SetEntityParent h\left_upper_arm\shoulder_model,h\pivot	
		MoveEntity h\right_upper_arm\shoulder_model,-h\torso\width/2,h\torso\height/2,0
		MoveEntity h\left_upper_arm\shoulder_model,h\torso\width/2,h\torso\height/2,0
		
		h\right_lower_arm = CreateHumanLowerArm("right")
		SetEntityParent h\right_lower_arm\elbow_model,h\right_upper_arm\model	
		MoveEntity h\right_lower_arm\elbow_model,0,-h\right_upper_arm\height/2,0	
		
		h\left_lower_arm = MirrorHumanLowerArm(h\right_lower_arm)
		SetEntityParent h\left_lower_arm\elbow_model,h\left_upper_arm\model	
		MoveEntity h\left_lower_arm\elbow_model,0,h\left_upper_arm\height/2,0	
		
		; position neck
		h\neck = CreateHumanNeck()
		SetEntityParent h\neck\model,h\torso\model
		Local offset# = h\torso\height/2 + h\neck\height / 2 - 0.02
		MoveEntity h\neck\model,0,offset,0
		
		; position head
		h\head = CreateHumanHead()	
		SetEntityParent h\head\model,h\torso\model
		offset# = h\torso\height/2 + h\neck\height / 2 + h\head\radius - 0.02
		MoveEntity h\head\model,0,offset,0		
				
		offset# = h\torso\width/2		
	
	Return h
End Function 

Function UpdateHuman(h.Human,t.Terrain)
	SetEntityRotation h\right_upper_arm\shoulder_model,Sin(h\angle) * 45,0,-65
	SetEntityRotation h\right_lower_arm\elbow_model,Sin(h\angle) * 22,0,-22
	SetEntityRotation h\left_upper_arm\shoulder_model,Sin(h\angle) * -45,0,65	
	SetEntityRotation h\left_lower_arm\elbow_model,Sin(h\angle) * 22,0,22			
	h\angle = h\angle + 5
	MoveEntity h\pivot,0,0,-0.03
	; check destination
	If (distance2D(GetEntityX(h\pivot),GetEntityZ(h\pivot),h\destx,h\destz) < 5)	
		; pick a new destination
		h\destx# = Rnd(t\width)
		h\destz# = Rnd(t\depth)
		; face that destination
		EntityFaceLocation(h\pivot,h\destx,h\destz)	; navigation.bb	
	End If	
	ClampEntityAboveTerrain(h\pivot,t,1)
End Function 