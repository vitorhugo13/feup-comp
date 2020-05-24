.class public Test3
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 6
    .limit locals 9

    new Test3
    dup
    invokespecial Test3/<init>()V
    astore_1

    bipush 34
    istore_2

    iconst_0
    iconst_5
    isub
    bipush 25
    invokestatic MathUtils/random(II)I
    istore_3

    iload_3
    bipush 20
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq else_0
    iconst_0
    istore 4
    bipush 15
    iload_3
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifeq else_0
    bipush 12
    istore 5
    iload_3
    bipush 17
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifeq else_0
    bipush 12
    istore_2
    iconst_1
    istore 6
    goto endif_0
  else_0:
    iconst_1
    istore 7
    iconst_2
    istore 6
  endif_0:
    goto endif_0
  else_0:
    iconst_0
    istore 5
    iconst_3
    istore 6
  endif_0:
    goto endif_0
  else_0:
    iconst_2
    istore 5
    iconst_4
    istore 6
  endif_0:

    iload 4
    iload 5
    iadd
    iload 7
    iadd
    iload_2
    iadd
    iload 6
    iadd
    aload_1
    invokevirtual Test3/getValue()I
    iadd
    istore 8

    iload 8
    invokestatic ioPlus/printResult(I)V

    return

.end method

.method public getValue()I
    .limit stack 3
    .limit locals 3

    iconst_0
    bipush 25
    invokestatic MathUtils/random(II)I
    istore_1

    iload_1
    bipush 10
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    ifeq end_while_0
  start_while_0:
    iconst_2
    istore_2
    iload_1
    iconst_1
    iadd
    istore_1
    iload_1
    bipush 10
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    ifne start_while_0
  end_while_0:


    iload_2
    ireturn

.end method
