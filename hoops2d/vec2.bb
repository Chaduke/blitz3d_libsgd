; vec2.bb
; by Chaduke
; 20240722

; 20240808 - added to engine/math2d.bb

Type Vec2 
	Field x#,y#
End Type 

Function CreateVec2.Vec2(x#=0,y#=0)
	Local v.Vec2 = New Vec2
	v\x# = x#
	v\y# = y#	
	Return v
End Function

Function AddToVec2(v1.Vec2,v2.Vec2)
	v1\x = v1\x + v2\x
	v1\y = v1\y + v2\y
End Function 	