.source while4.j
.class public while4
.super java/lang/Object

.field private a I
.field private b I = 1
.field private c [I
.field private d [I
.field private b I = 2
.field private c I

.method public <init>()V
	.limit stack 2
	aload 0
	invokenonvirtual java/lang/Object/<init>()V

	aload 0
	ldc 0
	putfield while4/a I

	aload 0
	ldc 2
	putfield while4/b I

	aload 0
	ldc 0
	putfield while4/c I

	aload 0
	ldc 10
	newarray int
	putfield while4/c [I

	aload 0
	ldc 5
	newarray int
	putfield while4/d [I

	return
.end method

.method public f1([III)V
	.limit locals 4
If1:
	if_icmpge EndIf1
	goto If1
EndIf1:
While1:
	if_icmpge EndWhile1
	goto While1
EndWhile1:
	return
.end method
