; escape_the_maze.bb
; by Chaduke
; 20240602

; HELPER FUNCTIONS
Function DisplayTextCenter(msg$,offsetY#,font)
	Local centerX# = GetWindowWidth()/2
	Local centerY# = GetWindowHeight()/2
	Local mhw# = GetTextWidth(font,msg$) / 2 ; message half width
	Set2DTextColor 1,1,1,1
	Draw2DText msg$,centerX#-mhw#,centerY# + offsetY
End Function 
	
Function RectsCollided(l1,t1,r1,b1,l2,t2,r2,b2) ; left, top, right, bottom	
	Return (l1 < r2 And t1 < b2 And r1 > l2 And b1 > t2)
End Function 

Function GetCollisionType(l1,t1,r1,b1,l2,t2,r2,b2) ; left, top, right, bottom
	; 0 = no collision 
	; 1 = right / left collision 
	; 2 = bottom / top collision 
	; 3 = left / right collision 
	; 4 = top / bottom collision	
					
	Local c = 0
	d1 = r2 - l1
	d2 = b2 - t1
	d3 = r1 - l2
	d4 = b1 - t2	
	
	; display these 4 amounts for debugging purposes
	; DisplayTextCenter d1,-150,constan_font
	; DisplayTextCenter d2,-120,constan_font
	; DisplayTextCenter d3,-90,constan_font
	; DisplayTextCenter d4,-60,constan_font
	
	; smallest amount determines the type
	If (d1 < d2 And d1 < d3 And d1 < d4) Then c = 1
	If (d2 < d1 And d2 < d3 And d2 < d4) Then c = 2
	If (d3 < d1 And d3 < d2 And d3 < d4) Then c = 3
	If (d4 < d1 And d4 < d2 And d4 < d3) Then c = 4
	
	Return c 		
End Function 

; INITIAL SETUP

; initialize a game window 
CreateWindow 1920,1080,"Escape the Maze", 1
env = LoadTexture("sgd://envmaps/grimmnight-cube.jpg", 4, 56)
SetEnvTexture env
skybox = CreateSkybox(env)	

; load fonts / images / etc
Global constan_font = LoadFont("c:\windows\fonts\constan.ttf",26)
Set2DFont constan_font 
SetMouseCursorMode 3

; setup the maze 
; the maze will consist of cubes on a grid 
; we can edit the maze right here in the editor 
; let's make it 8 x 8 to start 

Const cubes = 7 ; 0-7 Each way = 8 by 8
Dim maze$(cubes) 

maze$(0) = "1111E111"
maze$(1) = "11010001"
maze$(2) = "10011101"
maze$(3) = "11000101"
maze$(4) = "10010101"
maze$(5) = "10110101"
maze$(6) = "10000001"
maze$(7) = "1S111111"

; create a camera
camera = CreatePerspectiveCamera()
pivot = CreateModel(0)
SetEntityParent camera,pivot
MoveEntity pivot,1.5,0.5,0.5

; create the maze in 3D

; create a floor 
ground_material = LoadPBRMaterial("sgd://materials/Tiles093_1K-JPG")
ground_mesh = CreateBoxMesh(0,-0.1,0,cubes+1,0,cubes+1,ground_material)
TFormMeshTexCoords ground_mesh,8,8,0,0
ground_model = CreateModel(ground_mesh)

cube_material1 = LoadPBRMaterial("sgd://materials/Bricks076C_1K-JPG")
cube_mesh1 = CreateBoxMesh(0,0,0,1,1,1,cube_material1)

cube_material_start = CreatePBRMaterial()
SetMeshShadowCastingEnabled cube_mesh1,True

; create the cubes 
For x = 0 To cubes
	For z = 0 To cubes
		c$ = Mid$(maze$(cubes-z),x+1,1)
		If c$ <> "0" Then
			If c$ = "1" Then 
				cube_model = CreateModel(cube_mesh1)
				SetEntityPosition cube_model,x,0,z				
			End If 	
			If c$ = "S" Then 
				; make a starting cube with a transparent red material
				
			End If	
			; If c$ = "E" Then			
		End If	
	Next
Next	

; create lights
; light = CreateDirectionalLight()
; TurnEntity light,-30,0,0
; SetSceneAmbientLightColor 0,0.5,1,0.05

; point lights 
For x = 0 To cubes Step 2.5
	For z = 0 To cubes Step 2.5
		plight = CreatePointLight()
		MoveEntity plight,x,3,z
		SetLightShadowMappingEnabled plight,True
		SetLightRange plight,7
		SetLightColor plight,1,1,1,0.2
	Next 
Next 

; MAIN LOOP
loop = True 
quit = False 
drawmaze2d = True 
a# = 1 ; alpha value for 2D map overlay
gridsize2d = 20
mapdrawoffset = 10
prad# = gridsize2d / 4 ; player radius
While loop 
	; create branches based on game state
	; startup menu
	; options menu
	; tutorial / help
	; edit / sandbox mode
	; story / game mode 
	; get user input 
	e = PollEvents()
		
	If (e = 1 Or IsKeyHit(256)) Then 
		If quit = False Then
			quit = True 
		Else 
			quit = False
		End If 	
	End If
	
	If (quit = True And IsKeyHit(89)) Then 
		loop = False
	End If
	
	; update objects and data structures based on user input
	If IsKeyDown(265) Then MoveEntity pivot,0,0,0.02 ; UP ARROW
	If IsKeyDown(264) Then MoveEntity pivot,0,0,-0.02 ; DOWN ARROW
	If IsKeyDown(263) Then TurnEntity pivot,0,2,0 ; LEFT ARROW
	If IsKeyDown(262) Then TurnEntity pivot,0,-2,0 ; RIGHT ARROW
	
	; perform corrections / implement constraints / apply game rules
	
	; determine player location in the 2D maze
	px# = GetEntityX(pivot) * gridsize2d
	py# = (gridsize2d * (cubes + 1)) - GetEntityZ(pivot) * gridsize2d
	
	; determine the sides of the player rectangle 
	pl = px - prad ; player left
	pt = py - prad ; player top
	pr = px + prad ; player right 
	pb = py + prad ; player botttom
	
	; do rectangle collisions with grid squares 
	For x = 0 To cubes
		For y = 0 To cubes
			c$ = Mid$(maze$(y),x+1,1)
			If c$ = "1" Then 
				; get sides of this block 
				cl = x * gridsize2d
				ct = y * gridsize2d
				cr = x * gridsize2d + gridsize2d
				cb = y * gridsize2d + gridsize2d
				If RectsCollided(pl,pt,pr,pb,cl,ct,cr,cb) Then 
					; DisplayTextCenter "Colliding!!",-30,constan_font
					t = GetCollisionType(pl,pt,pr,pb,cl,ct,cr,cb)
					; make the correction in 3D space
					If t = 1 Then SetEntityPosition pivot,GetEntityX(pivot) + 0.02,GetEntityY(pivot),GetEntityZ(pivot)
					If t = 2 Then SetEntityPosition pivot,GetEntityX(pivot),GetEntityY(pivot),GetEntityZ(pivot)-0.02
					If t = 3 Then SetEntityPosition pivot,GetEntityX(pivot) - 0.02,GetEntityY(pivot),GetEntityZ(pivot)
					If t = 4 Then SetEntityPosition pivot,GetEntityX(pivot),GetEntityY(pivot),GetEntityZ(pivot)+0.02				
				End If		
			End If
		Next
	Next			
					
	; render 
	RenderScene()
	; draw 2D overlay	
	Clear2D()
	If quit = True Then
		DisplayTextCenter "Really Quit ?? (Y to Confirm, Escape to Cancel)",0,constan_font
	End If	
	
	If drawmaze2d = True Then 
		; draw the maze in 2D 
		For x = 0 To cubes 
			For y = 0 To cubes
				c$ = Mid$(maze$(y),x+1,1)
				If c$ <> "0" Then
					If c$ = "1" Then Set2DFillColor 1,1,0,a
					If c$ = "S" Then Set2DFillColor 1,0,0,a
					If c$ = "E" Then Set2DFillColor 0,0,1,a
					rx = x * gridsize2d + mapdrawoffset
					ry = y * gridsize2d + mapdrawoffset
					Draw2DRect rx,ry, rx + gridsize2d, ry + gridsize2d
				End If	
			Next
		Next
		; draw the player in the maze		
		Set2DFillColor 0,1,0,a
		Draw2DOval pl + mapdrawoffset,pt + mapdrawoffset,pr + mapdrawoffset,pb + mapdrawoffset ; draw the player as a circle
		; draw a line to indicate the player's direction 
		Set2DFillColor 1,0,0,1
		Draw2DLine px+mapdrawoffset,py+mapdrawoffset,px + mapdrawoffset + Sin(GetEntityRY(pivot)-180) * prad,py + mapdrawoffset + Cos(GetEntityRY(pivot)-180) * prad
	End If	
	Draw2DText GetFPS(),10,GetWindowHeight() - 30
	Present()
Wend 
; CLEANUP AND SHUTDOWN

End 
 