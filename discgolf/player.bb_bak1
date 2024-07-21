; player.bb (Disc Golf)
; by Chaduke
; 20240704

; functionality related to a specific "Disc Golf" player

Type Player
	Field windup
	Field ready
	Field power#
	Field throw 
	Field total_throws
	Field score
	Field round_score
	Field par_score
	Field minidisc_mode
	Field minidisc
	Field new_hole
	Field release_height#
	Field model	
	Field pivot
	Field camera
	Field current_course.Course
	Field current_hole.Hole
	Field hole_count
End Type 

Function CreatePlayer.Player()
	Local p.Player = New Player 
	p\windup = False
	p\ready = True 
	p\power = 0
	p\throw = 1
	p\total_throws = 0
	p\score = 0
	p\par_score = 0
	p\minidisc_mode = False
	p\minidisc = LoadModel("../engine/assets/models/disc_golf/minidisc.glb")
	p\new_hole = False
	p\release_height# = 1.2
	p\model = CreateModel(0) ; will be fixed later	
	Return p
End Function 

Function UpdatePlayer(p.Player,d.Disc,h.Hole,g.GameApp)
	; keyboard input
	If p\ready Then 
		If debug Then KeyBoardInputThirdPerson d\pivot,p\pivot,0.1	
		If p\minidisc_mode Then KeyBoardInputThirdPerson(d\pivot,p\pivot)				
		SetEntityRotation d\pivot,GetEntityRX(d\pivot),GetEntityRY(p\pivot),GetEntityRZ(d\pivot)							
		If debug=False Then ClampEntityAboveTerrain d\pivot,h\trn,p\release_height				
	End If	
	
	If IsKeyHit(KEY_M) Then 
		If p\minidisc_mode = True Then 
			p\minidisc_mode = False 
			; move regular disc to minidisc
			SetEntityPosition d\pivot,GetEntityX(p\minidisc),GetEntityY(p\minidisc),GetEntityZ(p\minidisc)
		Else 
			If ready Then 				
				minidisc_mode = True 
				; place minidisc and start walking 
				PlaceEntityOnTerrain p\minidisc,h\trn,0.1,False,False,GetEntityX(p\pivot),GetEntityZ(p\pivot)				
			End If	
		End If 	
	End If 			

	; get mouse input 
	If (p\ready Or d\lying) Then 
		If (p\ready And IsMouseButtonDown(1)) Then 
			; adjust disc angle
			TurnEntity d\pivot,GetMouseVY() * 0.01,0,GetMouseVX() * 0.01					
		Else 					
			ThirdPersonMouseInputEditor g\camera,g\pivot,0.2,0.05
		End If 	
	End If 

	; adjust pivot location and pitch
	If (p\ready Or d\grounded Or d\lying) Then SetEntityPosition p\pivot,GetEntityX(d\pivot),GetEntityY(d\pivot)+0.1,GetEntityZ(d\pivot)					
	ClampEntityPitch(p\pivot,0,-70)	
		
		If IsMouseButtonDown(0) Then
			If d\lying Then 
				d\lying = False
				PlaceEntityOnTerrain d\pivot,h\trn,p\release_height,False,False,GetEntityX(d\pivot),GetEntityZ(d\pivot)				
				SetMouseZ -5
				p\ready = False
			Else							
				If (p\windup = False And p\ready = True) Then   
					p\windup = True
					p\ready=False						
				Else 	
					If p\windup = True Then 
						p\power = p\power + 1
						If p\power > 100 Then							
							ThrowDisc d,p
						End If		
					End If					
				End If	
			End If 					
		Else
			If p\windup = True Then 
				ThrowDisc d,p
			Else 
				If (p\ready = False And d\thrown=False And d\lying=False And d\grounded=False) Then						
					p\ready=True
					If p\minidisc_mode = False Then 
						d\ob = False																				
						If p\new_hole Then 
							p\round_score = p\throw - h\par	
							p\par_score = p\par_score + p\round_score	
							p\throw = 1
							p\current_hole = After p\current_hole 
							; reset the course
							If p\current_hole = Null Then 
								p\current_hole = First Hole
								p\par_score = 0
								p\total_throws = 0
							End If	
							; setup the next hole	
							; SetupHole p\course_number,p\hole_number 
							p\new_hole = False
						Else 
							p\throw = p\throw + 1	
							p\total_throws = p\total_throws + 1	
						End If	
					End If							
				End If 	
			End If			 
		End If ; END MOUSE DOWN LEFT 
End Function 