; editor.bb
; by Chaduke
; 20240517

; functionality related to "Level Edit" mode
; (gamemode = false)

; Note that when I use the term "Editor" here
; I'm referring to elements in a scene and how they are placed within.
; Beyond that different values can be tweaked like light angle 
; or the scale of static objects, or what type of material a platform has
; the assets are not created here, mostly just placed and adjusted
; you would use an external program like Blender / Gimp / Audacity / Reaper
; to create these assets and then use these editors to organize them within your 
; scene / levels

; so when I say "Sprite / Billboard Editor" 
; I'm referring to placing these in the 3D scene, not creating them 

; FEATURE LIST :
; * generic input functions - navigation.bb
; * switching between 10 different modes
; * Basic Navigation Mode 
; * Working Platform Editor
; * Working Terrain Editor
; * Working Tree / Static Object Editor 
; * Beginnings of a Sprite / Billboard Editor

; TODO LIST : 
; start Environment / Lights Editor
; start Player / NPC / Dialogue Editor
; start Special Objects Editor
; start Sound / Music Editor
; start Keys / Gamepad setup

Include "navigation.bb" ; contains standard input functions 

; use "Unreal Editor" style controls to move around	
; left mouse button - turn left and right, move forwards and back
; right mouse button - flymode
; both buttons - strafe left and right, move up and down

Global submode = 4 ; set the initial submode

; SUBMODES LIST
; this is a basic outline, probably will change
; I can see them being combined, I'd like to keep 
; everything condensed to 10 maximum
; to keep things less complicated 
; and easy keyboard access

; 0 = Navigation 
; 1 = Platform Editor
; 2 = Terrain Editor
; 3 = Tree Editor
; 4 = Sprite / Billboard Editor
; 5 = Environment / Lights Editor
; 6 = Player / NPC / Dialogue Editor
; 7 = Special Objects Editor
; 8 = Sound / Music Editor
; 9 = Keys / Gamepad setup

; these modes can be switched with the number keys 

; Numpad numbers will be used for quick loading and saving levels / settings / etc
; I'd like to set it up with a Bank / Slot system the way drum machines and keyboards work

; this is called whenever we switch from Game Mode
; when called from the main loop in platformer.bb
Function SetupEditMode()
	If (submode > 0 And submode <> 2) Then 
		SwitchToThirdPerson()
	Else 
		SwitchToFirstPerson()
	End If		
End Function 

; This is called every frame from main loop
; While in Edit Mode
Function ProcessEditMode()
	; LEVEL EDIT MODE	
		; 0 will switch to Navigation Mode	
		If IsKeyDown(48) Then 
			If key0 = False Then 
				key0=True
				If submode>0 Then 
					submode = 0
					SwitchToFirstPerson()
				End If
			End If	
		Else 
			key0=False
		End If			
	
		; 1 will switch to Platform Edit
		If IsKeyDown(49) Then  
			If key1 = False Then
				If submode <> 1 Then 
					submode = 1
					SwitchToThirdPerson() ; navigation.bb
				End If 
			End If	
		Else 
			key1 = False
		End If	
		
		; 2 - switch to Terrain Editor
		If IsKeyDown(50) Then 
			If key2 = False Then 
				If submode<>2 Then 
					submode = 2
					SwitchToFirstPerson()
				End If
			End If	
		Else 
			key2 = False
		End If	
		
		; 3 - switch to Tree Editor
		If IsKeyDown(51) Then
			If key3 = False Then  
				If submode<>3 Then 
					submode = 3
					SwitchToThirdPerson() ; navigation.bb
				End If	
			End If 
		Else 
			key3 = False
		End If
						
		; 4 - switch to Sprite / Billboard Editor	
		If IsKeyDown(52) Then
			If key4 = False Then  
				If submode<>4 Then 
					submode = 4
				End If	
			End If 
		Else 
			key4 = False
		End If
		
		; 5 - switch to env / lights editor 	
		If IsKeyDown(53) Then
			If key5 = False Then  
				If submode<>5 Then 
					submode = 5
				End If	
			End If 
		Else 
			key5 = False
		End If
		
		; 6 - switch to Player / NPC / Dialogue Editor
		If IsKeyDown(54) Then
			If key6 = False Then  
				If submode<>6 Then 
					submode = 6
				End If	
			End If 
		Else 
			key6 = False
		End If
		
		; 7 - switch to static / special objects editor
		If IsKeyDown(55) Then
			If key7 = False Then  
				If submode<>7 Then 
					submode = 7
				End If	
			End If 
		Else 
			key7 = False
		End If
		
		; 8 - switch to sound / music editor		
		If IsKeyDown(56) Then
			If key8 = False Then  
				If submode<>8 Then 
					submode = 8
				End If	
			End If 
		Else 
			key8 = False
		End If
		
		; 9 - switch to keyboard / gamepad editor
		If IsKeyDown(57) Then
			If key9 = False Then  
				If submode<>9 Then 
					submode = 9
				End If	
			End If 
		Else 
			key9 = False
		End If	
	
	; G will completely regenerate the scene 					
	If IsKeyHit(71) Then 
		Delete Each Platform
		Delete Each Cylinder
		Delete Each Player
		GenerateScene()
		SetupEditMode()	
	End If
		
	; Save Keys 
	If (IsKeyDown(341) Or IsKeyDown(345)) Then ; left or right control down
		For k=320 To 329
			If IsKeyHit(k) Then SaveScene 0,k-320
		Next 	
	Else 
		; Load Keys 
		For k=320 To 329
			If IsKeyHit(k) Then LoadScene 0,k-320
		Next 	
	End If
	
	; call the appropriate functions for each mode
	Select submode 	
	Case 0 ; Navigation Mode
		NavigationMode(trn)
	Case 1 ; Platform Editor
		EditPlatforms(trn) ; platforms.bb
	Case 2 ; Terrain Editor 
		; EditTerrain()
	Case 3 ; Tree Editor
		EditTrees(trn)	
	Case 4 ; Sprite / Billboard Editor
		EditBillboards(trn)		
	Default 
		; Navigation mode
		NavigationMode(trn)
	End Select 	
	
	; render 3D and begin 2D overlay
	RenderScene()	
	Clear2D()
	Set2DTextColor 0,0,1,1	
	msg$ = "Level Editor"
	If submode = 0 Then msg$ = msg$ + " (Navigation Mode)"
	If submode = 1 Then msg$ = msg$ + " (Edit Platforms)"
	If submode = 2 Then msg$ = msg$ + " (Edit Terrain)"
	If submode = 3 Then msg$ = msg$ + " (Edit Trees)"
	If submode = 4 Then msg$ = msg$ + " (Edit Sprites / Billboards)"
	If submode = 5 Then msg$ = msg$ + " (Edit Environment / Lights)"
	If submode = 6 Then msg$ = msg$ + " (Edit Player / NPCs / Dialogue)"
	If submode = 7 Then msg$ = msg$ + " (Edit Special Objects)"
	If submode = 8 Then msg$ = msg$ + " (Visual Scripting)"
	If submode = 9 Then msg$ = msg$ + " (Keyboard / Gamepad Setup)"
	Draw2DText msg$,10,10						
	
End Function 