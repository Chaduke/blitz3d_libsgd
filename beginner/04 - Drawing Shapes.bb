; Step 4 - Drawing Shapes

CreateWindow 640,480,"Hello Step 4!",0

Repeat 
	Clear2D() ; clears the screen first
	Draw2DText "Hello Step 4!!",10,10 ; prints the string to the screen at 10,10
	Set2DFillColor 1,0,0,1 ; sets the color for 2D objects to be red
	Draw2DOval 50,50,100,100 ; draws an oval from 50,50 to 100,100	
	Set2DFillColor 0,1,0,0.5 ; sets the drawing color to green
	Draw2DOval 75,75,125,125 ; draws a green circle at 75,75 with a diameter of 50 units across 125 - 75 = 50

	RenderScene() ; arranges the scene to be drawn
	Present() ; presents the scene to the screen
Until PollEvents() = 1

; Starting from the code in Step 3, we have a few new additions
; the first is Set2DFillColor, which takes 4 parameters
; the parameters are values that range from 0.0 to 1.0
; the values represent amounts of red, green and blue
; the fourth value represents alpha, which is how 
; opaque the color is, in other words if you draw something
; before it, how much it will show through after you draw on top of it
; think of it like a thin piece of paper that you can see through
; to see how alpha works, draw another oval on top of the first one 
; in a different color by adding these two lines to the loop above
; add them right after the Draw2DOval command and before the RenderScene()

; Set2DFillColor 0,1,0,1 ; sets the drawing color to green
; Draw2DOval 75,75,125,125 ; draws a green circle at 75,75 with a diameter of 50 units across 125 - 75 = 50

; Run the program and notice how the green circle overlaps the red circle.
; Now try changing the 4th parameter of the Set2DFillColor 0, 1, 0, 1 to 0 , 1, 0, 0.5
; This changes the alpha value of the green fill color, leave the red as is
; Notice how the red circle now shows through the green, and the resulting combined color is yellow
; this is commonly called "alpha blending" 
; feel free to experiment with what you've learned so far
; to make a circle move try replacing the first parameter of the Draw2D oval command with a variable
; like this Draw2DOval x,50,100,100
; then on the next line, type x=x+1 
; when you run it, notice how the oval stretches out across the screen
; this is because you're telling it to start drawing the oval at the value of x horizontally 
; if you change the second parameter, the "y" value, you'll see the oval stretch vertically
; if you want the whole circle to move do something like this : 

; Draw2DOval x,y,x+50,y+50
; x=x+1 
; y=y+1

; this will make the circle move diagonally right and downward
; to draw a square try this - Draw2DRect 50,50,100,100
; to draw a line try this - Draw2DLine 50,50,100,100
; in the next tutorial we'll start working in 3D!
; experiment and have fun!!



