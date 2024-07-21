; basket.bb
; by Chaduke 
; 20240704

Type Basket	
	Field model
	Field base.Cylinder
	Field cage.Cylinder
	Field toprim.Cylinder
	Field bottomrim.Cylinder
	Field se.SoundEmitter	
End Type 

Function CreateBasket.Basket(mesh,t.Terrain,x#,z#)
	Local b.Basket = New Basket
	b\model = CreateModel(mesh)
	PlaceEntityOnTerrain b\model,t,0,False,False,x,z
	; create a collision cylinder for basket
	b\cage = CreateCylinder(1.5,0.5)
	b\base = CreateCylinder(1.5,0.1)
	b\toprim = CreateCylinder(0.1,0.5)
	b\bottomrim = CreateCylinder(0.1,0.5)	
	b\se = CreateSoundEmitter("Basket Sounds","../engine/assets/audio/disc_golf/basket/basket","wav",2)
	Return b
End Function 