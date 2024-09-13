CreateWindow 1280,720,"Test",0

Dim tiles(8)

For i = 1 To 7
	tiles(i) = LoadImage("assets/images/t" + i + ".png",1)
Next 	

loop = True

While loop
	e = PollEvents()
	If e = 1 Then loop = False
	If IsKeyHit(256) loop = False
	RenderScene()
	Clear2D()
	For i = 1 To 7
		Draw2DImage tiles(i),(i * 18)-9,9,1
	Next	
	Present()
Wend 
End 

