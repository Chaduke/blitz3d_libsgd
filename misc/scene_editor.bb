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

ga.GameApp = CreateGameApp("Scene Editor","Chaduke's")
e.Environment = CreateEnvironment(ga\gui_font)
trn.Terrain = New Terrain
SetTerrainDefaults trn
CreateTerrain trn
PlaceEntityOnTerrain ga\pivot,trn,1 
fb.FileBrowser = CreateFileBrowser("Nature Pack","d:\blender\nature_pack\gltf","gltf",ga\browser_font)

ga\debug = True 

While ga\loop
	BeginFrame ga
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	
	; toggle model browser visiblity 	
	If (IsKeyHit(KEY_1)) Then 
		If Not fbc Then 
			fb\visible = True
			GetFileList fb 
		Else 
			fb\visible = False	
		End If 
	End If 		
	
	fbc = FileBrowserCheck(trn)	

	If GUIDragCheck() Then 		
		UpdateEnvironment e,True
	Else 
		NavigationMode ga\pivot,trn
	End If 
				
	DrawAllGUIs()
	
	EndFrame ga
Wend 
End 

Function FileBrowserCheck(trn.Terrain)
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
					SetMeshShadowCastingEnabled mesh,True
					For i = 0 To 10							
						Local model = CreateModel(mesh)
						PlaceEntityOnTerrain model,trn,0,False,True	
						sc# = Rnd(2) + 0.5
						SetEntityScale model,sc,sc,sc
						TurnEntity model,0,Rand(0,360),0
					Next 								
				End If 
				f\visible = False
				f\newfile = False	
			End If		
		End If 	
	Next
	Return r	
End Function 