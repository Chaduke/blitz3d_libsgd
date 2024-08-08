; debug_window.bb
; by Chaduke
; 20240724 


Type DebugWindow
	Field gui.GUIWindow
	Field player_x.Widget
	Field player_y.Widget
	Field player_z.Widget
	Field player_vx.Widget
	Field player_vy.Widget
	Field player_vz.Widget
	Field player_jumping.Widget
	Field player_collision_count.Widget
	Field player_collision_nx.Widget
	Field player_collision_ny.Widget
	Field player_collision_nz.Widget
	
	Field ball_x.Widget
	Field ball_y.Widget
	Field ball_z.Widget
	Field ball_vx.Widget
	Field ball_vy.Widget
	Field ball_vz.Widget	
	Field ball_collision_count.Widget
	Field ball_collision_nx.Widget
	Field ball_collision_ny.Widget
	Field ball_collision_nz.Widget
	
	Field camera_x.Widget
	Field camera_y.Widget
	Field camera_z.Widget	
	Field pivot_x.Widget
	Field pivot_y.Widget
	Field pivot_z.Widget	
End Type 

Function CreateDebugWindow.DebugWindow(g.GameApp)
	Local d.DebugWindow = New DebugWindow
		d\gui = CreateGUI(g\gui_font,"Debug Window",5,5)
		d\player_x = AddWidget(d\gui,"Player X",0,64,0,128,70)
		d\player_y = AddWidget(d\gui,"Player Y",0,0,0,10,70)
		d\player_z = AddWidget(d\gui,"Player Z",0,64,0,128,70)
		d\player_vx = AddWidget(d\gui,"Player VX",0,0,0,10,70)
		d\player_vy = AddWidget(d\gui,"Player VY",0,0,0,10,70)
		d\player_vz = AddWidget(d\gui,"Player VZ",0,0,0,10,70)
		d\player_jumping = AddWidget(d\gui,"Player Jumping",2,"False")	
		d\player_collision_count = AddWidget(d\gui,"Player Collisions",1,0,0,1,70)	
		d\player_collision_nx = AddWidget(d\gui,"Player Collision NX",0,0,0.0,1.0,70)
		d\player_collision_ny = AddWidget(d\gui,"Player Collision NY",0,0,0.0,1.0,70)
		d\player_collision_nz = AddWidget(d\gui,"Player Collision NZ",0,0,0.0,1.0,70)
		
		d\ball_x = AddWidget(d\gui,"Ball X",0,64,0,128,70)
		d\ball_y = AddWidget(d\gui,"Ball Y",0,0,0,10,70)
		d\ball_z = AddWidget(d\gui,"Ball Z",0,64,0,128,70)
		d\ball_vx = AddWidget(d\gui,"Ball VX",0,0,0,10,70)
		d\ball_vy = AddWidget(d\gui,"Ball VY",0,0,0,10,70)
		d\ball_vz = AddWidget(d\gui,"Ball VZ",0,0,0,10,70)
			
		d\ball_collision_count = AddWidget(d\gui,"Ball Collisions",1,0,0,1,70)	
		d\ball_collision_nx = AddWidget(d\gui,"Ball Collision NX",0,0,0.0,1.0,70)
		d\ball_collision_ny = AddWidget(d\gui,"Ball Collision NY",0,0,0.0,1.0,70)
		d\ball_collision_nz = AddWidget(d\gui,"Ball Collision NZ",0,0,0.0,1.0,70)

		d\camera_x = AddWidget(d\gui,"Camera X",0,64,0,128,70)
		d\camera_y = AddWidget(d\gui,"Camera Y",0,0,0,10,70)
		d\camera_z = AddWidget(d\gui,"Camera Z",0,64,0,128,70)
		d\pivot_x = AddWidget(d\gui,"Pivot X",0,64,0,128,70)
		d\pivot_y = AddWidget(d\gui,"Pivot Y",0,0,0,10,70)
		d\pivot_z = AddWidget(d\gui,"Pivot Z",0,64,0,128,70)
	Return d
End Function 

Function UpdateDebugWindow(d.DebugWindow,g.GameApp,p.Player,b.Basketball)
	d\player_x\val_float# = GetEntityX(p\pivot)
	d\player_y\val_float# = GetEntityY(p\pivot)
	d\player_z\val_float# = GetEntityZ(p\pivot)
	
	d\player_vx\val_float# = p\vel\x
	d\player_vy\val_float# = p\vel\y
	d\player_vz\val_float# = p\vel\z
	
	If p\jumping Then d\player_jumping\val_string$ = "True" Else d\player_jumping\val_string$	= "False"
	
	Local pc = GetCollisionCount(p\collider)
	d\player_collision_count\val_float# = pc
	If pc > 0 Then 
		d\player_collision_nx\val_float# = GetCollisionNX(p\collider,0)
		d\player_collision_ny\val_float# = GetCollisionNY(p\collider,0)
		d\player_collision_nz\val_float# = GetCollisionNZ(p\collider,0)
	End If 
	
	d\ball_x\val_float# = GetEntityX(b\pivot)
	d\ball_y\val_float# = GetEntityY(b\pivot)
	d\ball_z\val_float# = GetEntityZ(b\pivot)
	
	d\ball_vx\val_float# = b\vel\x
	d\ball_vy\val_float# = b\vel\y
	d\ball_vz\val_float# = b\vel\z
	
	d\camera_x\val_float# = GetEntityX(g\camera)
	d\camera_y\val_float# = GetEntityY(g\camera)
	d\camera_z\val_float# = GetEntityZ(g\camera)
	
	d\pivot_x\val_float# = GetEntityX(g\pivot)
	d\pivot_y\val_float# = GetEntityY(g\pivot)
	d\pivot_z\val_float# = GetEntityZ(g\pivot)	
	
	Local bc = GetCollisionCount(b\collider)
	d\ball_collision_count\val_float# = bc
	If bc > 0 Then 
		d\ball_collision_nx\val_float# = GetCollisionNX(b\collider,0)
		d\ball_collision_ny\val_float# = GetCollisionNY(b\collider,0)
		d\ball_collision_nz\val_float# = GetCollisionNZ(b\collider,0)
	End If	
	
End Function 