; test_scene.bb
; by Chaduke
; 20240704

Type TestScene	
	Field directional_light	
	Field ambient_red# 
	Field ambient_green#
	Field ambient_blue# 
	Field ambient_alpha# 	
	Field sky_texture
	Field sky_roughness
	Field skybox 		
End Type

Function CreateTestScene.TestScene()
	Local t.TestScene = New TestScene
	
	SetCSMTextureSize 4096
	; SetCSMSplitDistances 8,32,64,128
	; SetCSMClipRange 1024	
	t\directional_light = CreateDirectionalLight()
	SetCSMDepthBias 0.0003
	SetLightShadowsEnabled t\directional_light,True 
	TurnEntity t\directional_light,-35,-20,0	
	t\ambient_red = 0.38
	t\ambient_green = 0.33
	t\ambient_blue = 0.46
	t\ambient_alpha = 0.40
	SetAmbientLightColor t\ambient_red,t\ambient_green,t\ambient_blue,t\ambient_alpha	
	DisplayLoadingMessage "Creating Sky"
	t\sky_texture = Load2DTexture("../engine/assets/textures/skybox/skyboxsun5deg.png",4,56)		
	SetEnvTexture t\sky_texture
	t\skybox = CreateSkybox(t\sky_texture)	
	t\sky_roughness = 0.0
	SetSkyboxRoughness t\skybox,t\sky_roughness	
	Return t											
End Function 