.class public Test2
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public closestElement([II)I
    .limit stack 99
    .limit locals 99

    istore_3




    iload_-1
    ireturn
.end method

.method public binarySearch([II)I
    .limit stack 99
    .limit locals 99

    iconst_0
    istore_3

    iconst_0
    istore 4

    iconst_0
    istore 5

    istore 6

    iload 6
    istore 7

    iconst_0
    istore 8

    iconst_0
    istore 9

    iconst_0
    istore 10

    iconst_0
    istore 11




    iload_3
    ireturn
.end method

.method public getClosest(III)I
    .limit stack 99
    .limit locals 99



    iload_-1
    ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 99
    .limit locals 99

    bipush 20
    newarray int
    astore_1

    aload_1
    iconst_0
    iconst_0
    iastore

    aload_1
    iconst_1
    iconst_1
    iastore

    aload_1
    iconst_2
    iconst_2
    iastore

    aload_1
    iconst_3
    iconst_3
    iastore

    aload_1
    iconst_4
    iconst_4
    iastore

    aload_1
    iconst_5
    iconst_5
    iastore

    aload_1
    bipush 6
    bipush 6
    iastore

    aload_1
    bipush 7
    bipush 7
    iastore

    aload_1
    bipush 8
    bipush 8
    iastore

    aload_1
    bipush 9
    bipush 9
    iastore

    aload_1
    bipush 10
    bipush 10
    iastore

    aload_1
    bipush 11
    bipush 11
    iastore

    aload_1
    bipush 12
    bipush 12
    iastore

    aload_1
    bipush 13
    bipush 13
    iastore

    aload_1
    bipush 14
    bipush 14
    iastore

    aload_1
    bipush 15
    bipush 15
    iastore

    aload_1
    bipush 16
    bipush 16
    iastore

    aload_1
    bipush 17
    bipush 17
    iastore

    aload_1
    bipush 18
    bipush 18
    iastore

    aload_1
    bipush 19
    bipush 19
    iastore

    iconst_0
    bipush 20
    isub
    bipush 20
    invokestatic MathUtils/random(II)I
    istore_2

    aload_1
    iload_2
    invokevirtual Test2/closestElement([II)I
    istore_3

    iload_3
    invokestatic ioPlus/printResult(I)V

    return
.end method

.method public equals(II)Z
    .limit stack 99
    .limit locals 99



    iload_-1
    ireturn
.end method

.method public minorEquals(II)Z
    .limit stack 99
    .limit locals 99



    iload_-1
    ireturn
.end method
