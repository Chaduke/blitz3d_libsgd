; rock.bb
; by Chaduke
; 20240821

Type Rock
	Field color
	Field r#,g#,b#,a#
	Field x#,y# ; position	
	Field direction 
	Field speed#
End Type

Function CreateRock.Rock(maxspeed#=1.0)
	SeedRnd MilliSecs()
	Local r.Rock = New Rock
		r\color = Rand(0,3)
		If r\color = 0 Then 
			r\r# = 1.0 : r\g# = 0.0 : r\b# = 0.0 : r\a# = 1.0
		End If 
		
		If r\color = 1 Then 
			r\r# = 0.0 : r\g# = 1.0 : r\b# = 0.0 : r\a# = 1.0
		End If 
		
		If r\color = 2 Then 
			r\r# = 0.0 : r\g# = 0.0 : r\b# = 1.0 : r\a# = 1.0
		End If 

		If r\color = 3 Then 
			r\r# = 1.0 : r\g# = 1.0 : r\b# = 0.0 : r\a# = 1.0
		End If
		
		r\x = -10
		r\y = GetWindowHeight() - 5		
		r\direction = 0
		r\speed = Rnd(0.1,maxspeed)
	Return r
End Function 

Function UpdateRock(r.Rock)	
	Set2DFillColor r\r#,r\g#,r\b#,r\a#
	Set2DOutlineEnabled False 
	Draw2DRect r\x-10,r\y,r\x + 10,r\y+5

	If r\direction = 0 Then 
		r\x = r\x + r\speed
		If r\x > GetWindowWidth() Then 
			r\direction = 1
			r\y = r\y - 5
		End If 
	End If 
		
	If r\direction = 1 Then 
		r\x = r\x - r\speed
		If r\x < -10 Then 
			r\direction = 0
			r\y = r\y - 5
		End If 	
	End If
End Function 