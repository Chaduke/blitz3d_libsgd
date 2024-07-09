; influence.bb
; by Chaduke
; 20240615

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

CreateWindow sw,sh,"Influence",1

loop = True
While loop
	; input 
	e = PollEvents()
	If e = 1 Then loop = False
	If IsKeyHit(256) Then loop = False	
	
	; render 3D stuff 
	RenderScene()	
	; render 2D stuff 
	Clear2D()		
	md# = Distance2D(sw/2 - rw/2,sh/2 - rh/2,sw/2 + rw/2,sh/2 + rh/2)
	i1# = 1.0 - Distance2D(GetMouseX(),GetMouseY(),sw/2 - rw/2,sh/2 - rh/2) / md
	i2# = 1.0 - Distance2D(GetMouseX(),GetMouseY(),sw/2 - rw/2,sh/2 + rh/2) / md
	i3# = 1.0 - Distance2D(GetMouseX(),GetMouseY(),sw/2 + rw/2,sh/2 - rh/2) / md
	i4# = 1.0 - Distance2D(GetMouseX(),GetMouseY(),sw/2 + rw/2,sh/2 + rh/2) / md
			
	Draw2DText i1,sw/2 - rw/2 - 20,sh/2 - rh/2 - 20
	Draw2DText i2,sw/2 - rw/2 - 20,sh/2 + rh/2 + 10
	Draw2DText i3,sw/2 + rw/2 + 10,sh/2 - rh/2 - 20
	Draw2DText i4,sw/2 + rw/2 + 10,sh/2 + rh/2 + 10	
	
	Set2DFillColor 1,1,1,1
	Draw2DRect sw/2 - rw/2,sh/2 - rh/2,sw/2 + rw/2,sh/2 + rh/2
	Set2DFillColor 1,0,0,1
	Draw2DOval sw/2 - rw/2 - 5,sh/2 - rh/2 - 5,sw/2 - rw/2 + 5,sh/2 - rh/2 + 5
	Draw2DOval sw/2 - rw/2 - 5,sh/2 + rh/2 - 5,sw/2 - rw/2 + 5,sh/2 + rh/2 + 5
	Draw2DOval sw/2 + rw/2 - 5,sh/2 - rh/2 - 5,sw/2 + rw/2 + 5,sh/2 - rh/2 + 5
	Draw2DOval sw/2 + rw/2 - 5,sh/2 + rh/2 - 5,sw/2 + rw/2 + 5,sh/2 + rh/2 + 5
	
	Draw2DLine sw/2+rw/2,sh/2-rh/2,sw/2-rw/2,sh/2+rh/2
	
	Present()
Wend 
End
	