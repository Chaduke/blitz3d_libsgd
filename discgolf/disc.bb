; disc.bb
; by Chaduke 
; 20240704 

; all functionality related to a flying disc

Type Disc	
	Field name$
	Field velocity.Vec3
	Field pivot
	Field model
	Field lying
	Field thrown
	Field grounded
	Field ob
	Field cyl.Cylinder
	
	; flight characteristics
	Field speed
	Field glide 
	Field turn
	Field fade	
End Type 

Function CreateDisc.Disc(name$,path$,speed#,glide#,turn#,fade#)
	Local d.Disc = New Disc 
	d\name$ = name$
	d\velocity = CreateVec3()
	d\pivot = CreateModel(0)
	d\model = LoadModel(path$)
	SetEntityParent d\model,d\pivot
	d\cyl = CreateCylinder(0.1,0.2)
	
	; flight numbers
	d\speed# = speed#
	d\glide# = glide#
	d\turn# = turn#
	d\fade# = fade#
	Return d
End Function 

Function UpdateDisc(d.Disc,h.Hole,p.Player)		
	; DISC FLIGHT AREA						
	If d\thrown Then 	
		; slow disc down in air
		d\velocity\z = d\velocity\z - (0.001 + GetEntityRX(d\pivot) * 0.0005)
		
		; the disc velocity can actually go negative if the nose angle is tilted upwards enough
		; this is exactly how flying discs work 
		; in some cases we may want to be less realistic so we'd use the following line
		; or sometimes discs seem to slow down, come to a mid air stop and just drop
		
		; If d\velocity\z < 0 Then d\velocity\z = 0	
		
		; make it spin				
		TurnEntity d\model,0,-d\velocity\z * 100,0	
		
		; make it fall		
		; this needs a glide and possibly spin adjustment 	
		d\velocity\y = d\velocity\y - 0.001	
		
		; make it turn / fade
		d\velocity\x = d\velocity\x - (-GetEntityRZ(d\pivot) * 0.0005 * d\velocity\z)
		
		FollowEntity(p\pivot,d\pivot) ; make the player camera follow the disc in flight 
		
		; check if the disc hits the terrain		
		If GetEntityY(d\pivot) < GetTerrainHeight(GetEntityX(d\pivot),GetEntityZ(d\pivot),h\trn) Then	
			ClampEntityAboveTerrain d\pivot,h\trn,0.1			
			d\grounded = True				
			d\velocity\y = 0					
			d\thrown = False
		End If 
		
		; check for collision between disc and basket
		If CylindersCollided(d\cyl,h\bsk\cage) Then 
			
			; get the horizontal distance between the middle of disc and the middle of the basket	
			Local hdiff#=Distance2D(GetEntityX(d\pivot),GetEntityZ(d\pivot),GetEntityX(h\bsk\model),GetEntityZ(h\bsk\model))
			; get the vertical height difference 
			Local vdiff# = GetEntityY(d\pivot) - GetEntityY(h\bsk\model)
			
			; check if we hit the chains
			If (vdiff# > 0.8 And vdiff# < 1.6 And hdiff# < 1) Then						
				EndThrow d,p
				PlaySound h\bsk\se\sounds[0] ; play chains sound 
				
				If p\minidisc_mode = False Then 	
					newhole = True 	
					If throw - hole_par = -1 Then 
						SetEntityVisible birdie,True
						PlaySound h\bsk\se\sounds[1]
					End If
						
					If throw - hole_par = -2 Then 
						SetEntityVisible eagle,True
						PlaySound h\bsk\se\sounds[2]
					End If 	
					SetMouseZ -10
				End If								
			Else 
				; richochet!
				PlaySound h\bsk\se\sounds[3]
				TurnEntity d\pivot,0,Rand(0,180),0	
				d\velocity\z = d\velocity\z * 0.5						
			End If	
		End If	
		; check for tree collisions
		For stree.SolidTree = Each SolidTree
			If (CylindersCollided(stree\trunk_cyl,d\cyl) Or CylindersCollided(stree\leaf_cyl,d\cyl)) Then 				
				TurnEntity d\pivot,0,Rand(0,180),0	
				d\velocity\z = d\velocity\z * 0.8
			End If 
		Next									
	End If	 ; end disc thrown 
	
	; sliding on the ground	
	If d\grounded Then 
		ClampEntityAboveTerrain d\pivot,h\trn,0.02			
		d\velocity\z = d\velocity\z - 0.015	
		TurnEntity d\model,0,-d\velocity\z * 100,0			
		If d\velocity\z < 0 Then 	EndThrow d,p			
	End If				
									 
	; disc has gone ob left
	If GetEntityX(d\pivot) < h\ob_padding Then 
		SetEntityPosition d\pivot,h\ob_padding,GetEntityY(d\pivot),GetEntityZ(d\pivot)
		OBBounce d
	End If
	
	; disc has gone ob right
	If GetEntityX(d\pivot) > h\trn\width-h\ob_padding Then 
		SetEntityPosition d\pivot,h\trn\width-h\ob_padding,GetEntityY(d\pivot),GetEntityZ(d\pivot)
		OBBounce d
	End If
	
	; disc has gone ob short somehow
	If GetEntityZ(d\pivot) < h\ob_padding Then 
		SetEntityPosition d\pivot,GetEntityX(d\pivot),GetEntityY(d\pivot),h\ob_padding
		OBBounce d
	End If 
	
	; disc has gone ob long 
	If GetEntityZ(d\pivot) > h\trn\depth-h\ob_padding Then 
		SetEntityPosition d\pivot,GetEntityX(d\pivot),GetEntityY(d\pivot),h\trn\depth-h\ob_padding
		OBBounce d
	End If 
	
	; move the disc
	MoveEntity d\pivot,d\velocity\x,d\velocity\y,d\velocity\z 	
End Function 

Function OBBounce(d.Disc)
	If d\thrown Then 
		d\ob = True
		TurnEntity d\pivot,0,90,0
		d\velocity\z = d\velocity\z * 0.5
	End If 
End Function 

Function ThrowDisc(d.Disc,p.Player)
		
	d\velocity\x = -p\power * 0.001
	d\velocity\y = p\power * 0.0002
	d\velocity\z = p\power * 0.015
	d\thrown=True
	
	; I think these should be part of the player state
	p\windup=False	
	p\ready=False
	
	p\power = 0	; not sure if this matters 	
End Function

Function EndThrow(d.Disc,p.Player)
	d\velocity\x = 0 : d\velocity\y = 0 : d\velocity\z = 0	
	d\lying = True : d\thrown = False : d\grounded = False
	
	If (d\ob And p\minidisc_mode = False) Then 
		p\throw = p\throw + 1	
		p\total_throws = p\total_throws + 1
	End If 	
End Function 