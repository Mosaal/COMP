.class public HelloWorld
.super java/lang/Object

.method public <init>()V
   aload 0
   invokenonvirtual java/lang/Object/<init>()V
   return
.end method

.method public static main([Ljava/lang/String;)V
   .limit stack 10
	 new while4
	 dup
	 invokespecial while4/<init>()V
	 getfield while4/a I
	 istore 0
	 getstatic java/lang/System/out Ljava/io/PrintStream;
	 iload 0
   invokevirtual java/io/PrintStream/println(I)V
   return
.end method
