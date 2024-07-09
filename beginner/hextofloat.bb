; hextofloat.bb
; by Chaduke
; 20240513

; converts a 2 character hex value to a 
; float value from 0.0 to 1.0
; useful for color conversion

; tests and useful functions at the bottom

; testhf()

Function hf#(v$)
	; extract the two characters into 
	; separate strings c1$ and c2$
	c1$ = Left(v$,1)
	c2$ = Right(v$,1)
	; convert them to their ascii values
	; store them in a1 and a2 as integers
	a1% = Asc(c1)
	a2% = Asc(c2)
	; find the decimal value of a1
	; store the value in d1
	If (a1 > 47 And a1<58) Then
		d1 = a1 - 48
	Else If (a1 > 64 And a1 < 71) Then 
		d1 = a1 - 65 + 10
	Else If (a1 > 96 And a1 < 103) Then 
		d1 = a1 - 97 + 10
	Else 
		d1 = 0
	End If
	; do the same for a2 and store in d2
	If (a2 > 47 And a2<58) Then
		d2 = a2 - 48
	Else If (a2 > 64 And a2 < 71) Then 
		d2 = a2 - 65 + 10
	Else If (a2 > 96 And a2 < 103) Then 
		d2 = a2 - 97 + 10
	Else 
		d2 = 0
	End If
	; now we need to get the decimal
	; value from 0 - 255	
	d# = d1 * 16 + d2
	; finally map this value to 
	; a float 0.0 to 1.0
	r#  = d# / 255
	Return r
End Function	

Function SetSceneClearColorHex(h$) 
	If Len(h$) = 6 Then 
		a$ = "FF"		
	Else If Len(h$)=8 Then 
		a$ = Right$(h$,2)
	Else 
		Alert "Invalid Hex value supplied to SetSceneClearColorHex" 
		End
	End If
	r$ = Left$(h$,2)
	g$ = Mid$(h$,3,2)
	b$ = Mid$(h$,5,2)
	SetClearColor hf(r$),hf(g$),hf(b$),hf(a$)
End Function 

Function Set2DTextColorHex(h$) 
	If Len(h$) = 6 Then 
		a$ = "FF"		
	Else If Len(h$)=8 Then 
		a$ = Right$(h$,2)
	Else 
		Alert "Invalid Hex value supplied to Set2DTextColorHex" 
		End
	End If
	r$ = Left$(h$,2)
	g$ = Mid$(h$,3,2)
	b$ = Mid$(h$,5,2)
	Set2DTextColor hf(r$),hf(g$),hf(b$),hf(a$)
End Function

Function Set2DFillColorHex(h$) 
	If Len(h$) = 6 Then 
		a$ = "FF"		
	Else If Len(h$)=8 Then 
		a$ = Right$(h$,2)
	Else 
		Alert "Invalid Hex value supplied to Set2DFillColorHex" 
		End
	End If
	r$ = Left$(h$,2)
	g$ = Mid$(h$,3,2)
	b$ = Mid$(h$,5,2)
	Set2DFillColor hf(r$),hf(g$),hf(b$),hf(a$)
End Function

Function Set2DOutlineColorHex(h$) 
	If Len(h$) = 6 Then 
		a$ = "FF"		
	Else If Len(h$)=8 Then 
		a$ = Right$(h$,2)
	Else 
		Alert "Invalid Hex value supplied to Set2DOutlineColorHex" 
		End
	End If
	r$ = Left$(h$,2)
	g$ = Mid$(h$,3,2)
	b$ = Mid$(h$,5,2)
	Set2DOutlineColor hf(r$),hf(g$),hf(b$),hf(a$)
End Function

Function testhf()	
	CreateWindow 640,480,"Test Hex to Float",0	
	Set2DOutlineEnabled True
	Set2DOutlineWidth 5
	Repeat 
		; set background sky blue	
		SetSceneClearColorHex "00bfff" 
		Clear2D() 
		; set text color to bright red
		Set2DTextColorHex "ff0800" 
		Draw2DText "Hex to float is working",10,10 
		; set the fill color to a yellow
		Set2DFillColorHex "F9D71C"
		; set the outline color to orange
		Set2DOutlineColorHex "f09200"
		Draw2DOval 250,100,350,200
		RenderScene() 
		Present() 
	Until PollEvents() = 1
End Function 

; Notes --
; the hexidecimal number system is a way of representing numbers
; with values of 16 in each column rather than 10 like in the normal
; decimal system we use
; counting from 0 to 15 (16 values total including 0) 
; goes like this - 0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F
; hex values are often used in sequences of two characters
; each representing a value of 0 to 255
; 00 in hex represents 0 in decimal
; 0F in hex represents 15 in decimal
; FF in hex represents 255 in decimal
; 80 in hex represents 128 in decimal 

; a color value of Red Green and Blue 
; might look like this C843F7
; C8 is the red value 43 is the green value F7 is the blue value

; The ascii table is a standard representation of 128 character codes
; and it stands for American Standard Code for Information Interchange
; In modern times this system has been replaced by unicode (UTF-8)
; to better represent worldwide language and character sets
; UTF-8 uses the same codes as ascii for the first 128 characters
; to ensure backwards compatibility

; ascii values for numeric characters "0 to 9" run from 48 to 57
; ascii values for uppercase characters "A to F" are 65 to 70
; ascii values for lowercase characters "a to f" are 97 to 102	
	