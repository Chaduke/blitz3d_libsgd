Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"

Include "player.bb"
Include "level.bb"

g.GameApp=CreateGameApp("Sphere Nothing","Chaduke's")

p.Player = CreatePlayer()
level_path$ = "levels/tutorial.txt"
l.Level = LoadLevel(level_path$)
BuildLevel l
l\env = CreateEnvironment(g\gui_font)
SetEntityPosition(p\pivot, l\startpos\x, l\startpos\y + 1, 0)

SetMouseZ -10
EnableCollisions 0,1,2

cursor_mesh =CreateSphereMesh(0.5,16,16,l\ground_material)
cursor_model=CreateModel(cursor_mesh)
SetEntityPosition cursor_model,-100,-100,0

g\debug = False
g\game_state = GAME_STATE_STORY

While g\loop
	BeginFrame g	
	
	Select g\game_state
	Case GAME_STATE_MENU 
		DisplayStartMenu g
	Case GAME_STATE_STORY
		If IsKeyHit(KEY_TAB) Then g\game_state = GAME_STATE_SANDBOX
			
		UpdatePlayer p,l	
		SetEntityPosition g\camera,0,0,GetMouseZ()			
		CamFollow(g\pivot,p\pivot)	
		UpdateColliders()
				
		If p\dead Then 
			DisplayTextCenter "YOU ARE DEAD!! PRESS SPACE TO REVERSE TIME",g\font
		ElseIf p\level_finished Then 
			DisplayTextCenter "LEVEL COMPLETE!! PRESS SPACE TO ADVANCE",g\font	
		ElseIf p\advancing Then 
			DisplayTextCenter "Loading Next Level",g\font	
			p\current_level = p\current_level + 1
			ClearLevel g,p
			level_path$ = "levels/level" + p\current_level + ".txt"
			l.Level = LoadLevel(level_path$)
			BuildLevel l
			l\env = CreateEnvironment(g\gui_font)
			SetEntityPosition(p\pivot, l\startpos\x, l\startpos\y + 1, 0)
			p\collider = CreateSphereCollider(p\pivot,0,0.49)
			p\advancing = false 				
		End If 	
	Case GAME_STATE_SANDBOX
		If IsKeyHit(KEY_TAB) Then g\game_state = GAME_STATE_STORY
		UpdateEnvironment l\env
		If (GuiDraggedCheck()=False And WidgetDraggedCheck() = False) Then 
			; SetMouseCursorMode 3
			; translate mouseX and mouseY to 3D space
			xunits = Abs(GetMouseZ()) * 1.6
			yunits = GetMouseZ() * .93
		   x# = GetMouseX() / GetWindowWidth() * xunits - GetEntityX(g\camera) - (xunits / 20)
			y# = GetMouseY() / GetWindowHeight() * yunits - GetEntityY(g\camera) + (yunits / 20)
			SetEntityPosition cursor_model,x,y,0
			Set2DTextColor 1,0,0,1
			Draw2DText "Cursor X : " + GetEntityX(cursor_model),5,5
			Draw2DText "Cursor Y : " + GetEntityY(cursor_model),5,25
		End If
		SetEntityPosition g\camera,0,0,GetMouseZ() 	
	End Select	
		
	EndFrame g,False
	
	; debug info
	If g\debug Then		
		local c = GetCollisionCount(p\collider)
		Set2DTextColor 1,0,0,1
		If  c > 0 Then 
			Draw2DText("Collision Count :" + c, 5, 5)
			Draw2DText "Collision NY : " + GetCollisionNY(p\collider,0),5,25
		End If
		Draw2DText "Player Velocity Y : " + p\vel\y,5,45
		Draw2DText "Distance from Checkpoint 0 : " + Distance2D(GetEntityX(p\pivot),GetEntityY(p\pivot),l\checkpoints[0]\x,l\checkpoints[0]\y),5,65
	End If 
	Present()
Wend 
End 