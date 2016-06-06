.source while4.j
.class public while4
.super java/lang/Object


;
; CONSTRUCTORS
;
.method static <clinit>()V
	.limit stack 2
	.limit locals 0

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
.method public static f1(I[I)I
	.limit locals 5
	.limit stack 3

	; ASSIGNMENT
	bipush 0
	istore_2

	; ASSIGNMENT
	bipush 0
	istore 4

	; ASSIGNMENT
	aload_1
	iload 4
	iaload
	istore_3

	; ASSIGNMENT
	iload_2
	iload_3
	iadd
	istore_2

	; ASSIGNMENT
	iload 4
	bipush 1
	iadd
	istore 4

	iload_2
	ireturn 
.end method

