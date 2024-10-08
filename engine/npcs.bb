; npcs.bb
; by Chaduke
; 20240524

; everything related to Non Player Characters

; first I'll move everything related to Larry in here
; then I'll integrate Randimal (Larry's Pet that follows him around the island)
; then I'll try to refactor this code into reusable functionality

; EACH NPC WOULD BE BETTER WITH ITS OWN CODE FILE 

Type NPC
	Field name$
	Field model 
	Field destx#
	Field destz#
	Field anim
	Field anim_time#	
End Type

Function CreateNPC.NPC(name$,path$)
	Local n.NPC = New NPC
	n\name$ = name$	
	n\model = LoadBonedModel(path$,True)
	n\destx# = 0
	n\destz# = 0
	n\anim = 0
	n\anim_time# = 0
	Return n
End Function 

Function AddAllNPCs()	
	larry.NPC = CreateNPC("Larry Lemon","../engine/assets/models/npcs/larry_dg.glb")
	randimal.NPC = CreateNPC("Randimal","../engine/assets/models/npcs/randimal.glb")
End Function 

Function UpdateNPCs(t.Terrain)
	UpdateLarry t
	UpdateRandimal t	
End Function

Function UpdateRandimal(t.Terrain,n.NPC)
	n\anim_time = n\anim_time + 0.05	
	AnimateModel n\model,0,n\anim_time,2,1	
	MoveEntity n\model,0,0,-0.05
	; check if Randimal has found his destination
	If (distance2D(GetEntityX(randimal\model),GetEntityZ(randimal\model),randimal\destx#,randimal\destz#) < 5)
		; pick a new destination
		; for Randimal he'll either go to Larry
		; or he'll pick a random location
		Local r# = Rnd(1)
		If r>0.9 Then
			randimal\destx# = Rnd(t\width)
			randimal\destz# = Rnd(t\depth)
		Else 
			randimal\destx# = GetEntityX(larry\model)
			randimal\destz# = GetEntityZ(larry\model)
		End If
		; face that destination
		EntityFaceLocation(randimal\model,randimal\destx,randimal\destz)	; navigation.bb	
	End If	
	ClampEntityAboveTerrain(randimal\model,t,0)
End Function 

Function UpdateLarry(t.Terrain,n.NPC)
	larry\anim_time = larry\anim_time + 0.02	
	AnimateModel larry\model,0,larry\anim_time,2,1	
	MoveEntity larry\model,0,0,-0.035
	; check if Larry has found his destination
	If (distance2D(GetEntityX(larry\model),GetEntityZ(larry\model),larry\destx,larry\destz) < 5)	
		; pick a new destination
		larry\destx# = Rnd(t\width)
		larry\destz# = Rnd(t\depth)
		; face that destination
		EntityFaceLocation(larry\model,larry\destx,larry\destz)	; navigation.bb	
	End If	
	ClampEntityAboveTerrain(larry\model,t,0)
End Function 