; attributes.bb
; by Chaduke
; 20240526 

; functionality to setup / edit different values that exist at runtime
; at the start of the program we'll load up the attributes into memory
; with the default values listed in the Data statements at the bottom
; the system works with an "attribute" type data structure
; one of them is a Global that represents the current attribute being edited 
; if current_attribute\number = 0 (the last attribute in the data list)
; then that just means we're not currrently editing an attribute
; and our program can flow normally

Global current_attribute.attribute
Global characters$ = ""

Type attribute
	Field number ; the attribute number from the data list at the bottom of this file
	Field name$ ; the string value name of the attribute for displaying on the screen
	Field defaultval# ; the default value loaded at the start of runtime
	Field currentval# ; the current value of the attribute, right now only supporting floats
	Field prefunc ; the function that is run while the atrtibute is being edited, displays info on the screen 
	Field postfunc ; the function that is run after the attribute value has been changed by pressing ENTER 
	Field postmsg$ ; a description of what the post function does so the user can know what to expect
End Type	

Function CreateAttributes() ; called at the start of runtime 
	Restore attributes	; just to make sure we're in the right place
	Local readdata = True
	While readdata	
		a.attribute = New attribute	
		Read a\number,a\name$,a\defaultval#,a\prefunc,a\postfunc,a\postmsg$
		a\currentval = a\defaultval		
		If a\number = 0 Then
			readdata = False
			current_attribute = a			
		End If	
	Wend 		
End Function 

; normally used to set current_attribute like this
; current_attribute = GetAttribute([number])
; once we're done editing an attribute or 
; editing is canceled we'd call this 
; current_attribute = GetAttribute(0)
Function GetAttribute.attribute(number)
	Local a.attribute
	a = First attribute
	While a\number <> number 
		a = After a
	Wend 	
	Return a
End Function 

Function GetAttributeByName.attribute(name$)
	Local a.attribute
	a = First attribute
	While a\name$ <> name$
		a = After a
	Wend 	
	Return a
End Function 

Function DisplayTextCenter(msg$,offsetY#)
	Local centerX# = WindowWidth()/2
	Local centerY# = WindowHeight()/2
	Local mhl# = FontTextWidth(bradybunch,msg$) / 2 ; message half width
	Set2DTextColor 1,1,1,1
	Draw2DText msg$,centerX#-mhl#,centerY# + offsetY
End Function 

Function GetValue()
	k = GetChar()
	If k > 0 Then 		
		characters$ = characters$ + Chr$(k)
	End If	
		
	If KeyHit(259) Then characters$ = Left(characters$,Len(characters$)-1) ; BACKSPACE
	If KeyHit(257) Then ; ENTER		
		; validate entry
		current_attribute\currentval# = Float(characters$) 	
		characters$=""			
		; call postfunc		
		CallPostEdit(current_attribute)	
		current_attribute = GetAttribute(0)	
	End If	
End Function 

Function GetMaterial(n)	
	Restore materials
	Local readdata = True
	While readdata		
		Read number,name$		
		If (number = n  Or number = 0) Then
			readdata = False				
		End If	
	Wend 
	If number = 0 Then 
		Alert "Could not find requested material number"
		Return 0
	End If	
	Local path$ = "assets/materials/" + name$ + "_1K-JPG"
	Local r = LoadPBRMaterial(path$)
	Return r
End Function 		

Function GetMaterialByName(n$)	
	Restore materials
	Local readdata = True
	While readdata		
		Read number,name$		
		If (name$="END" Or name$=n$) Then
			readdata = False				
		End If	
	Wend 
	If name$="END" Then 
		Alert "Could not find requested material name"
		Return 0
	End If	
	Local path$ = "assets/materials/" + name$ + "_1K-JPG"
	Local r = LoadPBRMaterial(path$)
	Return r
End Function

Function EditAttribute(a)
	If current_attribute\number <> a Then 
		; we need to select the attribute		
		current_attribute = GetAttribute(a)		
	End If		
	CallPreEdit(current_attribute)	
End Function 

Function CallPreEdit(a.attribute)
	Select a\prefunc
		Case 1 ; Edit Float
			EditFloat(a)
	End Select 
End Function 

Function CallPostEdit(a.attribute)
	Select a\postfunc
		Case 1			
			GenerateTerrain()
			CenterPlayerOnTerrain(playerone)	
		Case 2
			; do nothing 		
	End Select 
End Function 

; PRE-EDIT functions
; 1 - Edit Float Value
Function EditFloat(a.attribute)
	DisplayTextCenter "You are currently editing a float value for attribute " + a\name$,-50
	DisplayTextCenter "The current value is " + a\currentval,-30	
	DisplayTextCenter "Enter the new value and press ENTER, or ESCAPE to cancel",-10
	DisplayTextCenter "Value : " + characters$,10
	DisplayTextCenter "After changing this value the following will occur : " + a\postmsg$,40
	GetValue()
End Function 

; POST-EDIT functions 
; 1 - GenerateTerain() found in terrain.bb

; attributes 
; number, name, default value, pre-edit function, post-edit function, post-edit message

.attributes 
Data 1,"Terrain Width",512,1,1,"Scene will be regenerated, avoid values > 512"
Data 2,"Terrain Depth",512,1,1,"Scene will be regenerated, avoid values > 64, never over 255"
Data 3,"Terrain Height",32,1,1, "Scene will be regenerated, avoid values > 512" 
Data 4,"Perlin Offset",0.02,1,1,"Scene will be regenerated, lower values result in smoother terrain, keep between 0.01 and 0.05 for best results"
Data 5,"Terrain Material",1,1,1,"Scene will be regenerated"
Data 6,"Sun Angle",-30,1,1,"Scene will be regenerated"
Data 7,"Create Trees", False,1,1,"Scene will be regenerated"
Data 8,"Number of Trees", False,1,1,"Scene will be regenerated"
Data 9,"Create Foliage", False,1,1,"Scene will be regenerated"
Data 10,"Number of Foliage", False,1,1,"Scene will be regenerated"
Data 11, "Create Rock Wall", False,1,1,"Scene will be regenerated"
Data 12,"Create Platforms", False,1,1,"Scene will be regenerated"
Data 13,"Number of Platforms", False,1,1,"Scene will be regenerated"
Data 14,"Add NPCs", False,1,1,"Scene will be regenerated"
Data 15,"Default Billboard Image",1,1,2,"Value will be changed"
Data 0,"END",0,0,0,"END"

.materials 
Data 1,"Grass001"
Data 2,"Concrete031"
Data 3,"Planks037B"
Data 4,"Tiles074"
Data 5,"Travertine013"
Data 6,"Ground075"
Data 7,"Grass004"
Data 8,"Ground037"
Data 9,"Ground020"
Data 10,"Ground054"
Data 0,"END"