; destroy_entity_bug.bb
; by Chaduke
; 20240718

Const KEY_ESCAPE = 256
Const KEY_SPACE = 32

Type Test 
	Field model 
End Type 

CreateWindow 1280,720,"Keyhit Bug",0

num = 5
For x = 1 To num
	t.Test = New Test
	t\model = CreateModel(0)
Next 
		
While (IsKeyHit(KEY_ESCAPE) = False And PollEvents() <> 1) 	

	count = 0 
	For t.Test = Each Test 
		count = count + 1
	Next 
		
	If IsKeyDown(KEY_SPACE) Then 
		If Not SpaceDown Then 
			SpaceDown = True 
			t = First Test 
			DestroyEntity t\model ; if you don't check for Null here and you're not in Debug mode it just crashes, no error
			Delete t
		End If 
	Else 
		SpaceDown = False 
	End If 
				
	RenderScene()
	Clear2D()
	Draw2DText "Count : " + count,5,5
	Present()
Wend 
End 