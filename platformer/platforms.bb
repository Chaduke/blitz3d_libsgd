; platforms.bb
; this code is everything related to platforms

; Circular platforms are 3D cylinders
; should be very easy to detect collisions

Type Platform
	Field name$	
	Field cyl.Cylinder
	Field mesh	
	Field material	
End Type	

Global current_platform.Platform

Function RemovePlatform(p.Platform,t.Terrain)
	DestroyEntity p\cyl\model	
	Delete p\cyl
	Delete p
	CheckForZeroPlatforms(t)
End Function

Function CheckForZeroPlatforms(t.Terrain)
	; make sure a platform is selected 
	; if not get the first one
	; if that doesn't exist create one	
	
	If current_platform = Null Then 
		current_platform = First Platform
		If current_platform=Null Then 			
			Local tw = t\width
			Local td = t\depth
			AddOnePlatform "Our First Platform",Rand(1,3),Rand(3,6),tw/2,GetTerrainHeight(tw/2,td/2,t) + 3, td/2,0
			current_platform = First Platform
		End If			
	End If
End Function 

; make sure we are not outside the terrain limits
Function KeepPlatformInsideLevel(p.Platform,t.Terrain)
	
	Local tw = t\width
	Local td = t\depth

	Local plx# = GetEntityX(p\cyl\model)
	Local ply# = GetEntityY(p\cyl\model)	
	Local plz# = GetEntityZ(p\cyl\model)
	Local plr# = p\cyl\radius
	Local plh# = p\cyl\height
	
	If plx<plr Then plx = plr
	If plx>tw-plr Then plx = tw-plr	
	If plz<plr Then plz = plr
	If plz>td-plr Then plz = td-plr
	
	Local th# = GetTerrainHeight(plx,plz,t)  
	If ply < th - (plh / 2) Then ply = th - (plh / 2)
	
	SetEntityPosition p\cyl\model,plx,ply,plz
	SetEntityPosition pivot,plx,ply + (plh / 2),plz	

End Function	

Function EditPlatforms(t.Terrain)
	
	CheckForZeroPlatforms(t)
	; process keyboard input	
	; in this case the keyboard moves the platform
	KeyboardInputThirdPerson(current_platform\cyl\model,pivot)	
	
	; we'll use using Third Person mouse input version 2
	; which turns the pivot on the X-axis as well as the Y-Axis
	; and corrects for the Z 
	ThirdPersonMouseInputEditor(camera,pivot)
	ClampEntityPitch(pivot,70,-70)		
	KeepPlatformInsideLevel(current_platform,t)	
	
	; left bracket or Z
	If (IsKeyHit(91) Or IsKeyHit(90)) Then		
		; switch to previous 
		current_platform = Before(current_platform)
		; check if null
		If current_platform = Null Then 
			; switch to last 
			current_platform = Last Platform
		End If		
	End If
		
	; right bracket or C
	If (IsKeyHit(93) Or IsKeyHit(67)) Then 		
		; switch to next
		current_platform = After(current_platform)
		; check if null
		If current_platform = Null Then 
			; switch to first
			current_platform = First Platform
		End If		
	End If	
	
	; INSERT adds a new platform
	If (IsKeyHit(260) Or IsMouseButtonHit(0)) Then 
		Local count = 0
		Local i.Platform
		For i.Platform = Each Platform
			count = count + 1
		Next 	
		AddOnePlatform "Circular Platform " + count + 1, Rand(1,3), Rand(3,6), GetEntityX(pivot) + 5, GetEntityY(pivot), GetEntityZ(pivot),0
		current_platform = Last platform
	End If
	
	; DELETE or right mouse deletes the current platform
	If (IsKeyHit(261) Or IsMouseButtonHit(1)) Then 
		RemovePlatform current_platform,t
		; simply calling delete doesn't clean everything		
	End If
	
	; change platform attributes 
	If IsMouseButtonHit(0)		
		If MouseCollidedRect(10,70,200,90) Then 
			; we clicked to change the platform material
			ChangePlatformMaterial()		
		End If	
	End If
	
	; this is being called before RenderScene()
	; but it seems to work ok
	; I was afraid RenderScene() might wipe it out		
	
	Set2DTextColor 1,0,0,1		
	Draw2DText "Current Platform Name : " + current_platform\name$,10,30
	plx# = GetEntityX(current_platform\cyl\model)
	ply# = GetEntityY(current_platform\cyl\model)
	plz# = GetEntityZ(current_platform\cyl\model)
	Draw2DText "Current Platform Location : " + plx + "," + ply + "," + plz,10,50
	Draw2DText "Current Platform Material : " + current_platform\material,10,70
		
	; See if current platform is colliding with any other(s)
	count = 0
	For p.Platform = Each Platform		
		If p <> current_platform Then 
			If CylindersCollided(current_platform\cyl,p\cyl) Then				
				Draw2DText "Colliding with " + p\name$,10,150 + (count * 20)
				count = count + 1
			End If
		End If	
	Next		
End Function 

Function GetMaterial(n)	
	Restore materials
	Local readdata = True
	While readdata		
		Read number,name$		
		If (number = n  Or number = 0) Then
			readdata = False				
		End If	
	Wend 
	If number = 0 Then 
		Alert "Could not find requested material number"
		Return 0
	End If	
	Local path$ = "../engine/assets/materials/" + name$ + "_1K-JPG"
	Local r = LoadPBRMaterial(path$)
	Return r
End Function 		


Function ChangePlatformMaterial()
	Local name$ = current_platform\name$
	Local height# = current_platform\cyl\height#
	Local radius# = current_platform\cyl\radius#
	Local x# = GetEntityX(current_platform\cyl\model)
	Local y# = GetEntityY(current_platform\cyl\model)
	Local z# = GetEntityZ(current_platform\cyl\model)	
	Local material = current_platform\material 
	
	material = material + 1
	If material > 9 Then material = 1
	
	Delete current_platform\cyl
	Delete current_platform
	
	AddOnePlatform name$,height#,radius#,x#,y#,z#,material	
End Function 

Function AddOnePlatform(name$,height#,radius#,x#,y#,z#,m=0)	
	p.Platform = New Platform
	p\name$ = name$
	p\cyl.Cylinder = New Cylinder
	p\cyl\height = height
	p\cyl\radius = radius	
	
	Local r
	If m = 0 Then 
		r = Rand(1,9)
	Else 
		r = m
	End If
				
	p\material = GetMaterial(r)
	p\mesh = CreateCylinderMesh(p\cyl\height,p\cyl\radius,32,p\material)	
	TFormMeshTexCoords p\mesh,p\cyl\radius / 4,p\cyl\height / 3,0,0
	p\cyl\model = CreateModel(p\mesh)			
	SetEntityPosition p\cyl\model,x,y,z
End Function 

Function AddPlatforms(t.Terrain,num=5,random=True)	
	Local tw = t\width
	Local td = t\depth

	DisplayLoadingMessage "Adding Platforms..."		
	
	; create random platforms per definitions in scene.bb
	If random Then 
		For i=0 To num
			Local px# = Rnd(10,tw-10)
			Local pz# = Rnd(10,td-10)	
			Local py# = GetTerrainHeight(px,pz,t) + Rand(6,35)				
			AddOnePlatform "Circular Platform " + i,Rand(1,3),Rand(3,6),	px,py,pz,0		   
		Next
	Else 
		; manually create some platforms for testing
		AddOnePlatform "Test Platform 1", 1,3,50,GetTerrainHeight(50,50,t) + 2,50,0
		AddOnePlatform "Test Platform 2", 1,3,55,GetTerrainHeight(55,55,t) + 3,55,0		
	End If	
		
End Function 

.materials 
Data 1,"Grass001"
Data 2,"Concrete031"
Data 3,"Planks037B"
Data 4,"Tiles074"
Data 5,"Travertine013"
Data 6,"Ground075"
Data 7,"Grass004"
Data 8,"Ground037"
Data 9,"Ground020"
Data 10,"Ground054"
Data 0,"END"