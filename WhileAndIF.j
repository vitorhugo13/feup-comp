.class public WhileAndIF
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 7
    .limit locals 5

    bipush 20
    istore_1

    bipush 10
    istore_2

    bipush 10
    newarray int
    astore_3

    iload_1
    iload_2
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq else_0
    iload_1
    iconst_1
    isub
    istore 4
    goto endif_0
  else_0:
    iload_2
    iconst_1
    isub
    istore 4
  endif_0:

    iconst_0
    iconst_1
    isub
    iload 4
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifeq end_while_0
  start_while_0:
    aload_3
    iload 4
    iload_1
    iload_2
    isub
    iastore
    iload 4
    iconst_1
    isub
    istore 4
    iload_1
    iconst_1
    isub
    istore_1
    iload_2
    iconst_1
    isub
    istore_2
    iconst_0
    iconst_1
    isub
    iload 4
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifne start_while_0
  end_while_0:

    iconst_0
    istore 4

    iload 4
    aload_3
    arraylength
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    ifeq end_while_1
  start_while_1:
    aload_3
    iload 4
    iaload
    invokestatic io/println(I)V
    iload 4
    iconst_1
    iadd
    istore 4
    iload 4
    aload_3
    arraylength
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    ifne start_while_1
  end_while_1:

    return

.end method
