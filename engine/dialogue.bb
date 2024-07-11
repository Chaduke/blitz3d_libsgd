; dialogue.bb
; by Chaduke
; 20240524

; my first attempt at some sort of dialogue system 

; in the beginning I'd like it to mimic retro adventure games
; similar to SNES / N64 era rpgs 
; although a lot of modern games still use this style
; where the text loads across the screen like its being typed
; and makes a slight sound with each key as to mimic a typewriter
; I think I can make use of the Data and Read system for this
; I'm thinking different codes in the text could be used 
; to trigger events, animations, even change the text color and fonts
; the dialogue could have almost a scripting language built into it

Type DialogueMessage
	Field msg$
	Field last_index
	Field msg_index_float#
	Field sound.SoundEmitter
	Field font
	Field xpos 
	Field ypos 	
	Field r#,g#,b#,a#
End Type 

Function CreateDialogueMessage.DialogueMessage(msg$="This is a test message!!",sound.SoundEmitter,font=0,xpos=5,ypos=5,r#=1,g#=0.8,b#=0,a#=1)
	Local dm.DialogueMessage = New DialogueMessage
	dm\msg$ = msg$
	dm\sound = sound 			
	If font = 0 Then 
		dm\font = LoadFont("../engine/assets/fonts/bb.ttf",24)
	Else 
		dm\font = font
	End If 
			
	dm\xpos = xpos
	dm\ypos = ypos
	dm\r = r : dm\g = g : dm\b = b : dm\a = a
	Return dm
End Function 

Function DisplayDialogueMessage(d.DialogueMessage)
	Local msglen = Len(d\msg$)
	Local output$
	
	If d\msg_index_float# > msglen Then 
		output$ = d\msg$ 
	Else 
		d\last_index = Floor(d\msg_index_float#)
		output$ = Left$(d\msg$,d\last_index)
		d\msg_index_float# = d\msg_index_float# + Rnd(1) * 0.6
	End If		
	Set2DTextColor d\r,d\g,d\b,d\a
	Set2DFont d\font
	Draw2DText output$,d\xpos,d\ypos
	If (Floor(d\msg_index_float#) > d\last_index And d\msg_index_float# < msglen) Then PlaySoundByLetter(d\sound,Mid$(d\msg$,Floor(d\msg_index_float#),1))
End Function 

Function GetDialogueMessage() 
	k = GetChar()
	If k > 0 Then
		msg$ = msg$ + Chr$(k)
		PlaySound blip_sound	
	End If	
	If IsKeyHit(259) Then 
		If Len(msg$) > 0 Then 
			msg$ = Left(msg$,Len(msg$)-1) ; BACKSPACE
			PlaySound blip_sound
		End If	
	End If	
End Function

Function EditDialogue()
	GetDialogueMessage()
	Set2DTextColor 0,0,1,1
	Draw2DText msg$,10,10
End Function 