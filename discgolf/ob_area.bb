; ob_area.bb
; by Chaduke
; 20240705 

Type OBArea
	Field poly.Polygon		
End Type 

Function CreateOBArea.OBArea()
	Local o.OBArea = New OBArea
	o\poly = CreatePolygon()
	Return o
End Function 


