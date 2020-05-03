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

    new Encadeado
    dup
    invokespecial Encadeado/<init>()V
    astore_1


    aload_1
    iload_-1
    invokevirtual Encadeado/add(I)I
    istore_2

    return
.end method

.method public add(I)I
    .limit stack 99
    .limit locals 99


    iload_1
    ireturn
.end method
