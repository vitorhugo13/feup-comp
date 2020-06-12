.class public Test1
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 3
    .limit locals 4

    new Test1
    dup
    invokespecial Test1/<init>()V
    astore_1

    aload_1
    invokevirtual Test1/initializeSquare()[I
    astore_2

    aload_1
    aload_2
    invokevirtual Test1/verifySquare([I)Z
    ifeq else_0
    iconst_1
    istore_3
    goto endif_0
  else_0:
    iconst_0
    istore_3
  endif_0:

    iload_3
    invokestatic ioPlus/printResult(I)V

    return

.end method

.method public initializeSquare()[I
    .limit stack 4
    .limit locals 2

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
    .limit stack 3
    .limit locals 3

    aload_0
    aload_1
    invokevirtual Test1/verifyCol([I)Z
    aload_0
    aload_1
    invokevirtual Test1/verifyDiagonal([I)Z
    iand
    aload_0
    aload_1
    invokevirtual Test1/verifyLines([I)Z
    iand
    ifeq else_1
    iconst_1
    istore_2
    goto endif_1
  else_1:
    iconst_0
    istore_2
  endif_1:


    iload_2
    ireturn

.end method

.method public verifyLines([I)Z
    .limit stack 3
    .limit locals 5

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

    iload_2
    iload_3
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq else_2
    iconst_0
    istore 4
    goto endif_2
  else_2:
    iload_3
    iload_2
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifeq else_3
    iconst_0
    istore 4
    goto endif_3
  else_3:
    iconst_1
    istore 4
  endif_3:
  endif_2:


    iload 4
    ireturn

.end method

.method public verifyCol([I)Z
    .limit stack 3
    .limit locals 5

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

    iload_2
    iload_3
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifeq else_4
    iconst_0
    istore 4
    goto endif_4
  else_4:
    iload_3
    iload_2
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    ifeq else_5
    iconst_0
    istore 4
    goto endif_5
  else_5:
    iconst_1
    istore 4
  endif_5:
  endif_4:


    iload 4
    ireturn

.end method

.method public verifyDiagonal([I)Z
    .limit stack 3
    .limit locals 5

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

    iload_2
    iload_3
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    ifeq else_6
    iconst_0
    istore 4
    goto endif_6
  else_6:
    iload_3
    iload_2
    if_icmplt lt_true_5
    iconst_0
    goto lt_false_5
  lt_true_5:
    iconst_1
  lt_false_5:
    ifeq else_7
    iconst_0
    istore 4
    goto endif_7
  else_7:
    iconst_1
    istore 4
  endif_7:
  endif_6:


    iload 4
    ireturn

.end method
