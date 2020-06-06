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

    iconst_3
    newarray int
    astore_1

    aload_1
    iconst_1
    iconst_0
    iastore

    iconst_3
    istore_2

    iload_2
    iconst_1
    iadd
    istore_2

    return

.end method
