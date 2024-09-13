; spider.bb
; by Chaduke
; 20240821

Type Spider	
	Field x#,y# ; position		
	Field fire_timer
	Field rocks_hit
	Field last_rocks_hit
	Field total_rocks
End Type

Function CreateSpider.Spider()	
	Local s.Spider = New Spider		
		s\x = GetWindowWidth() / 2
		s\y = 20		
		s\fire_timer = 0
		s\rocks_hit = 0
		s\last_rocks_hit = 0
		s\total_rocks = 0
	Return s
End Function 

Function UpdateSpider(s.Spider)	
	Set2DFillColor 0,0,0,0
	Set2DOutlineColor 1,1,1,1
	Set2DOutlineEnabled True 
	Draw2DOval s\x-10,s\y-10,s\x+10,s\y+10
	Set2DOutlineColor 0,1,0,1
	Draw2DOval s\x-7,s\y-2,s\x-3,s\y+2
	Draw2DOval s\x+7,s\y-2,s\x+3,s\y+2
	If IsKeyDown(65) Then s\x = s\x - 2
	If s\x < 10 Then s\x = 10
	If IsKeyDown(68) Then s\x = s\x + 2	
	If s\x > GetWindowWidth() -10 Then s\x = GetWindowWidth() -10
	If IsKeyHit(264) Then 
		If s\fire_timer < 1 Then s\fire_timer = 5	
	End If
	If s\fire_timer > 0 Then  	
		Set2DLineWidth 0.1
		Draw2DLine s\x-5,s\y,s\x,GetWindowHeight()- 5
		Draw2DLine s\x+5,s\y,s\x,GetWindowHeight() - 5
		s\fire_timer = s\fire_timer - 1
		If s\fire_timer < 1 Then 
			s\total_rocks = s\total_rocks + s\rocks_hit
			s\last_rocks_hit = s\rocks_hit 
			s\rocks_hit = 0
		End If		
	End If 			
End Function 