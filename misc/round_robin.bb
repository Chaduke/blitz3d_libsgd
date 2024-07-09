; round_robin.bb 
; by Chaduke 
; 20240628 

; a coding experiment to examine how Blitz3D uses custom types 


Type fruit 
	Field name$ 
End Type 

Function Display()
	Local num = 1
	Local msg$ = ""
	For f.fruit = Each fruit 
		msg$ = msg$ + num + " : " + f\name$ + Chr$(13)
		num = num + 1 
	Next 
	Alert msg$ 
End Function

f.fruit = New fruit 
f\name$ = "Apple" 
current.fruit = f

f2.fruit = New fruit 
f2\name$ = "Orange"
current = After f
Delete current 
current = First fruit 

Display()

Alert current\name$

 
