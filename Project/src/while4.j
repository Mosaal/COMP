.source while4.j
.class public while4
.super java/lang/Object

.field static a I = 0
.field static b I = 1
.field static c [I

;
; CONSTRUCTORS
;
.method static <clinit>()V
	.limit stack 2
	.limit locals 0

	bipush 10
	newarray int
	putstatic while4/c [I

	return
.end method

.method public <init>()V
	.limit stack 2

	aload 0
	invokenonvirtual java/lang/Object/<init>()V

	return
.end method



;
; FUNCTIONS
;
.method public f1([III)V
	.limit locals 5
	.limit stack 2

	; ASSIGNMENT
	bipush 0
	istore 4

	return
.end method

.method public f(II[I)I
	.limit locals 5
	.limit stack 2

	; ASSIGNMENT
	bipush 2
	istore_1

	iload_1
	ireturn 
.end method

