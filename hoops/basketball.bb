; basketball.bb
; by Chaduke
; 20240721

Type Basketball
	Field model 
	Field pivot
	Field vel.Vec3
	Field acc.Vec3
	Field rot.Vec3
	Field collider	
	Field power#
End Type 

Function CreateBasketball.Basketball(bs.BasicScene)
	Local b.Basketball = New Basketball
	b\model = LoadModel("../engine/assets/models/hoops/basketball.glb")
	SetMeshShadowCastingEnabled GetModelMesh(b\model),True 
	b\pivot = CreateModel(0)
	SetEntityParent b\model,b\pivot
	PlaceEntityOnTerrain b\pivot,bs\trn	
	MoveEntity b\pivot,0,4,0	
	b\vel = CreateVec3("Basketball Velocity")
	b\acc = CreateVec3("Basketball Acceleration",0,-0.005,0)
	b\rot = CreateVec3("Basketball Rotation")
	b\collider = CreateSphereCollider(b\pivot,1,0.13)	
	Return b
End Function 

Function UpdateBasketball(b.Basketball)
	AddToVec3 b\vel,b\acc
	MoveEntityVec3 b\pivot,b\vel
	TurnEntityVec3 b\model,b\rot	
	If GetCollisionCount(b\collider) > 0 Then 
		; handle bounce from bottom
		If GetCollisionNX(b\collider,0) > 0 Then 		
			If b\vel\y < 0 Then b\vel\y = -b\vel\y * 0.7	
			; ground friction 
			If b\vel\z <> 0 Then b\vel\z = b\vel\z * 0.9	
			b\rot\x = 0
		End If 
		; handle bounce from top
		If GetCollisionNX(b\collider,0) < 0 Then 		
			If b\vel\y > 0 Then b\vel\y = -b\vel\y * 0.7		
		End If 		
	End If 	
End Function 