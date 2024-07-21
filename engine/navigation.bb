; navigation.bb 
; by Chaduke
; 20240517

; this is functionality for navigation around the level editor
; the mouse / keyboard movement is patterned after Unreal Editor
; most of these functions should be generic enough to be used
; in all the modes and even in different games / programs

; the more specialized input should go in places like 
; platforms.bb to edit platforms - Function EditPlatforms()
; trees.bb to edit trees - Function EditPlatforms()
; I may change this in the future, not sure yet

Global cam_turn_speed# = 0.2
Global cam_move_speed_keyboard# = 0.2
Global cam_move_speed_mouse# = 0.02
Global sprint# = 0.0

Function UnrealMouseInput(e,move_speed#=0.02,turn_speed#=0.2)						
	If IsMouseButtonDown(0) ; left mouse button down
		SetMouseCursorMode 3 ; hide and lock the cursor
		If IsMouseButtonDown(1) ; right mouse button down (both are held down now)
			MoveEntity e, GetMouseVX() * move_speed, -GetMouseVY() * move_speed, 0	
		Else ; only left mouse button is down
			If value_adjust Then 
				; adjust a slider type value
			Else					
				MoveEntity e, 0, 0, -GetMouseVY() * move_speed
				TurnEntity e, 0, -GetMouseVX() * turn_speed, 0	
			End If			
		EndIf		
	ElseIf IsMouseButtonDown(1) ; only right mouse button is down
		SetMouseCursorMode 3
		TurnEntity e, -GetMouseVY() * turn_speed, -GetMouseVX() * turn_speed, 0	
	Else 
		SetMouseCursorMode 1 ; set the mouse cursor to normal
	End If	
	If (GetEntityRZ(e) < 0 Or GetEntityRZ(e) > 0) Then SetEntityRotation e, GetEntityRX(e), GetEntityRY(e), 0	
End Function

Function ClampEntityPitch(e,rangehi#,rangelo#)
	; prevent looking too far downwards
	If GetEntityRX(e) < rangelo Then SetEntityRotation e, rangelo, GetEntityRY(e), 0
	
	; prevent looking too far upward
	If GetEntityRX(e) > rangehi Then SetEntityRotation e, rangehi, GetEntityRY(e), 0	
End Function 

Function CorrectEntityRoll(e)
	; prevent entity from rolling
	If (GetEntityRZ(e) < 0 Or GetEntityRZ(e) > 0) Then SetEntityRotation e, GetEntityRX(e), GetEntityRY(e), 0			
End Function

Function ClampEntityAboveTerrain(e,t.Terrain,height#=1)
	Local ex# = GetEntityX(e)
	Local ey# = GetEntityY(e)
	Local ez# = GetEntityZ(e)	
	Local h# = GetTerrainHeight(ex,ez,t)
	
	If ey < h + height Then MoveEntity e,0,(h + height-ey),0
	If ey > h + height Then MoveEntity e,0, -(ey-(h + height)),0
End Function 

Function KeepEntityAboveTerrain(e,t.Terrain,height#=1)	
	Local ex# = GetEntityX(e)
	Local ey# = GetEntityY(e)
	Local ez# = GetEntityZ(e)	
	If (ex < 0 Or ex > t\width Or ez < 0 Or ez > t\depth) Then Return 
	Local h# = GetTerrainHeight#(ex,ez,t)
		
	; prevent entity from going under ground	
	If ey < h + height Then MoveEntity e,0,h+height-ey,0	
End Function 

Function EntityFaceLocation(e,locx#,locz#,turn180=False)
	Local dz# = locz-GetEntityZ(e)
	Local dx# = locx-GetEntityX(e)
	Local angle# = ATan2(dz#,dx#)+90
	If turn180 Then angle = angle + 180
	SetEntityRotation e,0,angle#,0		
End Function 

Function StandardEntityAdjustments(e,t.Terrain)	
	
	KeepEntityAboveTerrain(e,t)
	ClampEntityPitch(e,70,-70)	
	CorrectEntityRoll(e)
	
End Function 

Function KeyBoardInputFirstPerson(e,camspeed#=0.2)
	
	If (IsKeyDown(340) Or IsKeyDown(344)) Then sprint=0.3 Else sprint=0.0 ; SHIFT to sprint
	If (IsKeyDown(87) Or IsKeyDown(265)) Then MoveEntity e, 0, 0, camspeed + sprint ; w or up
	If (IsKeyDown(83) Or IsKeyDown(264)) Then MoveEntity e, 0, 0, -(camspeed + sprint) ; s or down 
   	If (IsKeyDown(65) Or IsKeyDown(263)) Then MoveEntity e, -(camspeed + sprint), 0, 0 ; a or left
	If (IsKeyDown(68) Or IsKeyDown(262)) Then MoveEntity e, camspeed+sprint, 0, 0 ; d or right
	
	; add support for moving cam up / down with the keys Q / E or Pageup / Pagedown	
	If (IsKeyDown(81) Or IsKeyDown(266)) Then MoveEntity e, 0, camspeed + sprint, 0 ; Q or Pageup
	If (IsKeyDown(69) Or IsKeyDown(267)) Then MoveEntity e, 0, -(camspeed+sprint), 0 ; E or Pagedown
End Function 

Function KeyBoardInputThirdPerson(e,follower,camspeed#=0.2,updown_adjust#=0.3)
		
	SetEntityRotation e,GetEntityRX(e),GetEntityRY(follower),GetEntityRZ(e)	
	If (IsKeyDown(340) Or IsKeyDown(344)) Then sprint#=0.3 Else sprint#=0.0 ; SHIFT to sprint
	If (IsKeyDown(87) Or IsKeyDown(265)) Then MoveEntity e, 0, 0, camspeed# + sprint ; w or up
	If (IsKeyDown(83) Or IsKeyDown(264)) Then MoveEntity e, 0, 0, -(camspeed# + sprint) ; s or down 
   	If (IsKeyDown(65) Or IsKeyDown(263)) Then MoveEntity e, -(camspeed# + sprint), 0, 0 ; a or left
	If (IsKeyDown(68) Or IsKeyDown(262)) Then MoveEntity e, camspeed#+sprint, 0, 0 ; d or right
	
	; add support for moving cam up / down with the keys Q / E or Pageup / Pagedown	
	If (IsKeyDown(81) Or IsKeyDown(266)) Then 
		If (IsKeyDown(KEY_LEFT_CONTROL) Or IsKeyDown(KEY_RIGHT_CONTROL)) Then 
			ScaleEntity e,1.1,1.1,1.1
		Else 		 
			MoveEntity e, 0, camspeed# * updown_adjust# + sprint, 0 ; Q or Pageup
		End If 	
	End If 
		
	If (IsKeyDown(69) Or IsKeyDown(267)) Then	
		If (IsKeyDown(KEY_LEFT_CONTROL) Or IsKeyDown(KEY_RIGHT_CONTROL)) Then 
			ScaleEntity e,0.9,0.9,0.9
		Else 	
			MoveEntity e, 0, -(camspeed# * updown_adjust# + sprint), 0 ; E or Pagedown
		End If 	
	End If 
		
	SetEntityPosition follower,GetEntityX(e),GetEntityY(e),GetEntityZ(e)
End Function

Function SwitchToFirstPerson()
	; unparent the camera from the pivot
	SetEntityParent camera,0
End Function 	

Function SwitchToThirdPerson()	
	; parent the camera to the pivot	
	SetEntityParent camera,pivot
	; zero out the cameras position And rotation 
	SetEntityPosition camera,0,0,0
	SetEntityRotation camera,0,0,0
	; now the camera should equal to the pivot
	; set mouse z position to -10
	SetMouseZ(-10)
	SetMouseCursorMode 3 ; lock and hide
End Function 

Function ThirdPersonMouseInputEditor(cam,piv,mz#=1.0,ts#=0.2)	
	; adjust for mousewheel
	SetEntityPosition cam,0,0,GetMouseZ() * mz#
	; moving the mouse left and right 
	; rotates the pivot clockwise and counter clockwise
	TurnEntity piv, 0, -GetMouseVX() * ts, 0
	; moving the mouse up and down
	; changes the pivot's pitch in this version
	TurnEntity piv, -GetMouseVY() * ts, 0, 0		
	; because we change both the pitch and yaw 
	; of the same entity we must correct the roll
	; unless you like flying around like 
	; like you're in an out of control airplane
	; or playing descent, remember that game?
	CorrectEntityRoll(piv)	
End Function

Function NavigationMode(e,t.Terrain)
	
	KeyboardInputFirstPerson e
	UnrealMouseInput e
	StandardEntityAdjustments e,t 
	
End Function 