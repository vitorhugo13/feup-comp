.class public Test2
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 5
    .limit locals 5

    new Test2
    dup
    invokespecial Test2/<init>()V
    astore_1

    iconst_2
    newarray int
    astore_2

    iconst_0
    istore_3

    bipush 20
    newarray int
    astore 4

    aload 4
    iconst_0
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    iconst_1
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    iconst_2
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    iconst_3
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    iconst_4
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    iconst_5
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 6
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 7
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 8
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 9
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 10
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 11
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 12
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 13
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 14
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 15
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 16
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 17
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 18
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload 4
    bipush 19
    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    iastore

    aload_1
    aload 4
    invokevirtual Test2/findLimits([I)[I
    astore_2

    aload_2
    iconst_0
    iaload
    invokestatic ioPlus/printResult(I)V

    aload_2
    iconst_1
    iaload
    invokestatic ioPlus/printResult(I)V

    aload_1
    aload 4
    invokevirtual Test2/findAverage([I)I
    invokestatic ioPlus/printResult(I)V

    return

.end method

.method public findLimits([I)[I
    .limit stack 7
    .limit locals 6

    iconst_2
    newarray int
    astore_2

    bipush 100
    istore_3

    bipush 100
    ineg
    istore 4

    iconst_0
    istore 5

    iload 5
    aload_1
    arraylength
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq end_while_0
  start_while_0:
    aload_1
    iload 5
    iaload
    iload_3
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifeq else_0
    aload_1
    iload 5
    iaload
    istore_3
    goto endif_0
  else_0:
  endif_0:
    iload 4
    aload_1
    iload 5
    iaload
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifeq else_1
    aload_1
    iload 5
    iaload
    istore 4
    goto endif_1
  else_1:
  endif_1:
    iload 5
    iconst_1
    iadd
    istore 5
    iload 5
    aload_1
    arraylength
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    ifne start_while_0
  end_while_0:

    aload_2
    iconst_0
    iload_3
    iastore

    aload_2
    iconst_1
    iload 4
    iastore


    aload_2
    areturn

.end method

.method public findAverage([I)I
    .limit stack 4
    .limit locals 5

    aload_1
    arraylength
    istore_2

    iconst_0
    istore_3

    iconst_0
    istore 4

    iload_3
    iload_2
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    ifeq end_while_1
  start_while_1:
    iload 4
    aload_1
    iload_3
    iaload
    iadd
    istore 4
    iload_3
    iconst_1
    iadd
    istore_3
    iload_3
    iload_2
    if_icmplt lt_true_5
    iconst_0
    goto lt_false_5
  lt_true_5:
    iconst_1
  lt_false_5:
    ifne start_while_1
  end_while_1:


    iload 4
    iload_2
    idiv
    ireturn

.end method
