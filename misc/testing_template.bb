CreateWindow 1280,720,"Testing Template",0

camera=CreatePerspectiveCamera()
MoveEntity camera,0,1.5,-4
TurnEntity camera,-10,0,0

dl = CreateDirectionalLight()
TurnEntity dl,-30,0,0
SetAmbientLightColor 1,1,1,0.2

light = CreatePointLight()
MoveEntity light, 0,3,0
SetLightShadowsEnabled light,True

ground_material = LoadPBRMaterial("../engine/assets/materials/Concrete031_1K-JPG")
ground_mesh = CreateBoxMesh(-16,-0.1,-16,16,0,16,ground_material)
TFormMeshTexCoords ground_mesh,8,8,0,0
ground_model = CreateModel(ground_mesh)

cube_material = LoadPBRMaterial("../engine/assets/materials/Planks037B_1K-JPG")
cube_mesh = CreateBoxMesh(-0.5,0,-0.5,0.5,1,0.5,cube_material)
SetMeshShadowsEnabled cube_mesh,True
cube_model = CreateModel(cube_mesh)

Global constan_font = LoadFont("c:\windows\fonts\constan.ttf",26)
Set2DFont constan_font 

loop=True
quit = False

While loop 
	e=PollEvents()	
	
	If (e = 1 Or IsKeyHit(256)) Then 
		If quit = False Then quit = True Else quit = False		
	End If
	
	If (quit = True And IsKeyHit(89)) Then loop = False
	
	RenderScene()
		
	; draw 2D overlay	
	Clear2D()
	If quit = True Then
		DisplayTextCenter "Really Quit ?? (Y to Confirm, Escape to Cancel)",0,constan_font
	End If	

	Present()
Wend 
End 

Function DisplayTextCenter(msg$,offsetY#,font)
	Local centerX# = GetWindowWidth()/2
	Local centerY# = GetWindowHeight()/2
	Local mhw# = GetTextWidth(font,msg$) / 2 ; message half width
	Set2DTextColor 1,1,1,1
	Draw2DText msg$,centerX#-mhw#,centerY# + offsetY
End Function 