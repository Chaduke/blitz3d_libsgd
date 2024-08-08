; sound.bb
; by Chaduke
; 20240630

Type SoundEmitter
	Field name$
	Field sounds[30]
	Field sound_count
	Field playing 
	Field rest# 
	Field startms
	Field offset#
	Field model 
End Type 

Function CreateSoundEmitter.SoundEmitter(name$,path$,ext$,count)
	Local se.SoundEmitter = New SoundEmitter
	se\name$ = name$ 
	se\sound_count = count
	Local i
	For i = 0 To se\sound_count-1
		Local fullpath$ = path$ + (i+1) + "." + ext$
		se\sounds[i] = LoadSound(fullpath$)
	Next 	
	; se\model = CreateModel(0)
	Return se 	
End Function 

Function AdjustVolumeByDistance(s.SoundEmitter,e) 
	Local d# = Distance3D(GetEntityX(s\model), GetEntityY(s\model),GetEntityZ(s\model),GetEntityX(e),GetEntityY(e),GetEntityZ(e))	
	Local v# = 1.0
	If (d > 0 And d < 10) Then 
		v# = 1 - (d/10)		
	Else 
		v# = 0.0
	End If 
	Local i	
	SetAudioVolume s\playing,v#
		
End Function 

Function PlayRandomSound(s.SoundEmitter)
	timeleft = MilliSecs() - s\startms
	If timeleft > s\rest Then 
		Local i = Floor(noise(777+s\offset) * (s\sound_count-1))
		Local p = s\sounds[i]	
		PlaySound p		
		s\rest = noise(777+s\offset) * 1000
		s\startms = MilliSecs()
		s\offset =  s\offset + 0.02
	End If 		
	Draw2DText s\rest, 10,10
	Draw2DText timeleft,10,30
End Function 

Function PlaySoundByLetter(s.SoundEmitter,l$)
	; the goal here is to look at the 26 letters of the alphabet
	; then look at the frequency in which they appear in normal 
	; english sentences.  Then we have a sequence of 26 musical notes 
	; ordered by the frequency in which we deem them common 
	; and we will map these notes to the letters 

	; letters by commonality 
	Local common$ = "eariotnslcudpmhgbfywkvxzjq"
	
	; find out how common l$ is 
	l$ = Lower(l$)
	Local c = Instr(common$,l$)
	
	; convert to the note commonality
	Local i = 0
	If c>0 Then 
		If c = 1 Then i = 13 ; root note
		If c = 2 Then i = 26 ; high octave
		If c = 3 Then i = 25 ; highest fifth
		If c = 4 Then i = 1 ; low octave
		If c = 5 Then i = 20 ; high fifth
		If c = 6 Then i = 8 ; low fifth 
		If c = 7 Then i = 22 ; 6th high
		If c = 8 Then i = 16 ; minor third high
		If c = 9 Then i = 4 ; minor third low
		If c = 10 Then i = 17 ; major third high
		If c = 11 Then i = 5 ; major third low
		If c = 12 Then i = 18 ; fourth high
		If c = 13 Then i = 6 ; fourth low
		If c = 14 Then i = 12 ; maj 7th low
		If c = 15 Then i = 24 ; maj 7th high
		If c = 16 Then i = 15 ; maj 2nd high
		If c = 17 Then i = 3 ; maj 2nd low 
		If c = 18 Then i = 23 ; flat 7th high
		If c = 19 Then i = 11 ; flat 7th low
		If c = 20 Then i = 19 ; tritone high
		If c = 21 Then i = 7 ; tritone low 
		If c = 22 Then i = 9 ; flat 6th low
		If c = 23 Then i = 10  ; 6th low 
		If c = 24 Then i = 21 ; flat 6
		If c = 25 Then i = 14 ; minor 2nd high
		If c = 26 Then i = 	2  ; minor 2nd low
		
		; play the note according to where it's found 
		If i > 0 Then PlaySound s\sounds[i-1]
	End If
	
End Function 