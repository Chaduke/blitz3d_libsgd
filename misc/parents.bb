; parents.bb
; by Chaduke
; 20240518

; Entity / Camera Parenting Test

CreateWindow 1366,768,"Entity / Camera Parenting Test",256
camera = CreatePerspectiveCamera()
pivot = CreateModel(0)
MoveEntity pivot,5,0,0
; TurnEntity camera,0,5,0
; TurnEntity pivot,0,30,0
MoveEntity camera,5,0,0
SetEntityParent camera,pivot
loop = True
While(loop)
	e=PollEvents()
	If e=1 Then loop=False
	If IsKeyHit(256) Then loop=False
	RenderScene()
	Clear2D()
	Draw2DText "Camera Location " + GetEntityX(camera) + "," + GetEntityY(camera) + "," + GetEntityZ(camera),10,10
	Draw2DText "Pivot Location " + GetEntityX(pivot) + "," + GetEntityY(pivot) + "," + GetEntityZ(pivot),10,30
	Draw2DText "Camera Rotation " + GetEntityRX(camera) + "," + GetEntityRY(camera) + "," + GetEntityRZ(camera),10,50
	Draw2DText "Pivot Rotation " + GetEntityRX(pivot) + "," + GetEntityRY(pivot) + "," + GetEntityRZ(pivot),10,70

	Present()
Wend 	
End 
	
	
	
	

