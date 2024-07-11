; disc_golf.bb
; by Chaduke
; 20240615

; includes 
Include "../engine/gameapp.bb"
Include "disc.bb"
Include "player.bb"
Include "course.bb"
Include "basket.bb"
Include "hole.bb"
Include "editor.bb"
Include "../engine/basic_scene.bb"
Include "load_save.bb"
Include "start_menu.bb"
Include "minimap.bb"
Include "display_text.bb" 
Include "ob_area.bb"
Include "story.bb"
Include "tutorial.bb"
Include "options.bb"
Include "../engine/dialogue.bb"

ga.GameApp = CreateGameApp("Disc Golf", "Chaduke's")

music = LoadSound("../engine/assets/audio/disc_golf/disc_golf.mp3")
CueSound music 
PlaySound music 
SetAudioLooping music,True

; MAIN LOOP
While ga\loop 
	BeginFrame ga	
	; create branches based on game state
	Select ga\game_state 
		Case GAME_STATE_START_MENU		
		; main menu		
		If Not ga\quit Then DisplayStartMenu ga
				
		Case GAME_STATE_STORY
		; story mode	
		If Not ga\story_loaded Then
			st.Story = CreateStory(ga)
		Else 
			if not ga\quit then RunStory(st)
		End If
		
		Case GAME_STATE_SANDBOX
		; sandbox mode
		If Not ga\editor_loaded Then
			StopAudio music  
			ed.Editor = CreateEditor(ga)			
			bs.BasicScene = CreateBasicScene(ga,False)
		Else 
			If Not ga\quit Then 
				Local r = UpdateEditorGUIS(bs,ed)
				If Not r Then UpdateEditorNavigation ga,bs
			End If 		
		End If			
		
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