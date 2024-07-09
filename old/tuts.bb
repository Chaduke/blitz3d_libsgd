camera = CreateCamera()
CameraViewport camera,0,0,GraphicsWidth()/2,GraphicsHeight()/2
CameraRange camera,5,5000
CameraZoom camera,1.5
CameraFogColor camera,0,128,255
CameraFogRange camera,1,1000
CameraFogMode camera,1
clouds = CreatePlane()
RotateEntity clouds,0,0,180
PositionEntity clouds,0,100,0
