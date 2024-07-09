; platformer.bb
; by Chaduke
; started on 20240512

; ******************************************************
; see README.bb for all the keyboard and mouse controls 
; ******************************************************

; a 3D first person platformer 
; I'm thinking of calling the editor
; "Larry Lemon's Platformer Construction Kit"
; and the first "story mode" called Citrus Island
; everything is very much in its infancy right now

; a * indicates that the feature has been completed 
; to a degree that it functions 
; but I plan to keep working on everything indefinitely
; after I release the first version I'll start adding 
; dated change notes 

; FEATURE LIST :
; * procedurally generated terrain
; * trees and grass 
; * animated NPC character(s)
; * toggling between level edit mode and game mode
; * simple circular platforms and collision

; TODO LIST : 
; 
; proper loading and saving of levels  (this is very buggy)
; a simple dialogue system 
; sound (there's only very basic stand-in sounds right now)
; music
; todo - tons of stuff related to the editor, see editor.bb

Include "scene.bb" ; builds the scene
Include "game.bb" ; everything related to game mode
Include "editor.bb" ; everything related to level edit mode

Function DisplayTextCenter(msg$,offsetY#)
	Local centerX# = GetWindowWidth()/2
	Local centerY# = GetWindowHeight()/2
	Local mhl# = GetTextWidth(bradybunch,msg$) / 2 ; message half width
	Set2DTextColor 1,1,1,1
	Draw2DText msg$,centerX#-mhl#,centerY# + offsetY
End Function 

; create our window and scene	
CreateWindow(1920,1080,"3D Platformer",1)

; call GenerateScene which creates everything step by step
; and displays the progress on the 2D overlay
; everything related to that is in scene.bb
If generate_scene Then 
	GenerateScene() 
Else 
	LoadScene(0,0)
End If 
	
; RUNTIME variables and flags

; press TAB to toggle Level Edit / Game Mode 
gamemode = True ; run game on start
; gamemode = False ; go into editor on start

If gamemode = True Then SetupGameMode() ; game.bb
If gamemode = False Then SetupEditMode() ; editor.bb

; info and fps are currently displayed 
; at the bottom left of the screen
; they can be toggled with F2 and F3

show_info = True
show_fps = True 

quit = False
loop = True ; changed by ESC or window events
While loop
	e = PollEvents()
	If e=1 Then loop=False ; window close clicked
	
	; Y and N to Confirm / Cancel Quit
	If (IsKeyHit(89) And quit = True) Then loop = False
	If (IsKeyHit(78) And quit = True) Then quit = False
	
	; ESC to quit 	
	If IsKeyHit(256) Then quit = True			
	
	; F2 toggles info 
	If IsKeyHit(291) Then show_info = Not show_info
	
	; F3 toggles FPS
	If IsKeyHit(292) Then show_fps = Not show_fps
	
	; TAB to toggle Level Edit Mode / Game Mode
	If IsKeyDown(258) Then 	
		If Not tabdown Then 
			tabdown = True 		
			If gamemode Then
				gamemode = False
				SetupEditMode() ; editor.bb			
			Else 
				gamemode = True
				SetupGameMode() ; game.bb
			End If
		End If			
	Else 
		tabdown = False
	End If	
	
	; toggle Edit mode / Game mode with middle mouse in Debug Mode
	If IsMouseButtonDown(2) Then 
		If middown = False Then 
			; it was just pressed
			If gamemode Then
				gamemode = False
				SetupEditMode() ; editor.bb			
			Else 
				gamemode = True
				SetupGameMode() ; game.bb
			End If
		End If	
		middown = True 
	Else 
		middown = False
	End If 
	
	; switch program flow between main modes of operation 
	If gamemode Then 
		ProcessGame() ; game.bb				
	Else 
		ProcessEditMode() ; editor.bb
	End If	 		
	
	; show fps
	If show_fps Then 
		Set2DTextColor 1,1,0,1	
		Draw2DText "FPS " + GetFPS(),10,GetWindowHeight()-30
	End If	
	
	; show info
	If show_info Then 
		Set2DTextColor 1,0,0,1
		Draw2DText "Camera " + GetEntityX(camera) + "," + GetEntityY(camera) + "," + GetEntityZ(camera),10,GetWindowHeight()-170
		Draw2DText "Pivot " + GetEntityX(pivot) + "," + GetEntityY(pivot) + "," + GetEntityZ(pivot),10,GetWindowHeight()-150
		Draw2DText "Terrain Height at Pivot : " + GetTerrainHeight(GetEntityX(pivot),GetEntityZ(pivot),trn),10,GetWindowHeight()-130
	
		Set2DTextColor 1,0,1,1
		Draw2DText "Camera Rotation : " + GetEntityRX(camera) + "," + GetEntityRY(camera) + "," + GetEntityRZ(camera),10,GetWindowHeight()-110
		Draw2DText "Pivot Rotation : " + GetEntityRX(pivot) + "," + GetEntityRY(pivot) + "," + GetEntityRZ(pivot),10,GetWindowHeight()-90		
	End If		
	; swap buffers
	
	If quit = True Then DisplayTextCenter("REALLY QUIT? (Y / N)",-20)
	Present()
Wend 	
End 