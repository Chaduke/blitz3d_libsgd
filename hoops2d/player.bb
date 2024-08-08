; player.bb
; by Chaduke 
; 20240723

; player for hoops2d

Type Player
	Field image_dribble
	Field image_shoot
	Field image_idle
	Field pos.Vec2
	Field vel.Vec2
	Field acc.Vec2
	Field speed#
	Field shooting,dribbling
	Field power#
	Field shot_time 
	Field jumping 
	Field jump_starty
End Type 

Function CreatePlayer.Player(x#,y#)
	Local p.Player = New Player
		p\image_dribble = LoadImage("assets/player_dribble.png",1)
		p\image_shoot = LoadImage("assets/player_shoot.png",1)	
		p\image_idle = LoadImage("assets/player_idle.png",1)		
		p\pos = CreateVec2(x,y)
		p\vel = CreateVec2()
		p\acc = CreateVec2()
		p\speed# = 3		
		p\dribbling = True
		p\shooting = False 
	Return p
End Function 

Function UpdatePlayer(p.Player,b.Ball,c.Court)
	
	If p\jumping = False Then 
		If IsKeyDown(KEY_A) Then 
			p\vel\x = -p\speed
		ElseIf IsKeyDown(KEY_D) Then 
			p\vel\x = p\speed
		Else 
			p\vel\x = 0
		End If
		
		If IsKeyDown(KEY_W) Then 
			p\vel\y = -p\speed
		ElseIf IsKeyDown(KEY_S) Then 
			p\vel\y = p\speed
		Else 
			If p\jumping = False Then p\vel\y = 0
		End If 		
	End If 	
 	
	If IsKeyHit(KEY_B) Then 
		If p\dribbling = False Then 
			p\dribbling = True 
			p\shooting = False 
		End If 		
	End If 	
	
	If IsKeyHit(KEY_SPACE) Then 
		If p\jumping = False Then 
			p\jumping = True 
			p\jump_starty = p\pos\y
			p\vel\y = -8
			p\acc\y = 0.2			
		End If 
	End If 	
	
	If p\jumping = True Then 
		If p\pos\y > p\jump_starty Then 
			p\pos\y = p\jump_starty
			p\jumping = False 
			p\acc\y = 0
			p\vel\y = 0
		End If 
	End If 					
	
	If IsMouseButtonDown(0) Then	
		; switch to shooting if conditions are met
		If (p\shooting = False And p\dribbling = True) Then 
			p\dribbling = False 
			p\shooting = True 
			p\power = 10
		End If
		
		; increase power
		If p\shooting = True Then 
			p\power = p\power + 0.3
			If p\power > 30 Then p\power = 30
		End If 	 	 
	Else 
		; mouse is not down
		If p\shooting Then
			; release the ball
			p\shooting = False 
			b\vel\y = - p\power * 1.5
			b\vel\x = p\power	
			p\shot_time = 0		
		End If 
	End If 	
	
	If (p\shooting = False And p\dribbling = False) Then 
		p\shot_time = p\shot_time + 1	
		If Abs(p\pos\x - b\pos\x) < 300 Then 			
			If (b\pos\y - p\pos\y) < 350 Then 
				If p\shot_time > 60 Then p\dribbling = True
			End If 	 
		End If 
	End If 	
		
	AddToVec2 p\vel,p\acc
	AddToVec2 p\pos,p\vel
	
	; check court bounds
	If p\pos\x < c\bounds_left Then	p\pos\x = c\bounds_left
	If p\pos\x > c\bounds_right Then p\pos\x = c\bounds_right
	If p\pos\y > c\player_bounds_bottom Then 
		If p\jumping = False Then p\pos\y = c\player_bounds_bottom
	End If 
		
	If p\pos\y < c\player_bounds_top Then 
		If p\jumping = False Then p\pos\y = c\player_bounds_top
	End If 	
	
	DrawPlayer p
End Function 

Function DrawPlayer(p.Player)
	If p\shooting Then 
		Draw2DImage p\image_shoot,p\pos\x,p\pos\y,1
	ElseIf p\dribbling Then 
		Draw2DImage p\image_dribble,p\pos\x,p\pos\y,1
	Else 
		Draw2DImage p\image_idle,p\pos\x,p\pos\y,1	
	End If 	
End Function 