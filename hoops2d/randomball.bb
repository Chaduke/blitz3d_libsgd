; randomball.bb
; by Chaduke
; 20240723

Include "../engine/gameapp.bb"

ga.GameApp = CreateGameApp()
Const numballs = 100

While ga\loop
	BeginFrame ga
	count = 0
	For r.RandomBall = Each RandomBall
		UpdateRandomBall r
		count = count + 1
	Next 
	If count < numballs Then CreateRandomBall()
	EndFrame ga
Wend
End 

Type RandomBall 
	Field radius#
	Field damping#
	Field pos.Vec2
	Field vel.Vec2
	Field acc.Vec2
	Field rot.Vec2
	Field red#,green#,blue#,alpha#
	Field life,age	
End Type

Function CreateRandomBall.RandomBall()
	Local b.RandomBall = New RandomBall
		b\radius# = Rand(10,100)
		b\damping# = Rnd(0.1,1.0)
		b\pos = CreateVec2( Rand( 0,GetWindowWidth() ),Rand( 0,GetWindowHeight() ) )
		b\vel = CreateVec2(Rnd(-5.0,5.0),Rnd(-5.0,5.0))
		b\acc = CreateVec2(Rnd(-0.2,0.2),Rnd(-0.2,0.2))
		b\rot = CreateVec2(0,Rnd(-5,5))	
		b\red# = Rnd(0.2,1.0)
		b\green# = Rnd(0.2,1.0)
		b\blue# = Rnd(0.2,1.0)
		b\alpha# = Rnd(0.5,1.0)
		b\life = Rand(100,1000)
		b\age=0
	Return b
End Function
 
Function UpdateRandomBall(b.RandomBall)
	AddToVec2 b\vel,b\acc
	AddToVec2 b\pos,b\vel
	b\rot\x = b\rot\x + b\rot\y
	
	; bounce off left wall
	If b\pos\x < 0 Then
		b\pos\x = 0
		b\vel\x = -b\vel\x * b\damping#		
		b\acc\y = -b\acc\y 
	End If
	
	; bounce off right wall
	If b\pos\x > GetWindowWidth() Then
		b\pos\x = GetWindowWidth()
		b\vel\x = -b\vel\x * b\damping#	
		b\acc\y = -b\acc\y
	End If
	
	; bounce off top
	If b\pos\y < 0 Then
		b\pos\y = 0
		b\vel\y = -b\vel\y * b\damping#	
		b\acc\x = -b\acc\x 	
	End If
	
	; bounce off bottom	
	If b\pos\y > GetWindowHeight() Then
		b\pos\y = GetWindowHeight()
		b\vel\y = -b\vel\y * b\damping#	
		b\acc\x = -b\acc\x
	End If
	
	b\age = b\age + 1
	If b\age > b\life Then 
		Delete b
	Else 	
		DrawBall b
	End If	 	
End Function 

Function DrawBall(b.RandomBall)
	Set2DFillColor b\red,b\green,b\blue,b\alpha
	Draw2DOval b\pos\x - b\radius,b\pos\y - b\radius,b\pos\x + b\radius,b\pos\y + b\radius
	; draw line to indicate rotation 
	Set2DFillColor 0,0,0,1
	Draw2DLine b\pos\x,b\pos\y,b\pos\x + Sin(b\rot\x) * b\radius,b\pos\y + Cos(b\rot\x) * b\radius
End Function 