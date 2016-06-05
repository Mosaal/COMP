.class public HelloWorld
.super java/lang/Object

.method public <init>()V
   aload 0
   invokenonvirtual java/lang/Object/<init>()V
   return
.end method

.method public static main([Ljava/lang/String;)V
   .limit stack 5
   .limit locals 5

   ; Invoke a function
   invokestatic while4/f1()I

   ; Get a variable and store in local
	 getstatic while4/a I
   istore_0

   ; Print
	 getstatic java/lang/System/out Ljava/io/PrintStream;
	 iload_0
   invokevirtual java/io/PrintStream/println(I)V
   return
.end method
