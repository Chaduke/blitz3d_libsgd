Include "../engine/testapp.bb"

t.TestApp = CreateTestApp()
t\bs = CreateBasicScene(True)

shape_material = LoadPBRMaterial("sgd://misc/test-texture.png")
SetMaterialFloat shape_material, "roughnessFactor1f", .5
SetMaterialCullMode shape_material,1

shape_mesh = CreateCube(-0.5,-0.5,-0.5,0.5,0.5,0.5,shape_material)

SetMeshShadowsEnabled shape_mesh,True 
shape_model = CreateModel(shape_mesh)

MoveEntity t\bs\pivot,0,0,-2

While t\loop
	BeginFrame t
	UnrealMouseInput t\bs\pivot
	TurnEntity shape_model,1,0,0
	EndFrame t
	Present()
Wend 
End 

Function CreateShapeMesh(material)
	Local mesh = CreateMesh(0,0)
	
	; triangle 1
	AddMeshVertex mesh,-0.5,0.5,0,0,0,-1,0,0
	AddMeshVertex mesh,-0.5,-0.5,0,0,0,-1,0,1
	AddMeshVertex mesh,0.5,-0.5,0,0,0,-1,1,1
	
	; triangle 2
	AddMeshVertex mesh,0.5,0.5,0,0,0,-1,1,0
	
	Local surf = CreateSurface(mesh,0,material)
	AddSurfaceTriangle surf,0,1,2
	AddSurfaceTriangle surf,0,2,3
	Return mesh
End Function 

Function CreateCube(l#,b#,f#,r#,t#,bk#,material)
	Local mesh = CreateMesh(0,0)
	
	; front face
	
	; triangle 1
	AddMeshVertex mesh,l,t,f,0,0,-1,0,0
	AddMeshVertex mesh,l,b,f,0,0,-1,0,1
	AddMeshVertex mesh,r,b,f,0,0,-1,1,1	
	
	; triangle 2
	AddMeshVertex mesh,r,t,f,0,0,-1,1,0	
	
	; top face	
	; triangle 1
	AddMeshVertex mesh,l,t,bk,0,-1,0,0,0	
	
	Local surf = CreateSurface(mesh,0,material)
	
	; front face
	AddSurfaceTriangle surf,0,1,2
	AddSurfaceTriangle surf,0,2,3
	
	; top face 
	AddSurfaceTriangle surf,4,0,3

	Return mesh
End Function 


Function createSphere(radius#, xSegs, ySegs, material)

	Local mesh = CreateMesh(0,0)
	
	Local fxSegs# = 1/Float(xSegs), fySegs# = 1/Float(ySegs)
	
	For i=0 To xSegs-1
		AddMeshVertex mesh, 0, radius, 0, 0, 1, 0, (Float(i) + .5) * 2 * fxSegs, 0
	Next
	For j = 1 To ySegs-1
		Local pitch# = HALF_PI - Float(j) * Pi * fySegs;
		For i = 0 To xSegs
			Local yaw# = Float(i) * TWO_PI / fxSegs;
			Local r# = Cos(pitch);
			Local y# = Sin(pitch);
			Local x# = Cos(yaw) * r;
			Local z# = Sin(yaw) * r;
			AddMeshVertex mesh, x * radius, y * radius, z * radius, x, y, z, Float(i) * 2 * fxSegs, Float(j) * fySegs
		Next
	Next
	For i = 0 To xSegs-1
		AddMeshVertex mesh, 0, -radius, 0, 0, -1, 0, (Float(i) + .5) * 2 * fxSegs, 1
	Next
	
	Local surf = CreateSurface(mesh, 0, material);
	
	For i = 0 To xSegs-1
		AddSurfaceTriangle surf, i, i + xSegs, i + xSegs + 1
	Next
	
	For j = 1 To ySegs-2
		For i = 0 To xSegs-1
			Local v0 = j * (xSegs + 1) + i - 1
			Local v2 = v0 + xSegs + 2
			AddSurfaceTriangle surf, v0, v2, v0 + 1
			AddSurfaceTriangle surf, v0, v2 - 1, v2
		Next
	Next
	For i = 0 To xSegs-1
		v0 = (xSegs + 1) * (ySegs - 1) + i - 1
		AddSurfaceTriangle surf, v0, v0 + xSegs + 1, v0 + 1
	Next
	
	Return mesh
	
End Function