; sprites.bb
; by Chaduke
; 20240601

; at this point this is basic funtionality to add sprites/billboards and place them in a scene
; all of the foliage / grass functionality is here at the moment
; if you want to add a new sprite image to the list of currently supported ones
; put the image in the /assets/images folder 
; then add it to the Data statments at the bottom of this file

Global current_billboard.Billboard

Type Billboard
	Field name$
	Field sprite
	Field scale#
	Field height#
End Type	

; make sure we are not outside the terrain limits
Function KeepBillboardInsideLevel(t.Terrain)
	
	; get our terrain values	
	Local tw = t\width
	Local td = t\depth
	
	; check for zero billboards
	If current_billboard = Null Then		
		AddOneBillboard "Our First Billboard",image,tw/2,GetTerrainHeight(tw/2,td/2,t),td/2,1
		current_billboard = First Billboard
	End If 	
	
	; get our current location 
	Local tlx# = GetEntityX(current_billboard\sprite)
	Local tly# = GetEntityY(current_billboard\sprite)		
	Local tlz# = GetEntityZ(current_billboard\sprite)	
	
	; correct if outside the terrain
	If tlx<2 Then tlx = 2
	If tlx>tw-2 Then tlx = tw-2
	If tlz<2 Then tlz = 2
	If tlz>td-2 Then tlz = td-2
	
	; keep sprite within reasonable terrain height limits
	Local th# = GetTerrainHeight(tlx,tlz,t) + 0.3
	
	If tly > th + 0.2 Then tly = th + 0.2	
	If tly < th - 0.2 Then tly = th - 0.2
	
	SetEntityPosition current_billboard\sprite,tlx,tly,tlz
	SetEntityPosition pivot,tlx,tly,tlz
End Function

Function AddOneBillboard(name$,image,x#,y#,z#,scale#)
	b.Billboard = New Billboard
	b\name$ = name$
	b\sprite = CreateSprite(image)
	; b\height = GetSpriteHeight(image)		
	; SetEntityScale b\sprite,scale,scale,scale
	SetEntityPosition b\sprite,x,y+0.4,z
	b\scale# = scale		
End Function 

Function EditBillboards(t.Terrain)
	
	; get our terrain values	
	Local tw = t\width
	Local td = t\depth		
			
	ThirdPersonMouseInputEditor(camera,pivot)
	ClampEntityPitch(pivot,70,-70)
	KeepBillboardInsideLevel(t)
	KeyboardInputThirdPerson(current_billboard\sprite,0.05)	
	
	; If current_attribute\number > 0 Then 
		; EditAttribute current_attribute\number
	; Else 
		; GetChar()	
		; make sure keys stay flushed
		; If IsKeyHit(92) Then ; backslash
			; EditAttribute 15
		; End If			
	; End If
		
	; left bracket or Z
	If (IsKeyHit(91) Or IsKeyHit(90)) Then		
		; switch to previous 
		current_billboard = Before(current_billboard)
		; check if null
		If current_billboard = Null Then 
			; switch to last 
			current_billboard = Last Billboard
		End If		
	End If
		
	; right bracket or C
	If (IsKeyHit(93) Or IsKeyHit(67)) Then		
		; switch to next
		current_billboard = After(current_billboard)
		; check if null
		If current_billboard = Null Then 
			; switch to first
			current_billboard = First Billboard
		End If		
	End If	
	
	; numpad minus or left alt scales down 
	If (IsKeyHit(333) Or IsKeyHit(342)) Then 
		current_billboard\scale# = current_billboard\scale - 0.1
		If current_billboard\scale < 0.1 Then current_billboard\scale = 0.1
		SetEntityScale current_billboard\sprite, current_billboard\scale#,current_billboard\scale#,current_billboard\scale#
	End If	
	
	; numpad plus or SPACE scales up
	If (IsKeyHit(334) Or IsKeyHit(32)) Then 
		current_billboard\scale# = current_billboard\scale + 0.1
		If current_billboard\scale# > 3 Then current_billboard\scale# = 3
		SetEntityScale current_billboard\sprite, current_billboard\scale#,current_billboard\scale#,current_billboard\scale#
	End If	
	
	; INSERT or left mouse adds a new billboard
	If (IsKeyHit(260) Or IsMouseButtonHit(0)) Then 
		Local count = 0
		Local i.Billboard
		For i.Billboard = Each Billboard
			count = count + 1
		Next 	
		image = LoadImage("assets/images/grass1.png",1)
		Local n$ = "Grass Patch " + (count + 1)
		AddOneBillboard n$,image,GetEntityX(pivot), GetEntityY(pivot), GetEntityZ(pivot),1
		current_billboard = Last Billboard
	End If
	
	; DELETE or Right Mouse deletes the current billboard
	If (IsKeyHit(261) Or IsMouseButtonHit(1)) Then 
		DestroyEntity current_billboard\sprite 
		Delete current_billboard	
		; check for zero trees		
		current_billboard = Last Billboard
		If current_billboard = Null Then 
			image = LoadImage("assets/images/grass1.png",1)
			AddOneBillboard "Our First Billboard",image,tw/2,GetTerrainHeight(tw/2,td/2,t),td/2,1
			current_billboard = First Billboard
		End If	
	End If	
	Draw2DText "Current Billboard Name : " + current_billboard\name$,10,30	
	Draw2DText "Default Billboard Image : " + image_name$,10,50	
End Function 

Function GenerateGrass(t.Terrain,numsprites,path$="assets/images/grass1.png")
	; DisplayLoadingMessage "Adding foliage..."	
	
	Local tw = t\width
	Local td = t\depth
	
	; add some grass / bushes	
	grass = LoadImage(path$,1)	

	SeedRnd(MilliSecs())		
	For i=0 To numsprites	
		sx# = Rnd(tw)
		sz# = Rnd(td)
		sy# = GetTerrainHeight(sx,sz,t)	 
				
		r# = Rnd(1)
		If r > 0.2 Then 
			AddOneBillboard "Grass Patch " + i,grass,sx,sy,sz,1
		Else			
			AddOneBillboard "Standard Bush " + i,grass,sx,sy,sz,1
		End If		
	Next
End Function 