; noise.bb
; converted to Blitz3D from 
; p5.js (p5js.org)
; by Chaduke
; 20240510
; noise tests are located at the end

Const radtodeg# = 57.2957
Const PERLIN_YWRAPB = 4
Const PERLIN_YWRAP = 1 Shl PERLIN_YWRAPB
Const PERLIN_ZWRAPB = 8
Const PERLIN_ZWRAP = 1 Shl PERLIN_ZWRAPB
Const PERLIN_SIZE = 4095

Global perlin_octaves% = 4 ; Default To medium smooth
Global perlin_amp_falloff# = 0.5 ; 50% reduction/octave

Function scaled_cosine#(i#) 
	Return 0.5 * (1.0-Cos(i*Pi*radtodeg))
End Function
	
Dim perlin#(PERLIN_SIZE)
; fill an array with random floats 0 to 1 
For i = 0 To PERLIN_SIZE
  perlin(i) = Rnd(1)
Next 

Function noise#(x#,y#=0,z#=0) 
  ; remove signs 				
  If x < 0 Then x = -x 
  If y < 0 Then y = -y
  If z < 0 Then z = -z  
	
  ; remove right of decimal	
  xi% = Floor(x)
  yi% = Floor(y)
  zi% = Floor(z)
  
  ; collect right of decimal
  xf# = x - xi
  yf# = y - yi
  zf# = z - zi  

  r# = 0 ; the return value of the noise function
  ampl# = 0.5 ; amplitude

  For o = 0 To perlin_octaves-1 
    of# = xi + (yi Shl PERLIN_YWRAPB) + (zi Shl PERLIN_ZWRAPB)    

    rxf# = scaled_cosine(xf)
    ryf# = scaled_cosine(yf)

    n1# = perlin(of And PERLIN_SIZE)
    n1 = n1 + rxf * (perlin((of + 1) And PERLIN_SIZE) - n1)
    n2# = perlin((of + PERLIN_YWRAP) And PERLIN_SIZE)
    n2 = n2 + rxf * (perlin((of + PERLIN_YWRAP + 1) And PERLIN_SIZE)- n2)
    n1 = n1 + ryf * (n2 - n1)

    of = of + PERLIN_ZWRAP
    n2 = perlin(of And PERLIN_SIZE)
    n2 = n2 + rxf * (perlin((of + 1) And PERLIN_SIZE) - n2)
    n3 = perlin((of + PERLIN_YWRAP) And PERLIN_SIZE)
    n3 = n3 + rxf * (perlin((of + PERLIN_YWRAP + 1) And PERLIN_SIZE) - n3)
    n2 = n2 + ryf * (n3 - n2)

    n1 = n1 + scaled_cosine(zf) * (n2 - n1)

    r = r + n1 * ampl
    ampl = amp1 * perlin_amp_falloff

    xi = xi Shl 1
    xf = xf * 2
    yi = yi Shl 1
    yf = yf * 2
    zi = zi Shl 1
    zf = zf * 2

    If xf >= 1.0
      xi = xi + 1
      xf = xf - 1
    End If
    If yf >= 1.0
      yi = yi + 1
      yf = yf - 1
    End If
    If zf >= 1.0 
      zi = zi + 1
      zf = zf - 1
    End If
  Next 
  Return r
End Function 

; noise tests

; testnoise1D()
; testnoise2D()

Function testnoise1D()
	CreateWindow(1280,720,"Noise Test",256) 
	
  	start_offset# = 777
	loop = True
	While loop
		PollEvents()
		If IsKeyHit(256) Then loop = False
		start_offset = start_offset + 0.1
		offset# = start_offset		
		RenderScene()
		Clear2D()
		For x=0 To GetWindowWidth()
			offset = offset + 0.005
			y% = noise(offset) * 400 + 400
			Set2DFillColor 1,1,0,1
			Draw2DLine x,y,x,GetWindowHeight()
		Next	
		Present()
	Wend 
End Function 

Function testnoise2D()
	; if you try make this window larger than 
	; about 400x400 you'll exceed the max buffer size
	; I'm guessing for number of 2D draw calls per frame	
	CreateWindow(400,400,"Noise Test",0)  
	
  	start_offset#=777
	loop = True
	While loop
		PollEvents()
		If IsKeyHit(256) Then loop = False
		start_offset = start_offset + 0.01		
		RenderScene()
		Clear2D()
		xoffset#=start_offset
	  	For x=0 To GetWindowWidth()
		    yoffset#=start_offset
		    For y=0 To GetWindowHeight()     
		      yoffset=yoffset+0.01
				c# = noise(xoffset,yoffset)
				Set2DFillColor c,c,c,1
		      Draw2DPoint x,y
			Next
			xoffset=xoffset+0.01 	 
		Next
		Present()
	Wend 
End Function 