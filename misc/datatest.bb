; data test 

Read x$,y$,z$
Alert "Pass 1 : " + Chr$(13) + x$ + Chr$(13) + y$ + Chr$(13) + z$
Restore dataArea1
Read x$,y$,z$
Alert "Pass 2 : " + Chr$(13) + x$ + Chr$(13) + y$ + Chr$(13) + z$

.dataArea1
Data "This is Data area 1"

.dataArea2
Data "This is Data area 2"
Data "This is another string"