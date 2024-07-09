; parents.bb
; by Chaduke
; 20240518

; Entity / Camera Parenting Test

CreateWindow 1366,768,"Entity / Camera Parenting Test",256
CreateScene()
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
	If KeyHit(256) Then loop=False
	RenderScene()
	Clear2D()
	Draw2DText "Camera Location " + EntityX(camera) + "," + EntityY(camera) + "," + EntityZ(camera),10,10
	Draw2DText "Pivot Location " + EntityX(pivot) + "," + EntityY(pivot) + "," + EntityZ(pivot),10,30
	Draw2DText "Camera Rotation " + EntityRX(camera) + "," + EntityRY(camera) + "," + EntityRZ(camera),10,50
	Draw2DText "Pivot Rotation " + EntityRX(pivot) + "," + EntityRY(pivot) + "," + EntityRZ(pivot),10,70

	Present()
Wend 	
End 
	
	
	
	


