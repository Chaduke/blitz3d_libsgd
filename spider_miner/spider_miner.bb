; spider_miner.bb
; by Chaduke
; 20240820

Include "rock.bb"
Include "spider.bb"

Const grid_width = 22
Const grid_height = 45
Const cell_width = 20
Const cell_height = 10

Dim grid(grid_width,grid_height)
For x=0 To grid_width - 1
	For y = 0 To grid_height - 1
		grid(x,y) = Rand(0,4) 
	Next 
Next 

Function DrawGrid()
	Set2DOutlineEnabled False
	offsetx = GetWindowWidth() / 2 - (grid_width * cell_width) / 2
	offsety = GetWindowHeight() / 2 - (grid_height * cell_height) / 2
	For x=0 To grid_width - 1
		For y = 0 To grid_height - 1
			If grid(x,y) > 0 Then 
				Select grid(x,y)
					Case 1
					Set2DFillColor 1,0,0,1
					Case 2
					Set2DFillColor 1,1,0,1
					Case 3 
					Set2DFillColor 0,1,0,1
					Case 4
					Set2DFillColor 0,0,1,1
				End Select
				Draw2DRect x * cell_width - cell_width / 2 + offsetx,y*cell_height-cell_height / 2 + offsety, x * cell_width + cell_width / 2 + offsetx,y*cell_height + cell_height / 2 + offsety
			End If
		Next 
	Next 
End Function 	

CreateWindow(480,560,"Spider Miner",0)
s.Spider = CreateSpider()

loop = True 
interval = 100
counter = interval
While loop
	e = PollEvents()
	If e = 1 Then loop = False
	If IsKeyHit(256) Then loop = False
	RenderScene()
	Clear2D()	
	DrawGrid()	
	UpdateSpider s
	counter = counter - 1
	If counter < 1 Then 
		counter = interval
		; MakeNewLine()
	End If 	
	Present()
Wend
End 
