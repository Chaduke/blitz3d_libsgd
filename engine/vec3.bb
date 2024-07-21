; vec3.bb
; by Chaduke
; 20240511

; Vector3 Type and Functions for Blitz3D

; testVec3()

Type Vec3
	Field name$
	Field x#
	Field y#
	Field z#
End Type

Function CreateVec3.Vec3(name$="Vector 3",x#=0,y#=0,z#=0)
	Local v.Vec3 = New Vec3
	v\name$ = name$
	v\x# = x# 
	v\y# = y#
	v\z# = z#
	Return v
End Function 

Function MoveEntityVec3(e,v.Vec3)
	MoveEntity e,v\x,v\y,v\z
End Function 

Function TurnEntityVec3(e,v.Vec3)
	TurnEntity e,v\x,v\y,v\z
End Function 

; calculates the distance between 2 Vec3s
Function distVec3#(v1.Vec3,v2.Vec3)
	Local diff.Vec3 = New Vec3
	SubVec3 diff,v1,v2
	Return MagVec3(diff)
End Function

; returns a copy of a Vec3
Function Vec3Copy.Vec3(v.Vec3) 
	c.Vec3 = New Vec3
	SetVec3(c,v\name$ + "(Copy)",v\x,v\y,v\z)
	Return c
End Function

; returns a string that displays the values of the
; passed in Vec3 data structure
Function Vec3ToString$(v.Vec3)
	Return v\name$ + " = " + v\x + "," + v\y + "," + v\z + Chr$(13)
End Function 

Function SetVec3(v.Vec3,name$="Vector 3",x#=0,y#=0,z#=0)
	v\name$ = name$
	v\x = x
	v\y = y
	v\z = z	
End Function 

Function SubVec3(r.Vec3,v1.Vec3,v2.Vec3)
	r\x = v1\x-v2\x
	r\y = v1\y-v2\y
	r\z = v1\z-v2\z
End Function	

Function AddVec3(r.Vec3,v1.Vec3,v2.Vec3)
	r\x = v1\x+v2\x
	r\y = v1\y+v2\y
	r\z = v1\z+v2\z
End Function	

Function AddToVec3(v1.Vec3,v2.Vec3)
	v1\x = v1\x + v2\x
	v1\y = v1\y + v2\y
	v1\z = v1\z + v2\z
End Function 

Function MultVec3Float(r.Vec3,f#)
	r\x = r\x * f
	r\y = r\y * f
	r\z = r\z * f
End Function	

Function CrossVec3(r.Vec3,e1.Vec3,e2.Vec3)
	r\x = e1\y * e2\z - e1\z * e2\y
	r\y = e1\z * e2\x - e1\x * e2\z
	r\z = e1\x * e2\y - e1\y * e2\x	
End Function

Function MagSqrVec3#(v.Vec3) 
    Return v\x * v\x + v\y * v\y + v\z * v\z
End Function

Function MagVec3#(v.Vec3)
    Return Sqr(MagSqrVec3(v))
End Function

Function NormalizeVec3(v.Vec3)
	 Local m# = MagVec3(v)    
    If m<>0 Then MultVec3Float(v,1/m)   
End Function	

Function testVec3()
	v1.Vec3 = New Vec3
	v2.Vec3 = New Vec3	
	diff.Vec3 = New Vec3
	sum.Vec3 = New Vec3	
	mult.Vec3 = New Vec3	
	cross.Vec3 = New Vec3	 
		
	SetVec3 v1,"Vector 1",14.5,15.7,19.3	
	SetVec3 v2,"Vector 2",12.2,13.1,15.6
	SetVec3 diff,"Difference",0,0,0
	SetVec3 sum,"Sum",0,0,0	
	SetVec3 cross,"Cross Product",0,0,0
	SubVec3(diff,v1,v2)
	AddVec3(sum,v1,v2)
	mult = Vec3Copy(v1)
	MultVec3Float mult,2.5	
	CrossVec3 cross,v1,v2
	magsqr# = MagSqrVec3(cross)
	mag# = MagVec3(cross)
	
	msg$ = Vec3ToString$(v1) + Vec3ToString$(v2) + Vec3ToString$(diff) + Vec3ToString(sum) + Vec3ToString(mult) + Vec3ToString(cross)
	msg$ = msg$ + "Magnitude Squared of Cross : " + magsqr + Chr$(13)
	msg$ = msg$ + "Magnitude of Cross : " + mag + Chr$(13)
	NormalizeVec3 cross 
	msg$ = msg$ + "Cross Normalized : " + Vec3ToString(cross)
	
	Alert msg$
End Function 