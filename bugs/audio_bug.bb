; audio_bug.bb
; by Chaduke
; 20240718

; this is working ok for now

Const KEY_ESCAPE = 256

CreateWindow 1280,720,"Audio Bug",0

fine = LoadSound("sgd://audio/fine_morning.wav")
PlaySound(fine)
SetAudioLooping fine,True

loop = True 
While (loop)
	e = PollEvents()
	If e = 1 Then loop = False 
	If IsKeyDown(KEY_ESCAPE) Then loop = False 
	If IsMouseButtonHit(0) Then StopAudio fine
	RenderScene()
	Present()
Wend 
End 