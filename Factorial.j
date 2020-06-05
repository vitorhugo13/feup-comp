.class public Factorial
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public factorial(I)I
    .limit stack 3
    .limit locals 4

    iconst_1
    istore_2

    iconst_1
    istore_3

    iload_3
    iconst_1
    isub
    iload_1
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq end_while_0
  start_while_0:
    iload_2
    iload_3
    imul
    istore_2
    iload_3
    iconst_1
    iadd
    istore_3
    iload_3
    iconst_1
    isub
    iload_1
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifne start_while_0
  end_while_0:


    iload_2
    ireturn

.end method
