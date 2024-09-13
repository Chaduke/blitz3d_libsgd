; test_water.bb
; by Chaduke
; 20240808

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/trees.bb"
Include "../engine/navigation.bb"
Include "water.bb"

ga.GameApp = CreateGameApp("Water Test","Chaduke's",1,False)
e.Environment = CreateEnvironment(ga\gui_font)
w.Water = CreateWater()
trn.Terrain = New Terrain
SetTerrainDefaults trn
CreateTerrain trn
GenerateTrees trn,300
PlaceEntityOnTerrain ga\pivot,trn,1
MoveEntity w\model,trn\width / 2, 3,trn\depth / 2
ScaleEntity w\model,trn\width * 3,1,trn\depth * 3

While ga\loop
	BeginFrame ga
	UpdateWater w
	If (GUIDraggedCheck() = False And WidgetDraggedCheck()=False) Then 
		NavigationMode ga\pivot,trn
	End If 
	UpdateEnvironment e,GUIDragCheck()	
	DrawAllGUIs()
	EndFrame ga
Wend 
End 	


