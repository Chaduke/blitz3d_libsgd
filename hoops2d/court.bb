; court.bb
; by Chaduke
; 20240723 

; a basketball court to demonstrate boundaries 

Type Court
	Field background_image
	Field bounds_left
	Field bounds_right
	Field player_bounds_bottom
	Field ball_bounds_bottom
	Field player_bounds_top
	Field ball_bounds_top
End Type 

Function CreateCourt.Court()
	Local c.Court = New Court
	c\background_image = LoadImage("assets/background.png",1)
	c\player_bounds_bottom = GetWindowHeight() - 322
	c\ball_bounds_bottom = GetWindowHeight() - 20
	c\bounds_left = 0
	c\bounds_right = GetWindowWidth() - 100
	c\player_bounds_top = GetWindowHeight() - 468
	c\ball_bounds_top = -100
	Return c
End Function 

Function DrawCourt(c.Court)
	Draw2DImage c\background_image,0,0,1
End Function 