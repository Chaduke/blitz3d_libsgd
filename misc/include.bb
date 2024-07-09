CreateWindow 1280,720,"Test Program",0
CreateScene()
loop=True
while loop
e = pollevents()
if e=1 then loop = False
if KeyHit(256) then loop = False
RenderScene()
Present()
Wend
End
