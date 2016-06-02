.source while4.j
.class public while4
.super java/lang/Object

.field private a I = 0
.field private b I = 1
.field private c [I

.method public <init>()V
	.limit stack 2
	aload 0
	invokenonvirtual java/lang/Object/<init>()V

	aload 0
	ldc 0
	putfield while4/a I

	aload 0
	ldc 1
	putfield while4/b I

	aload 0
	ldc 10
	newarray int
	putfield while4/c [I

	return
.end method

.method public f1([III)V
	.limit locals 5

	invokestatic while4/f(II[I)I

	return
.end method

.method public f(II[I)I
	.limit locals 4

	return
.end method

