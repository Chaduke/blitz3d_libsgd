; hoops.bb
; by Chaduke 
; 20240721

Include "../engine/gameapp.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/trees.bb"
Include "../engine/sprites.bb"
Include "../engine/navigation.bb"
Include "../engine/basic_scene.bb"
Include "basketball.bb"
Include "player.bb"


g.GameApp = CreateGameApp("Hoops","Chaduke's")
bs.BasicScene = CreateBasicScene(g)
b.Basketball = CreateBasketball(bs)
p.Player = CreatePlayer(bs)

hoop = LoadModel("../engine/assets/models/hoops/hoop.glb")
SetMeshShadowCastingEnabled GetModelMesh(hoop),True 
PlaceEntityOnTerrain hoop,bs\trn
hoop_collider = CreateMeshCollider(hoop,0,GetModelMesh(hoop))
terrain_collider = CreateMeshCollider(bs\trn\model,0,bs\trn\mesh)

EnableCollisions 1,0,2
SetMouseZ -5
SetMouseCursorMode 3

g\debug = True 

While g\loop
	BeginFrame g	
	UpdateBasketball b
	UpdatePlayer p,b 	
				 
	SetEntityPosition g\pivot,GetEntityX(p\pivot),GetEntityY(p\pivot) + 0.85,GetEntityZ(p\pivot)
	SetEntityRotation g\pivot,GetEntityRX(g\pivot),GetEntityRY(p\pivot),0
	TurnEntity g\pivot,-GetMouseVY() * 0.1,0,0
	SetEntityPosition g\camera,0,0,GetMouseZ()
	UpdateColliders()
	EndFrame g,False 
	If p\shooting Then DisplayTextCenter "Power : " + b\power,g\font
	Present()
	Wend 
End 