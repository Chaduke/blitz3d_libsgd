; Step 6 - More 3D! 

; now that you're probably getting the hang of things I'm going to start adding some 
; miscellaneous functionality here and there to help you along the way
; and show you how you can build your own set of tools for programming

; the Include command takes a string value that represents the contents of an outside code file.
; If you don't include a directory path, it searches the current folder for this file
; this is the equivalent if you taking the contents of hextofloat.bb and copy / pasting it here

Include "hextofloat.bb" 
; hextofloat.bb contains a function that converts the hexidecimal codes for color 
; that you can find all over the web, to values 0.0 to 1.0 that are used in LibSGD
; maybe one day you'll want to study what goes on in that function 
; Studying the code in it and figuring out what it does could be a very useful
; learning exercise, probably best left for later on...
; To find colors in this format google search something like "lime green hex"

; Being able to include outside files into your program is an essential part 
; of computer programming.  It helps you organize your projects and also 
; provides a method to share useful, reusable parts of your code with others,
; as well as use other people's code in your own projects

CreateWindow 1024,768,"Hello Step 6!",0
light = CreateDirectionalLight() ; creates a light so we can see things, otherwise it would pitch black
TurnEntity light,-45,0,0 ; turns the directional light downward 45 degrees so we can see the top of the cube
camera = CreatePerspectiveCamera() ; creates a camera to look at things in the scene
MoveEntity camera,0,3,-5 ; moves the camera 3 unit upwards, and 5 units backward
TurnEntity camera,-22,0,0 ; turns the camera downwards 22 degrees to see the top of the cube
material=LoadPBRMaterial("sgd://materials/Fabric050_1K-JPG") ; loads up a nice fabric texture from the materials library
cubemesh = CreateBoxMesh(-1,0,-1,1,2,1,material) ; creates a cube of 2x2x2 units in size
cubemodel = CreateModel(cubemesh) ; creates a model to attach the mesh to so we can display it, move it, rotate it, etc

sylfaen = LoadFont("C:\windows\fonts\sylfaen.ttf",40) ; loads the "sylfaen" font from your c:\windows\fonts folder at size 40
Set2DFont sylfaen ; sets the font

SetSceneClearColorHex "041a40" ; scene will clear to dark sky blue
Set2DTextColorHex "f7330e" ; text will be bright orange-red

Repeat 	
	TurnEntity cubemodel,0,1,0 ; turns the cube 1 unit counter-clockwise on its y-axis
	x = x + 1	; increment the x angle
	SetEntityPosition cubemodel,Sin(x),0,0 ; sets the cube left-right position with a sine wave pattern	
	Clear2D() 	
	Draw2DText "Are you ready for Step 6 ??",10,10
	RenderScene() 
	Present() 
Until PollEvents() = 1