.class public MonteCarloPi
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public performSingleEstimate()Z
    .limit stack 3
    .limit locals 5

    iconst_0
    bipush 100
    isub
    bipush 100
    invokestatic MathUtils/random(II)I
    istore_1

    iconst_0
    bipush 100
    isub
    bipush 100
    invokestatic MathUtils/random(II)I
    istore_2

    iload_1
    iload_1
    imul
    iload_2
    iload_2
    imul
    iadd
    bipush 100
    idiv
    istore_3

    iload_3
    bipush 100
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq else_0
    iconst_1
    istore 4
    goto endif_0
  else_0:
    iconst_0
    istore 4
  endif_0:


    iload 4
    ireturn

.end method

.method public estimatePi100(I)I
    .limit stack 4
    .limit locals 5

    iconst_0
    istore_2

    iconst_0
    istore_3

    iload_2
    iload_1
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifeq end_while_0
  start_while_0:
    aload_0
    invokevirtual MonteCarloPi/performSingleEstimate()Z
    ifeq else_1
    iload_3
    iconst_1
    iadd
    istore_3
    goto endif_1
  else_1:
  endif_1:
    iload_2
    iconst_1
    iadd
    istore_2
    iload_2
    iload_1
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifne start_while_0
  end_while_0:

    sipush 400
    iload_3
    imul
    iload_1
    idiv
    istore 4


    iload 4
    ireturn

.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 3
    .limit locals 3

    invokestatic ioPlus/requestNumber()I
    istore_1

    new MonteCarloPi
    dup
    invokespecial MonteCarloPi/<init>()V
    iload_1
    invokevirtual MonteCarloPi/estimatePi100(I)I
    istore_2

    iload_2
    invokestatic ioPlus/printResult(I)V

    return

.end method
