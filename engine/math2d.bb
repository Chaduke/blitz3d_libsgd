; math2d.bb
; by Chaduke
; 20240517 

; helper functions for 2D math and also called from math3d.bb
; testDistance2D()

Type Point
	Field x#
	Field y#
End Type 	

Type Polygon 
	Field numpoints
	Field points.Point[7]
End Type	

Function CreatePoint.Point(x#,y#)
	Local p.Point = New Point 
	p\x = x
	p\y = y
	Return p
End Function  

Function CreatePolygon.Polygon()
	Local p.Polygon = New Polygon	
	Local i
	For i = 0 To 7 
		pt.Point = CreatePoint(-1,-1)
		p\points[i] = pt
	Next 	
	p\numpoints = 0	
	Return p	
End Function 

Function AddPolygonPoint(pg.Polygon,pt.Point)	
	If pg\numpoints < 8 Then		
		pg\points[pg\numpoints] = pt 
		pg\numpoints = pg\numpoints + 1
	End If 	
End Function 

Function DrawPolygon(p.Polygon)	
	Local i
	For i = 0 To p\numpoints-1
		Draw2DOval p\points[i]\x-2,p\points[i]\y-2,p\points[i]\x+2,p\points[i]\y+2
		If i > 0 Draw2DLine p\points[i-1]\x,p\points[i-1]\y,p\points[i]\x,p\points[i]\y
	Next 
	If p\numpoints > 0 Then Draw2DLine p\points[p\numpoints-1]\x,p\points[p\numpoints-1]\y,p\points[0]\x,p\points[0]\y	
End Function 

Function PointCollidedPolygon%(pt.Point,pg.Polygon)
	Local r = False 
	Local j = pg\numpoints - 1 
	Local i
	For i = 0 To pg\numpoints - 1
		If ((pg\points[i]\y < pt\y And pg\points[j]\y >= pt\y) Or (pg\points[j]\y < pt\y And pg\points[i]\y >= pt\y)) Then
			If (pg\points[i]\x + (pt\y - pg\points[i]\y) / (pg\points[j]\y - pg\points[i]\y) * (pg\points[j]\x - pg\points[i]\x) < pt\x) Then
				r = Not r
			End If
		End If
		j = i
	Next
	Return r
End Function 

Function MouseCollidedRect%(l,t,r,b) ; left, top, right, bottom
	Local c = False
	If (GetMouseX() > l And GetMouseX() < r And GetMouseY() > t And GetMouseY() < b) Then c = True
	Return c 	
End Function 

Function Distance2D#(x1#,y1#,x2#,y2#)
	local dx# = x2-x1
	local dy# = y2-y1
	local d# = Sqr(dx * dx + dy * dy)
	Return d#
End Function 

Function GetEntityDistanceFlat#(e1,e2)
	Local d# = Distance2D(GetEntityX(e1),GetEntityZ(e1),GetEntityX(e2),GetEntityZ(e2))
	Return d
End Function 

Function CirclesCollided(x1#,y1#,r1#,x2#,y2#,r2#) 
	If Distance2D(x1,y1,x2,y2) < r1 + r2 Then 
		Return True 
	Else
		Return False
	End If				
End Function 

Function RectsCollided(l1#,t1#,r1#,b1#,l2#,t2#,r2#,b2#)
	Return (l1 < r2 And t1 < b2 And r1 > l2 And b1 > t2)
End Function 

Function GetCollisionType%(l1#,t1#,r1#,b1#,l2#,t2#,r2#,b2#) ; left, top, right, bottom
	; 0 = no collision 
	; 1 = right / left collision 
	; 2 = bottom / top collision 
	; 3 = left / right collision 
	; 4 = top / bottom collision	
					
	Local c = 0
	local d1# = r2 - l1
	local d2# = b2 - t1
	local d3# = r1 - l2
	local d4# = b1 - t2	
	
	; display these 4 amounts for debugging purposes
	; DisplayTextCenter d1,-150,constan_font
	; DisplayTextCenter d2,-120,constan_font
	; DisplayTextCenter d3,-90,constan_font
	; DisplayTextCenter d4,-60,constan_font
	
	; smallest amount determines the type
	If (d1 < d2 And d1 < d3 And d1 < d4) Then c = 1
	If (d2 < d1 And d2 < d3 And d2 < d4) Then c = 2
	If (d3 < d1 And d3 < d2 And d3 < d4) Then c = 3
	If (d4 < d1 And d4 < d2 And d4 < d3) Then c = 4
	
	Return c 		
End Function 

Function testDistance2D()
	x1# = 5
	y1# = 7
	x2# = 10
	y2# = 20
	d# = Distance2D(x1,y1,x2,y2)
	msg$ = "Points are " + x1 + "," + y1 + " And " + x2 + "," + y2 + Chr$(13)
	msg$ = msg$ + "Distance is " + d
	Alert msg$
End Function 