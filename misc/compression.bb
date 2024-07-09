; compression.bb 
; by Chaduke
; 20240606 

; this is my attempt at a compression alogorithm 

; the first step is to take a message and figure out what numeric value components
; appear in the message, then attach a frequency value to each component
; in the case of a byte encoding it could be values of 0-255
; lets do some testing

Type UniqueByte
	Field val%
	Field count
	Field sort_rank
End Type

; Global msg$ = "For instance, on the planet Earth, man had always assumed that he was more intelligent than dolphins"
; msg$ = msg$ + " because he had achieved so much - the wheel, New York, wars and so on - whilst all the dolphins had ever done"
; msg$ = msg$ +  " was muck about in the water having a good time. But conversely, the dolphins had always believed that they"
; msg$ = msg$ + " were far more intelligent than man - For precisely the same reasons."

log_details = True 
Global fileout = WriteFile("log.txt")

start_time = MilliSecs()
GetUniqueBytes "test"
sort_by_count()
end_time = MilliSecs()
WriteLine fileout,"Job completed in " + (end_time-start_time) + " milliseconds."
CloseFile fileout
End 

Function GetUniqueBytes(msg$)
	; loop thru the message	
	For i = 1 To Len(msg$)
		; get character at current index 
		Local char$=Mid$(msg$,i,1)
		Local byte_val = Asc(char$)
		Local u.UniqueByte = First UniqueByte
		If u = Null Then 
			; create our first 
			n.UniqueByte = New UniqueByte
			n\val = byte_val
			n\count = 1
			n\sort_rank = 0
			If log_details Then WriteLine fileout,"Added our first unique byte - '" + char$ + "'" + " - " + byte_val
		Else				
			; check our list of unique bytes
			loop=True
			While loop 			
				If u\val = byte_val Then 
					u\count = u\count + 1
					loop = False
					If log_details Then WriteLine fileout,"Unique byte '" + char$ + "' " + byte_val + " already exists, incremented count To " + u\count
				Else
					u = After u
					If u = Null Then loop = False
				End If 		
			Wend
			; we didn't find it, add it
			n.UniqueByte = New UniqueByte
			n\val = byte_val
			n\count = 1
			n\sort_rank = 0
			If log_details Then WriteLine fileout,"Added a new unique byte - '" + char$ + "'" + " - " + byte_val
		End If		
	Next			
End Function 

Function PrintStats()
	WriteLine fileout, Chr$(13) + "Message Length " + Len(msg$)
	WriteLine fileout, "Total unique characters counted " + total_chars
	; write out the counts table 
	WriteLine fileout, Chr$(13) + "Counts Table :"
	WriteLine fileout, "Index" + Chr$(9) + "Char" + Chr$(9) + "Count" + Chr$(9) + "Val"
	For i = 0 To total_chars-1
		WriteLine fileout, i + Chr$(9) + Chr$(9) + lookup$(i) + Chr$(9) + Chr$(9) + counts(i) + Chr$(9) + Chr$(9) + Asc(lookup$(i))
	Next 
End Function 

Function init_values()
	WriteLine fileout, "Initializing lookup and counts table..."
	; clear the lookup table 
	For i=0 To 255
		lookup$(i) = ""
		counts(i) = 0
	Next	
	; init the values	
	msg$ = "Four score and seven years ago our fathers brought forth on this continent, a new nation, conceived in Liberty, and dedicated to the proposition that all men are created equal."
	total_chars = 0
	WriteLine fileout, "The message to be encoded is as follows :"
	WriteLine fileout, msg$
End Function 

Function find_total_chars()
	; loop thru the message
	For i = 1 To Len(msg$)	
		char$= Mid$(msg$,i,1) 
		; now we have to check our lookup table to see if the value exists
		If total_chars > 0 Then 
			; check to see if the character exists
			loop = True
			c = 0
			While loop
				If lookup$(c) = char$ Then 
					; it already exists				
					If log_details Then WriteLine fileout,"Character '" + char$ + "' already exists at index " + c + " incrementing to " + (counts(c) + 1)
					; increment the count table 
					counts(c) = counts(c) + 1
					loop = False
				Else 
					; increment c and check the next character
					c = c + 1
					If c > total_chars Then 
						loop = False
						; it doesn't exist so insert it						
						lookup$(total_chars) = char$
						counts(total_chars) = 1	
						total_chars = total_chars + 1
						If log_details Then WriteLine fileout,"Inserted character at index " + total_chars + " - '" + char$ + "' - " + Asc(char$)
					End If							
				End If	; end lookup		
			Wend ; end while loop					
		Else 
			; insert the first character		
			lookup$(0) = char$
			total_chars = total_chars + 1
			counts(0) = 1
			If log_details Then WriteLine fileout,"Inserted the first character at index 0 - '" + char$ + "'" + "' - " + Asc(char$)
		End If ; end total > 0 		
	Next ; end message loop 
End Function 

Function is_sorted(index,sorted_count) 	
	For j = 0 To sorted_count
	 	If sort(j) = index Then Return True 
	Next 
	Return False
End Function 	

Function sort_by_count()
	; my own sorting algorithm
	; I'm going to just try without doing any research
	; first I need to find the amount thats greatest of all 
	; store that index as the first index of the sort table 
	; then find greatest of the remainding values 
	; in order to do that I have to first check that the value doesn't already exist in the sort table
	; very similar to what I've already done
	
	; find the top count value
	current = 0
	While current < total_chars
		top = 0	
		For i = 0 To total_chars - 1
			c = counts(i)
			If (c > top And is_sorted(i,current)=False) Then 
				top=c
				topi=i
			EndIf	
		Next 
		; now insert the new top value into the sort table 
		sort(current) = topi
		WriteLine fileout, "Index : " + current + " Sort index : " + topi + " Value : " + top
		current = current + 1
	Wend 		
End Function 	






