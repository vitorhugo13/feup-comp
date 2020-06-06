.class public Folding
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 3
    .limit locals 3

    new Folding
    dup
    invokespecial Folding/<init>()V
    astore_1

    iconst_3
    iconst_3
    invokevirtual Folding/test(I)I
    iadd
    istore_2

    bipush 6
    invokestatic io/println(I)V

    return

.end method

.method public test(I)I
    .limit stack 1
    .limit locals 2


    iconst_3
    ireturn

.end method
