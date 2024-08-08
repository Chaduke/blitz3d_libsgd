; hoops.bb
; by Chaduke 
; 20240721

Include "../engine/gameapp.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/trees.bb"
Include "../engine/sprites.bb"
Include "../engine/navigation.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "basketball.bb"
Include "player.bb"
Include "goal.bb"
Include "debug_window.bb"

g.GameApp = CreateGameApp("Hoops","Chaduke's",1)
env.Environment = CreateEnvironment(g\gui_font)
UpdateEnvironment env,True 
ground = CreateGround()
p.Player = CreatePlayer()
MoveEntity p\pivot,64,3,62
b.Basketball = CreateBasketball()
MoveEntity b\pivot,64,2,60
gl.Goal = CreateGoal(64,0,64)
EnableCollisions 1,0,2
EnableCollisions 2,0,1
SetMouseZ -5
g\debug = True 

d.DebugWindow = CreateDebugWindow(g)

While g\loop
	BeginFrame g	
	If (g\debug Or env\gui\v) Then r = GUIDragCheck()
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow env\gui	
	If IsKeyHit(KEY_F2) Then ShowHideGUIWindow d\gui	

	If env\gui\v = False Then 
		SetMouseCursorMode 3	
		UpdateBasketball b
		UpdatePlayer g,p,b 	
		UpdateColliders()				 
		SetEntityPosition g\pivot,GetEntityX(p\pivot),GetEntityY(p\pivot)+0.85,GetEntityZ(p\pivot)				
		TurnEntity g\pivot,-GetMouseVY() * 0.1,-GetMouseVX() * 0.1,0
		SetEntityRotation g\pivot,GetEntityRX(g\pivot),GetEntityRY(g\pivot),0
		SetEntityPosition g\camera,0,0,GetMouseZ()		
	Else 
		SetMouseCursorMode 1
		UpdateEnvironment env,r	
	End If 
		
	EndFrame g,False 
	If p\shooting Then DisplayTextCenter "Power : " + b\power,g\font
	If g\debug Then 		
		UpdateDebugWindow d,g,p,b
		DrawAllGUIs()
	End If		
	Present()
	Wend 
End 