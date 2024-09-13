; terrain.bb
; by Chaduke 
; 20240510

; creates a terrain mesh using Perlin Noise

Dim noisemap#(0,0) ; 2-dimensional array to hold height values
Dim normals.Vec3(0)

Type Terrain	
	Field material_path$	
	Field width#
	Field depth#
	Field height#
	Field start_offset#
	Field offset_inc#
	Field calc_normals	
	Field falloff_width#
	Field falloff_height# 	
	Field mesh	
	Field model		
End Type

Function SetTerrainDefaults(t.Terrain)
	t\material_path$ = "../engine/assets/materials/Ground048_1K-JPG"
	t\width# = 128
	t\height# = 16
	t\depth# = 128
	t\start_offset# = 777
	t\offset_inc# = 0.02
	t\calc_normals = True
	t\falloff_width = 16
	t\falloff_height = 8	
End Function 

Function PlaceEntityOnTerrain(e,t.Terrain,yoffset#=0,center=True,random=False,x#=0,z#=0)
	Local ex#
	Local ey#
	Local ez#
	
	If center Then 
		ex = t\width/2
		ez = t\depth/2
	Else If random Then		
		ex = Rand(2,t\width-2)
		ez = Rand(2,t\depth-2)
	Else 
		ex = x
		ez = z
	End If 
	ey = GetTerrainHeight(ex,ez,t) + yoffset
	SetEntityPosition e,ex,ey,ez
End Function
	
Function GetTerrainHeight#(x#, z#,t.Terrain)
	Local ix = Int(x)
	Local iz = Int(z)
	Local rx# = x - ix
	Local rz# = z - iz

	; Ensure we don't go out of bounds
	If ix >= t\width - 1 Then ix = t\width - 2
	If iz >= t\depth - 1 Then iz = t\depth - 2

	; Get the heights of the four corners of the grid cell
	Local h00# = noisemap(ix, iz)
	Local h10# = noisemap(ix + 1, iz)
	Local h01# = noisemap(ix, iz + 1)
	Local h11# = noisemap(ix + 1, iz + 1)

	; Interpolate along x for the top and bottom of the cell
	Local top# = h00 + rx * (h10 - h00)
	Local bottom# = h01 + rx * (h11 - h01)

	; Finally, interpolate along z to get the height at (x, z)
	Local height# = top + rz * (bottom - top)

	Return height
End Function

Function Lerp#(a#, b#, t#)
	Return a + (b - a) * t
End Function

Function Min(v1,v2)
	If v1 < v2 Then Return v1 Else Return v2
End Function 

Function CreateTerrain(t.Terrain)		
	; create the noisemap
	Dim noisemap#(t\width,t\depth)	
	zoffset#=t\start_offset
	For z = 0 To t\depth
		xoffset#= t\start_offset
		zoffset=zoffset + t\offset_inc
		For x = 0 To t\width
			xoffset=xoffset + t\offset_inc
			noisemap#(x,z) = noise(xoffset,zoffset) * t\height
			
			; Adjust the height based on the distance from the edge
			Local dist_x# = Min(x, t\width - x)
			Local dist_z# = Min(z, t\depth - z)
			Local dist# = Min(dist_x, dist_z)

			If dist < t\falloff_width Then
				Local falloff# = dist / t\falloff_width
				noisemap#(x,z) = Lerp(t\falloff_height, noisemap#(x,z), falloff)
			End If
		Next
	Next	
	
	If t\calc_normals Then 
		; create the normal map
		Dim normals.Vec3((t\width+1) * (t\depth+1))
		Local normal.Vec3 = New Vec3
		Local vertexA.Vec3 = New Vec3
		Local vertexB.Vec3 = New Vec3
	   Local vertexC.Vec3 = New Vec3	
	   Local edge1.Vec3 = New Vec3
	   Local edge2.Vec3 = New Vec3
	   Local triangleNormal.Vec3 = New Vec3
		i%=0
		For z=0 To t\depth
			For x=0 To t\width
				SetVec3 normal,"Normal",0,0,0
				; Calculate the normal based on neighboring triangles
				For dz = -1 To 1
					For dx = -1 To 1
						If (z + dz >= 0 And z + dz < t\depth And x + dx >= 0 And x + dx < t\width) Then					
						   ; setup triangle verts
						   SetVec3 vertexA,"Vertex A",x,noisemap(x,z),z
							SetVec3 vertexB,"Vertex B",x+dx,noisemap(x+dx,z+dz),z+dz
							SetVec3 vertexC,"Vertex C",x+dx,noisemap(x + dx, z), z
							
							; Calculate the normal of the triangle												
							SubVec3 edge1,vertexB,vertexA						
							SubVec3 edge2,vertexC,vertexA						
							CrossVec3 triangleNormal,edge1,edge2
							NormalizeVec3 triangleNormal 					
							; Add the triangle normal to the vertex normal
							AddToVec3 normal,triangleNormal 
						EndIf
					Next
				Next
				; Normalize the vertex normal
				NormalizeVec3 normal
				normals(i) = normal
				i=i+1
			Next
		Next	
	End If
	; create the mesh
	t\mesh = CreateMesh(0,0)
	
	; values for uv positioning
	Local u# = 1 / Float(t\width)
	Local v# = 1 / Float(t\depth)	

	; add the vertices to a heightplane
	i = 0
	For z=0 To t\depth
		For x = 0 To t\width		  
			; Add the vertex
			If t\calc_normals Then 
				AddVertex t\mesh, x, noisemap(x, z), z, normals(i)\x, normals(i)\y, normals(i)\z, x * u, (t\depth - z) * v
			Else 
				AddVertex t\mesh, x, noisemap(x, z), z, 0,1,0, x * u, (t\depth - z) * v
			End If
			i = i + 1
		Next
	Next
	
	; create the surface and triangles
	Local material = LoadPBRMaterial(t\material_path$)
	Local surf = CreateSurface(t\mesh,material,0)	
	
	For z=0 To t\depth - 1
		For x = 0 To t\width-1			
			Local v0 = x + (z * t\width + z)
			Local v1 = v0 + 1
      		Local v2 = v0 + t\width + 1
     		Local v3 = v2 + 1
			AddTriangle surf, v0, v1, v2
			AddTriangle surf, v1, v3, v2
		Next
	Next
	
	If t\calc_normals = False Then UpdateMeshNormals t\mesh	
	TFormMeshTexCoords t\mesh,t\width/4,t\depth/4,0,0	
	t\model = CreateModel(t\mesh)	
End Function

Function GenerateRockFence(t.Terrain,path$="../engine/assets/models/disc_golf/rock.glb")
	
	Local tw = t\width
	Local td = t\depth
	
	; create a "rock fence" around the outside edges of the terrain
	; DisplayLoadingMessage "Generating rock fence..."
	Local rock_mesh = LoadMesh(path$)
	Local rock
	For x = 0 To tw Step 6			
		rock = CreateModel(rock_mesh)							
		tx = x
		tz = 0
		ty# = GetTerrainHeight(tx,tz,t) - 0.2		
		SetEntityPosition rock,tx,ty,tz-1	
		rs# = Rnd(1.5) + 1
		SetEntityScale rock,1,rs,1
		TurnEntity rock,0,Rand(360),0	
						
		rock = CreateModel(rock_mesh)			
		tz = td
		ty# = GetTerrainHeight(tx,tz,t) - 0.2
		SetEntityPosition rock,tx,ty,tz+1	
		rs# = Rnd(2) + 1
		SetEntityScale rock,1,rs,1
		TurnEntity rock,0,Rand(360),0
	Next
	For z = 0 To td Step 6					
		rock = CreateModel(rock_mesh)						
		tx = 0
		tz = z
		ty# = GetTerrainHeight(tx,tz,t) - 0.2
		SetEntityPosition rock,tx-1,ty,tz	
		rs# = Rnd(2) + 1
		SetEntityScale rock,1,rs,1
		TurnEntity rock,0,Rand(360),0						
		rock = CreateModel(rock_mesh)				
		tx = tw
		ty# = GetTerrainHeight(tx,tz,t) - 0.2
		SetEntityPosition rock,tx+1,ty,tz	
		rs# = Rnd(2) + 1
		SetEntityScale rock,1,rs,1
		TurnEntity rock,0,Rand(360),0
	Next
End Function 