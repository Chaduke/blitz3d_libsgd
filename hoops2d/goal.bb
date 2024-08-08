; goal.bb
; by Chaduke 
; 20240723

; a 2D basketball goal for hoops2d

Type Goal
	Field image
	Field pos.Vec2	
End Type 

Function CreateGoal.Goal(x,y)
	Local g.Goal = New Goal
		g\image = LoadImage("assets/hoop_side.png",1)	
		g\pos = CreateVec2(x,y)		
	Return g
End Function 

Function DrawGoal(g.Goal)
	Draw2DImage g\image,g\pos\x,g\pos\y,1
End Function 

Function MoveGoal(g.Goal)
	If IsKeyDown(KEY_UP) Then g\pos\y = g\pos\y - 5
	If IsKeyDown(KEY_DOWN) Then g\pos\y = g\pos\y + 5
	If IsKeyDown(KEY_LEFT) Then g\pos\x = g\pos\x - 5
	If IsKeyDown(KEY_RIGHT) Then g\pos\x = g\pos\x + 5
End Function 

