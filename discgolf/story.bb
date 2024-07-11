; story.bb
; by Chaduke 
; 20240708 

; a disc golf story 

Type Story
	Field messages.DialogueMessage[100]
	field message_count%	
End Type

Function CreateStory.Story(g.GameApp)	
	Local s.Story = New Story
	local se.SoundEmitter = CreateSoundEmitter("Story Sounds", "../engine/assets/audio/disc_golf/notes/note","mp3", 26)
	s\messages[0] = CreateDialogueMessage("This is a story about the game of Disc Golf.",se, g\font, 10,10, 1,1,0,1)	
	s\message_count = 1
	g\story_loaded = true 
	Return s 
End Function 

Function RunStory(s.Story)
	DisplayDialogueMessage(s\messages[0])
End Function 

Data "This is a story about the game of Disc Golf."
Data "END"
