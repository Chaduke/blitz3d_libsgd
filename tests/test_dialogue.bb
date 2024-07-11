; test_dialogue.bb
; by Chaduke 
; 20240709 

Include "../engine/testapp.bb" : Dim messages.DialogueMessage(2) : TestDialogue()
Function TestDialogue()
	ta.TestApp = CreateTestApp(True)	
	Local se.SoundEmitter = CreateSoundEmitter("Common Notes","../engine/assets/audio/disc_golf/notes/note","mp3",26)	
	Local loop = True 
	Local i = 0
	While 	loop 
		Read msg$ 
		If msg$="END" Then 
			loop = False
		Else 
			Local x = (GetWindowWidth() / 2) - (GetTextWidth(ta\font,msg$) / 2)
			Local y = GetWindowHeight() / 2
			messages(i) = CreateDialogueMessage(msg$,se,ta\font,x,y)
			i = i + 1
		End If 				
	Wend 
	i = 0
	While ta\loop 
		BeginFrame ta
		If Not ta\quit Then DisplayDialogueMessage messages(i)
		If IsMouseButtonHit(0) Then 
			messages(i)\msg_index_float = 0
			i = i + 1
			If i>2 Then i = 0
		End If 	
		EndFrame ta
		Present()
	Wend 
	End 
End Function 

Function TestRandom()
	ta.TestApp = CreateTestApp(True)	
	Local se.SoundEmitter = CreateSoundEmitter("Common Notes","../engine/assets/audio/disc_golf/notes/note","mp3",26)	
	
	While ta\loop 
		BeginFrame ta
		If Not ta\quit Then PlayRandomSound se		
		EndFrame ta
		Present()
	Wend 
	End
End Function 


Data "This is a test of dialogue messages"
Data "This can be used to create a story"
Data "This will be the last message"
Data "END"
