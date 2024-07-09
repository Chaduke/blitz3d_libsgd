; slopes.bb
; by Chaduke
; 20240617

; init scene
Const sw = 1920
Const sh = 1080
Const rw = 300
Const rh = 300

Function Distance2D#(x1#,y1#,x2#,y2#)
	Local x# = x2#-x1#
	Local y# = y2#-y1#
	Local d# = Sqr(x*x + y*y)
	Return d
End Function 

Type Point 
	Field x#
	Field y#	
	Field r#
End Type

Function CreatePoint.Point(x#,y#,r#)
	Local p.Point = New Point
	p\x = x
	p\y = y
	p\r = r
	Return p
End Function 

CreateWindow sw,sh,"Slopes",1

loop = True
While loop
	; input 
	e = PollEvents()
	If e = 1 Then loop = False
	If IsKeyHit(256) Then loop = False	
	If IsMouseButtonHit(0) Then 
		newPoint.Point = CreatePoint(GetMouseX(),GetMouseY(),10)
	End If	
	; render 3D stuff 
	RenderScene()
		
	; render 2D stuff 
	Clear2D()		
	oldx = -1 : oldy = -1
	For p.Point = Each Point
		If (oldx > -1 And oldy > -1) Then Draw2DLine oldx,oldy,p\x,p\y
		Draw2DText Int(p\x) + "," + Int(p\y),p\x-p\r * 3,p\y-p\r * 3
		Draw2DOval p\x-p\r,p\y-p\r,p\x+p\r,p\y+p\r		
		oldx = p\x : oldy = p\y
	Next 
	Present()
Wend 
End
	