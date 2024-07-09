; Welcome to LibSGD!! or Blitz3D2!!
 
; This an exciting time for indie game developers and creative folk in general.  
; In 2024, never before in history has there been at the fingertips of the game developer
; so many tools designed to bring your technical skill and creativity together to make pretty 
; much anything you can imagine, both on the computer screen or even in real life.

; LibSGD is a tool that I personally feel has enormous potential, and the reason might
; not be completely evident from the start, but once you begin creating things with it
; you'll understand.  LibSGD is just fun.  Making things with it is enjoyable, more
; enjoyable than any other software tool I've ever used.  A tool can have all the features
; anyone can dream up, but unless you are willing to use that tool, and learn how to use 
; it well, you'll never get anything created. 

; Game development is a long, arduous process.  If you're enjoying this process, it will
; never seem like work.  I hope that in these tutorials I'll help you to learn how to use this 
; marvelous tool for game development.  Let's Go!!
; 
; Chaduke
; 20240512

; Part 1 - Hello World!

Alert "Hello Blitz3D!!" ; Press F5 to run this program
End ; placed here for compatibility with the blitz debugger

; the Alert command takes anything you put after it in a message box and displays it.
; anything that is NOT a mathematical expression must be put in quotes " "
; and what is inside the quotes is normally called a "string"

; Before we get into mathematical expressions and strings let's talk about "comments".
; The semicolon ";" is used to "comment" out a line of program code
; When you press F5 to run the program, the computer will ignore everything that 
; follows the semicolon and appears in yellow text like these words you are reading.

; These words are normally called "comments", and are used to document your program.
; Documentation is very important, not only to help others understand your code
; but also to remind you of what you were doing at the time you wrote it.
; believe me, you will forget, especially when you start creating large projects!

; Mathematical expressions 
; example 2+2 

; Alert 2+2

; put a semicolon in front of the line above that says Alert "Hello Blitz3D2!!"
; then remove the semicolon from the line above that says Alert 2+2
; then press F5.  You should see a messagebox appear that says "4".
; 2+2 is called a mathematical expression.  When the computer sees code that is not 
; surrounded with quotes, it evaluates that code as a mathematical expression in 
; many cases.  In this case with the Alert command, that's how it works.  
; comment out the above code, and uncomment the code below, then press F5.

; Alert "The sum of 2 plus 2 is " + (2 + 2)

; note that you have to place 2 + 2 in parenthesis here, otherwise it will 
; will print out 22 instead of 4.  When you combine strings with mathematical 
; expressions using the "+" sign, make sure to add parenthesis around the 
; expressions, and always put the strings in quotes.
; Now you know how to use Alert, how comments work, and how to use strings
; along with mathematical expressions
; In the next tutorial we'll learn how to create a window and display some text on 
; the screen.  Pat yourself on the back, you've just taken the first step in
; becoming a game developer!  




