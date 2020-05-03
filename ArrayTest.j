.class public ArrayTest
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 99
    .limit locals 99

    iconst_1
    newarray int
    astore_1

    iconst_2
    istore_2

    iload_2
    newarray int
    astore_3

    iload_2
    istore 4

    aload_1
    iconst_0
    iconst_2
    iastore

    aload_1
    iconst_1
    iaload
    istore_2

    aload_3
    iload_2
    aload_1
    iload_2
    iload 4
    iadd
    iaload
    iastore

    return
.end method
