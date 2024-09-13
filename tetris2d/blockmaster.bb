Include "../engine/gameapp.bb"

Global ga.GameApp = CreateGameApp("BlockMaster","Chaduke's",0,False)
ga\info$ = "Welcome to Block Master!"

Const grid_width = 15
Const grid_height = 20
Const cell_size = 18

Dim grid(grid_width,grid_height)

Global offsetx = (GetWindowWidth() / 2) - (grid_width * cell_size / 2)
Global offsety = (GetWindowHeight() / 2) - (grid_height * cell_size / 2)

; create tetramino shapes
Dim tiles(8)
For i = 1 To 7
	tiles(i) = LoadImage("assets/images/t" + i + ".png",1)
Next 	

Const num_shapes = 4
Const shape_points = 3
	
Dim shapes(num_shapes,shape_points)

For x = 0 To num_shapes -1
	For y = 0 To shape_points-1
		Read i
		shapes(x,y) = i
	Next
Next

Data 0,1,3
Data 0,2,3
Data 0,2,4
Data 2,3,5
		
; Data 1,3,5,7 ; I
; Data 2,4,5,7 ; Z
; Data 3,5,4,6 ; S
; Data 3,5,4,7 ; T
; Data 2,3,5,7 ; L
; Data 3,5,7,6 ; J
; Data 2,3,4,5 ; O
; Data 0,3,5,6 ; V
; Data 0,3,4,7 ; W

Type Pt 
	Field x%,y%
End Type

Dim a.Pt(4)

For i = 0 To shape_points-1 
	a(i) = New Pt	
Next 

Global current_color

Function NewShape()
	SeedRnd MilliSecs()
	Local current_shape = Rand(0,num_shapes-1) 
	For i = 0 To shape_points-1			
      a(i)\x = shapes(current_shape,i) Mod 2
	   a(i)\x = a(i)\x + Floor(grid_width/2)		
      a(i)\y = shapes(current_shape,i) / 2          
	Next 			
	current_color = Rand(1,7)	
End Function 

Function DropShape()
	For i = 0 To shape_points-1		
		grid(a(i)\x,a(i)\y) = current_color     
	Next 
	For i = 0 To shape_points-1
		If a(i)\y = 0 Then game_over = True 
	Next 
End Function 

Function RandomizeGrid()
	SeedRnd MilliSecs()
	Local col
	For x = 0 To grid_width - 1
		For y = 0 To grid_height - 1
			col = Rand(0,7)
			grid(x,y) = col			
		Next 
	Next 			
End Function 

Function ClearGrid()	
	For x = 0 To grid_width - 1
		For y = 0 To grid_height - 1			
			grid(x,y) = 0		
		Next 
	Next 			
End Function 


Function MoveShape(direction$)
	Local n = False	
	Local i,nextx
	
	If direction$ = "left" Then 
		; check to make sure we can move left		
		For i = 0 To shape_points-1
			nextx = a(i)\x-1
			; check to make sure we don't go thru left wall
			If nextx < 0 Then 
				n=True
			; check to make sure left cell is not already occupied	
			ElseIf grid(nextx,a(i)\y) > 0 Then 
				n=True
			End If 	
		Next 
		; if no wall or occupied cell
		If (Not n) Then 
			; move the shape left		
			For i = 0 To shape_points-1
				a(i)\x = a(i)\x - 1			
			Next	
		End If 
	End If 
	
	If direction$ = "right" Then 
		; check to make sure we can move right	
		For i = 0 To shape_points-1
			nextx = a(i)\x+1
			; check to make sure we don't go thru right wall
			If nextx > grid_width-1 Then 
				n=True
			; check to make sure right cell is not already occupied	
			ElseIf grid(nextx,a(i)\y) > 0 Then 
				n=True
			End If 	
		Next 
		; if no wall or occupied cell
		If (Not n) Then 
			; move the shape right	
			For i = 0 To shape_points-1
				a(i)\x = a(i)\x + 1			
			Next	
		End If 
	End If
End Function 

Function TurnShape(direction$)

	Local p.Pt = a(1) ; center of rotation
	Local x%,y%,nextx%,nexty%
	Local i%	
	
	Local n = False	
		
	If direction$ = "left" Then 
		For i = 0 To shape_points-1           
			x = a(i)\y - p\y
			y = a(i)\x - p\x
			nextx = p\x - x
			nexty = p\y + y
			If (nextx < 0 Or nextx > grid_width - 1) Then n = True 
			If (nexty < 0 Or nexty > grid_height-1) Then n = True
			If n = False Then 
				If grid(nextx,nexty) > 0 Then n = True	
			End If 	
		Next	
		If n = False Then 
			For i = 0 To shape_points-1 
				x = a(i)\y - p\y
				y = a(i)\x - p\x
				a(i)\x = p\x - x
				a(i)\y = p\y + y
			Next 
		End If 
	End If 
	
	If direction$ = "right" Then		
		For i = 0 To shape_points-1           
			x = a(i)\y - p\y
			y = a(i)\x - p\x
			nextx = p\x + x
			nexty = p\y - y
			If (nextx < 0 Or nextx > grid_width-1) Then n = True 
			If (nexty < 0 Or nexty > grid_height-1) Then n = True 
			If n = False Then 
				If grid(nextx,nexty) > 0 Then n = True	
			End If 				
		Next				
		If n = False Then 
			For i = 0 To shape_points-1 
				x = a(i)\y - p\y
				y = a(i)\x - p\x
				a(i)\x = p\x + x
				a(i)\y = p\y - y
			Next 
		End If		
	End If
End Function 

Function DrawShape()
	For i = 0 To shape_points-1
		Draw2DImage tiles(current_color),a(i)\x * cell_size + offsetx+9,a(i)\y * cell_size + offsety+9,1
	Next		
End Function 

Function DrawGrid()
	Local col,cx,cy 	
	For x = 0 To grid_width - 1
		For y = 0 To grid_height - 1
			col = grid(x,y)
			cx = x * cell_size + offsetx : cy = y * cell_size + offsety
			Set2DFillColor 1,1,1,0.1
			Draw2DRect cx,cy,cx+cell_size,cy+cell_size
			Set2DFillColor 1,1,1,1
			If col > 0 Then Draw2DImage tiles(col),x * cell_size + offsetx+9,y*cell_size + offsety+9,1
		Next 
	Next 		
End Function 

Function CheckLines()
	; loop thru lines from bottom to top
	Local y = grid_height - 1
	Local loop = True 
	Local line_count = 0
	
	While (loop)
		; count the number of colored spaces in the line 
		count = 0
		For x = 0 To grid_width - 1
			If grid(x,y) > 0 Then count = count + 1
		Next 
		If count = 0 Then loop = False 
		If count = grid_width Then 
			; convert each lines into the line above them  
			For y1 = y To 1	Step -1		
				For x = 0 To grid_width - 1
					grid(x,y1) = grid(x,y1-1)				
				Next	
			Next 
			line_count = line_count + 1
		Else 
			y = y - 1
			If y < 0 Then loop = False
		End If
	Wend 	
	Return line_count
End Function 

ClearGrid()
NewShape()

Global lastms = MilliSecs()
Global tick_interval = 500
Global saved_interval
Global fast_interval = 30
Global ticks = 0

Function Tick()
	Local diff = MilliSecs() - lastms
	Local r = False 
	
	If diff > tick_interval Then 
		lastms = MilliSecs()
		r = True
	End If 
	
	Return r		
End Function 

Function ProcessTick()
	ticks = ticks + 1
	Local n = False 
	Local i,nexty,lc
	
	For i = 0 To shape_points-1	
		nexty = a(i)\y + 1	
		If nexty > grid_height-1 Then 
			n = True
		ElseIf grid(a(i)\x,nexty) > 0 Then 
			n = True 
		End If 
	Next 
	
	If n = True Then 
		DropShape()
		NewShape()
		lc = CheckLines()
		If lc > 0 Then 
			PlaySound chime
			ga\info$ = "You just cleared " + lc + " lines!" 
			total_lines = total_lines + lc
			score = score + lc * lc
			If tick_interval = fast_interval Then tick_interval = saved_interval
			tick_interval = tick_interval - (lc * 10)
		End If 			 	
	Else 
		For i = 0 To shape_points-1 
			a(i)\y = a(i)\y + 1
		Next
	End If 				
End Function 

Set2DOutlineEnabled True
Set2DOutlineColor 0.2,0.2,0.2,0.2

sugar = LoadSound("assets/music/sugar.mp3")
Global chime = LoadSound("../engine/assets/audio/misc/snack_chime.mp3")

PlaySound sugar
Global total_lines = 0
Global score = 0
Global game_over = False 

While ga\loop
	BeginFrame ga
	RenderScene()
	Clear2D()
	
	If (game_over = False And ga\game_state=GAME_STATE_STORY) Then 
		If IsKeyHit(KEY_A) Then MoveShape("left")	
		If IsKeyHit(KEY_D) Then MoveShape("right")
							
		If IsKeyHit(KEY_LEFT) Then TurnShape("left")
		If IsKeyHit(KEY_RIGHT) Then TurnShape("right")		
		
		If IsKeyDown(KEY_S) Then 
			If tick_interval <> fast_interval Then 
				saved_interval = tick_interval
				tick_interval = fast_interval
			End If 
		Else 
			If tick_interval = fast_interval Then tick_interval = saved_interval
		End If 			 
	
		If Tick() Then ProcessTick()
		
		DrawGrid()
		DrawShape()		
	Else 
		DrawGrid()		
		Set2DTextColor 1,1,1,1
		If game_over = True Then 
			DisplayTextCenter "GAME OVER",ga\font,GetWindowHeight() / 2 - 100
			DisplayTextCenter "R to Restart",ga\gui_font,GetWindowHeight() / 2 - 75
			If IsKeyHit(KEY_R) Then 
				ClearGrid()
				total_lines = 0
				score = 0
				game_over = False
				tick_interval = 500
				NewShape()
			End If 	
		End If 	
	End If 			
	
	Draw2DText "Total Lines : " + total_lines,5,5
	Draw2DText "Tick Interval : " + tick_interval,5,25
	Draw2DText "Score : " + score,5,45
	
	EndFrame ga,True,False,False
Wend 
End 