; README.bb
; by Chaduke
; 20240524 

; Platformer Demo alpha 0.01

; if you are having performance issues 
; go to scene.bb and edit the settings at the top of the file
; particularly the number of trees, this seems to have the greatest 
; effect on FPS at this point
; keep in mind this is a very early alpha release 
; and you're likely to find bugs all over the place
; and many features missing

; USE the Mousewheel to Zoom In / Out in both Game Mode and Edit Mode

; keyboard controls 

; TAB - toggles between Game Mode and Edit Mode
; walk / run WASD or Arrow Keys
; SHIFT - sprint (notice it drains the yellow stamina bar in the upper right)
; SPACE jump (you can double jump, triple jump if you time it correctly at the top of the jump)
; technically you could keep jumping higher and higher forver but it gets harder to do each additional jump

; if you jump and miss the platform but you're close enough to grab on
; you will grab on and can "shimmy" left and right with the left and right or A and D keys
; press S or down to let go
; press jump to climb up

; in EDIT mode
; use arrows or WASD to "drive around" the current platform
; Use Q and E to move up and down, you can also use Pageup and Pagedown
; careful putting platforms too close to the ground or each other 
; this will likely cause unexpected behaviour, but defintely go ahead and test it
; use INSERT to add a new platform 
; use DELETE to delete the current platform 
; to cycle through the platfoms use [ and ] or Z and X (much easier if you use WASD)
; SHIFT will work like sprint in game mode to move around faster

; I have very early implementation of loading and saving the platforms right now
; Keypad 0-9 loads a level and CTRL - Keypad 0-9 saves
; this is may not work, loads too many platforms, or might even crash the game
; so if you're even going to mess with it probably best to run in non fullscreen 

; let me know what you think on the forums 
; thanks for checking this out 

; -Chad










 







