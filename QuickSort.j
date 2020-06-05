.class public QuickSort
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 5
    .limit locals 4

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

    new QuickSort
    dup
    invokespecial QuickSort/<init>()V
    astore_3

    aload_3
    aload_1
    invokevirtual QuickSort/quicksort([I)Z
    pop

    aload_3
    aload_1
    invokevirtual QuickSort/printL([I)Z
    pop

    return

.end method

.method public printL([I)Z
    .limit stack 4
    .limit locals 3

    iconst_0
    istore_2

    iload_2
    aload_1
    arraylength
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifeq end_while_1
  start_while_1:
    aload_1
    iload_2
    iaload
    invokestatic io/println(I)V
    iload_2
    iconst_1
    iadd
    istore_2
    iload_2
    aload_1
    arraylength
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    ifne start_while_1
  end_while_1:


    iconst_1
    ireturn

.end method

.method public quicksort([I)Z
    .limit stack 5
    .limit locals 2


    aload_0
    aload_1
    iconst_0
    aload_1
    arraylength
    iconst_1
    isub
    invokevirtual QuickSort/quicksort([III)Z
    ireturn

.end method

.method public quicksort([III)Z
    .limit stack 6
    .limit locals 5

    iload_2
    iload_3
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    ifeq else_0
    aload_0
    aload_1
    iload_2
    iload_3
    invokevirtual QuickSort/partition([III)I
    istore 4
    aload_0
    aload_1
    iload_2
    iload 4
    iconst_1
    isub
    invokevirtual QuickSort/quicksort([III)Z
    pop
    aload_0
    aload_1
    iload 4
    iconst_1
    iadd
    iload_3
    invokevirtual QuickSort/quicksort([III)Z
    pop
    goto endif_0
  else_0:
  endif_0:


    iconst_1
    ireturn

.end method

.method public partition([III)I
    .limit stack 7
    .limit locals 8

    aload_1
    iload_3
    iaload
    istore 4

    iload_2
    istore 5

    iload_2
    istore 6

    iload 6
    iload_3
    if_icmplt lt_true_5
    iconst_0
    goto lt_false_5
  lt_true_5:
    iconst_1
  lt_false_5:
    ifeq end_while_2
  start_while_2:
    aload_1
    iload 6
    iaload
    iload 4
    if_icmplt lt_true_6
    iconst_0
    goto lt_false_6
  lt_true_6:
    iconst_1
  lt_false_6:
    ifeq else_1
    aload_1
    iload 5
    iaload
    istore 7
    aload_1
    iload 5
    aload_1
    iload 6
    iaload
    iastore
    aload_1
    iload 6
    iload 7
    iastore
    iload 5
    iconst_1
    iadd
    istore 5
    goto endif_1
  else_1:
  endif_1:
    iload 6
    iconst_1
    iadd
    istore 6
    iload 6
    iload_3
    if_icmplt lt_true_7
    iconst_0
    goto lt_false_7
  lt_true_7:
    iconst_1
  lt_false_7:
    ifne start_while_2
  end_while_2:

    aload_1
    iload 5
    iaload
    istore 7

    aload_1
    iload 5
    aload_1
    iload_3
    iaload
    iastore

    aload_1
    iload_3
    iload 7
    iastore


    iload 5
    ireturn

.end method
