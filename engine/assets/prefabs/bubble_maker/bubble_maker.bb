; bubble_maker.bb
; by Chaduke
; 20240703

; a bubble maker prefab

Type BubbleMaker 
	Field model 
	Field pink_bubble_image
	Field blue_bubble_image	
	Field running
End Type 

Function CreateBubbleMaker.BubbleMaker()
	Local b.BubbleMaker = New BubbleMaker 
	Local bm_mesh = LoadMesh("../engine/assets/models/theme_park/bubble_maker.glb")
	SetMeshShadowsEnabled bm_mesh,True
	b\model = CreateModel(bm_mesh)
	
	b\blue_bubble_image = LoadImage("../engine/assets/textures/misc/blue_bubble.png",1)
	b\pink_bubble_image = LoadImage("../engine/assets/textures/misc/pink_bubble.png",1)

	SetImageBlendMode b\blue_bubble_image,3
	SetImageBlendMode b\pink_bubble_image,3
	
	b\running = True 
	Return b	
End Function 

Function UpdateBubbleMaker(b.BubbleMaker)
	If b\running Then 
		If Rnd(1) > 0.5 Then 
			CreateBubble b\blue_bubble_image,b\model
		Else 
			CreateBubble b\pink_bubble_image,b\model
		End If 
	End If	
	UpdateBubbles()
End Function 

Function UpdateBubbleMakers()
	For b.BubbleMaker = Each BubbleMaker
		UpdateBubbleMaker b
	Next 	
End Function 

; bubbles
Type Bubble
	Field sprite
	Field x#,y#,z#
	Field life 
End Type 
	
Function CreateBubble.Bubble(image,e)
	Local b.Bubble = New Bubble
	b\sprite = CreateSprite(image)
	Local sc# = Rnd(0.01,0.1)
	ScaleEntity b\sprite,sc,sc,sc
	SetEntityPosition  b\sprite,GetEntityX(e),GetEntityY(e),GetEntityZ(e)
	b\x = Rnd(-0.02,0.02)
	b\y = Rnd(0.01,0.08)
	b\z = Rnd(-0.02,0.02)
	b\life = Rand(1000,7000)
	Return b
End Function 

Function UpdateBubbles()
	For b.Bubble = Each Bubble
		MoveEntity b\sprite,b\x,b\y,b\z
		b\life = b\life - 1
		If b\life = 0 Then 
			DestroyEntity b\sprite
			Delete b
		End If 	
	Next 		
End Function 