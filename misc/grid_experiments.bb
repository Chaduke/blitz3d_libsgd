; grid_experiments.bb
; by Chaduke
; 20240613

; init scene
Const sw = 1920
Const sh = 1080
CreateWindow sw,sh,"Grid Experiments",1

Const cs = 10 ; cell size
Const cols = sw / cs ; grid cols 
Const rows = sh / cs ; grid rows

Dim grid(cols,rows)
Dim next_grid(cols,rows)

Function InitGrid()
	SeedRnd MilliSecs()
	; init the grid to random
	For x = 0 To cols
		For y = 0 To rows
			grid(x,y) = Rand(0,1)		
		Next
	Next	
End Function 	

InitGrid()
	
Set2DOutlineEnabled True ; see the grid squares 

loop = True
auto = True 

While loop
	; input 
	e = PollEvents()
	If e = 1 Then loop = False
	If IsKeyHit(256) Then loop = False
	
	; spacebar calculates the next grid 
	If (IsKeyHit(32) Or IsKeyDown(78) Or auto = True) Then 
		For x = 0 To cols
			For y = 0 To rows
				; make sure we don't get an array oob
				If (x > 0 And x < cols And y > 0 And y < rows) Then
					If grid(x,y) = 1 Then 
						If(grid(x-1,y) = 1 And grid(x+1,y) = 1 And grid(x,y-1) = 1 And grid(x,y+1) = 1) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 1 And grid(x+1,y) = 1 And grid(x,y-1) = 1 And grid(x,y+1) = 0) Then next_grid(x,y) = 0
						If(grid(x-1,y) = 1 And grid(x+1,y) = 1 And grid(x,y-1) = 0 And grid(x,y+1) = 1) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 1 And grid(x+1,y) = 1 And grid(x,y-1) = 0 And grid(x,y+1) = 0) Then next_grid(x,y) = 0
						
						If(grid(x-1,y) = 1 And grid(x+1,y) = 0 And grid(x,y-1) = 0 And grid(x,y+1) = 1) Then next_grid(x,y) = 0
						If(grid(x-1,y) = 1 And grid(x+1,y) = 0 And grid(x,y-1) = 0 And grid(x,y+1) = 0) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 0 And grid(x+1,y) = 1 And grid(x,y-1) = 1 And grid(x,y+1) = 1) Then next_grid(x,y) = 0
						If(grid(x-1,y) = 0 And grid(x+1,y) = 1 And grid(x,y-1) = 1 And grid(x,y+1) = 0) Then next_grid(x,y) = 0
						
						If(grid(x-1,y) = 0 And grid(x+1,y) = 1 And grid(x,y-1) = 0 And grid(x,y+1) = 0) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 0 And grid(x+1,y) = 0 And grid(x,y-1) = 1 And grid(x,y+1) = 0) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 0 And grid(x+1,y) = 0 And grid(x,y-1) = 0 And grid(x,y+1) = 1) Then next_grid(x,y) = 0
						If(grid(x-1,y) = 0 And grid(x+1,y) = 0 And grid(x,y-1) = 0 And grid(x,y+1) = 0) Then next_grid(x,y) = 0
					Else 
						If(grid(x-1,y) = 1 And grid(x+1,y) = 1 And grid(x,y-1) = 1 And grid(x,y+1) = 1) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 1 And grid(x+1,y) = 1 And grid(x,y-1) = 1 And grid(x,y+1) = 0) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 1 And grid(x+1,y) = 1 And grid(x,y-1) = 0 And grid(x,y+1) = 1) Then next_grid(x,y) = 0
						If(grid(x-1,y) = 1 And grid(x+1,y) = 1 And grid(x,y-1) = 0 And grid(x,y+1) = 0) Then next_grid(x,y) = 1
						
						If(grid(x-1,y) = 1 And grid(x+1,y) = 0 And grid(x,y-1) = 0 And grid(x,y+1) = 1) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 1 And grid(x+1,y) = 0 And grid(x,y-1) = 0 And grid(x,y+1) = 0) Then next_grid(x,y) = 0
						If(grid(x-1,y) = 0 And grid(x+1,y) = 1 And grid(x,y-1) = 1 And grid(x,y+1) = 1) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 0 And grid(x+1,y) = 1 And grid(x,y-1) = 1 And grid(x,y+1) = 0) Then next_grid(x,y) = 0
						
						If(grid(x-1,y) = 0 And grid(x+1,y) = 1 And grid(x,y-1) = 0 And grid(x,y+1) = 0) Then next_grid(x,y) = 0
						If(grid(x-1,y) = 0 And grid(x+1,y) = 0 And grid(x,y-1) = 1 And grid(x,y+1) = 0) Then next_grid(x,y) = 1
						If(grid(x-1,y) = 0 And grid(x+1,y) = 0 And grid(x,y-1) = 0 And grid(x,y+1) = 1) Then next_grid(x,y) = 0
						If(grid(x-1,y) = 0 And grid(x+1,y) = 0 And grid(x,y-1) = 0 And grid(x,y+1) = 0) Then next_grid(x,y) = 0
					End If
				End If							
			Next
		Next
		For x = 0 To cols
			For y = 0 To rows
				grid(x,y) = next_grid(x,y)
			Next
		Next	
	End If	
	
	If IsKeyHit(88) Then InitGrid()
	
	t = t + 1
	If t > 1000 Then 
		InitGrid()
		t = 0
	End If	
		
	; render 3D stuff 
	RenderScene()
	
	; render 2D stuff 
	Clear2D()	
	For x = 0 To cols
		For y = 0 To rows
			c# = grid(x,y)
			r# = c	 * (1 - Sin(y))
			g# = c * (1 - Cos(x))
			b# = c * (1 - Sin(x-y))
			Set2DFillColor r,g,b,1	
			Draw2DRect x*cs,y*cs,x*cs+cs,y*cs+cs			
		Next
	Next
	
	Present()
Wend 
End
	