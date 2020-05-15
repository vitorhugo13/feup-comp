.class public Test1
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 99
    .limit locals 99

    new Test1
    dup
    invokespecial Test1/<init>()V
    astore_1

    aload_1
    invokevirtual Test1/initializeSquare()[I
    astore_2


    iload_-1
    invokestatic ioPlus/printResult(I)V

    return
.end method

.method public initializeSquare()[I
    .limit stack 99
    .limit locals 99

    iconst_4
    newarray int
    astore_1

    aload_1
    iconst_0
    iconst_0
    bipush 9
    isub
    bipush 9
    invokestatic MathUtils/random(II)I
    iastore

    aload_1
    iconst_1
    iconst_0
    bipush 9
    isub
    bipush 9
    invokestatic MathUtils/random(II)I
    iastore

    aload_1
    iconst_2
    iconst_0
    bipush 9
    isub
    bipush 9
    invokestatic MathUtils/random(II)I
    iastore

    aload_1
    iconst_3
    iconst_0
    bipush 9
    isub
    bipush 9
    invokestatic MathUtils/random(II)I
    iastore


    aload_1
    areturn
.end method

.method public verifySquare([I)Z
    .limit stack 99
    .limit locals 99



    iload_-1
    ireturn
.end method

.method public verifyLines([I)Z
    .limit stack 99
    .limit locals 99

    aload_1
    iconst_0
    iaload
    aload_1
    iconst_1
    iaload
    iadd
    istore_2

    aload_1
    iconst_2
    iaload
    aload_1
    iconst_3
    iaload
    iadd
    istore_3



    iload_-1
    ireturn
.end method

.method public verifyCol([I)Z
    .limit stack 99
    .limit locals 99

    aload_1
    iconst_0
    iaload
    aload_1
    iconst_2
    iaload
    iadd
    istore_2

    aload_1
    iconst_1
    iaload
    aload_1
    iconst_3
    iaload
    iadd
    istore_3



    iload_-1
    ireturn
.end method

.method public verifyDiagonal([I)Z
    .limit stack 99
    .limit locals 99

    aload_1
    iconst_0
    iaload
    aload_1
    iconst_3
    iaload
    iadd
    istore_2

    aload_1
    iconst_2
    iaload
    aload_1
    iconst_1
    iaload
    iadd
    istore_3



    iload_-1
    ireturn
.end method
