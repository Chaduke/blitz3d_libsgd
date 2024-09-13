; model_browser.bb
; by Chaduke
; 20240604 

; loops through a list of files within a certain directory
; lists their names, allows you to scroll through them 
; would be nice to integrate thumbnails at some point
; allows you to select a model (or any type of file) and then choose to load it
; by pressing ENTER.  Change the code if you want to load something other than 
; GLTF / GLB models, like images or materials, etc..
; if the model name starts with "Characters" it will animate it 
; with animation number 3 which is the Idle animation in the 
; Pirate models pack

; if you're browsing a new set of models for the first time 
; I'd suggest running in a window rather than fullscreen
; because some models WILL cause blitzcc.exe to stop responding 

; CONTROLS 
; use the mouse and a combination of left mouse, right mouse, or both buttons to navigate the scene 
; press SPACE to toggle the file browser
; press up arrow and down arrow to select a file
; press ENTER to load the file 
 
; GLOBALS
Dim file_list$(0)

; SET THIS path to whereever you extracted your models 
Global path$ = "c:\dev\pirate_game\assets\models\environment"
Global pivot 

; HELPER FUNCTIONS
Function DisplayTextCenter(msg$,offsetY#,font)
	Local centerX# = WindowWidth()/2
	Local centerY# = WindowHeight()/2
	Local mhw# = FontTextWidth(font,msg$) / 2 ; message half width
	Set2DTextColor 1,1,1,1
	Set2DFont font
	Draw2DText msg$,centerX#-mhw#,centerY# + offsetY
End Function 

Function GetFileCount()
	Local count = 0
	Local loop = True 
	Local folder = ReadDir(path$)
	While loop
		Local file$ = NextFile$(folder)
		If file$ = "" Then 
			loop = False
		Else 
			If FileType(path$ + "\" + file$) <> 2	Then count = count + 1
		End If		
	Wend 
	CloseDir folder 
	Return count 
End Function 

Function GetFileList()	
	Local file_count = GetFileCount()
	Dim file_list$(file_count-1)	
	Local folder = ReadDir(path$)	
	Local loop = True 
	Local current = 0
	While loop 
		Local file$ = NextFile$(folder)
		If file$ = "" Then 
			loop = False
		Else 
			If FileType(path$ + "\" + file$) <> 2 Then 
				file_list$(current) = file$
				current = current + 1
			End If	
		End If		
	Wend 
	CloseDir folder	
	Return current 
End Function 

Function StandardMouseInput(e,cam_move_speed_mouse#=0.01,cam_turn_speed#=0.1)
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
End Function

Function LoadCarousel(filecount)
	For i = 0 To filecount - 1
		Local file$ = file_list$(i)
		If (Right$(file$,4) = "gltf" Or Right$(file$,3) = "glb") Then 
			UpdateStatus "Loading file " + (i + 1) + " of " + filecount + " - " + file$
			Local mesh = LoadMesh(path$ + "\" + file$)
			SetMeshCastsShadow mesh,True 
			Local current_model = CreateModel(mesh)		
			SetEntityParent current_model,pivot 			
			MoveEntity current_model,0,0,20
			TurnEntity pivot,0,360 / filecount,0					
		End If	
	Next 			
End Function 

Function UpdateStatus(msg$)
	PollEvents()
	RenderScene()
	Clear2D()
	Draw2DText msg$,10,10
	Present()
End Function 

CreateWindow 1920,1080,"Model Browser",1
CreateScene()

; setup night scene 
env = Load2DTexture("sgd://envmaps/grimmnight-cube.jpg", 4, 56)
SetSceneEnvTexture env
skybox = CreateSkybox(env)	

camera=CreatePerspectiveCamera()
MoveEntity camera,0,1.5,-6
TurnEntity camera,5,0,0
pivot = CreateModel(0)
MoveEntity pivot,0,0,20

dl = CreateDirectionalLight()
TurnEntity dl,-30,0,0
SetSceneAmbientLightColor 1,1,1,0.1

light = CreatePointLight()
MoveEntity light, 0,3,0
SetLightCastsShadow light,True

ground_material = LoadPBRMaterial("sgd://materials/Tiles093_1K-JPG")
ground_mesh = CreateBoxMesh(-16,-0.1,-16,16,0,16,ground_material)
TransformMeshTexCoords ground_mesh,16,16,0,0
ground_model = CreateModel(ground_mesh)

; these should be available on any Windows 10 machine 
; I don't use Windows 11, replace if necessary with your own ttf files
constan_font = LoadFont("c:\windows\fonts\constan.ttf",26)
small_font = LoadFont("c:\windows\fonts\consola.ttf",12)
filecount = GetFileList() 

loop=True
quit = False
show_file_list = False
selected_file = 0
animated = False
current_model = 0

While loop 
	e=PollEvents()	
	
	If (e = 1 Or KeyHit(256)) Then 
		If quit = False Then quit = True Else quit = False		
	End If
	
	If (quit = True And KeyHit(89)) Then loop = False
	If KeyHit(32) Then 
		If show_file_list = False Then show_file_list = True Else show_file_list = False
	End If 
	
	If KeyHit(264) Then ; key down 
		selected_file = selected_file + 1
		If selected_file > filecount - 1 Then selected_file = 0
	End If 
	
	If KeyHit(265) Then ; key up
		selected_file = selected_file - 1
		If selected_file < 0 Then selected_file = filecount - 1
	End If 	
	
	If KeyHit(257) Then 
		If show_file_list Then 
			; load the selected model
			If current_model <> 0 Then DestroyEntity current_model 				
			If Left$(file_list$(selected_file),10)="Characters" Then 
				animated = True				
				current_model = LoadBonedModel(path$ + "\" + file_list$(selected_file),True)
			Else 
				animated = False
				mesh = LoadMesh(path$ + "\" + file_list$(selected_file))
				SetMeshCastsShadow mesh,True 
				current_model = CreateModel(mesh)
			End If
			show_file_list = False						 
		End If 	
	End If 	
	
	If KeyHit(290) Then ; F1
		LoadCarousel(filecount)
	End If 
		
	StandardMouseInput(camera)		
	
	If animated = True Then
		atime# = atime# + 0.02
		AnimateModel current_model,3,atime,2,1	
	End If 
			
	RenderScene()		
	; draw 2D overlay	
	Clear2D()
	
	If quit = True Then	 DisplayTextCenter "Really Quit ?? (Y to Confirm, Escape to Cancel)",0,constan_font
		
	If show_file_list Then
		Set2DFont small_font 		
		For i=0 To filecount-1			
			x=14
			y=i*14 + 14
			If y > WindowHeight() - 14 Then 
				y = y - WindowHeight() + 14
				x = x + 300
			End If		
			Set2DTextColor 1,1,1,1		
			Draw2DText file_list$(i),x,y
			If i = selected_file Then 
				Set2DTextColor 1,1,0,1
				Draw2DText "*",x-7,y + 2
			End If 			
		Next 	
	Else 
		Set2DFont constan_font 
		Draw2DText "Press SPACE to open file browser",10,10
	End If 		
	
	Present()
Wend 
End 
