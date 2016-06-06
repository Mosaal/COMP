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
.method public static f([II)I
	.limit locals 5
	.limit stack 4

	; ASSIGNMENT
	iconst_0
	istore_2

	; ASSIGNMENT
	iconst_0
	istore 4

	; WHILE
label1:
	iload_1
	iload 4
	if_icmple label2
	; ASSIGNMENT
	aload_0
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
	iconst_1
	iadd
	istore 4

	goto label1
label2:
	iload_2
	ireturn 
.end method

