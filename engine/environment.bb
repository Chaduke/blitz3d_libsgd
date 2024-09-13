; environment.bb
; by Chaduke 
; 20240719 

Type Environment
	Field skybox
	Field dl ; directional light 
	Field gui.GUIWindow
	Field btn_sky_texture.Widget
	Field txt_sky_roughness.Widget	
	Field lbl_dl.Widget
	Field txt_dl_rx.Widget
	Field txt_dl_ry.Widget
	Field txt_amb_r.Widget
	Field txt_amb_g.Widget
	Field txt_amb_b.Widget
	Field txt_amb_a.Widget	
	Field btn_env_generate.Widget	
	Field btn_env_load.Widget
	Field btn_env_save.Widget	
End Type

Function CreateEnvironment.Environment(font)
	Local e.Environment = New Environment 	
	e\gui=CreateGUI(font,"Environment Settings",GetWindowWidth()-290,GetWindowHeight()-300)
	e\gui\v = True 
	e\btn_sky_texture = AddWidget(e\gui,"Skybox Texture",2,"skybox_sunny.png")
	e\txt_sky_roughness = AddWidget(e\gui,"Sky Roughness",0,0.0,0.0,1.0,70) 
	e\lbl_dl = AddWidget(e\gui,"Directional Light",2,"")
	e\txt_dl_rx = AddWidget(e\gui,"Rotation X",1,-107,-180,180,70)
	e\txt_dl_ry = AddWidget(e\gui,"Rotation Y",1,-60,-180,180,70)
	e\txt_amb_r = AddWidget(e\gui,"Ambient Red",0,0.38,0.0,1.0,70)
	e\txt_amb_g = AddWidget(e\gui,"Ambient Green",0,0.33,0.0,1.0,70)
	e\txt_amb_b = AddWidget(e\gui,"Ambient Blue",0,0.46,0.0,1.0,70)
	e\txt_amb_a = AddWidget(e\gui,"Ambient Alpha",0,0.50,0.0,1.0,70)	
	e\btn_env_generate = AddWidget(e\gui,"Generate",2,"")
	e\btn_env_load = AddWidget(e\gui,"Load",2,"")
	e\btn_env_save = AddWidget(e\gui,"Save",2,"")	
	Local sky_texture = LoadCubeTexture("../engine/assets/textures/skybox/skybox_sunny.png",4,56)	
	SetEnvTexture sky_texture
	e\skybox = CreateSkybox(sky_texture)			
	e\dl = CreateDirectionalLight() 
	turnentity e\dl,-107,-60,0
	SetAmbientLightColor 0.38,0.33,0.46,0.50
	SetLightShadowsEnabled e\dl,True 
	Return e
End Function 

Function UpdateEnvironment(e.Environment,r=False)	
	If r Then 
		; check to see if a widget is dragged and update accordingly
		If e\txt_sky_roughness\dragged Then SetSkyboxRoughness e\skybox,e\txt_sky_roughness\val_float#		
		If (e\txt_dl_rx\dragged Or e\txt_dl_ry\dragged) Then SetEntityRotation e\dl,e\txt_dl_rx\val_float#,e\txt_dl_ry\val_float#,0
		If (e\txt_amb_r\dragged Or e\txt_amb_g\dragged Or e\txt_amb_b\dragged Or e\txt_amb_a\dragged) Then 
			SetAmbientLightColor e\txt_amb_r\val_float#,e\txt_amb_g\val_float#, e\txt_amb_b\val_float#,	 e\txt_amb_a\val_float#
		End If 	
	End If 
		
	If (e\btn_env_load\clicked And e\btn_env_load\click_handled = False) Then 
		LoadEnvironment e,"env.txt"
		e\btn_env_load\click_handled = True 
	End If 
		
	If (e\btn_env_save\clicked And e\btn_env_save\click_handled = False) Then 
		SaveEnvironment e,"env.txt"
		e\btn_env_save\click_handled = True 
	End If 	
	
	Return r		
End Function

Function LoadEnvironment(e.Environment,path$)
	infile= ReadFile(path$)
	
	e\btn_sky_texture\val_string$ = ReadString(infile)
	e\txt_sky_roughness\val_float# = ReadFloat(infile)
	e\txt_dl_rx\val_float#	 = ReadFloat(infile)
	e\txt_dl_ry\val_float#	 = ReadFloat(infile)
	e\txt_amb_r\val_float# = ReadFloat(infile)
	e\txt_amb_g\val_float# = ReadFloat(infile)
	e\txt_amb_b\val_float# = ReadFloat(infile)
	e\txt_amb_a\val_float# = ReadFloat(infile)
	
	CloseFile infile
	SetSkyboxRoughness e\skybox,e\txt_sky_roughness\val_float#	
	SetEntityRotation e\dl,e\txt_dl_rx\val_float#,e\txt_dl_ry\val_float#,0
	SetAmbientLightColor e\txt_amb_r\val_float#,e\txt_amb_g\val_float#, e\txt_amb_b\val_float#, e\txt_amb_a\val_float#
End Function 

Function SaveEnvironment(e.Environment,path$)
	outfile = WriteFile(path$)
	
	WriteString outfile,e\btn_sky_texture\val_string$
	WriteFloat outfile,e\txt_sky_roughness\val_float#
	WriteFloat outfile,e\txt_dl_rx\val_float#	
	WriteFloat outfile,e\txt_dl_ry\val_float#	
	WriteFloat outfile,e\txt_amb_r\val_float#
	WriteFloat outfile,e\txt_amb_g\val_float#
	WriteFloat outfile,e\txt_amb_b\val_float#
	WriteFloat outfile,e\txt_amb_a\val_float#
	
	CloseFile outfile	
End Function 

Function CreateGround()
	DisplayLoadingMessage "Creating Ground"
	Local ground_material = LoadPBRMaterial("../engine/assets/materials/Ground048_1K-JPG")
	Local ground_mesh = CreateBoxMesh(-128,-0.1,-128,128,0,128,ground_material)
	TFormMeshTexCoords ground_mesh,32,32,0,0
	Local ground_model = CreateModel(ground_mesh)
	Local ground_collider = CreateMeshCollider(ground_model,0,ground_mesh)
	Return ground_model
End Function 