; ball.bb
; by Chaduke
; 20240722

Type Ball
	Field radius#,damping#
	Field red#,green#,blue#,alpha#
	Field pos.Vec2
	Field vel.Vec2
	Field acc.Vec2
	Field rot.Vec2	
	Field made, missed
	Field bounce1,bounce2,made1,miss
End Type 

Function CreateBall.Ball(x#=0,y#=0,vx#=0,vy#=0,ax#=0,ay#=0,rx#=0,ry#=0,radius#=10,damping#=0.7,red#=0.5,green#=0.5,blue#=0.5,alpha#=1.0)
	Local b.Ball = New Ball
		b\radius# = radius#
		b\damping = damping#		
		b\pos = CreateVec2(x#,y#)
		b\vel = CreateVec2(vx#,vy#)
		b\acc = CreateVec2(ax#,ay#)
		b\rot = CreateVec2(rx#,ry#)
		b\red# = red#
		b\green# = green#
		b\blue# = blue#
		b\alpha# = alpha#
		b\made = False	
		b\missed = False 
		b\bounce1 = LoadSound("assets/bounce1.wav")
		b\bounce2 = LoadSound("assets/bounce2.wav")
		b\made1 = LoadSound("assets/made1.wav")
		b\miss = LoadSound("assets/miss.wav")
	Return b
End Function 

Function UpdateBall(b.Ball,p.Player,c.Court)
	
	AddToVec2 b\vel,b\acc
	AddToVec2 b\pos,b\vel
	
	If p\shot_time < 60 Then 
		b\rot\y = b\vel\x * 5
	Else 
		b\rot\y = -b\vel\x * 3
	End If 	
	
	b\rot\x = b\rot\x + b\rot\y
		
	; bounce off bottom	
	If b\pos\y > c\ball_bounds_bottom Then
		b\pos\y = c\ball_bounds_bottom
		b\vel\y = -b\vel\y * b\damping#		
		b\vel\x = b\vel\x * 0.9
		b\made = False
		b\missed = False
		If (Abs(b\vel\y) > 0.7) Then 
			If Rnd(1) > 0.5 Then 
				PlaySound b\bounce1
			Else 
				PlaySound b\bounce2
			End If 	
		End If 		 
	End If		
	
	; hit left wall
	If b\pos\x < c\bounds_left Then	
		b\pos\x = c\bounds_left
		b\vel\x = -b\vel\x * b\damping
		PlaySound b\miss
	End If 
	
	; hit right wall	
	If b\pos\x > c\bounds_right Then 
		b\pos\x = c\bounds_right
		b\vel\x = -b\vel\x * b\damping
		PlaySound b\miss
	End If 	
	
	; hit ceiling
	If b\pos\y < c\ball_bounds_top Then 
		b\pos\y = c\ball_bounds_top
		b\vel\y = -b\vel\y
		b\vel\x = b\vel\x * 0.2
		PlaySound b\miss
	End If 
	
	; hit backboard 
	If b\vel\x > 0 Then 
		If b\pos\x > 1648 Then 
			If (b\pos\y > 229 And b\pos\y < 437) Then 
				b\pos\x = 1648
				b\vel\x = -b\vel\x * 0.9
				PlaySound b\miss
			End If 
		End If 
	End If 		
	
	; near the basket	
	If CirclesCollided(b\pos\x,b\pos\y,b\radius,1595,435,20) Then 
		; made it 
		If b\missed = False Then 	
			b\vel\x = 0
			b\vel\y = 4
			b\pos\x = 1583
			If b\made = False Then PlaySound b\made1
			b\made = True 
		End If 	
	End If 
	
	; bounce off the rim 
	If CirclesCollided(b\pos\x,b\pos\y,b\radius,1552,452,20) Then 		
		If b\made = False Then			
			If b\pos\y > 1552 Then 
				; top bounce
				b\vel\y = -b\vel\y * b\damping
				PlaySound b\miss
			Else		
				; front bounce	
				b\vel\x = -b\vel\x * b\damping
				PlaySound b\miss
			End If				
		End If			
	End If 
	
	; back rim bounce
	If CirclesCollided(b\pos\x,b\pos\y,b\radius,1634,468,20) Then 
		If b\made = False Then 
			b\missed = True 
			b\vel\y = -b\vel\y
			PlaySound b\miss
		End If 
	End If 		

	If p\dribbling Then 
		b\vel\x = 0
		b\pos\x = p\pos\x + 125
		b\damping = 1
		
		; hand
		If b\pos\y < p\pos\y + 220 Then 
			b\pos\y = p\pos\y + 220
			b\vel\y = -b\vel\y			
		End If 
		
		; ground 
		If b\pos\y > p\pos\y + 330 Then 
			b\pos\y = p\pos\y + 330
			b\vel\y = -b\vel\y	
			If Rnd(1) > 0.5 Then 
				PlaySound b\bounce1
			Else 
				PlaySound b\bounce2
			End If 		
		End If
		If b\vel\y > 10 Then b\vel\y = 10
		If b\vel\y < -10 Then b\vel\y = -10
		If (b\vel\y < 0 And b\vel\y > -10) Then b\vel\y = -10
	Else 
		b\damping=0.7
	End If				
	
	If p\shooting Then 
		b\pos\x = p\pos\x + 90
		b\pos\y = p\pos\y + 25
	End If 
		
	DrawBall b
End Function 

Function DrawPrediction(b.Ball,p.Player)
	Local frame_step = 5 ; draw prediction at these frame points
	Local total_frames = 120 ; show a prediction covering this many frames
	Local current frame = 0
	Local x# = b\pos\x
	Local y# = b\pos\y
	Local yv# = -p\power * 1.5
	Local xv# = p\power
	Set2DFillColor 1,1,1,0.2 
	
	While (current_frame < total_frames) 		
		If (current_frame Mod frame_step = 0 And y < b\pos\y) Then Draw2DCircle(x,y,20)		
		current_frame = current_frame + 1
		x = x + xv
		yv = yv + 0.98
		y = y + yv 
	Wend 
End Function 

Function DrawBall(b.Ball)
	Set2DFillColor b\red,b\green,b\blue,b\alpha
	Draw2DOval b\pos\x - b\radius,b\pos\y - b\radius,b\pos\x + b\radius,b\pos\y + b\radius
	; draw line to indicate rotation 
	Set2DFillColor 0,0,0,1
	Draw2DLine b\pos\x,b\pos\y,b\pos\x + Sin(b\rot\x) * b\radius,b\pos\y + Cos(b\rot\x) * b\radius
End Function 