.class public Folding
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 2

    new Folding
    dup
    invokespecial Folding/<init>()V
    astore_1

    iconst_4
    invokestatic io/println(I)V

    return

.end method

.method public test(I)I
    .limit stack 2
    .limit locals 2


    iconst_1
    iload_1
    iadd
    ireturn

.end method
