.class public TestLength
.super java/lang/Object

.method public<init>()V
    aload 0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([L/java/lang/String;)V
    .limit_stack 99
    .limit_locals 99

    new TestLength
    dup
    invokespecial TestLength/<init>()V
    astore 1

    iastore

    iconst_1
    istore 3

    iconst_1
    astore 4

    aload 0
    aload 4
    invokevirtual TestLength/add(Boolean)TestLength
    astore 5
    return
.end method

.method public add(TestLength)TestLength
    .limit_stack 99
    .limit_locals 99

    aload 1
    areturn
.end method

.method public add(I)I
    .limit_stack 99
    .limit_locals 99

    iload 1
    iconst_1
    iadd
    ireturn
.end method

.method public add([I)[I
    .limit_stack 99
    .limit_locals 99

    aload 1
    areturn
.end method

.method public add(Z)Boolean
    .limit_stack 99
    .limit_locals 99

    aload 1
    areturn
.end method
