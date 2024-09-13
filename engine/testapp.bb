; testapp.bb 
; by Chaduke
; 20240620 

; a test runner framework for testing components 
; creates a window and a main loop 

Include "../engine/constants.bb"
Include "../engine/text_helpers.bb"
Include "../engine/vec3.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/navigation.bb"
Include "../engine/math2d.bb" ; for Distance2D() and CirclesCollided()
Include "../engine/math3d.bb" ; for Type Cylinder and CylindersCollided()
Include "../engine/trees.bb"
Include "../engine/sprites.bb"
Include "../engine/gui.bb"
Include "../engine/file_browser.bb"
Include "../engine/sound.bb"
Include "../engine/test_scene.bb"
Include "../engine/dialogue.bb"

Type TestApp	 
	Field loop
	Field quit
	Field font 
	Field gui_font 
	Field browser_font	
	Field ts.TestScene	
	Field logfile
	Field help_visible
	Field camera
	Field pivot 
End Type

Function CreateTestApp.TestApp(fullscreen = False,name$="Test App")
	If fullscreen Then 
		CreateWindow 1920,1080,"",1	
	Else 
		CreateWindow 1366,768,name$,256		
	End If 
	
	Set2DOutlineWidth 2
	Set2DLineWidth 1
	Set2DOutlineEnabled True
	Set2DFillEnabled True
	
	Local t.TestApp = New TestApp	
	t\font = LoadFont("c:\windows\fonts\constan.ttf",20)	
	t\gui_font = LoadFont("c:\windows\fonts\consola.ttf",14)	
	t\browser_font = LoadFont("../engine/assets/fonts/bb.ttf",18)
	t\loop = True 
	t\quit = False	
	t\ts = CreateTestScene()
	; t\logfile = WriteFile("../engine/debuglog.txt")
	t\camera = CreatePerspectiveCamera()
	t\pivot = CreateModel(0)
	SetEntityParent t\camera,t\pivot	
	MoveEntity t\pivot,0,1,0
	Return t
End Function 

Function GetColliderMaterial()
	Local collider_texture = Load2DTexture("../engine/assets/textures/misc/yellow_grid.png",4,18)
	Local collider_material = CreatePBRMaterial()
	SetMaterialTexture collider_material,"albedoTexture",collider_texture
	SetMaterialBlendMode collider_material,3
	Return collider_material
End Function 

Function BeginFrame(t.TestApp)	
	e = PollEvents()
	If e = 1 Then t\loop = False
	If IsKeyHit(KEY_ESCAPE) Then 
		If t\quit = False Then t\quit = True Else t\quit = False
	End If 			
	If (IsKeyHit(KEY_Y) And t\quit=True) Then t\loop = False				
End Function

Function EndFrame(t.TestApp)			
	RenderScene()
	Clear2D()		
	Set2DTextColor 1,1,0,1
	If t\quit Then DisplayTextCenter "Quit? Y to confirm | ESC to Cancel",t\font	
	Set2DFont t\gui_font	
	msg$ = "FPS : " + Floor(GetFPS())
	msg$ = msg$ + " | RPS : " + GetRPS()
	msg$ = msg$ + " | MouseZ " + GetMouseZ()
	Draw2DText msg$,5,GetWindowHeight() - 20	
	Present()
End Function

Function LogEntry(t.TestApp,msg$)
	WriteLine t\logfile,msg$
End Function 