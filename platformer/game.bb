; game.bb
; by Chaduke
; 20240517

; functionality related to "Game Mode" (gamemode = true)

; press SPACE To Jump	
; if you time it right when you are floating still in mid air
; then you get another jump, it's not that easy to do indefintely 
; each succesive jump goes higher but are harder to time

Include "player.bb"

Global playerone.Player
Global gravity# = -0.0004
Global friction# = 0.02
Global show_game_info = False

; this is called whenever we switch from Edit Mode
; when called from the main loop in platformer.bb
Function SetupGameMode()
	If playerone = Null Then 	playerone = CreatePlayer(trn) ; player.bb		
	SetEntityPosition pivot, GetEntityX(playerone\pivot),GetEntityY(playerone\pivot),GetEntityZ(playerone\pivot)
	SetEntityRotation pivot, 0,GetEntityY(playerone\pivot),0
	SetEntityParent camera,pivot
	SetEntityPosition camera, 0,0,0
	SetEntityRotation camera, 0,0,0				
	SetMouseCursorMode 3	
	SetMouseZ -5
	; PlaySound song1_music
End Function 

; This is called every frame from main loop
; While in Game Mode
Function ProcessGame() 
	; GAME MODE	
	GetChar() ; keeps the buffer from filling up 
	GetPlayerInput(playerone)
	UpdatePlayer(playerone,trn)	
	
	; set the pivot based on location of the player
	SetEntityPosition pivot,playerone\pos\x,playerone\pos\y,playerone\pos\z	
	
	; update NPCs
	If add_npcs Then UpdateNPCs(trn)	
	
	; render 3D and begin 2D overlay
	RenderScene()
	Clear2D()	
	Set2DTextColor 0,0,1,1	
	Draw2DText "Game Mode",10,10	
	
	DisplayDialogueMessage "Welcome to Citrus Island", GetWindowWidth() / 2, 10
	
	If show_game_info Then 
		Draw2DText "H = " + playerone\h#,10,30
		Draw2DText "Hdiff = " + playerone\hdiff#,10,50
		msg$ = "Landed Platform = " 
		If playerone\landed_platform <> Null Then 
			msg$ = msg$ +  playerone\landed_platform\name$
		Else 
			msg$ = msg$ + " None"
		End If		
		Draw2DText msg$,10,70
		Draw2DText "Anim Time = " + playerone\anim_time,10,90
	End If
	
	; draw health bar
	Set2DFillColor 1,0,0,1
	Draw2DRect GetWindowWidth() - 200,20,GetWindowWidth() - 200 + (playerone\health * 180),40
	
	; draw stamina bar
	Set2DFillColor 1,1,0,1
	Draw2DRect GetWindowWidth() - 200,45,GetWindowWidth() - 200 + (playerone\stamina * 180),65
	
	If (playerone\jumps>0) Then 
		Set2DTextColor 0,0,0,1
		Draw2DText "AIRBORNE!! " + playerone\hdiff,GetWindowWidth() - 200, 70
		Draw2DText "Jump # " + playerone\jumps,GetWindowWidth() - 200, 90
	End If	

End Function 
