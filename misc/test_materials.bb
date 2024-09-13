; test_materials.bb 
; by Chaduke
; 20240603 

; material descriptors 

; albedoColor4f
; emissiveColor3f
; metallicFactor1f	
; roughnessFactor1f

; {"albedoTexture", {1, whiteTexture()}},
; {"emissiveTexture", {3, whiteTexture()}},
; {"metallicTexture", {5, whiteTexture()}},
; {"roughnessTexture", {7, whiteTexture()}},
; {"occlusionTexture", {9, whiteTexture()}},
; {"normalTexture", {11, flatNormalTexture()}},

Global cam_turn_speed# = 0.2
Global cam_move_speed_mouse# = 0.02

CreateWindow 1280,720,"Test Materials",0
CreateScene()

camera=CreatePerspectiveCamera()
MoveEntity camera,0,1.5,-4
TurnEntity camera,-10,0,0

dl = CreateDirectionalLight()
TurnEntity dl,-30,0,0
SetSceneAmbientLightColor 1,1,1,0.2

light = CreatePointLight()
MoveEntity light, 0,3,0
SetLightCastsShadow light,True

ground_material = LoadPBRMaterial("../platformer/assets/materials/Concrete031_1K-JPG")
ground_mesh = CreateBoxMesh(-16,-0.1,-16,16,0,16,ground_material)
TransformMeshTexCoords ground_mesh,8,8,0,0
ground_model = CreateModel(ground_mesh)

cube_material = CreatePBRMaterial()
; SetMaterialVector4f cube_material, "albedoColor4f",1,0,0,0.1
cube_texture = Load2DTexture("assets/materials/test_material/test_albedo.png",3,20)
SetMaterialTexture cube_material,"albedoTexture",cube_texture
SetMaterialFloat cube_material,"roughnessFactor1f",0.2
SetMaterialBlendMode cube_material,2

cube_mesh = CreateBoxMesh(-0.5,0,-0.5,0.5,1,0.5,cube_material)
SetMeshCastsShadow cube_mesh,True
cube_model = CreateModel(cube_mesh)

Global constan_font = LoadFont("c:\windows\fonts\constan.ttf",26)
Set2DFont constan_font 

loop=True
quit = False

While loop 
	e=PollEvents()	
	
	If (e = 1 Or KeyHit(256)) Then 
		If quit = False Then quit = True Else quit = False		
	End If
	
	If (quit = True And KeyHit(89)) Then loop = False
	
	TurnEntity cube_model,0,10,0
	
	StandardMouseInput(camera)
	
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
	Local centerX# = WindowWidth()/2
	Local centerY# = WindowHeight()/2
	Local mhw# = FontTextWidth(font,msg$) / 2 ; message half width
	Set2DTextColor 1,1,1,1
	Draw2DText msg$,centerX#-mhw#,centerY# + offsetY
End Function 

Function StandardMouseInput(e)
	; Mouse Input					
	If MouseButtonDown(0) ; left mouse button down
		SetMouseCursorMode 3 ; hide and lock the cursor
		If MouseButtonDown(1) ; right mouse button down (both are held down now)
			MoveEntity e, MouseVX() * cam_move_speed_mouse, -MouseVY() * cam_move_speed_mouse, 0	
		Else ; only left mouse button is down
			MoveEntity e, 0, 0, -MouseVY() * cam_move_speed_mouse
			TurnEntity e, 0, -MouseVX() * cam_turn_speed, 0			
		EndIf		
	ElseIf MouseButtonDown(1) ; only right mouse button is down
		SetMouseCursorMode 3
		TurnEntity e, -MouseVY() * cam_turn_speed, -MouseVX() * cam_turn_speed, 0	
	Else 
		SetMouseCursorMode 1 ; set the mouse cursor to normal
	End If	
	If (EntityRZ(e) < 0 Or EntityRZ(e) > 0) Then SetEntityRotation e, EntityRX(e), EntityRY(e), 0	
	If EntityY(e) < 0.5 Then SetEntityPosition e,EntityX(e),0.5,EntityZ(e)
End Function