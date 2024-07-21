; tests.bb
; by Chaduke
; 20240703 

; a test runner for all the engine functionality

Include "../engine/testapp.bb"
Include "../engine/assets/prefabs/ferris_wheel/ferris_wheel.bb"
Include "../engine/assets/prefabs/bubble_maker/bubble_maker.bb"	
Include "particle.bb"

ta.TestApp = CreateTestApp(True)
trn.Terrain = New Terrain 
SetTerrainDefaults trn
CreateTerrain trn

test_particles = True 
test_ferris_wheel = True 

If test_ferris_wheel Then
	fw.FerrisWheel = CreateFerrisWheel()
	PlaceEntityOnTerrain fw\base_pivot,trn,22.5
	AimEntityAtEntity ta\pivot,fw\wheel_pivot,0
	bm.BubbleMaker = CreateBubbleMaker()
	bm2.BubbleMaker = CreateBubbleMaker()
	bm3.BubbleMaker = CreateBubbleMaker()
	
	PlaceEntityOnTerrain bm3\model,trn,0.1,False,True 
	
	BoardPassenger fw,bm\model,Rand(0,15)
	BoardPassenger fw,bm2\model,Rand(0,15)
	
	boarded = False 
End If 

If test_particles Then 
	particle_image = LoadImage("../engine/assets/textures/misc/raindrop2.png",1)	
	SetImageBlendMode particle_image,3
	SetImageSpriteViewMode particle_image,1
End If 

While ta\loop 
	BeginFrame ta
	UnrealMouseInput ta\pivot
	KeyboardInputFirstPerson ta\pivot	
	KeepEntityAboveTerrain ta\pivot,trn
	
	; ferris wheel and bubble maker
	If test_ferris_wheel Then 
		If (IsKeyHit(KEY_SPACE)) Then 
			If boarded = True Then 
				boarded = False 
				RemovePassenger fw,ta\pivot,trn 
			Else 
				boarded = True 
				BoardPassenger fw,ta\pivot,Rand(0,15)
			End If
		End If 	 			
		
		UpdateFerrisWheel fw
		
		UpdateBubbleMakers()
		UpdateBubbles()
	End If 	
	
	; basic particles 	
	If test_particles Then 
		For i = 0 To 5
			CreateParticle particle_image
		Next 	
		UpdateParticles()
	End If 		
	EndFrame ta
	Present()
Wend
End 