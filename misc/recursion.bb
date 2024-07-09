; recursion.bb
; by Chaduke
; 20240609

; demonstration of recursion in Blitz3D 

Global t# = 0.0

Function DrawPattern(size#)	
	If size > 10 Then 		
		x = WindowWidth() / 2
		y = WindowHeight() / 2		
		Set2DFillColor Cos(t),1-Sin(Rnd(t)),Sin(t),1
		Draw2DOval x - size,y - size,x + size, y + size
		DrawPattern(size  * 0.8)
	End If	
End Function 

CreateWindow 1920,1080,"Hello Recursion",1
CreateScene()

loop = True 
While loop 	
	e = PollEvents()
	If e = 1 loop = False
	If KeyDown(256) Then loop = False
	t = t + Rnd(1)
	Clear2D()	
	DrawPattern(500)
	Draw2DText "Hello Recursion",10,10
	RenderScene()
	Present()
Wend 
End 

