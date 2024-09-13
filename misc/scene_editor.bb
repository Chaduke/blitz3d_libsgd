; scene_editor.bb
; by Chaduke 
; 20240806 

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/navigation.bb"
Include "../engine/file_browser.bb"

Type SceneEditor
	Field current_brush
	Field edit_mode
End Type

Function CreateSceneEditor.SceneEditor()
	Local se.SceneEditor = New SceneEditor
	se\edit_mode = 0	
	Return se	
End Function 

ga.GameApp = CreateGameApp("Scene Editor","Chaduke's",1,False)
e.Environment = CreateEnvironment(ga\gui_font)
trn.Terrain = New Terrain
SetTerrainDefaults trn
trn\calc_normals = False
CreateTerrain trn
PlaceEntityOnTerrain ga\pivot,trn,1 
fb.FileBrowser = CreateFileBrowser("Nature Pack","d:\blender\nature_pack\gltf","gltf",ga\browser_font)

ga\debug = True 
se.SceneEditor = CreateSceneEditor()

While ga\loop
	BeginFrame ga	
	; show / hide environment gui
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	
	; draw file browsers if visible 
	fbc = FileBrowserCheck(trn,se)
	
	; toggle model browser visiblity 		
	If (IsKeyHit(KEY_1)) Then 
		If Not fbc Then 
			fb\visible = True
			GetFileList fb 
		Else 
			fb\visible = False	
		End If 
	End If		
	
	; update guis if intereacted with
	; else process normal mouse and keyboard input
	; to navigate the scene
	If GUIDragCheck() Then 		
		UpdateEnvironment e,True
	Else 
		If se\edit_mode = 0 Then 
			NavigationMode ga\pivot,trn
		Else 
			ThirdPersonMouseInputEditor ga\camera,ga\pivot
			KeyboardInputThirdPerson se\current_brush,ga\pivot
		End If	
	End If 
				
	DrawAllGUIs()	
	EndFrame ga
Wend 
End 

Function FileBrowserCheck(trn.Terrain,se.SceneEditor)
	Local r = False 
	For f.FileBrowser = Each FileBrowser 
		If f\visible Then 
			r = True 
			DrawFileBrowser f
			FileBrowserInput f			
			If f\newfile Then
				; handle the file based on its type
				If (f\ext$ = "gltf") Then	
					Local mesh = LoadMesh(f\filepath$)
					SetMeshShadowsEnabled mesh,True
					Local model = CreateModel(mesh)
					se\current_brush = model
					se\edit_mode = 1
				End If 
				f\visible = False
				f\newfile = False	
			End If		
		End If 	
	Next
	Return r	
End Function 