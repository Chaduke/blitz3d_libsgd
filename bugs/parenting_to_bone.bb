CreateWindow 1280,720,"Parenting to bone",0

player = LoadBonedModel("sgd://models/base_male_animated.glb",True)
armature = GetEntityChild(player,0)
hips = GetEntityChild(armature,1)
spine = GetEntityChild(hips,0)
spine1 = GetEntityChild(spine,0)
spine2 = GetEntityChild(spine1,0)
right_shoulder = GetEntityChild(spine2,2)
right_arm = GetEntityChild(right_shoulder,0)
right_forearm = GetEntityChild(right_arm,0)
right_hand = GetEntityChild(right_forearm,0)
found_hand = FindEntityChild(right_shoulder,"mixamorig:RightHand")

Local time# 
camera = CreatePerspectiveCamera()
MoveEntity camera,0,1,-4
light = CreatePointLight()
MoveEntity light,0,2,-1

disc_material = CreatePBRMaterial()
disc_mesh = CreateCylinderMesh(0.02,0.12,16,disc_material)
disc = CreateModel(disc_mesh)
MoveEntity disc,-0.4,0.7,0
TurnEntity disc,90,0,0
While Not (PollEvents() And 1)	
	If IsKeyHit(32) Then 
		SetEntityParent disc,right_hand
		ScaleEntity disc,100,100,100
	End If 
	If IsKeyHit(70) Then SetEntityParent disc,found_hand
	time = time + 0.02
	AnimateModel player,1,time,2,1.0
	TurnEntity disc,0,1,1
	RenderScene()
	Clear2D()
	Draw2DText "Player xyz : " + GetEntityX(player) + "," + GetEntityY(player) + "," + GetEntityZ(player),5,5
	Draw2DText "Disc xyz : " + GetEntityX(disc) + "," + GetEntityY(disc) + "," + GetEntityZ(disc),5,25
	Draw2DText "Disc scale : " + GetEntitySX(disc) + "," + GetEntitySY(disc) + "," + GetEntitySZ(disc),5,125

	Draw2DText "Entity name of right hand bone : " + GetEntityName(right_hand),5,45
	; Draw2DText "Entity name of found bone : " + GetEntityName(found_hand),5,65
	Draw2DText "Press SPACE to parent disc to player's right hand",5,85
	Draw2DText "Press F to parent disc to found hand and crash",5,105
	Present()
Wend 
End 