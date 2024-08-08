 ; voronoi_and_worley.bb
; by Chaduke 
; 20240626

; trying to make a version of voronoi and/or worley noise 

Include "../engine/noise.bb"
Include "../engine/math2d.bb"

CreateWindow 1920,1080,"Voronoi and Worley",1

Const cellsize = 10
Const drawpoints = False 

; Set2DOutlineEnabled True : Set2DOutlineColor 1,1,1,0.5 : Set2DOutlineWidth 1

Const numpoints = 200
Dim points.Point(numpoints)

SeedRnd(MilliSecs())

For i = 0 To numpoints - 1
	points(i) = New Point
	points(i)\x = Rand(0,GetWindowWidth())
	points(i)\y = Rand(0,GetWindowHeight())
Next 

Clear2D()

For col = 0 To GetWindowWidth()-cellsize Step cellsize 
	For row = 0 To GetWindowHeight()-cellsize Step cellsize 
		p = FindNearestPoint(col,row)
		n# = noise(points(p)\x,points(p)\y)
		Set2DFillColor n,n,n,1
		Draw2DRect col,row,col+cellsize,row+cellsize	
	Next 
Next 	

; see the points 
If drawpoints Then 
	For i = 0 To numpoints - 1
		Set2DFillColor 0.2,1.0,0.2,1
		Draw2DOval points(i)\x-5,points(i)\y-5,points(i)\x+5,points(i)\y+5
	Next 	
End If 

RenderScene()
Present()
	
loop = True 
While loop	
	e=PollEvents()
	If IsKeyDown(256) Then loop = False
	If e = 1 Then loop = False	
Wend 
End 

Function FindNearestPoint(x,y)	
	nd = 1000
	Local n = 0
	For i = 0 To numpoints - 1
		d = Distance2D(x,y,points(i)\x,points(i)\y)
		If d < nd Then 
			nd = d
			n = i
		End If 				
	Next 
	Return n
End Function 