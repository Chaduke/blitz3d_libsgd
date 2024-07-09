; png_writer.bb
; by Chaduke
; 20240603

; Hexidecimal is used a lot here so I could properly compare files
; in a Hex Editor and make sure the bytes were getting written correctly
; VSCode has a very nice Hex Extension, as well as the ability to view png files

Include "crc.bb" ; for the CRC checks at the end of each chunk

; simple data structure to be passed into the WritePNG function
; this is just to avoid a long list of parameters
; which will likely be an issue when I add different options

Type PNG
	Field width%
	Field height%
End Type

; init the data structure
p.PNG = New PNG
p\width = 1
p\height = 1

; write a test file
WritePNG "assets/materials/test_material/test.png",p

Function WritePNG(path$,p.PNG)
	; open the file for writing 
	fileout = WriteFile(path$)
	
	; ---------------------
	; PNG Signature Section 
	; ---------------------
	
	; the first byte 137, or hex 89 
	; converts To 10001001 in binary setting the Last bit To 1
	; used to detect systems that don't support 8-bit data		
	WriteByte fileout,htob("89")
	; Write out the letters "PNG"
	WriteByte fileout,htob("50") ; P
	WriteByte fileout,htob("4E") ; N
	WriteByte fileout,htob("47") ; G
	; Write a CRLF, DOS style line ending 
	WriteByte fileout,htob("0D")
	WriteByte fileout,htob("0A")
	; Prevent use of Type command under DOS
	WriteByte fileout,htob("1A")
	; UNIX style line ending 
	WriteByte fileout,htob("0A")
	
	; -------------------------
	; Image Header IHDR Section 
	; -------------------------
	
	; identify the chunk as having 13 bytes of data 
	; using a 4 byte integer and identifer IHDR	
	WriteInt32 fileout,13	
	
	; build the header array so we can get a CRC check
	Dim buffer(16)	 ; includes the IHDR id
	
	buffer(0) = htob("49") ; I
	buffer(1) = htob("48") ; H 
	buffer(2) = htob("44") ; D
	buffer(3) = htob("52") ; R
	
	h$ = Hex(p\width) ; 4 byte image width
	buffer(4) = htob(Left$(h$,2))
	buffer(5) = htob(Mid$(h$,3,2))
	buffer(6) = htob(Mid$(h$,5,2))
	buffer(7) = htob(Right$(h$,2))
	h$ = Hex(p\height) ; 4 byte image height
	buffer(8) = htob(Left$(h$,2))
	buffer(9) = htob(Mid$(h$,3,2))
	buffer(10) = htob(Mid$(h$,5,2))
	buffer(11) = htob(Right$(h$,2))
	buffer(12) = htob("08") ; Bits Per Pixel
	
	; Color Type 
	; truecolor = 2
	; truecolor + alpha = 6	
	buffer(13) = htob("02") 
	; Compression Method
	buffer(14) = htob("00") 
	; Filter Method
	buffer(15) = htob("00")
	; Interlaced 0 = false
	buffer(16) = htob("00")
	
	WriteByteArray(fileout,16) ; write the header array to the file 
	
	; Call the CRC function from crc.bb	
	crc% = GetCRC(17) 
		
	; 4 byte checksum 
	WriteInt32 fileout,crc
	
	; -------------------------
	; Image Data IDAT Chunk
	; -------------------------	
	
	; identify the chunk as having 12 bytes of data 
	; using a 4 byte integer		
	WriteInt32 fileout,12
	
	; build the data array so we can get a CRC check
	Dim buffer(15)	 ; includes the IDAT id
	
	buffer(0) = htob("49") ; I
	buffer(1) = htob("44") ; D 
	buffer(2) = htob("41") ; A
	buffer(3) = htob("54") ; T
	
	buffer(4) = htob("08") ; DEFLATE compression method using a 256-byte window
	buffer(5) = htob("D7") ; ZLIB FCHECK value, no dictionary used, maximum compression algorithm
	buffer(6) = htob("63") ; 6 - 11 is a compressed DEFLATE block using the static Huffman code that decodes to 0x00 0xFF 0x00 0x00
	buffer(7) = htob("F8")
	buffer(8) = htob("CF")
	buffer(9) = htob("C0")
	buffer(10) = htob("00")
	buffer(11) = htob("00")
	buffer(12) = htob("03") ; 12 - 15 The ZLIB check value: the Adler32 checksum of the uncompressed data
	buffer(13) = htob("01")
	buffer(14) = htob("01")
	buffer(15) = htob("00")
	
	WriteByteArray(fileout,15) ; write the header array to the file
		
	; Call the CRC function from crc.bb	
	crc% = GetCRC(16)		
	; 4 byte checksum 
	WriteInt32 fileout,crc		
			
	; -------------------------
	; Ending IEND Chunk 
	; -------------------------
	WriteInt fileout,0 ; a 4 byte empty block
	Dim buffer(3)
	buffer(0) = htob("49") ; I
	buffer(1) = htob("45") ; E 
	buffer(2) = htob("4E") ; N
	buffer(3) = htob("44") ; D
	WriteByteArray(fileout,3)
	; Call the CRC function from crc.bb	
	crc% = GetCRC(4)		
	; 4 byte checksum 
	WriteInt32 fileout,crc	
		
	; close the file 
	CloseFile fileout
	Alert "File written to - " + path$
End Function 

Function WriteByteArray(fileout,length)
	For i = 0 To length 
		WriteByte fileout,buffer(i)
	Next 		
End Function 

; required for correct endianness
; this reverses the byte order that WriteInt uses
; WriteInt uses Big Endian 
; but PNG format wants Little Endian
Function WriteInt32(fileout,val%)
	Local h$ = Hex(val)
	WriteByte fileout,htob(Left$(h$,2))
	WriteByte fileout,htob(Mid$(h$,3,2))
	WriteByte fileout,htob(Mid$(h$,5,2))
	WriteByte fileout,htob(Right$(h$,2))
End Function 

; might be required for correct endianness 
; this reverses the byte order
; that results from the Hex function 
; this switches the Hex functions output 
; from Little Endian to Big Endian
; not used at this time 
Function Int32toHex$(val%)
	Local h$ = Hex(val%)
	Local r$ = Right$(h$,2)
	r$ = r$ + Mid$(h$,5,2)
	r$ = r$ + Mid$(h$,3,2)
	r$ = r$ + Left$(h$,2)
	Return r$
End Function 

; convert a 2 character Hex Value string into a byte integer
Function htob(v$)
	; extract the two characters into 
	; separate strings c1$ and c2$
	c1$ = Left(v$,1)
	c2$ = Right(v$,1)
	; convert them to their ascii values
	; store them in a1 and a2 as integers
	a1% = Asc(c1)
	a2% = Asc(c2)
	; find the decimal value of a1
	; store the value in d1
	If (a1 > 47 And a1<58) Then
		d1 = a1 - 48
	Else If (a1 > 64 And a1 < 71) Then 
		d1 = a1 - 65 + 10
	Else If (a1 > 96 And a1 < 103) Then 
		d1 = a1 - 97 + 10
	Else 
		d1 = 0
	End If
	; do the same for a2 and store in d2
	If (a2 > 47 And a2<58) Then
		d2 = a2 - 48
	Else If (a2 > 64 And a2 < 71) Then 
		d2 = a2 - 65 + 10
	Else If (a2 > 96 And a2 < 103) Then 
		d2 = a2 - 97 + 10
	Else 
		d2 = 0
	End If
	; now we need to get the byte value from 0 - 255	
	d = d1 * 16 + d2		
	Return d
End Function	
