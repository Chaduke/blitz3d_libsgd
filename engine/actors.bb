; actors.bb
; by Chaduke
; 20240524

; contains functionality related to actors added to a scene
; I don't think this is going to be a good idea
; because having all these fields and conditions is too confusing
; I think we are better out with a specific custom type for every specific thing in a game 
; rather than trying to make a single type multi-purpose 
; I feel like it's harder To debug and increases complexity
; this layout would a good guide to creating all these custom types 
; problems seem to start occuring when I let a single code file get too big
; I need to keep just the essentials in one file and nothing more
; if it doesnt belong make a seperate file for it 
; stop trying so hard to reuse code, only when it makes sense to do so
; not at the expense of creating extra complexity
; we can always type out and copy paste new code 
; simplifying complex code takes a lot more time
; and hunting down bugs expends a lot of creative energy 

Type Actor 
	Field name$
	; actor types 
	; 0 = static_model, 1 = sprite, 2 = point_light, 3 = spot_light
	; 4 = collision_cylinder, 5 = collision_aabb, 6 = boned_model, 7 = sound	
	Field actor_type 
	Field actor_path$	
	; Field mesh
	; Field model
	; Field image
	; Field sprite	
	; Field point_light
	; Field spot_light 
	; Field collision_cylinder.Cylinder
	; Field collision_aabb.AABB 
	; Field boned_model	
	; Field sound
	Field outliner_widget.Widget
End Type 

Function CreateActor.Actor(name$,actor_type,actor_path$,gui.GUIWindow)	
	Local a.Actor = New Actor	
	
	a\name$ = name$
	a\actor_type = actor_type
	a\actor_path$ = actor_path$ 
	
	; check if there is already an actor with this name 
	count = 0
	For a2.Actor = Each Actor 
		If (Left$(a2\name$,Len(name$)) = name$ And a2<>a) Then count = count + 1
	Next	
	
	If count > 0 Then a\name$ = a\name$ + "(" + count + ")"		
	a\outliner_widget = AddWidget(gui,a\name$,2,"")
	Return a			  
End Function 

Function SaveActors(file$)
	fileout = WriteFile(scene_path$ + "/" + file$)
	Local count = 0
	For a.Actor = Each Actor 
		count = count + 1
	Next 
	WriteInt fileout,count	
	For a.Actor = Each Actor
		WriteString fileout,a\name$
		WriteInt fileout,a\actor_type
		WriteString fileout,a\actor_path$ 
		If a\actor_type = 0 Then 
			WriteFloat fileout,GetEntityX(a\model) : WriteFloat fileout,GetEntityY(a\model) : WriteFloat fileout,GetEntityZ(a\model)
			WriteFloat fileout,GetEntityRX(a\model) : WriteFloat fileout,GetEntityRY(a\model) : WriteFloat fileout,GetEntityRZ(a\model)
			WriteFloat fileout,GetEntitySX(a\model) : WriteFloat fileout,GetEntitySY(a\model) : WriteFloat fileout,GetEntitySZ(a\model)	
		End If 
		
		If a\actor_type = 1 Then 
			WriteFloat fileout,GetEntityX(a\sprite) : WriteFloat fileout,GetEntityY(a\sprite) : WriteFloat fileout,GetEntityZ(a\sprite)
			WriteFloat fileout,GetEntityRX(a\sprite) : WriteFloat fileout,GetEntityRY(a\sprite) : WriteFloat fileout,GetEntityRZ(a\sprite)
			WriteFloat fileout,GetEntitySX(a\sprite) : WriteFloat fileout,GetEntitySY(a\sprite) : WriteFloat fileout,GetEntitySZ(a\sprite)	
		End If 							
	Next 
	CloseFile fileout
End Function 

Function LoadActors(file$,gui.GUIWindow)
	filein = ReadFile(scene_path$ + "/" + file$)
	Local count = ReadInt(filein)
	For i = 1 To count		
		Local name$ = ReadString(filein)
		Local actor_type = ReadInt(filein)
		Local actor_path$ = ReadString(filein)			
		a.Actor = CreateActor(name$,actor_type,actor_path$,gui)			
		px# = ReadFloat(filein) : py# = ReadFloat(filein) : pz# = ReadFloat(filein)
		rx# = ReadFloat(filein) : ry# = ReadFloat(filein) : rz# = ReadFloat(filein)
		sx# = ReadFloat(filein) : sy# = ReadFloat(filein) : sz# = ReadFloat(filein)	
		
		If actor_type = 0 Then 
			SetEntityPosition a\model,px,py,pz
			SetEntityRotation a\model,rx,ry,rz
			SetEntityScale a\model,sx,sy,sz
		End If 
		
		If actor_type = 1 Then 
			SetEntityPosition a\sprite,px,py,pz
			SetEntityRotation a\sprite,rx,ry,rz
			SetEntityScale a\sprite,sx,sy,sz
		End If 			
	Next 	
	CloseFile filein
End Function 