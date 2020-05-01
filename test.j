.class public ArrayTest
.super Simple

.method public<init>()V
    aload 0
    invokenonvirtual Simple/<init>()V
    return
.end method

.method public static main([L/java/lang/String;)V
    .limit_stack 99
    .limit_locals 99

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

    iconst_1
    iconst_0
    iconst_2
    iastore

    iconst_1
    iconst_1
    iaload
    istore_2

    iconst_3
    iload_2
    iconst_1
    iload_2
    iload 4
    iadd
    iaload
    iastore

    return
.end method
