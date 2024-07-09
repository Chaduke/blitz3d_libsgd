; display_text.bb
; by Chaduke
; 20240705 

Function DisplayTextDG(g.GameApp,p.Player,d.Disc)
	; display 2D Text				
	If p\windup Then 
		DisplayTextCenter "Power : " + p\power,g\font
		DisplayTextCenter "Release to Throw",g\font,30
	End If 						
	
	Draw2DText "Hole : " + p\current_hole\number,10,10 
	Draw2DText p\current_hole\name$,10,35
	dist# = Distance2D(GetEntityX(d\pivot),GetEntityZ(d\pivot),GetEntityX(p\current_hole\bsk\model),GetEntityZ(p\current_hole\bsk\model))
	Draw2DText "Distance : " + Int(dist) + " of " + Int(p\current_hole\distance) + " meters",10,60
	Draw2DText "" + Int(dist * 3.281) + " of " + Int(p\current_hole\distance * 3.281) + " feet",10,85
	Draw2DText "Par : " + p\current_hole\par,10,110
	Draw2DText "Throw : " + p\throw,10,135
	If d\ob Then Draw2DText "OB",10,160		
	
	DisplayTextRight "Score : " + p\par_score,g\font,10
	DisplayTextRight "Total : " + p\total_throws + " of " + p\current_course\par,g\font,35	
	DrawMinimap g,p\current_hole,d
		
	If p\ready Then 
		DisplayTextCenter "Ready to Throw",g\font,-(g\wh / 2) + 24
		DisplayTextCenter "Disc Angles - Nose : " + Int(GetEntityRX(d\pivot)) + " Tilt : " + Int(GetEntityRZ(d\pivot)),g\font,g\wh / 2 - 30
		Draw2DText "Hold Right Mouse Button to Adjust Angle",5, g\wh - 30			
		DisplayTextRight "Hold Left Mouse Button to Power Up Throw",g\font,g\wh - 30	
	End If
	
	If debug Then 	
		st# = g\wh - 260
		Draw2DText "Lying : " + d\lying,10,10 + st
		Draw2DText "Ready : " + p\ready,10,35 + st
		Draw2DText "Grounded : " + d\grounded,10,60 + st
		Draw2DText "Windup : " + p\windup,10,85+ st
		Draw2DText "Thrown : " + d\thrown,10,110	+ st	
		Draw2DText "Power : " + p\power,10,135 + st
		
		Draw2DText "Disc Location : " + GetEntityX(d\pivot) + "," + GetEntityY(d\pivot) + "," + GetEntityZ(d\pivot),10,160 + st
		Draw2DText "Terrain Height : " + GetTerrainHeight(GetEntityX(d\pivot),GetEntityZ(d\pivot),p\current_hole\trn),10,185 + st
		Draw2DText "Basket Y : " + GetEntityY(p\current_hole\bsk\model),10,210+st
		Draw2DText "FPS : " + GetFPS(),10,235 + st				
	End If	
End Function 	