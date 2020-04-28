.class public Simple
.super java/lang/Object

.method public<init>()V
    aload 0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([L/java/lang/String;)V
    .limit_stack 99
    .limit_locals 99

    bipush 30
    istore 1

    iconst_0
    bipush 10
    isub
    istore 2

    new Simple
    dup
    invokespecial Simple/<init>()V
    astore 3

    aload 3
    aload 1
    aload 2
    invokevirtual Simple/add(II)I
    istore 4

    aload 4
    invokestatic io/println(I)V
    return
.end method

.method public add(II)I
    .limit_stack 99
    .limit_locals 99

    aload 1
    aload 2
    iadd
    ireturn
.end method
