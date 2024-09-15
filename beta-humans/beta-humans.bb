; beta-humans.bb
; by Chaduke
; 20240809

; create humans and make them walk around and find food

Include "../engine/gameapp.bb"
Include "../engine/gui.bb"
Include "../engine/environment.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/navigation.bb"
Include "human.bb"

ga.GameApp=CreateGameApp("Beta-Humans","Chaduke's",1,True)
e.Environment = CreateEnvironment(ga\gui_font)
LoadEnvironment e,"env.txt"
e\gui\v=False

t.Terrain = New Terrain
SetTerrainDefaults t
CreateTerrain t

ga\info$ = "Basic Construct created..."

SeedRnd MilliSecs()
For i = 0 To 50
	h.Human = CreateHuman()
	PlaceEntityOnTerrain h\pivot,t,1,False,True
	h\destx# = Rnd(t\width)
	h\destz# = Rnd(t\depth)
	EntityFaceLocation(h\pivot,h\destx,h\destz)
Next 	

PlaceEntityOnTerrain ga\pivot,t,1

While ga\loop
	BeginFrame ga
	If IsKeyHit(KEY_F1) Then ShowHideGUIWindow e\gui
	For h.Human= Each Human
		UpdateHuman h,t
	Next
	If (GUIDraggedCheck() = False And WidgetDraggedCheck()=False) Then NavigationMode ga\pivot,t
	UpdateEnvironment e,GUIDragCheck()	: DrawAllGUIs()
	EndFrame ga
Wend 
End 
	



