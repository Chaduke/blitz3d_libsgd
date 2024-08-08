; hole.bb
; by Chaduke
; 20240704 

; even though there's no actual "hole" in Disc Golf 
; we still use the term carried over from "ball golf"

Type Hole 
	Field name$
	Field number
	Field env.Environment 
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
	Local font = LoadFont("../engine/assets/fonts/ds.ttf",16)
	h\env = CreateEnvironment(font)
	h\trn = New Terrain
	SetTerrainDefaults h\trn
	CreateTerrain h\trn	
	h\teepad=LoadModel("../engine/assets/models/disc_golf/teepad.glb")	
	PlaceEntityOnTerrain h\teepad,h\trn,0,False,False,h\trn\width / 2,5
	Local basket_mesh=LoadMesh("../engine/assets/models/disc_golf/basket.glb") 
	h\bsk = CreateBasket(basket_mesh,h\trn,20,20)
	For i=0 To 4
		h\ob_areas[i] = CreateOBArea()
	Next 
	h\par = par
	h\distance = GetEntityDistanceFlat#(h\teepad,h\bsk\model)
	Return h	
End Function