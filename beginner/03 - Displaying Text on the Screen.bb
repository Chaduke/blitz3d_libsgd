; Step 3 - Displaying Text on Screen
; CreateScene(), Clear2D(), Draw2DText, Variables, RenderScene(), Present()

; In order to start displaying text on the screen we need to do a few things 
; in preparation.  We'll begin with the code we had in step 2, with some additions :  

CreateWindow 640,480,"Hello Step 3!",0
CreateScene() ; creates a scene to draw on
Repeat 
	Clear2D() ; clears the screen first
	Draw2DText "Hello Step 3!!",10,10 ; prints the string to the screen at 10 pixels from the left side of the screen, and 10 pixels from the top
	; Draw2DText "X equals " + x,10,40 ; prints the value of x on the screen
	; x=x+1 ; makes the value of x go up by 1
	RenderScene() ; arranges the scene to be drawn
	Present() ; presents the scene to the screen
Until PollEvents() = 1

; study the new commands above, and also notice how I placed comments after each each line of code that is new 
; everything that comes after the semicolon on a line is ignored by the computer
; but it will execute any code that comes before it.  

; The CreateScene() command must be called after CreateWindow
; without it you wont be able to get anything happening other than alert windows 
; and other non graphical stuff that we'll get to later

; Clear2D must be called before you begin drawing 2D (non3D) stuff to the screen
; if you don't, everytime the code in the loop runs
; the stuff you drew in the previous loop will still be there
; and it will get all messy if you have things changing from one loop to 
; the next.  To experiment, uncomment the two lines of code above that 
; draws the value of x to the screen.  x is called a "variable".

; Each time the loop runs, the value of x goes up by one.
; Now we have the basic skeleton of a program and can begin doing some 
; really fun and interesting things!  In the next lesson we'll draw some
; shapes and make them move around. For now feel free to experiment with 
; code above and try commenting out or changing different lines to see what happens
; experimenting is a big part of learning, so don't be afraid!







