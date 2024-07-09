; test_editor.bb

Function TestEditor() 
	ta.TestApp = CreateTestApp(True,"Scene Editor")		
	ta\bs = CreateBasicScene(True)
	ta\ed = CreateEditor(ta)		
			
	; ta\ed\current_actor = First Actor
	; ta\bs\camera_mode = 1
	; SetMouseZ -25
	; s.SoundEmitter = CreateSoundEmitter("Test Emitter","../engine/assets/audio/misc/test_emitter/sound","mp3")
	
	While ta\loop = True 
		BeginFrame ta
		
		If IsKeyHit(KEY_L) Then LoadOnStart ta,True
		; LoadOnStart ta,True
		; show/hide guis
		If IsKeyHit(KEY_F1) Then ShowHideGUIWindow ta\ed\environment_settings
		If IsKeyHit(KEY_F2) Then ShowHideGUIWindow ta\ed\terrain_settings
		If IsKeyHit(KEY_F3) Then ShowHideGUIWindow ta\ed\outliner	
		If IsKeyHit(KEY_F4) Then ShowHideGUIWindow ta\ed\actor_settings		
		If IsKeyHit(KEY_F12) Then If ta\help_visible = True Then ta\help_visible = False Else ta\help_visible = True 	
		If IsKeyDown(32) Then 
			If spacedown = False Then 
				spacedown = True 
				If ta\bs\camera_mode = 1 Then 
					ta\bs\camera_mode = 0
					SetEntityPosition ta\bs\camera,0,0,0	
					MoveEntity ta\bs\pivot,0,0,-5					
				Else 					
					ta\bs\camera_mode = 1
				End If 
			End If 						
		Else 
			spacedown = False
		End If 	 
			
		If (ta\ed\btn_trn_generate\clicked And ta\ed\btn_trn_generate\click_handled = False) Then 
			If ta\bs\trn <> Null DestroyEntity ta\bs\trn\model											
			ta\bs\trn = New Terrain
			ta\bs\trn\material_path$ = ta\ed\materials_path$ + "/" + ta\ed\btn_trn_material\val_string$			
			ta\bs\trn\width# = ta\ed\txt_trn_width\val_float#
			ta\bs\trn\height# = ta\ed\txt_trn_height\val_float#
			ta\bs\trn\depth# = ta\ed\txt_trn_depth\val_float#
			ta\bs\trn\start_offset# = ta\ed\txt_trn_start_offset\val_float#
			ta\bs\trn\offset_inc# = ta\ed\txt_trn_offset_inc\val_float#
			ta\bs\trn\calc_normals = ta\ed\txt_trn_calc_normals\val_float#
			ta\bs\trn\falloff_width = ta\ed\txt_trn_falloff_width\val_float#
			ta\bs\trn\falloff_height = ta\ed\txt_trn_falloff_height\val_float#	
			CreateTerrain ta\bs\trn		
			PlaceEntityOnTerrain ta\bs\pivot,ta\bs\trn	
			ta\ed\btn_trn_generate\click_handled = True 	
		End If
		
		If (ta\ed\btn_env_generate\clicked And ta\ed\btn_env_generate\click_handled = False) Then 
			If ta\bs\skybox <> 0 DestroyEntity ta\bs\skybox	
			tex_path$ = ta\ed\sky_path$ + "/" + ta\ed\btn_sky_texture\val_string$
			ta\bs\sky_texture = LoadTexture(tex_path$,4,56)	
			SetEnvTexture ta\bs\sky_texture
			ta\bs\skybox = CreateSkybox(ta\bs\sky_texture)	
			ta\bs\sky_roughness = ta\ed\txt_sky_roughness\val_float#
			SetSkyboxRoughness ta\bs\skybox,ta\bs\sky_roughness
			SetEntityRotation ta\bs\directional_light,ta\ed\txt_dl_rx\val_float#,ta\ed\txt_dl_ry\val_float#,0			
			SetAmbientLightColor ta\ed\txt_amb_r\val_float#,ta\ed\txt_amb_g\val_float#,ta\ed\txt_amb_b\val_float#,ta\ed\txt_amb_a\val_float#
			ta\ed\btn_env_generate\click_handled = True 	
		End If
				
		If (ta\ed\btn_trn_material\clicked And ta\ed\btn_trn_material\click_handled = False) Then 
			ta\ed\material_browser\visible = True
			GetFileList ta\ed\material_browser		
			ta\ed\btn_trn_material\click_handled = True 
		End If 
		
		If (ta\ed\btn_sky_texture\clicked And ta\ed\btn_sky_texture\click_handled = False) Then 
			ta\ed\sky_browser\visible = True
			GetFileList ta\ed\sky_browser		
			ta\ed\btn_sky_texture\click_handled = True 
		End If 
		
		If (ta\ed\btn_env_save\clicked And ta\ed\btn_env_save\click_handled = False) Then 
			SaveEnvironment "environment0.env"			
			ta\ed\btn_env_save\click_handled = True 
		End If
		
		If (ta\ed\btn_env_load\clicked And ta\ed\btn_env_load\click_handled = False) Then 
			LoadEnvironment "environment0.env"		
			GUISizeAdjust ta\ed\environment_settings	
			ta\ed\btn_env_load\click_handled = True 
		End If 
		
		If (ta\ed\btn_trn_save\clicked And ta\ed\btn_trn_save\click_handled = False) Then 
			SaveTerrain "terrain0.trn"			
			ta\ed\btn_trn_save\click_handled = True 
		End If
		
		If (ta\ed\btn_trn_load\clicked And ta\ed\btn_trn_load\click_handled = False) Then 
			LoadTerrain "terrain0.trn"		
			GUISizeAdjust ta\ed\terrain_settings	
			ta\ed\btn_trn_load\click_handled = True 
		End If 
		
		actor_count = 0
		For a.Actor = Each Actor 
			If a\outliner_widget\clicked Then 
				ta\ed\current_actor = a
				a\outliner_widget\clicked = False 
				ta\bs\camera_mode = 1    
			End If 
			actor_count = actor_count + 1
		Next 
		ta\ed\txt_act_count\val_float# = actor_count
					
		fbc = FileBrowserCheck(ta)
		
		; toggle model browser visiblity 	
		If (IsKeyHit(49) And ta\bs\camera_mode=0) Then 
			If ta\ed\model_browser\visible = False Then 
				If Not fbc Then 
					ta\ed\model_browser\visible = True
					GetFileList ta\ed\model_browser 
				End If  
			Else 
				ta\ed\model_browser\visible = False				
			End If 
		End If
		
		; toggle tree browser visiblity 	
		If (IsKeyHit(50) And ta\bs\camera_mode=0) Then 
			If ta\ed\tree_browser\visible = False Then 
				If Not fbc 
					ta\ed\tree_browser\visible = True
					GetFileList ta\ed\tree_browser 
				End If 	
			Else 
				ta\ed\tree_browser\visible = False	
			End If 
		End If 
		
		; toggle foliage browser visiblity 	
		If (IsKeyDown(51) And ta\bs\camera_mode=0) Then 
			If threedown = False Then 
				threedown = True 
				If ta\ed\foliage_browser\visible = False Then 
					If Not fbc 
						ta\ed\foliage_browser\visible = True
						GetFileList ta\ed\foliage_browser 
					End If 	
				Else 
					ta\ed\foliage_browser\visible = False	
				End If 
			End If 	
		Else 
			threedown = False 
		End If  		

		If Not fbc Then 
			If ta\bs\camera_mode = 0 Then 
				KeyboardInputFirstPerson ta\bs\pivot
				If ta\bs\trn <> Null Then KeepEntityAboveTerrain ta\bs\pivot,ta\bs\trn
			End If 		
			; EDIT OBJECT
			If ta\bs\camera_mode = 1 Then 			
				; left bracket or Z
				If (IsKeyHit(91) Or IsKeyHit(90)) Then		
					; switch to previous 
					ta\ed\current_actor = Before ta\ed\current_actor
					; check if null
					If ta\ed\current_actor = Null Then 
					; switch to last 
					ta\ed\current_actor = Last Actor 
					End If		
				End If
				
				; right bracket or C
				If (IsKeyHit(93) Or IsKeyHit(67)) Then 		
					; switch to next
					ta\ed\current_actor = After ta\ed\current_actor					
					; check if null
					If ta\ed\current_actor = Null Then 
						; switch to first
						ta\ed\current_actor = First Actor 
					End If		
				End If		
				
				; sa or X
				If (IsKeyDown(261) Or IsKeyDown(88)) Then 
					If xdown = False Then 
						If ta\ed\current_actor <> Null Then 
							If ta\ed\current_actor\model Then DestroyEntity ta\ed\current_actor\model
							If ta\ed\current_actor\sprite Then DestroyEntity ta\ed\current_actor\sprite
							If ta\ed\current_actor\point_light Then DestroyEntity ta\ed\current_actor\point_light
							If ta\ed\current_actor\spot_light Then DestroyEntity ta\ed\current_actor\spot_light

							If ta\ed\current_actor\collision_cylinder\model Then 
								DestroyEntity ta\ed\current_actor\collision_cylinder\model
								Delete ta\ed\current_actor\collision_cylinder
							End If 
							
							If ta\ed\current_actor\collision_aabb\model Then 
								DestroyEntity ta\ed\current_actor\collision_aabb\model
								Delete ta\ed\current_actor\collision_aabb
							End If 
															
							If ta\ed\current_actor\boned_model Then DestroyEntity ta\ed\current_actor\boned_model	
													
							Delete ta\ed\current_actor\outliner_widget
							Delete ta\ed\current_actor 	
							GUISizeAdjust ta\ed\outliner						
							ta\bs\camera_mode=0
							xdown = True  
						End If 	
					End If 	
				Else 
					xdown = False
				End If 	 	
				
				; make a copy of the current actor and drop it 
				If IsMouseButtonHit(0) Then 
					acname$ = ta\ed\current_actor\name$ 
					actype = ta\ed\current_actor\actor_type				
					acpath$ = ta\ed\current_actor\actor_path$					
					actor_copy.Actor = CreateActor(acname$,actype,acpath$,ta\ed\outliner) 
					If actor_copy\actor_type = 0 Then					
						px# = GetEntityX(ta\ed\current_actor\model) : py# = GetEntityY(ta\ed\current_actor\model) : pz# = GetEntityZ(ta\ed\current_actor\model)
						prx# = GetEntityRX(ta\ed\current_actor\model) : pry# = GetEntityRY(ta\ed\current_actor\model) : prz# = GetEntityRZ(ta\ed\current_actor\model)
						psx# = GetEntitySX(ta\ed\current_actor\model) : psy# = GetEntitySY(ta\ed\current_actor\model) : psz# = GetEntitySZ(ta\ed\current_actor\model)	
						SetEntityPosition actor_copy\model,px,py,pz : SetEntityRotation actor_copy\model,prx,pry,prz : SetEntityScale actor_copy\model,psx,psy,psz			
					End If 
					
					If actor_copy\actor_type = 1 Then					
						px# = GetEntityX(ta\ed\current_actor\sprite) : py# = GetEntityY(ta\ed\current_actor\sprite) : pz# = GetEntityZ(ta\ed\current_actor\sprite)
						prx# = GetEntityRX(ta\ed\current_actor\sprite) : pry# = GetEntityRY(ta\ed\current_actor\sprite) : prz# = GetEntityRZ(ta\ed\current_actor\sprite)
						psx# = GetEntitySX(ta\ed\current_actor\sprite) : psy# = GetEntitySY(ta\ed\current_actor\sprite) : psz# = GetEntitySZ(ta\ed\current_actor\sprite)
						SetEntityPosition actor_copy\sprite,px,py,pz : SetEntityRotation actor_copy\sprite,prx,pry,prz : SetEntityScale actor_copy\sprite,psx,psy,psz						
					End If									
				End If  	
			   ; adjust actor 
				If ta\ed\current_actor <> Null Then 
					ta\ed\lbl_act_name\val_string$ = ta\ed\current_actor\name$
					If ta\ed\current_actor\actor_type = 0 Then 
						KeyboardInputThirdPerson ta\ed\current_actor\model,ta\bs\pivot,0.1,0.1
						If (ta\bs\trn <> Null And ta\ed\txt_act_snap\val_float=1) Then ClampEntityAboveTerrain ta\ed\current_actor\model,ta\bs\trn,0
						If IsMouseButtonDown(1) Then TurnEntity ta\ed\current_actor\model,GetMouseVY() * 0.01,0,GetMouseVX() * 0.01
						; update the gui with the current model position, rotation and scale					
						ta\ed\txt_act_posx\val_float# = GetEntityX(ta\ed\current_actor\model)
						ta\ed\txt_act_posy\val_float# = GetEntityY(ta\ed\current_actor\model)
						ta\ed\txt_act_posz\val_float# = GetEntityZ(ta\ed\current_actor\model)
						ta\ed\txt_act_rotx\val_float# = GetEntityRX(ta\ed\current_actor\model)
						ta\ed\txt_act_roty\val_float# = GetEntityRY(ta\ed\current_actor\model)
						ta\ed\txt_act_rotz\val_float# = GetEntityRZ(ta\ed\current_actor\model)
						ta\ed\txt_act_sclx\val_float# = GetEntitySX(ta\ed\current_actor\model)
						ta\ed\txt_act_scly\val_float# = GetEntitySY(ta\ed\current_actor\model)
						ta\ed\txt_act_sclz\val_float# = GetEntitySZ(ta\ed\current_actor\model)
					End If 
												
					If ta\ed\current_actor\actor_type = 1 Then 
						KeyboardInputThirdPerson ta\ed\current_actor\sprite,ta\bs\pivot,0.01,0.1
						If (ta\bs\trn <> Null And ta\ed\txt_act_snap\val_float=1) Then ClampEntityAboveTerrain ta\ed\current_actor\sprite,ta\bs\trn,0
						If IsMouseButtonDown(1) Then TurnEntity ta\ed\current_actor\sprite,GetMouseVY() * 0.01,0,GetMouseVX() * 0.01
						; update the gui with the current sprite position 					
						ta\ed\txt_act_posx\val_float# = GetEntityX(ta\ed\current_actor\sprite)
						ta\ed\txt_act_posy\val_float# = GetEntityY(ta\ed\current_actor\sprite)
						ta\ed\txt_act_posz\val_float# = GetEntityZ(ta\ed\current_actor\sprite)
					End If
					; update distance from teepad
					px# = GetEntityX(ta\bs\pivot) : py# = GetEntityY(ta\bs\pivot) : pz# = GetEntityZ(ta\bs\pivot)
					tx# = 0 : ty# = 0 : tz# = 0					
					ta\ed\lbl_dist_from_teepad\val_string$ = Dist3D(px,py,pz,tx,ty,tz)	
				End If 				
			End If 	
		End If 
		DrawAllGUIs()
		
		If debug Then GUIHoverCheck()				
		
		If (GUIDragCheck()=False And fbc=False) Then 
			If ta\bs\camera_mode = 0 Then UnrealMouseInput ta\bs\pivot
			
			If ta\bs\camera_mode = 1 Then 
				If IsMouseButtonDown(1) = False ThirdPersonMouseInputEditor(ta\bs\camera,ta\bs\pivot)
				ClampEntityPitch(ta\bs\pivot,0,-70)
				SetMouseCursorMode 3
			End If 	
		Else 
			If ta\bs\camera_mode = 0 Then SetMouseCursorMode 1			
			If ta\bs\skybox<>0 Then SetSkyboxRoughness ta\bs\skybox,ta\bs\sky_roughness
			SetEntityRotation ta\bs\directional_light,ta\ed\txt_dl_rx\val_float#,ta\ed\txt_dl_ry\val_float#,0
			SetAmbientLightColor ta\ed\txt_amb_r\val_float#,ta\ed\txt_amb_g\val_float#,ta\ed\txt_amb_b\val_float#,ta\ed\txt_amb_a\val_float#
		End If 		
		
		If (fbc=False And ta\help_visible=True) Then 
			Set2DTextColor 1,1,0,1
			Draw2DText "1 = models",10,10	
			Draw2DText "2 = trees",10,30
			Draw2DText "3 = foliage",10,50
			Draw2DText "F1 - Environment Settings",10,70
			Draw2DText "F2 - Terrain Settings",10,90
			Draw2DText "F3 - Outliner",10,110
			Draw2DText "F4 - Actor Settings",10,130	
			Draw2DText "F12 - Help / Info",10,150			
		End If 	

		EndFrame ta	
	Wend 	
	SaveOnClose ta
	; CloseFile ta\logfile
End Function 

Function FileBrowserCheck(t.TestApp)
	Local r = False 
	For f.FileBrowser = Each FileBrowser 
		If f\visible Then 
			r = True 
			DrawFileBrowser f
			FileBrowserInput f			
			If f\newfile Then
				; handle the file based on its type
				If f\ext$ = "glb" Or f\ext2$ = "gltf" Then					
					s.Actor = CreateActor(f\filename$,0,f\filepath$,t\ed\outliner)					
					If t\bs\trn <> Null Then 
						PlaceEntityOnTerrain s\model,t\bs\trn,0,False,False,GetEntityX(t\bs\pivot),GetEntityZ(t\bs\pivot)
					Else 
						SetEntityPosition s\model,GetEntityX(t\bs\pivot),GetEntityY(t\bs\pivot)-1,GetEntityZ(t\bs\pivot)
					End If 
					; switch to third person 
					t\bs\camera_mode = 1
					t\ed\current_actor = s
					If GetMouseZ() = 0 Then SetMouseZ -5										
				End If 
				
				If f\ext$="dir" Then 
					t\ed\btn_trn_material\val_string$ = file_list$(f\selected_file)
					GUISizeAdjust t\ed\terrain_settings		
				End If 
				
				If f\title$="Sky Browser" Then 					
					t\ed\btn_sky_texture\val_string$ = file_list$(f\selected_file)
					GUISizeAdjust t\ed\environment_settings	
				End If
				
				If f\title$="Foliage Browser" Then 
					s.Actor = CreateActor(f\filename$,1,f\filepath$,t\ed\outliner)					
					If t\bs\trn <> Null Then 
						PlaceEntityOnTerrain s\sprite,t\bs\trn,0,False,False,GetEntityX(t\bs\pivot),GetEntityZ(t\bs\pivot)
					Else 
						SetEntityPosition s\sprite,GetEntityX(t\bs\pivot),GetEntityY(t\bs\pivot)-1,GetEntityZ(t\bs\pivot)
					End If 
					; switch to third person 
					t\bs\camera_mode = 1
					t\ed\current_actor = s
					If GetMouseZ() = 0 Then SetMouseZ -5					
				End If  
					
				f\visible = False
				f\newfile = False	
			End If		
		End If 	
	Next
	Return r	
End Function 
