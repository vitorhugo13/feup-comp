.class public Lazysort
.super Quicksort


.method public <init>()V
    aload_0
    invokenonvirtual Quicksort/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 5
    .limit locals 5

    bipush 10
    newarray int
    astore_1

    iconst_0
    istore_2

    iload_2
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
    iload_2
    aload_1
    arraylength
    iload_2
    isub
    iastore
    iload_2
    iconst_1
    iadd
    istore_2
    iload_2
    aload_1
    arraylength
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifne start_while_0
  end_while_0:

    new Lazysort
    dup
    invokespecial Lazysort/<init>()V
    astore_3

    aload_3
    aload_1
    invokevirtual Lazysort/quicksort([I)Z
    pop

    aload_3
    aload_1
    invokevirtual Lazysort/printL([I)Z
    istore 4

    return

.end method

.method public quicksort([I)Z
    .limit stack 6
    .limit locals 3

    iconst_0
    iconst_5
    invokestatic MathUtils/random(II)I
    iconst_4
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifeq else_0
    aload_0
    aload_1
    invokevirtual Lazysort/beLazy([I)Z
    pop
    iconst_1
    istore_2
    goto endif_0
  else_0:
    iconst_0
    istore_2
  endif_0:

    iload_2
    ifeq else_1
    iload_2
    iconst_1
    ixor
    istore_2
    goto endif_1
  else_1:
    aload_0
    aload_1
    iconst_0
    aload_1
    arraylength
    iconst_1
    isub
    invokevirtual Lazysort/quicksort([III)Z
    istore_2
  endif_1:


    iload_2
    ireturn

.end method

.method public beLazy([I)Z
    .limit stack 7
    .limit locals 4

    aload_1
    arraylength
    istore_2

    iconst_0
    istore_3

    iload_3
    iload_2
    iconst_2
    idiv
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    ifeq end_while_1
  start_while_1:
    aload_1
    iload_3
    iconst_0
    bipush 10
    invokestatic MathUtils/random(II)I
    iastore
    iload_3
    iconst_1
    iadd
    istore_3
    iload_3
    iload_2
    iconst_2
    idiv
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    ifne start_while_1
  end_while_1:

    iload_3
    iload_2
    if_icmplt lt_true_5
    iconst_0
    goto lt_false_5
  lt_true_5:
    iconst_1
  lt_false_5:
    ifeq end_while_2
  start_while_2:
    aload_1
    iload_3
    iconst_0
    bipush 10
    invokestatic MathUtils/random(II)I
    iconst_1
    iadd
    iastore
    iload_3
    iconst_1
    iadd
    istore_3
    iload_3
    iload_2
    if_icmplt lt_true_6
    iconst_0
    goto lt_false_6
  lt_true_6:
    iconst_1
  lt_false_6:
    ifne start_while_2
  end_while_2:


    iconst_1
    ireturn

.end method
