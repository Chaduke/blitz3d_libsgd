; file_browser.bb
; by Chaduke
; 20240619

Type FileBrowser
	Field title$
	Field file_count	
	Field selected_file
	Field path$
	Field ext$
	Field ext2$ 
	Field font 	
	Field spacing
	Field newfile 
	Field filepath$ 
	Field filename$
	Field visible 
End Type 

Type LoadedMaterial 
	Field path$ 
	Field material
End Type 

Dim file_list$(0)

; uncomment the following line to test the file browser functionality 
; Include "../engine/math2d.bb" : Global model : TestFileBrowser() : End

Function CreateFileBrowser.FileBrowser(title$,path$,ext$,font)
	Local fb.FileBrowser = New FileBrowser
	fb\title$ = title$ 
	fb\path$ = path$ 
	fb\ext$ = ext$ 
	fb\font = font 
	fb\spacing = GetFontHeight(font)
	Return fb
End Function 
 
Function TestFileBrowser()
	
	CreateWindow 1280,720,"File Browser",0
	
	camera = CreatePerspectiveCamera()
	MoveEntity camera,0,5,-18
	light = CreateDirectionalLight()
	TurnEntity light,-45,0,0
	SetAmbientLightColor 1,1,1,0.1
	
	; load some different fonts to test with 
	small_font = LoadFont("c:\windows\fonts\consola.ttf",12)
	constan_font = LoadFont("c:\windows\fonts\constan.ttf",16)
	ebrima_font = LoadFont("c:\windows\fonts\ebrimabd.ttf",20)
	
	model_browser.FileBrowser=New FileBrowser	
	model_browser\title$ = "Model Browser" 
	model_browser\path$ = "../engine/assets/models/trees"	
	model_browser\ext$ = "glb"
	model_browser\ext2$ = "gltf"
	model_browser\font = ebrima_font
	model_browser\spacing = 24	
	
	loop=True	
	While loop 
		; get user input 
		e=PollEvents()			
		If (e = 1 Or IsKeyHit(256)) Then loop = False 
		
		If (IsKeyDown(KEY_UP_ARROW) And model_browser\visible=False) Then MoveEntity camera,0,0,0.2
		If (IsKeyDown(KEY_DOWN_ARROW) And model_browser\visible=False) Then MoveEntity camera,0,0,-0.2	
		
		; toggle model browser visiblity 	
		If (IsKeyHit(49)) Then 
			If Not fbc Then 
				model_browser\visible = True
				GetFileList model_browser 
			Else 
				model_browser\visible = False	
			End If 
		End If 		
		
		fbc = FileBrowserCheckSimple()				
		If model Then TurnEntity model,0,3,0
		
		RenderScene()
		Clear2D()	
		Set2DFont constan_font 
		If Not fbc Then 
			Draw2DText "Press 1 to open file browser",10,10	
			Draw2DText "Currently loaded file = " + model_browser\filepath$,10,GetWindowHeight() - 30	
		End If 				
		Present()
	Wend 			
End Function

Function FileBrowserCheckSimple()
	Local r = False 
	For f.FileBrowser = Each FileBrowser 
		If f\visible Then 
			r = True 
			DrawFileBrowser f
			FileBrowserInput f			
			If f\newfile Then
				; handle the file based on its type
				If (f\ext$ = "glb" Or f\ext2$ = "gltf") Then	 
					If model Then DestroyEntity model 
					mesh = LoadMesh(f\filepath$)
					FitMesh mesh,-8,0,-8,8,10,8,True
					model = CreateModel(mesh)					
				End If 
				f\visible = False
				f\newfile = False	
			End If		
		End If 	
	Next
	Return r	
End Function 

Function GetFileCount(f.FileBrowser)
	Local count = 0
	Local loop = True 
	Local folder = ReadDir(f\path$)
	While loop
		Local file$ = NextFile$(folder)		
		If file$ = "" Then 
			loop = False
		Else			
			If FileType(f\path$ + "\" + file$) <> 2 Then 
				ext$ = Right$(file$,Len(f\ext$))					
				If ext$=f\ext$ Then count=count + 1
			Else 
				If f\ext$="dir" Then If (file$<> "." And file$<>"..") Then count=count + 1
			End If				
		End If		
	Wend 
	CloseDir folder 
	f\file_count=count
End Function 

Function GetFileList(f.FileBrowser)	
	GetFileCount f
	Dim file_list$(f\file_count-1)	
	Local folder = ReadDir(f\path$)	
	Local loop = True 
	Local current = 0
	While loop 
		Local file$ = NextFile$(folder)
		If file$ = "" Then 
			loop = False
		Else 
			If FileType(f\path$ + "\" + file$) <> 2 Then
				ext$ = Right$(file$,Len(f\ext$))	
				If ext$=f\ext$ Then					
					file_list$(current) = file$
					current = current + 1
				End If	
			Else 
				If f\ext$ = "dir" Then 
					If (file$<> "." And file$<>"..") Then 
						file_list$(current) = file$
						current = current + 1
					End If 	
				End If 
			End If 				
		End If		
	Wend 
	CloseDir folder		
	f\selected_file=0	
	f\newfile = False
End Function 

Function DrawFileBrowser(f.FileBrowser)	
					
	Local col = 0
	Local row = 0
	Local i = o	
	Local left_margin = 15
	Local top_margin = 30
	Local col_width = 300	
	
	Set2DFillColor 0.5,0.5,0.5,1
	Draw2DRect 5,5,605,GetWindowHeight()-5
	Set2DTextColor 1,0,0,1
	Set2DFont f\font	
	Draw2DText f\title$,left_margin,5
	rowcount = GetWindowHeight() / (f\spacing	+ 1)
	While i < f\file_count 
		Local x = col * col_width + left_margin
		Local y = row * f\spacing + top_margin				
		If i = f\selected_file Then Set2DTextColor 1,1,0,1 Else Set2DTextColor 0,0,0,1
		Draw2DText file_list$(i),x,y
		
		; check mouse for selected file 		
		If MouseCollidedRect(x,y,x+col_width,y+f\spacing) Then 
			f\selected_file = i
		End If 
					
		row = row + 1
		If row > rowcount Then 
			col = col + 1
			row = 0
		End If
		i = i +1
	Wend
	If f\newfile Then Draw2DText "Loading file..." + f\filepath$,10,GetWindowHeight() - 30		
End Function 

Function FileBrowserInput(f.FileBrowser)	
	 
	If IsKeyHit(264) Then ; key down 
		f\selected_file = f\selected_file + 1
		If f\selected_file > f\file_count - 1 Then f\selected_file = 0
	End If 
	
	If IsKeyHit(265) Then ; key up
		f\selected_file = f\selected_file - 1
		If f\selected_file < 0 Then f\selected_file = f\file_count - 1
	End If 	
	
	; ENTER KEY
	If (IsKeyDown(32) Or IsMouseButtonHit(0)) Then 		
		If spacedown = False Then 
			f\newfile = True 
			f\filepath$ = f\path$ + "/" + file_list$(f\selected_file)
			f\filename$ = file_list$(f\selected_file)
			Clear2D()
			Draw2DText "Loading file..." + f\filepath$,10,GetWindowHeight() - 30				
			PollEvents()
			RenderScene()		
			Present()	
			spacedown = True 
		End If 	
	Else 
		spacedown = False 
	End If 	
End Function