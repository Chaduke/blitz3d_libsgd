; course.bb 
; by Chaduke
; 20240617 

Type Course
	Field id ; maybe for loading / saving
	Field name$
	Field holes.Hole[50]
	Field hole_count	
	Field par	
End Type 

Function CreateCourse.Course(name$,hole_count)
	Local c.Course = New Course
	c\name$ = name$ 
	c\hole_count = hole_count
	For i = 0 To hole_count 
		Local p = Rand(3,5)
		c\holes[i] = CreateHole("Hole " + i,i,p)
		c\par = c\par + p
	Next	
	Return c
End Function 


	
					
					
					