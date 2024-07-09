; performance_testing.bb
; by Chaduke 
; 20240605 

; my attempt here is to take a function that I use in the platformer demo
; something that gets called every frame, then test it a number of times 
; log the millisecs, then call an equivalent function created in a userlib dll
; and test it the same way, seeing if therein lies a significant difference

Function Distance2D#(x1#,y1#,x2#,y2#)
	dx# = x2-x1
	dy# = y2-y1
	Return Sqr(dx * dx + dy * dy)
End Function 

Function CirclesCollidedInternal(x1#,y1#,r1#,x2#,y2#,r2#) 
	If Distance2D(x1,y1,x2,y2) < r1 + r2 Then 
		Return True 
	Else
		Return False
	End If				
End Function 

start_time  = MilliSecs()
For i = 0 To 1000000
	test = CirclesCollided(10,10,10,30,10,9)
Next 
end_time = MilliSecs()

msg$ = "Start time : " + start_time + Chr$(13)
msg$ = msg$ + "End Time " + end_time + Chr$(13) 
msg$ = msg$ +  "Difference : " + (end_time - start_time)
Alert msg$