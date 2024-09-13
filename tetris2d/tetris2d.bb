; tetris2d.bb 
; by Chaduke
; 20240818

Include "../engine/constants.bb"

Type Point 
	Field x%
	Field y%
End Type 

Function CreatePoint.Point()
	Local p.Point = New Point
	p\x = 0
	p\y = 0
	Return p
End Function 	

Const M = 20
Const N = 10

Dim grid(M,N)

Dim a.Point(4)
Dim b.Point(4)

For i = 0 To 3 
	a(i) = CreatePoint()
	b(i) = CreatePoint()
Next 

; create tetramino shapes 	
Dim figures(7,4)

For x = 0 To 6
	For y = 0 To 3
		Read i
		figures(x,y) = i
	Next
Next
		
Data 1,3,5,7 ; I
Data 2,4,5,7 ; Z
Data 3,5,4,6 ; S
Data 3,5,4,7 ; T
Data 2,3,5,7	; L
Data 3,5,7,6 ; J
Data 2,3,4,5 ; O

SeedRnd(MilliSecs())
Dim tiles(8)

CreateWindow(320,480,"Tetris2D",256)	
	
For i = 1 To 7
	tiles(i) = LoadImage("assets/images/t" + i + ".png",1)
Next 	

background = LoadImage("assets/images/background.png",1)

Global dx = 0
Global rotate = False 
Global colorNum = 1
Global timer# = 0 
Global del# = 0.6
Global clock = 0
loop = True 
Global ms# = MilliSecs()

Function check()
	Local r = True
	For i = 0 To 3
		If ( a(i)\x < 0 Or a(i)\x >= N Or a(i)\y >= M ) Then 
			r = False
		Else 	
			If (grid(a(i)\y,a(i)\x)) Then r = False
		End If		
	Next	
   Return r
End Function 

Function move_piece()
	Local i%
	For i = 0 To 3		
		b(i)=a(i) 
		a(i)\x = a(i)\x + dx		
	Next 	
	
	If (Not check()) Then 
		For i = 0 To 3
			 a(i) = b(i)
		Next 
	Else 
		For i = 0 To 3
			If a(i)\x < 0 Then a(i)\x = 0
			If a(i)\x > N-1 Then a(i)\x = N-1
		Next 	
	End If 	
End Function 

Function rotate_piece()	
	Local p.Point = a(1) ; center of rotation
	Local x%,y%
	Local i%
	
	For i = 0 To 3           
		x = a(i)\y - p\y
		y = a(i)\x - p\x
		a(i)\x = p\x - x
		a(i)\y = p\y + y
	Next
		
	If (Not check()) Then 
		For i = 0 To 3
			a(i) = b(i)
		Next 
	End If	
End Function 

While loop    
	lastms# = ms#
	ms# = MilliSecs()
	diff# = (ms - lastms) / 1000
	timer# = timer# + diff

	e = PollEvents()      
	If e = 1 Then loop = False
	If IsKeyDown(KEY_ESCAPE) Then loop = False 
	If IsKeyHit(KEY_UP) Then rotate = True 
	If IsKeyHit(KEY_LEFT) Then dx = -1
	If IsKeyHit(KEY_RIGHT) Then dx = 1
	If IsKeyDown(KEY_DOWN) Then del = 0.1
	           
	move_piece()	
	If rotate Then rotate_piece()
	
	; tick
	If (timer > del) Then       
		For i = 0 To 3
			b(i) = a(i) 
			a(i)\y = a(i)\y + 1
		Next 	

		If (Not check()) Then 
			For i=0 To 3
				grid(b(i)\y,b(i)\x) = colorNum
			Next
			 	
			colorNum = Rand(1,7)
          o = Rand(0,6)
			For i = 0 To 3
          	a(i)\x = figures(o,i) Mod 2
             a(i)\y = figures(o,i) / 2 
          Next  
      End If 
	   timer = 0  
	End If 		     

	; check lines
	k% = M - 1	
	For i = M - 1 To 1 Step -1        
		count% = 0
 		For j = 0 To N-1
			If grid(i,j) Then count=count+1
			grid(k,j) = grid(i,j)   
		Next 	        
		If count < N Then k=k-1	
	Next
	
	dx = 0 : rotate = False : del = 0.6

	; draw
	RenderScene()
	Clear2D()
	Draw2DImage background,0,0,1 
	      
	; draw the fallen tiles
	For i = 0 To M-1
		For j = 0 To N-1       
			If grid(i,j) > 0 Then 							
				Draw2DImage tiles(grid(i,j)),(j*18) + 28,(i*18) + 31,1				
			End If 
		Next
	Next 		

	For i = 0 To 3		
		Draw2DImage tiles(colorNum),(a(i)\x * 18) + 28, (a(i)\y * 18) + 31,1		
	Next  
	     
	Present()
Wend 
End 