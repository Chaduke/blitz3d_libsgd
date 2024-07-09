; minimap.bb
; by Chaduke 
; 20240705

Function DrawMinimap(g.GameApp,h.Hole,d.Disc,yoffset=80,xoffset=10)
	
	; draw the course rectangle
	Set2DFillColor 0.1,0.7,0.2,1	
	Draw2DRect GetWindowWidth() - h\trn\width - xoffset,yoffset,GetWindowWidth()-xoffset,yoffset + h\trn\depth
	
	; draw the disc location 
	Set2DFillColor 1,0,1,1
	Local dx = GetWindowWidth() - h\trn\width - xoffset + GetEntityX(d\pivot)
	Local dy = yoffset + h\trn\depth - GetEntityZ(d\pivot) 	
	Draw2DOval dx-3,dy-3,dx+3,dy+3
	
	; draw a line indicating the player direction 
	Draw2DLine dx,dy,dx + Sin(GetEntityRY(g\pivot)-180) * 6,dy + Cos(GetEntityRY(g\pivot)-180) * 6

	; draw the basket location 
	Set2DFillColor 1,0,0,1
	Local bx = GetWindowWidth() - h\trn\width - xoffset + GetEntityX(h\bsk\model)	
	Local by = yoffset + h\trn\depth - GetEntityZ(h\bsk\model)
	Draw2DOval bx-3,by-3,bx+3,by+3
End Function 	 