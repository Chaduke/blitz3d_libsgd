; gridtest.bb
; by Chaduke
; 20240527

Function Distance2D#(x1#,y1#,x2#,y2#)
	dx# = x2-x1
	dy# = y2-y1
	Return Sqr(dx * dx + dy * dy)
End Function 

CreateWindow 1920,1080,"Grid Test", 1
SetClearColor 0,0,0,1
Const gs = 4 ; gridcell size 
SetMouseZ 200
SetMouseCursorMode 3
loop = True 
While loop 
	ms = MilliSecs()
	e = PollEvents()
	If e = 1 Then loop = False
	If IsKeyHit(256) Then loop = False	
	RenderScene()
	Clear2D()	
	r# = GetMouseZ()
	For y# = 0 To GetWindowHeight() Step gs 
		For x# = 0 To GetWindowWidth() Step gs
			d# = Distance2D(GetMouseX(),GetMouseY(),x#,y#)
			If d# < r Then 
				shade# = 1 - d/r
				Set2DFillColor shade,shade,shade,1
				If gs = 1 Then 
					Draw2DPoint x,y
				Else 	
					Draw2DRect x,y,x+gs,y+gs
				End If				
			End If			
		Next 
	Next	
	Set2DTextColor 1,1,1,1
	Draw2DText "Avg MS per Frame " + avgfps#,10,10	
	frames = frames + 1	
	me = MilliSecs() - ms
	totaltime# = totaltime# + me
	If frames > 59 Then 
		frames = 0
		avgfps# = totaltime / 60
		totaltime = 0
	End If 	
	
	Present()	
Wend 
End 