; the first command you'll normally use when creating a program with 
; LibSGD is "CreateWindow" This command takes 4 "parameters"
; Parameters are bits of code that represent values, like 
; mathematical expressions, strings, and other things we'll 
; learn about later.  Here is an example of CreateWindow

CreateWindow 640,480,"Hello LibSGD!",0

; If you press F5 you'll a see a window pop that displays a 
; title and waits for you to close it.
; Let's first talk about the 4 parameters that we fed to 
; CreateWindow command.  640 is the width of the window. This means
; its 640 "pixels" wide.  Think of pixels as just tiny squares on your 
; monitor that have a certain color.  480 is height of the window 
; in pixels.  The third parameter is a string.  This is what 
; shows up in the title bar at the top of the window.  
; the 4th parameter is what mode the window will be displayed in.
; This can be a window, or fullscreen, or other options.  
; For now let's just keep it at 0.

; Loops and Functions 

Repeat
 
; place program code here

Until PollEvents()=1
End

; look at the code above and then press F5
; what happens here is that we create a program loop.
; The beginning of the loop is the Repeat command
; and the end of the loop is the Until command
; everything in between is repeated Until the 
; condition is met that is shown after the Until command
; in this case we have a mathematical expression 
; PollEvents() = 1
; PollEvents() is command, but its also called a function
; the parenthesis after the command indicates that the 
; function will often "return" a value.  You can also 
; feed parameters into a function like we did with the 
; CreateWindow command above.  In this case PollEvents()
; checks to see if anything happens that the user does 
; to interact with the program, like pressing keys 
; moving or clicking the mouse, then it "returns"
; a value that indicates what that event was
; the event 1 means the user clicked the close button
; on the window, (the "X" in the upper right corner)
; that means the loop must come to end 
; in other words PollEvents() = 1
; If PollEvents does not equal 1, the program 
; just keeps looping 

; so now we have a program that opens a window 
; and waits until you click the close button
; in the next lesson we'll learn how to display some 
; text in the window.  Congratulations! 
; Step 2 Complete

