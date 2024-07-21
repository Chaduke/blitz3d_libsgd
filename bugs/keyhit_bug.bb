; keyhit_bug.bb
; by Chaduke
; 20240718

; a code example demonstrating a bug in IsKeyHit() while running in debug mode 

Const KEY_ESCAPE = 256

CreateWindow 1280,720,"Keyhit Bug",0

loop = True 

While (loop And IsMouseButtonHit(0) = False) ; this works

	e = PollEvents()
	If e = 1 Then loop = False 
	
	RenderScene()
	Present()
Wend 
End 