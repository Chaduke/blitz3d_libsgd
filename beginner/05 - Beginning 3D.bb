; Step 5 - Beginning 3D

; ok there's a lot to unpack here, but once you get the hang of this
; things will get really fun
; press F5 first and see what we're creating here with all this new code

CreateWindow 1024,768,"Hello Step 5!",0
CreateScene() 
light = CreateDirectionalLight() ; creates a light so we can see things, otherwise it would pitch black
camera = CreatePerspectiveCamera() ; creates a camera to look at things in the scene
MoveEntity camera,0,1,-5 ; moves the camera 1 unit upwards, and 5 units backward, away from the screen towards you
material=LoadPBRMaterial("sgd://misc/test-texture.png") ; loads up a test texture to wrap around the cube 
cubemesh = CreateBoxMesh(-1,0,-1,1,2,1,material) ; creates a cube of 2x2x2 units in size
cubemodel = CreateModel(cubemesh) ; creates a model to attach the mesh to so we can display it, move it, rotate it, etc

Repeat 
	TurnEntity cubemodel,0,1,0 ; turns the cube 1 unit counter-clockwise on its y-axis
	Clear2D() 
	Set2DTextColor 1,1,0,1 
	Draw2DText "Hello Step 5!!",10,10
	RenderScene() 
	Present() 
Until PollEvents() = 1

; let's go through the new commands 1 by 1
; notice I changed the size of the window to give us a little more room to work with
; read each line of code along with the comments
; different kinds of lights can be created, directional, spot, and point
; here we create a directional light which acts kind of like the sun
; directional lights work no matter where they are positioned in the scene
; only the rotation of a directional light matters
; the camera works like your eyeballs viewing the world in front of it
; or how you point a camera at what you want to take a picture or video of
; just like any normal camera
; in order to see what you want in the scene, you have to position and rotate the 
; camera in the correct way
; everything you create in 3D is placed on an X,Y,Z axis and begins at 0,0,0
; X-axis is left and right
; Y-axis is up and down
; Z-axis is foward and backwards, in and out of the screen
; X goes negative to left of 0, and positive to the right of 0
; Y works positive up and negative down
; Z works positive forward and negative backward
; notice the parameters of the MoveEntity command
; the camera, the lights, and any objects you add to the scene are called "entities"
; entities can be moved, rotated and scaled, among other things
; cameras and lights can only be moved and rotated in most cases
; in the example above I move the camera 0 units left or right
; I move it 1 unit up and 5 units back, this gives us a perfect view of the cube 
; the cube stays located at 0,0,0 and the camera points directly at it
; the creation of the cube might be a little confusing at first 
; the parameters are x,y,z,x,y,z plus the material
; the first x,y,z is the corner you want to start the cube at
; and the second x,y,z is where you want to end it
; its like creating a rectangle in 2D, but here we have 6 parameters instead of 4

; we start creating the cube 1 unit to the left, 0 units up/down and 1 unit forward
; we move diagonally from lower back left to upper front right
; we have to move 2 units up on the Y axis, because the floor starts at 0 and the cube is 2 units tall
; left to right and forward to backward we move from -1 to +1, which accounts for 2 units on the X and Z axis
; this may take some time to get used to if you're just getting started in 3D 
; watch a lot of videos that describe 3D axes visually to help it sink in
; I plan on making videos to help along with these tutorials once LibSGD is released

; in the next lesson I'll explain how to move, rotate and scale objects in 3D 
; and how to add different kinds of shapes