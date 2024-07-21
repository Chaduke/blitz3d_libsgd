; math3d.bb
; by Chaduke
; 20240517

; helper functions for simple 3D collisions and responses

Type Cylinder 
	Field height#
	Field radius#
	Field model
End Type

Type AABB
	Field width# 
	Field height# 	
	Field depth#
	Field model
End Type 	

Function GetCollisionMaterial%()
	Local collision_material = CreatePBRMaterial()
	collision_texture = LoadTexture("../engine/assets/textures/misc/yellow_grid.png",3,20)
	SetMaterialTexture collision_material,"albedoTexture",collision_texture
	SetMaterialFloat collision_material,"roughnessFactor1f",0.2
	SetMaterialBlendMode collision_material,2	
	Return collision_material
End Function 

Function CreateCylinder.Cylinder(height#,radius#)
	Local c.Cylinder = New Cylinder
	c\height = height 
	c\radius = radius	
	Local m = GetCollisionMaterial()	
	Local mesh = CreateCylinderMesh(height,radius,16,m)
	c\model = CreateModel(mesh)
	Return c
End Function 

Function CreateAABB.AABB(width#,height,depth#)
	Local a.AABB = New AABB
	Local m = GetCollisionMaterial()
	Local mesh = CreateBoxMesh(-width/2,-height/2,-depth/2,width/2,height/2,depth/2,m)
	Return a 
End Function 

Function AABBsCollided(a1.AABB,a2.AABB)
	; first check the height difference
	Local a1top# = GetEntityY(a1\model) + (a1\height# / 2)
	Local a1bottom# = GetEntityY(a1\model) - (a1\height# / 2)
	Local a2top# = GetEntityY(a2\model) + (a2\height# / 2)
	Local a2bottom# = GetEntityY(a2\model) - (a2\height# / 2)	
	
	If (a1top > a2bottom And a1bottom < a2top) Then
		Local a1front# = GetEntityZ(a1\model) - (a1\depth# / 2)
		Local a1back# = GetEntityZ(a1\model) + (a1\depth# / 2)
		Local a1left# = GetEntityX(a1\model) - (a1\width# / 2)
		Local a1right# = GetEntityX(a1\model) + (a1\width# / 2)	
		Local a2front# = GetEntityZ(a2\model) - (a2\depth# / 2)
		Local a2back# = GetEntityZ(a2\model) + (a2\depth# / 2)
		Local a2left# = GetEntityX(a2\model) - (a2\width# / 2)
		Local a2right# = GetEntityX(a2\model) + (a2\width# / 2)	

		If RectsCollided(a1left,a1right,a1front,a1back,a2left,a2right,a2front,a2back) Then Return True Else Return False 
	Else 
		Return False
	End If		
End Function 

Function CylindersCollided(c1.Cylinder,c2.Cylinder) 
	; first check the height difference
	Local c1top# = GetEntityY(c1\model) + (c1\height# / 2)
	Local c1bottom# = GetEntityY(c1\model) - (c1\height# / 2)
	Local c2top# = GetEntityY(c2\model) + (c2\height# / 2)
	Local c2bottom# = GetEntityY(c2\model) - (c2\height# / 2)
	
	If (c1top > c2bottom And c1bottom < c2top) Then 
		; check the circle collisions 
		Local c1x# = GetEntityX(c1\model)
		Local c1y# = GetEntityZ(c1\model)
		Local c2x# = GetEntityX(c2\model)
		Local c2y# = GetEntityZ(c2\model)				
		If CirclesCollided(c1x,c1y,c1\radius,c2x,c2y,c2\radius) Then Return True Else Return False		
	Else 		
		Return False
	End If				
End Function 

; calculates the distance between 2 3D points
Function Distance3D#(x1#,y1#,z1#,x2#,y2#,z2#)
	Return Sqr((x2-x1) * (x2-x1) + (y2-y1) * (y2-y1) + (z2-z1) * (z2-z1))
End Function 

Function EntityDistance#(e1,e2)
	Local x1# = GetEntityX(e1) : Local y1# = GetEntityY(e1) : Local z1# = GetEntityZ(e1)
	Local x2# = GetEntityX(e2) : Local y2# = GetEntityY(e2) : Local z2# = GetEntityZ(e2) 
	
	Return Distance3D(x1,y1,z1,x2,y2,z2)
End Function

Function FollowEntity(follower,followed,speed#=0.2)
	; first get the 3D distance between the two
	Local frx# = GetEntityX(follower)
	Local fry# = GetEntityY(follower)
	Local frz# = GetEntityZ(follower)
	Local fdx# = GetEntityX(followed)
	Local fdy# = GetEntityY(followed)
	Local fdz# = GetEntityZ(followed)
		
	Local xd# = fdx - frx 
	Local yd# = fdy - fry
	Local zd# = fdz - frz
		
	Local incx# = xd# * speed
	Local incy# = yd# * speed
	Local incz# = zd# * speed
	
	SetEntityPosition follower,frx + incx,fry + incy,frz + incz
End Function 