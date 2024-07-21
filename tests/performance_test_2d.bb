; performance_test_2D
; by Chaduke 
; 20240714

; SetWebGPUBackend "Vulkan"
CreateWindow 1920,1080,"",1

loop = True 
While loop
	e = PollEvents()
	If e = 1 Then loop = False
	If IsKeyDown(256) Then loop = False
	Clear2D()
	
	; draw multicolored rectangles 
	For i = 0 To 300000
		Set2DFillColor Rnd(1),Rnd(1),Rnd(1),1
		l# = Rand(0,GetWindowWidth() - 100)
		t# = Rand(0,GetWindowHeight() - 100)
		Draw2DRect l,t,l+Rand(10,100),t + Rand(10,100)
	Next 	
	Draw2DText "FPS : " + GetFPS(),5,GetWindowHeight() - 25
	RenderScene()
	Present()	
Wend 
End 


