; start_menu.bb
; by Chaduke 
; 20240705

Type StartMenu 
	Field title_font 
	Field subtitle_font 
	Field menu_sound 
	Field last_hover
End Type 

Function CreateStartMenu.StartMenu()
	Local s.StartMenu = New StartMenu
	s\title_font = LoadFont("../engine/assets/fonts/bb.ttf",160)
	s\subtitle_font = LoadFont("../engine/assets/fonts/bb.ttf",40)
	s\menu_sound = LoadSound("../engine/assets/audio/disc_golf/menu.mp3")
	Return s
End Function 

Function StartMenuHover(s.StartMenu,num)
	If s\last_hover <> num Then 
		PlaySound s\menu_sound
		s\last_hover = num
	End If 	
End Function 

Function DisplayStartMenu(g.Gameapp)
	DrawTitle g
	If (MouseCollidedRect(400,g\wh / 2 - 60,g\ww-400,g\wh / 2 - 30)) Then
		StartMenuHover g\start_menu,1
		If IsMouseButtonHit(0) Then g\game_state = GAME_STATE_STORY				 
		Set2DTextColor 1,1,0,1 
	Else 
		Set2DTextColor 1,1,1,1
	End If	
				
	DisplayTextCenter "Story Mode",g\start_menu\subtitle_font,-60
	
	If (MouseCollidedRect(400,g\wh / 2 - 30,g\ww-400,g\wh/ 2)) Then 
		StartMenuHover g\start_menu,2
		If IsMouseButtonHit(0) Then g\game_state = GAME_STATE_SANDBOX
		Set2DTextColor 1,1,0,1 
	Else 
		Set2DTextColor 1,1,1,1
	End If 			
	DisplayTextCenter "Sandbox Mode",g\start_menu\subtitle_font,-30
	
	If (MouseCollidedRect(400,g\wh / 2,g\ww-400,g\wh / 2 + 30)) Then 
		StartMenuHover g\start_menu,3
		If IsMouseButtonHit(0) Then g\game_state = GAME_STATE_TUTORIAL
		Set2DTextColor 1,1,0,1 
	Else 
		Set2DTextColor 1,1,1,1				
	End If			
	DisplayTextCenter "Tutorial",g\start_menu\subtitle_font
	
	If (MouseCollidedRect(400,g\wh / 2 + 30,g\ww-400,g\wh / 2 + 60)) Then 
		StartMenuHover g\start_menu,4
		If IsMouseButtonHit(0) Then g\game_state = GAME_STATE_OPTIONS
		Set2DTextColor 1,1,0,1 
	Else 
		Set2DTextColor 1,1,1,1
	End If 
	DisplayTextCenter "Options",g\start_menu\subtitle_font,30			
End Function 
