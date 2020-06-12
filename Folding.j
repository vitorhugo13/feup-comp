.class public Folding
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 3
    .limit locals 6

    new Folding
    dup
    invokespecial Folding/<init>()V
    astore_1

    iconst_2
    istore_2

    iconst_2
    aload_1
    invokevirtual Folding/itest()I
    iadd
    istore_3

    iconst_1
    istore 4

    aload_1
    invokevirtual Folding/btest()Z
    istore 5

    return

.end method

.method public itest()I
    .limit stack 1
    .limit locals 1


    bipush 11
    ireturn

.end method

.method public btest()Z
    .limit stack 1
    .limit locals 1


    iconst_0
    ireturn

.end method
