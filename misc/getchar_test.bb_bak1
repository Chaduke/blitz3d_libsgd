; getchar_test.bb
; by Chaduke
; 20240526

; This is a test of the GetChar() function 
; You can type lines in one by one 
; Enter enters a line and then it's displayed at the bottom
; I also added Backspace 
; hopefully someone finds this useful if you want to add text entry into 
; your program, I'm going to be using this in my level editor
; let me know if I'm missing something or doing something 
; less efficient or correct than it can be 

CreateWindow(1920,1080,"GetChar Test",1)
CreateScene()
loop=True

msg$ = ""
Dim lines$(30)
current_line = 0

While(loop) 
	e = PollEvents()
	k = GetChar()
	If k > 0 Then 
		lastcode = k
		msg$ = msg$ + Chr$(k)
	End If	
	If e=1 Then loop=False ; window close clicked
	If KeyHit(256) Then loop = False ; ESCAPE
	
	If KeyHit(259) Then 
		If Len(msg$) > 0 Then msg$ = Left(msg$,Len(msg$)-1) ; BACKSPACE
	End If
		
	If KeyHit(257) Then 
		lines$(current_line) = msg$
		current_line = current_line + 1
		msg$ = ""
	End If	
	RenderScene()	
	
	; 2D 
 	Clear2D()
	Draw2DText "Result of getchar : " + msg$,10,10
	Draw2DText "Last Code : " + lastcode,10,30
	Draw2DText "Entered Lines : ",10,70
	
	; display lines 
	l = 0
	For y = 100 To 700 Step 20
		Draw2DText lines$(l),10,y
		l = l + 1
	Next		
	
	Present()
Wend
End