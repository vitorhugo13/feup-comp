.class public Test2
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public closestElement([II)I
    .limit stack 4
    .limit locals 6

    aload_1
    arraylength
    istore_3

    aload_0
    iload_2
    aload_1
    iconst_0
    iaload
    invokevirtual Test2/minorEquals(II)Z
    ifeq else_0
    aload_1
    iconst_0
    iaload
    istore 4
    iconst_1
    istore 5
    goto endif_0
  else_0:
    aload_1
    iload_3
    iconst_1
    isub
    iaload
    iload_2
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq else_0
    aload_1
    iload_3
    iconst_1
    isub
    iaload
    istore 4
    iconst_1
    istore 5
    goto endif_0
  else_0:
    iconst_0
    istore 4
    iconst_0
    istore 5
  endif_0:
  endif_0:

    iload 5
    ifeq else_2
    iload 4
    istore 4
    goto endif_2
  else_2:
    aload_0
    aload_1
    iload_2
    invokevirtual Test2/binarySearch([II)I
    istore 4
  endif_2:


    iload 4
    ireturn

.end method

.method public binarySearch([II)I
    .limit stack 9
    .limit locals 12

    iconst_0
    istore_3

    iconst_0
    istore 4

    iconst_0
    istore 5

    aload_1
    arraylength
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

    iload 4
    iload 7
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifeq end_while_0
  start_while_0:
    iload 4
    iload 7
    iadd
    iconst_2
    idiv
    istore 5
    aload_0
    aload_1
    iload 5
    iaload
    iload_2
    invokevirtual Test2/equals(II)Z
    ifeq else_3
    aload_1
    iload 5
    iaload
    istore_3
    iconst_1
    istore 9
    iconst_1
    istore 8
    bipush 21
    istore 4
    goto endif_3
  else_3:
    iconst_0
    istore 9
  endif_3:
    iload 9
    iconst_1
    ixor
    ifeq else_4
    iload_2
    aload_1
    iload 5
    iaload
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifeq else_4
    aload_0
    iconst_0
    iload 5
    invokevirtual Test2/minorEquals(II)Z
    aload_0
    aload_1
    iload 5
    iconst_1
    isub
    iaload
    iload_2
    invokevirtual Test2/minorEquals(II)Z
    iand
    ifeq else_4
    aload_0
    aload_1
    iload 5
    iconst_1
    isub
    iaload
    aload_1
    iload 5
    iaload
    iload_2
    invokevirtual Test2/getClosest(III)I
    istore_3
    iconst_1
    istore 10
    iconst_1
    istore 8
    goto endif_4
  else_4:
    iconst_0
    istore 10
  endif_4:
    iload 10
    iconst_1
    ixor
    ifeq else_5
    iload 5
    istore 7
    goto endif_5
  else_5:
    bipush 21
    istore 4
  endif_5:
    goto endif_4
  else_4:
    iload 5
    iload 6
    iconst_1
    isub
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    iload_2
    aload_1
    iload 5
    iconst_1
    iadd
    iaload
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    iand
    ifeq else_6
    aload_0
    aload_1
    iload 5
    iaload
    aload_1
    iload 5
    iconst_1
    iadd
    iaload
    iload_2
    invokevirtual Test2/getClosest(III)I
    istore_3
    iconst_1
    istore 11
    iconst_1
    istore 8
    goto endif_6
  else_6:
    iconst_0
    istore 11
  endif_6:
    iload 11
    iconst_1
    ixor
    ifeq else_7
    iload 5
    iconst_1
    iadd
    istore 4
    goto endif_7
  else_7:
    bipush 21
    istore 4
  endif_7:
  endif_4:
    goto endif_4
  else_4:
  endif_4:
    iload 4
    iload 7
    if_icmplt lt_true_5
    iconst_0
    goto lt_false_5
  lt_true_5:
    iconst_1
  lt_false_5:
    ifne start_while_0
  end_while_0:

    iload 8
    iconst_1
    ixor
    ifeq else_10
    aload_1
    iload 5
    iaload
    istore_3
    goto endif_10
  else_10:
  endif_10:


    iload_3
    ireturn

.end method

.method public getClosest(III)I
    .limit stack 3
    .limit locals 5

    iload_2
    iload_3
    isub
    iload_3
    iload_1
    isub
    if_icmplt lt_true_6
    iconst_0
    goto lt_false_6
  lt_true_6:
    iconst_1
  lt_false_6:
    ifeq else_11
    iload_2
    istore 4
    goto endif_11
  else_11:
    iload_1
    istore 4
  endif_11:


    iload 4
    ireturn

.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 5
    .limit locals 5

    new Test2
    dup
    invokespecial Test2/<init>()V
    astore_1

    bipush 20
    newarray int
    astore_2

    aload_2
    iconst_0
    iconst_0
    iastore

    aload_2
    iconst_1
    iconst_1
    iastore

    aload_2
    iconst_2
    iconst_2
    iastore

    aload_2
    iconst_3
    iconst_3
    iastore

    aload_2
    iconst_4
    iconst_4
    iastore

    aload_2
    iconst_5
    iconst_5
    iastore

    aload_2
    bipush 6
    bipush 6
    iastore

    aload_2
    bipush 7
    bipush 7
    iastore

    aload_2
    bipush 8
    bipush 8
    iastore

    aload_2
    bipush 9
    bipush 9
    iastore

    aload_2
    bipush 10
    bipush 10
    iastore

    aload_2
    bipush 11
    bipush 11
    iastore

    aload_2
    bipush 12
    bipush 12
    iastore

    aload_2
    bipush 13
    bipush 13
    iastore

    aload_2
    bipush 14
    bipush 14
    iastore

    aload_2
    bipush 15
    bipush 15
    iastore

    aload_2
    bipush 16
    bipush 16
    iastore

    aload_2
    bipush 17
    bipush 17
    iastore

    aload_2
    bipush 18
    bipush 18
    iastore

    aload_2
    bipush 19
    bipush 19
    iastore

    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    istore_3

    iload_3
    invokestatic ioPlus/printResult(I)V

    aload_1
    aload_2
    iload_3
    invokevirtual Test2/closestElement([II)I
    istore 4

    iload 4
    invokestatic ioPlus/printResult(I)V

    return

.end method

.method public equals(II)Z
    .limit stack 3
    .limit locals 4

    iload_1
    iload_2
    if_icmplt lt_true_7
    iconst_0
    goto lt_false_7
  lt_true_7:
    iconst_1
  lt_false_7:
    ifeq else_12
    iconst_0
    istore_3
    goto endif_12
  else_12:
    iload_2
    iload_1
    if_icmplt lt_true_8
    iconst_0
    goto lt_false_8
  lt_true_8:
    iconst_1
  lt_false_8:
    ifeq else_12
    iconst_0
    istore_3
    goto endif_12
  else_12:
    iconst_1
    istore_3
  endif_12:
  endif_12:


    iload_3
    ireturn

.end method

.method public minorEquals(II)Z
    .limit stack 4
    .limit locals 4

    iload_1
    iload_2
    if_icmplt lt_true_9
    iconst_0
    goto lt_false_9
  lt_true_9:
    iconst_1
  lt_false_9:
    ifeq else_14
    iconst_1
    istore_3
    goto endif_14
  else_14:
    aload_0
    iload_1
    iload_2
    invokevirtual Test2/equals(II)Z
    ifeq else_14
    iconst_1
    istore_3
    goto endif_14
  else_14:
    iconst_0
    istore_3
  endif_14:
  endif_14:


    iload_3
    ireturn

.end method
