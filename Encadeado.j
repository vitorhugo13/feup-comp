.class public Encadeado
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 99
    .limit locals 99

    iconst_3
    newarray int
    astore_1

    iconst_3
    newarray int
    astore_2

    aload_1
    iconst_0
    iaload
    aload_2
    bipush 9
    iaload
    iadd
    istore_3

    return
.end method

.method public add(I)I
    .limit stack 99
    .limit locals 99


    iload_1
    ireturn
.end method
