; disc_golf.bb
; by Chaduke
; 20240615

; includes 
Include "../engine/gameapp.bb"
Include "../engine/navigation.bb"
Include "../engine/gui.bb"
Include "../engine/sound.bb"
Include "../engine/noise.bb"
Include "../engine/terrain.bb"
Include "../engine/dialogue.bb"
Include "../engine/file_browser.bb"
Include "../engine/environment.bb"
Include "../engine/trees.bb"
Include "disc.bb"
Include "player.bb"
Include "course.bb"
Include "basket.bb"
Include "hole.bb"
Include "load_save.bb"
Include "minimap.bb"
Include "display_text.bb" 
Include "ob_area.bb"
Include "story.bb"
Include "tutorial.bb"
Include "options.bb"

ga.GameApp = CreateGameApp("Disc Golf", "Chaduke's",256)

music = LoadSound("../engine/assets/audio/disc_golf/disc_golf.mp3")
CueSound music 
; PlaySound music 
; SetAudioLooping music,True

debug = True 

; MAIN LOOP
While ga\loop 
	BeginFrame ga	
	; create branches based on game state
	Select ga\game_state 
		Case GAME_STATE_START_MENU		
		; main menu		
		DisplayStartMenu ga				
		Case GAME_STATE_STORY
		; story mode	
		If Not ga\story_loaded Then			
			st.Story = CreateStory(ga)			
			p.Player = CreatePlayer()
			d.Disc = CreateDisc("Sidewinder","../engine/assets/models/disc_golf/disc.glb",9,5,-3,1)
			h.Hole = CreateHole("Hole 1",1,3)
			SetEntityPosition d\pivot,GetEntityX(h\teepad),GetEntityY(h\teepad) + p\release_height#,GetEntityZ(h\teepad)		
		Else 			
			RunStory(st)
			UpdatePlayer p,d,h,ga
			UpdateDisc d,h,p			
		End If
		
		Case GAME_STATE_SANDBOX
		; sandbox mode	
		
		Case GAME_STATE_TUTORIAL
		; tutorial
		If Not ga\tutorial_loaded Then 
			t.Tutorial = CreateTutorial()
		Else 
			RunTutorial t
		End If 		
				
		Case GAME_STATE_OPTIONS	
		; options
		If Not ga\options_loaded Then 
			o.Options = CreateOptions()
		Else 
			RunOptions o
		End If 		
	End Select 	
	EndFrame ga	
Wend 
End 