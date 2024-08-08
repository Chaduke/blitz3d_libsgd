; load_save.bb
; by Chaduke 
; 20240625

; functions to load and save different parts of a scene 

Function SaveTerrain(file$)
	fileout = WriteFile(scene_path$ + "/" + file$)
	For w.Widget = Each Widget 
		If w\gui_id = 2 Then 
			If w\widget_type = 0 Then 
				WriteFloat fileout,w\val_float#
			ElseIf w\widget_type = 1 Then
				WriteInt fileout,Int(w\val_float#)
			ElseIf w\widget_type = 2 Then 
				WriteString fileout,w\val_string$
			End If 
		End If 	
	Next 	
	CloseFile fileout
End Function 

Function LoadTerrain(file$)
	filein = ReadFile(scene_path$ + "/" + file$)
	For w.Widget = Each Widget 
		If w\gui_id = 2 Then 
			If w\widget_type = 0 Then 
				w\val_float#=ReadFloat(filein)
			ElseIf w\widget_type = 1 Then
				w\val_float#=ReadInt(filein)
			ElseIf w\widget_type = 2 Then 
				w\val_string$=ReadString(filein)
			End If 
		End If 	
	Next 	
	CloseFile filein
End Function 

Function SaveSolidObjects()

End Function 

Function LoadSolidObjects()

End Function 

Function SaveLighting()

End Function 

Function LoadLighting()

End Function 

Function SaveNPCs()

End Function 

Function LoadNPCs()

End Function 