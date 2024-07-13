; turtle.bb
; by Chaduke 
; 20240711

; turtle character for Turtle Hurdler

Type Turtle 
	Field model 
	Field collider
End Type 

Function CreateTurtle.Turtle(mesh)
	Local t.Turtle = New Turtle 
	t\model = CreateModel(mesh)
	t\collider = CreateEllipsoidCollider(t\model,1,1.6, 1.5)
	Return t		
End Function 	

Function GetRandomX#(t.Turtle,rangelow#,rangehigh#,z#)
	Local loop = True 
	Local i.Turtle = First Turtle
	Local x# =  Rnd(rangelow#,rangehigh#)
	If i = Null Then Return x#		
	While loop
		; check the distance between the passed in turtle and the indexed one 
		; also make sure they aren't the same		 
		If (Abs(Distance2D(x,z,GetEntityX(i\model),GetEntityZ(i\model)) < 6 And i <> t)) Then 
			; we need to repick x
			; AND restart the loop
			x# = Rnd(rangelow#,rangehigh#)
			i = First turtle
		Else 
			; move on to the next turtle 
			i = After i
			If i = Null Then loop = False
		End If			
	Wend 	
	Return x#
End Function 

Function GetRandomZ#(t.Turtle,rangelow#,rangehigh#,x#)
	Local loop = True 
	Local i.Turtle = First Turtle
	Local z# =  Rnd(rangelow#,rangehigh#)
	If i = Null Then Return z#		
	While loop
		; check the distance between the passed in turtle and the indexed one 
		; also make sure they aren't the same		 
		If (Abs(Distance2D(x,z,GetEntityX(i\model),GetEntityZ(i\model)) < 6 And i <> t)) Then 
			; we need to repick z
			z# = Rnd(rangelow#,rangehigh#)
			i = First turtle
		Else 
			; move on to the next turtle 
			i = After i
			If i = Null Then loop = False
		End If			
	Wend
	Return z#
End Function

Function GenerateTurtles(t.Terrain,numturtles,path$)

	Local mesh = LoadMesh(path$)
	SetMeshShadowCastingEnabled mesh,True
	Local x#,z#
	Local tt.Turtle
	
	; line along the front side 
	For i = 0 To numturtles
		tt = CreateTurtle(mesh)	
		z = 16
		x# = GetRandomX(tt,20,t\width-20,z)		
		
		PlaceEntityOnTerrain tt\model,t,0,False,False,x,z
		TurnEntity tt\model,0,-90,0
	Next 	
	
	; line along the back side
	For i = 0 To numturtles
		tt = CreateTurtle(mesh)	
		z = 239
		x = GetRandomX(tt,20,t\width-20,z) 		
		PlaceEntityOnTerrain tt\model,t,0,False,False,x,z
		TurnEntity tt\model,0,90,0
	Next 	
	
	; line aling the left side 
	For i = 0 To numturtles
		tt = CreateTurtle(mesh)	
		x = 16
		z = GetRandomZ(tt,20,t\depth-20,x) 
		PlaceEntityOnTerrain tt\model,t,0,False,False,x,z
		TurnEntity tt\model,0,180,0
	Next 	
	
	; line along the right side 
	For i = 0 To numturtles
		tt = CreateTurtle(mesh)	
		x = 239
		z = GetRandomZ(tt,20,t\depth-20,x)
		PlaceEntityOnTerrain tt\model,t,0,False,False,x,z		
	Next 	

End Function 