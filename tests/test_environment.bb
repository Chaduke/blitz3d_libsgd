; test_environment.bb
; by Chaduke 
; 20240719

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/navigation.bb"
Include "../engine/assets/prefabs/bubble_maker/bubble_maker.bb"

g.GameApp = CreateGameApp("Environment Test","Chaduke's",1,False)

e.Environment = CreateEnvironment(g\gui_font)
trn.Terrain = New Terrain
SetTerrainDefaults trn
CreateTerrain trn
PlaceEntityOnTerrain g\pivot,trn,2

b.BubbleMaker = CreateBubbleMaker()
PlaceEntityOnTerrain b\model,trn,0.2
MoveEntity b\model,-2,0,5

tree_mesh = LoadMesh("../engine/assets/models/trees/low_poly.glb")
SetMeshShadowsEnabled tree_mesh,True 
tree_model = CreateModel(tree_mesh)
PlaceEntityOnTerrain tree_model,trn
MoveEntity tree_model,0,0,8

LoadEnvironment e,"env.txt"

While g\loop
	BeginFrame g
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	If (GUIDraggedCheck()=False And WidgetDraggedCheck()=False) Then 
		NavigationMode g\pivot,trn
	End If 
		
	UpdateEnvironment e,GuiDragCheck()
	DrawAllGUIs()
		
	UpdateBubbleMaker b
	EndFrame g
Wend 
End 