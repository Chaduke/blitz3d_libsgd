; hoops2d.bb
; by Chaduke 
; 20240722

Include "../engine/gameapp.bb"
Include "ball.bb"
Include "player.bb"
Include "court.bb"
Include "goal.bb"

g.GameApp = CreateGameApp("Hoops 2D","Chaduke's",1)
g\debug = False

c.Court = CreateCourt()
gl.Goal = CreateGoal(1520,225)
p.Player = CreatePlayer(100,700)
b.Ball = CreateBall(200,GetWindowHeight()-800,0,0,0,0.98,0,0.1,20,0.9,0.9,0.5,0.03,1)

While g\loop
	BeginFrame g		
	; update 3D here
	RenderScene()
	; 2D Stuff starts after 3D is rendered
	
	Clear2D()
	DrawCourt c	
	; MoveGoal gl
	UpdatePlayer p,b,c
	UpdateBall b,p,c
	DrawGoal gl
	If p\shooting Then 
		DisplayTextCenter "Power : " + p\power,g\font	
		DrawPrediction b,p
	End If 
	If (p\shooting And p\jumping) Then DisplayTextCenter "Jump Shot!",g\font,30
	If g\debug Then 
		Draw2DCircle GetMouseX(),GetMouseY(),20
		Draw2DCircle 1595,435,20 ; made it 
		Draw2DCircle 1634,468,20 ; back rim bounce
		Draw2DCircle 1552,452,20 ; front rim bounce
		
		Draw2DText "Player X,Y : " + p\pos\x + "," + p\pos\y,5,5
		Draw2DText "Ball X,Y : " + b\pos\x + "," + b\pos\y,5,25
		Draw2DText "Goal X,Y : " + gl\pos\x + "," + gl\pos\y,5,45		 
		Draw2DText "Ball Vel Y Max: " + bvymax#,5,65	
		
		Draw2DText "Mouse X,Y : " + GetMouseX() + "," + GetMouseY(),5,85
	End If 		
	EndFrame g,True,False,False	
Wend 
End 

 



	