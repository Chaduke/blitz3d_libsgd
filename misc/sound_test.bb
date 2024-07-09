; sound_test.bb
; by Chaduke
; 20240701 

; Include "../engine/sound.bb"
; Include "../engine/noise.bb"
; Include "../engine/math3d.bb"

CreateWindow 1920,1080,"Sound Test",1

; s.SoundEmitter = CreateSoundEmitter("Test Emitter","../engine/assets/audio/misc/test_emitter/sound","mp3")

t = LoadSound("../engine/assets/audio/misc/test_emitter/sound1.mp3")
PlaySound t

loop = True 
While loop 
	e=PollEvents()	
	If (IsKeyDown(256) Or e=1) Then loop = False
	Clear2D()
	
	
	RenderScene()
	Present()
Wend 
	
	