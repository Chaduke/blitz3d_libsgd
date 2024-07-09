; Reading and writing custom types to files using ReadFile, WriteFile and CloseFile   
; Initialise some variables for the example  

Type HighScore  
	Field Name$  
	Field Score%  
	Field Level%  
End Type   

Best.HighScore = New HighScore  
Best\Name$ = "Mark"  
Best\Score% = 11657  
Best\Level% = 34   

; Open a file to write to 
fileout = WriteFile("mydata.dat")
   
; Write the information to the file  
WriteString( fileout, Best\Name$ )  
WriteInt( fileout, Best\Score% )  
WriteByte( fileout, Best\Level% )   
; Close the file  
CloseFile( fileout )
   
; Open the file to Read  
filein = ReadFile("mydata.dat")  
 
; Lets read the Greatest score from the file  
Greatest.HighScore = New HighScore  
Greatest\Name$ = ReadString$( filein )  
Greatest\Score = ReadInt( filein )  
Greatest\Level = ReadByte( filein )   
; Close the file once reading is finished  
CloseFile( filein )   

msg$ = "High score record read from - mydata.dat " + Chr$(13)
msg$=msg$ + "Name = " + Greatest\Name + Chr$(13)
msg$=msg$ + "Score = " + Greatest\Score + Chr$(13)
msg$=msg$ + "Level = " + Greatest\Level + Chr$(13)  

Alert msg$
