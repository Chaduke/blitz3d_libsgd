; hole.bb
; by Chaduke
; 20240704 

; even though there's no actual "hole" in Disc Golf 
; we still use the term carried over from "ball golf"

Type Hole 
	Field name$
	Field number
	Field trn.Terrain
	Field teepad ; static model 
	Field bsk.Basket ; possible custom type 
	Field ob_areas.OBArea[4] ; out of bounds areas 
	Field par
	Field distance#			
	Field ob_padding
	Field dropzone
End Type 

Function LoadHole.Hole(path$)
	Local h.Hole = New Hole
	
	Return h	
End Function 

Function CreateHole.Hole(name$,number,par)
	Local h.Hole = New Hole
	h\trn = New Terrain
	SetTerrainDefaults h\trn
	CreateTerrain(h\trn)	
	h\teepad=LoadModel("../engine/assets/models/disc_golf/teepad.glb")	
	Local basket_mesh=LoadMesh("../engine/assets/models/disc_golf/basket.glb") 
	h\bsk = CreateBasket(basket_mesh,h\trn,20,20)
	For i=0 To 4
		h\ob_areas[i] = CreateOBArea()
	Next 
	h\par = par
	h\distance = GetEntityDistanceFlat#(h\teepad,h\bsk\model)
	Return h	
End Function