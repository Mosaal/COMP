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
.method public static f1([III)V
	.limit locals 4
	.limit stack 4

	; ASSIGNMENT
	iconst_0
	istore_3

	getstatic while4/a I
	getstatic while4/b I
	getstatic while4/c [I
	invokestatic while4/f(II[I)I	return
.end method

.method public static f(II[I)I
	.limit locals 4
	.limit stack 4

	; ASSIGNMENT
	iconst_2
	istore_3

	iload_3
	ireturn 
.end method

