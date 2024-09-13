; mesh_building.bb
; by Chaduke
; 20240613

Global cam_turn_speed# = 0.2
Global cam_move_speed_keyboard# = 0.2
Global cam_move_speed_mouse# = 0.02
Global sprint# = 0.0

Function StandardMouseInput(e)
	; Mouse Input					
	If IsMouseButtonDown(0) ; left mouse button down
		SetMouseCursorMode 3 ; hide and lock the cursor
		If IsMouseButtonDown(1) ; right mouse button down (both are held down now)
			MoveEntity e, GetMouseVX() * cam_move_speed_mouse, -GetMouseVY() * cam_move_speed_mouse, 0	
		Else ; only left mouse button is down
			MoveEntity e, 0, 0, -GetMouseVY() * cam_move_speed_mouse
			TurnEntity e, 0, -GetMouseVX() * cam_turn_speed, 0			
		EndIf		
	ElseIf IsMouseButtonDown(1) ; only right mouse button is down
		SetMouseCursorMode 3
		TurnEntity e, -GetMouseVY() * cam_turn_speed, -GetMouseVX() * cam_turn_speed, 0	
	Else 
		SetMouseCursorMode 1 ; set the mouse cursor to normal
	End If	
	If (GetEntityRZ(e) < 0 Or GetEntityRZ(e) > 0) Then SetEntityRotation e, GetEntityRX(e), GetEntityRY(e), 0	
End Function

CreateWindow 1280,720,"Mesh Building",0

width = 1024
depth = 1024
height = 8

; create sky environment 
sky_texture = Load2DTexture("../engine/assets/textures/skybox/skyboxsun25degtest.png",4,56)
SetEnvTexture sky_texture
skybox = CreateSkybox(sky_texture)
SetSkyboxRoughness skybox,0.2

camera = CreatePerspectiveCamera()
MoveEntity camera,width/2,20,depth/2

light = CreateDirectionalLight()
TurnEntity light,-30,0,0

SetAmbientLightColor 1,1,1,0.5

mesh_material = LoadPBRMaterial("../engine/assets/materials/Marble012_1K-JPG")
 
; create a custom mesh
custom_mesh = CreateMesh(0,0)
; values for uv positioning
Local u# = 1 / Float(width)
Local v# = 1 / Float(depth)	

; add the vertices to a heightplane
i = 0
For z=0 To depth
	For x = 0 To width		  
		; Add the vertex		
		AddMeshVertex custom_mesh,x,Cos(x+z) * height,z,1,1,1, x * u, (depth - z) * v		
		i = i + 1
	Next
Next

; create the surface and triangles
Local surf = CreateSurface(custom_mesh, 0, mesh_material)	

For z = 0 To depth - 1
	For x = 0 To width-1			
		Local v0 = x + (z * width + z)
		Local v1 = v0 + 1
      		Local v2 = v0 + width + 1
     		Local v3 = v2 + 1
		AddSurfaceTriangle surf, v0, v1, v2
		AddSurfaceTriangle surf, v1, v3, v2
	Next
Next

UpdateMeshNormals custom_mesh
TFormMeshTexCoords custom_mesh,8,8,0,0
custom_model = CreateModel(custom_mesh)

loop = True 
While loop 
	e = PollEvents()
	If e = 1 Then loop = False 
	If IsKeyHit(256) Then loop = False
	StandardMouseInput camera	
	RenderScene()
	Present()
Wend 
End