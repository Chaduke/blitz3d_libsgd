; editor.bb
; by Chaduke
; 20240620

; scene editor with gui,file browser, and load / save scene functions 

Type Editor 
	Field environment_settings.GUIWindow
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
		
	Field terrain_settings.GUIWindow
	Field btn_trn_material.Widget
	Field txt_trn_width.Widget
	Field txt_trn_depth.Widget
	Field txt_trn_height.Widget
	Field txt_trn_start_offset.Widget
	Field txt_trn_offset_inc.Widget
	Field txt_trn_falloff_width.Widget
	Field txt_trn_falloff_height.Widget
	Field txt_trn_calc_normals.Widget
	Field btn_trn_mesh.Widget
	Field btn_trn_generate.Widget	
	Field btn_trn_load.Widget
	Field btn_trn_save.Widget
	
	Field outliner.GUIWindow	
		
	Field actor_settings.GUIWindow
	Field txt_act_count.Widget
	Field lbl_act_name.Widget
	Field txt_act_posx.Widget
	Field txt_act_posy.Widget
	Field txt_act_posz.Widget	
	Field txt_act_rotx.Widget
	Field txt_act_roty.Widget
	Field txt_act_rotz.Widget	
	Field txt_act_sclx.Widget
	Field txt_act_scly.Widget
	Field txt_act_sclz.Widget	

	Field lbl_dist_from_teepad.Widget
	Field txt_act_snap.Widget
	
	Field models_path$
	Field trees_path$
	Field materials_path$
	Field sky_path$
	Field foliage_path$

	Field model_browser.FileBrowser
	Field tree_browser.FileBrowser
	Field material_browser.FileBrowser
	Field sky_browser.FileBrowser	
	Field foliage_browser.FileBrowser
End Type

Function CreateEditor.Editor(g.GameApp)
	Local e.Editor = New Editor 
	e\environment_settings=CreateGUI(g\gui_font,"Environment Settings",300,10)
	e\btn_sky_texture = AddWidget(e\environment_settings,"Skybox Texture",2,"skybox_sunny.png")
	e\txt_sky_roughness = AddWidget(e\environment_settings,"Sky Roughness",0,0.2,0.0,1.0,70) 
	e\lbl_dl = AddWidget(e\environment_settings,"Directional Light",2,"")
	e\txt_dl_rx = AddWidget(e\environment_settings,"Rotation X",1,-25,-180,180,70)
	e\txt_dl_ry = AddWidget(e\environment_settings,"Rotation Y",1,0,-180,180,70)
	e\txt_amb_r = AddWidget(e\environment_settings,"Ambient Red",0,0.4,0.0,1.0,70)
	e\txt_amb_g = AddWidget(e\environment_settings,"Ambient Green",0,0.2,0.0,1.0,70)
	e\txt_amb_b = AddWidget(e\environment_settings,"Ambient Blue",0,0.5,0.0,1.0,70)
	e\txt_amb_a = AddWidget(e\environment_settings,"Ambient Alpha",0,0.2,0.0,1.0,70)	
	e\btn_env_generate = AddWidget(e\environment_settings,"Generate",2,"")
	e\btn_env_load = AddWidget(e\environment_settings,"Load",2,"")
	e\btn_env_save = AddWidget(e\environment_settings,"Save",2,"")	
	
	e\terrain_settings = CreateGUI(g\gui_font,"Terrain Settings",600,10)
	e\btn_trn_material = AddWidget(e\terrain_settings,"Material",2,"Grass004_1K-JPG")
	e\txt_trn_width = AddWidget(e\terrain_settings,"Width",1,64,8,1024)
	e\txt_trn_depth = AddWidget(e\terrain_settings,"Depth",1,64,8,1024)	
	e\txt_trn_height = AddWidget(e\terrain_settings,"Height",1,8,1,32)	
	e\txt_trn_start_offset = AddWidget(e\terrain_settings,"Start Offset",1,777,0,1000)	
	e\txt_trn_offset_inc = AddWidget(e\terrain_settings,"Offset Inc",0,0.02,0.01,0.05)
	e\txt_trn_falloff_width = AddWidget(e\terrain_settings,"Falloff Width",1,10,0,30)
	e\txt_trn_falloff_height = AddWidget(e\terrain_settings,"Falloff Height",1,0,0,64)
	e\txt_trn_calc_normals = AddWidget(e\terrain_settings,"Calculate Normals",1,1,0,1)
	e\btn_trn_mesh = AddWidget(e\terrain_settings,"Mesh",2,"None") 
	e\btn_trn_generate = AddWidget(e\terrain_settings,"Generate",2,"")
	e\btn_trn_load = AddWidget(e\terrain_settings,"Load",2,"")
	e\btn_trn_save = AddWidget(e\terrain_settings,"Save",2,"")
	
	e\outliner = CreateGUI(g\gui_font,"Outliner",900,10)
	
	e\actor_settings = CreateGUI(g\gui_font,"Actor Settings",1200,10)
	e\txt_act_count = AddWidget(e\actor_settings,"Actor Count",1,0,0,1024,70)
	e\lbl_act_name = AddWidget(e\actor_settings,"Name ",2,"None")
	e\txt_act_posx = AddWidget(e\actor_settings,"Position X",0,0,-1024,1024,70)
	e\txt_act_posy = AddWidget(e\actor_settings,"Position Y",0,0,-1024,1024,70)
	e\txt_act_posz = AddWidget(e\actor_settings,"Position Z",0,0,-1024,1024,70)
	e\txt_act_rotx = AddWidget(e\actor_settings,"Rotation X",0,0,0,360,70)
	e\txt_act_roty = AddWidget(e\actor_settings,"Rotation Y",0,0,0,360,70)
	e\txt_act_rotz = AddWidget(e\actor_settings,"Rotation Z",0,0,0,360,70)
	e\txt_act_sclx = AddWidget(e\actor_settings,"Scale X",0,1,-16,16,70)
	e\txt_act_scly = AddWidget(e\actor_settings,"Scale Y",0,1,-16,16,70)
	e\txt_act_sclz = AddWidget(e\actor_settings,"Scale Z",0,1,-16,16,70)

	e\lbl_dist_from_teepad = AddWidget(e\actor_settings,"TP Distance",2,0)
	e\txt_act_snap = AddWidget(e\actor_settings,"Terrain Snapping",1,0,0,1,70)
	
	e\models_path$ = "../engine/assets/models/disc_golf"
	e\trees_path$ = "../engine/assets/models/trees"
	e\materials_path$ = "../engine/assets/materials"
	e\sky_path$ = "../engine/assets/textures/skybox"
	e\foliage_path$ = "../engine/assets/textures/foliage"
	
	e\model_browser = CreateFileBrowser("Model Browser",e\models_path$,"glb",g\browser_font)
	e\tree_browser = CreateFileBrowser("Tree Browser",e\trees_path$,"glb",g\browser_font)
	e\material_browser = CreateFileBrowser("Material Browser",e\materials_path$,"dir",g\browser_font)
	e\sky_browser = CreateFileBrowser("Sky Browser",e\sky_path$,"png",g\browser_font)
	e\foliage_browser = CreateFileBrowser("Foliage Browser",e\foliage_path$,"png",g\browser_font)	
	g\editor_loaded = True 
	Return e	
End Function 

Function UpdateEditorGUIs()
	GuiDragCheck()
	DrawAllGUIs()		
End Function 

Function UpdateEditorNavigation(g.GameApp,b.BasicScene)

	NavigationMode g\pivot,b\trn	
	
End Function 

Function LoadScene(g.GameApp,e.Editor,bs.BasicScene,load_objects=True)
	DisplayLoadingMessage "Loading scene..."
	; let's load our last save files 
	LoadEnvironment "c0h1env.dat"
	LoadTerrain "c0h1trn.dat"
	GUISizeAdjust e\environment_settings
	GUISizeAdjust e\terrain_settings	
		
	; generate environment 
	tex_path$ = e\sky_path$ + "/" + e\btn_sky_texture\val_string$	
	bs\sky_texture = LoadTexture(tex_path$,4,56)			
	SetEnvTexture bs\sky_texture
	bs\skybox = CreateSkybox(bs\sky_texture)	
	bs\sky_roughness = e\txt_sky_roughness\val_float#
	SetSkyboxRoughness bs\skybox,bs\sky_roughness
	SetEntityRotation bs\directional_light,e\txt_dl_rx\val_float#,e\txt_dl_ry\val_float#,0
	SetAmbientLightColor e\txt_amb_r\val_float#,e\txt_amb_g\val_float#,e\txt_amb_b\val_float#,e\txt_amb_a\val_float#			
	 
	; generate terrain 
	bs\trn = New Terrain
	bs\trn\material_path$ = e\materials_path$ + "/" + e\btn_trn_material\val_string$			
	bs\trn\width# = e\txt_trn_width\val_float#
	bs\trn\height# = e\txt_trn_height\val_float#
	bs\trn\depth# = e\txt_trn_depth\val_float#
	bs\trn\start_offset# = e\txt_trn_start_offset\val_float#
	bs\trn\offset_inc# = e\txt_trn_offset_inc\val_float#
	bs\trn\calc_normals = e\txt_trn_calc_normals\val_float#
	bs\trn\falloff_width = e\txt_trn_falloff_width\val_float#
	bs\trn\falloff_height = e\txt_trn_falloff_height\val_float#	
	CreateTerrain bs\trn		
	PlaceEntityOnTerrain g\pivot,bs\trn	
	; load a water plane 
	water_plane = LoadModel("../engine/assets/models/disc_golf/water_plane.glb")
	MoveEntity water_plane,32,1,32
	; If load_objects Then LoadActors "c0h1act.dat",e\outliner
	
	LoadConfig g,e,bs,"c0h1edt.dat"	
End Function 

Function SaveScene()
	SaveEnvironment "c0h1env.dat"
	SaveTerrain "c0h1trn.dat"
	; SaveActors "c0h1act.dat"
	; SaveConfig "c0h1edt.dat"
End Function 

Function SaveConfig(g.GameApp,e.Editor,bs.BasicScene,file$)
	fileout = WriteFile(scene_path$ + "/" + file$)
	px# = GetEntityX(g\pivot) : py# = GetEntityY(g\pivot) : pz# = GetEntityZ(g\pivot)
	prx# = GetEntityRX(g\pivot) : pry# = GetEntityRY(g\pivot) : prz# = GetEntityRZ(g\pivot)
	WriteFloat fileout,px# : WriteFloat fileout,py# : WriteFloat fileout,pz#
	WriteFloat fileout,prx# : WriteFloat fileout,pry# : WriteFloat fileout,prz#
	WriteFloat fileout,e\environment_settings\l : WriteFloat fileout,e\environment_settings\t 
	WriteFloat fileout,e\environment_settings\r : WriteFloat fileout,e\environment_settings\b 
	WriteInt fileout,e\environment_settings\v
	WriteFloat fileout,e\terrain_settings\l : WriteFloat fileout,e\terrain_settings\t 
	WriteFloat fileout,e\terrain_settings\r : WriteFloat fileout,e\terrain_settings\b 
	WriteInt fileout,e\terrain_settings\v
	WriteFloat fileout,e\outliner\l : WriteFloat fileout,e\outliner\t 
	WriteFloat fileout,e\outliner\r : WriteFloat fileout,e\outliner\b 
	WriteInt fileout,e\outliner\v
	WriteFloat fileout,e\actor_settings\l : WriteFloat fileout,e\actor_settings\t 
	WriteFloat fileout,e\actor_settings\r : WriteFloat fileout,e\actor_settings\b 
	WriteInt fileout,e\actor_settings\v
	; WriteInt fileout,t\help_visible
	CloseFile fileout	
End Function 

Function LoadConfig(g.GameApp,e.Editor,bs.BasicScene,file$)
	filein = ReadFile(scene_path$ + "/" + file$)
	px# = ReadFloat(filein) : py# = ReadFloat(filein) : pz# = ReadFloat(filein) 
	prx# = ReadFloat(filein) : pry# = ReadFloat(filein) : prz# = ReadFloat(filein) 
	SetEntityPosition g\pivot,px,py,pz
	SetEntityRotation g\pivot,prx,pry,prz
	e\environment_settings\l = ReadFloat(filein) : e\environment_settings\t = ReadFloat(filein)
	e\environment_settings\r = ReadFloat(filein) : e\environment_settings\b = ReadFloat(filein)
	e\environment_settings\v = ReadInt(filein) 
	e\terrain_settings\l = ReadFloat(filein) : e\terrain_settings\t = ReadFloat(filein)
	e\terrain_settings\r = ReadFloat(filein) : e\terrain_settings\b = ReadFloat(filein)
	e\terrain_settings\v = ReadInt(filein) 
	e\outliner\l = ReadFloat(filein) : e\outliner\t = ReadFloat(filein)
	e\outliner\r = ReadFloat(filein) : e\outliner\b = ReadFloat(filein)
	e\outliner\v = ReadInt(filein)
	e\actor_settings\l = ReadFloat(filein) : e\actor_settings\t = ReadFloat(filein)
	e\actor_settings\r = ReadFloat(filein) : e\actor_settings\b = ReadFloat(filein)
	e\actor_settings\v = ReadInt(filein)
	; t\help_visible = ReadInt(filein)
	CloseFile filein		
End Function 