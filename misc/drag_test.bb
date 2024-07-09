; drag_test.bb
; by Chaduke
; 20240625

; test of dragging the mouse and changing small float values
; and testing for accuracy 

CreateWindow 1920,1080,"",1
x# = 0.02

loop = True
While loop 
	PollEvents()
	If IsKeyDown(256) Then loop = False
	If IsMouseButtonDown(0) Then 
		If dragging = False Then 
			dragging = True 
			start_x = GetMouseX()
			start_val# = x
		Else 
			x = (GetMouseX() - start_x) * 0.001 + start_val#
			If x < 0.01 Then x = 0.01
			If x > 0.05 Then x = 0.05
		End If 	
	Else 
		dragging = False 
	End If 	
		
	RenderScene()
	Clear2D()
	Draw2DText x,5,5
	Draw2DText start_x,5,25
	Present()
Wend 
End 

