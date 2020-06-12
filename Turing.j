.class public Turing
.super java/lang/Object

.field public _NUM_STATES I
.field public _curState I
.field public _L I
.field public _H I
.field public _NTABLE [I
.field public _TAPE [I
.field public _WTABLE [I
.field public _NUM_SYMBOLS I
.field public _R I
.field public _MTABLE [I
.field public _curPos I

.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 2

    new Turing
    dup
    invokespecial Turing/<init>()V
    astore_1

    aload_1
    invokevirtual Turing/init_bb_3s2sy()Z
    pop

    aload_1
    invokevirtual Turing/run()Z
    pop

    return

.end method

.method public init_bb_3s2sy()Z
    .limit stack 6
    .limit locals 1

    aload_0
    aload_0
    iconst_3
    iconst_2
    bipush 18
    invokevirtual Turing/initGeneric(III)[I
    putfield Turing/_TAPE [I

    aload_0
    iconst_0
    iconst_0
    iconst_1
    aload_0
    getfield Turing/_R I
    iconst_1
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_0
    iconst_1
    iconst_1
    aload_0
    getfield Turing/_L I
    iconst_0
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_0
    iconst_2
    iconst_1
    aload_0
    getfield Turing/_L I
    iconst_1
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_1
    iconst_0
    iconst_1
    aload_0
    getfield Turing/_L I
    iconst_2
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_1
    iconst_1
    iconst_1
    aload_0
    getfield Turing/_R I
    iconst_1
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_1
    iconst_2
    iconst_1
    aload_0
    getfield Turing/_R I
    aload_0
    getfield Turing/_H I
    invokevirtual Turing/setTrans(IIIII)Z
    pop


    iconst_1
    ireturn

.end method

.method public init_bb_4s2sy()Z
    .limit stack 6
    .limit locals 1

    aload_0
    aload_0
    iconst_4
    iconst_2
    bipush 20
    invokevirtual Turing/initGeneric(III)[I
    putfield Turing/_TAPE [I

    aload_0
    iconst_0
    iconst_0
    iconst_1
    aload_0
    getfield Turing/_R I
    iconst_1
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_0
    iconst_1
    iconst_1
    aload_0
    getfield Turing/_L I
    iconst_0
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_0
    iconst_2
    iconst_1
    aload_0
    getfield Turing/_R I
    aload_0
    getfield Turing/_H I
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_0
    iconst_3
    iconst_1
    aload_0
    getfield Turing/_R I
    iconst_3
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_1
    iconst_0
    iconst_1
    aload_0
    getfield Turing/_L I
    iconst_1
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_1
    iconst_1
    iconst_0
    aload_0
    getfield Turing/_L I
    iconst_2
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_1
    iconst_2
    iconst_1
    aload_0
    getfield Turing/_L I
    iconst_3
    invokevirtual Turing/setTrans(IIIII)Z
    pop

    aload_0
    iconst_1
    iconst_3
    iconst_0
    aload_0
    getfield Turing/_R I
    iconst_0
    invokevirtual Turing/setTrans(IIIII)Z
    pop


    iconst_1
    ireturn

.end method

.method public run()Z
    .limit stack 2
    .limit locals 4

    aload_0
    astore_1

    iconst_0
    istore_2

    iload_2
    iconst_1
    ixor
    ifeq end_while_0
  start_while_0:
    aload_0
    invokevirtual Turing/printTape()Z
    pop
    invokestatic io/read()I
    istore_3
    aload_1
    invokevirtual Turing/trans()Z
    iconst_1
    ixor
    istore_2
    iload_2
    iconst_1
    ixor
    ifne start_while_0
  end_while_0:

    aload_0
    invokevirtual Turing/printTape()Z
    pop


    iconst_1
    ireturn

.end method

.method public printTape()Z
    .limit stack 13
    .limit locals 2

    iconst_0
    istore_1

    iload_1
    aload_0
    getfield Turing/_TAPE [I
    arraylength
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq end_while_1
  start_while_1:
    iload_1
    aload_0
    getfield Turing/_curPos I
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    iconst_1
    ixor
    aload_0
    getfield Turing/_curPos I
    iload_1
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    iconst_1
    ixor
    iand
    iconst_1
    ixor
    ifeq else_0
    iconst_0
    invokestatic io/print(I)V
    goto endif_0
  else_0:
    aload_0
    getfield Turing/_curState I
    iconst_1
    iadd
    invokestatic io/print(I)V
  endif_0:
    iload_1
    iconst_1
    iadd
    istore_1
    iload_1
    aload_0
    getfield Turing/_TAPE [I
    arraylength
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    ifne start_while_1
  end_while_1:

    invokestatic io/println()V

    iconst_0
    istore_1

    iload_1
    aload_0
    getfield Turing/_TAPE [I
    arraylength
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    ifeq end_while_2
  start_while_2:
    aload_0
    getfield Turing/_TAPE [I
    iload_1
    iaload
    invokestatic io/print(I)V
    iload_1
    iconst_1
    iadd
    istore_1
    iload_1
    aload_0
    getfield Turing/_TAPE [I
    arraylength
    if_icmplt lt_true_5
    iconst_0
    goto lt_false_5
  lt_true_5:
    iconst_1
  lt_false_5:
    ifne start_while_2
  end_while_2:

    invokestatic io/println()V

    invokestatic io/println()V


    iconst_1
    ireturn

.end method

.method public trans()Z
    .limit stack 5
    .limit locals 6

    aload_0
    getfield Turing/_TAPE [I
    aload_0
    getfield Turing/_curPos I
    iaload
    istore_1

    aload_0
    getfield Turing/_WTABLE [I
    aload_0
    iload_1
    aload_0
    getfield Turing/_curState I
    invokevirtual Turing/ss2i(II)I
    iaload
    istore_2

    aload_0
    getfield Turing/_MTABLE [I
    aload_0
    iload_1
    aload_0
    getfield Turing/_curState I
    invokevirtual Turing/ss2i(II)I
    iaload
    istore_3

    aload_0
    getfield Turing/_NTABLE [I
    aload_0
    iload_1
    aload_0
    getfield Turing/_curState I
    invokevirtual Turing/ss2i(II)I
    iaload
    istore 4

    aload_0
    getfield Turing/_TAPE [I
    aload_0
    getfield Turing/_curPos I
    iload_2
    iastore

    aload_0
    aload_0
    getfield Turing/_curPos I
    iload_3
    iadd
    putfield Turing/_curPos I

    aload_0
    iload 4
    putfield Turing/_curState I

    aload_0
    getfield Turing/_H I
    aload_0
    getfield Turing/_curState I
    if_icmplt lt_true_6
    iconst_0
    goto lt_false_6
  lt_true_6:
    iconst_1
  lt_false_6:
    iconst_1
    ixor
    aload_0
    getfield Turing/_curState I
    aload_0
    getfield Turing/_H I
    if_icmplt lt_true_7
    iconst_0
    goto lt_false_7
  lt_true_7:
    iconst_1
  lt_false_7:
    iconst_1
    ixor
    iand
    ifeq else_1
    iconst_0
    istore 5
    goto endif_1
  else_1:
    iconst_1
    istore 5
  endif_1:


    iload 5
    ireturn

.end method

.method public initGeneric(III)[I
    .limit stack 3
    .limit locals 6

    aload_0
    iload_2
    putfield Turing/_NUM_SYMBOLS I

    aload_0
    iload_1
    putfield Turing/_NUM_STATES I

    aload_0
    getfield Turing/_NUM_SYMBOLS I
    aload_0
    getfield Turing/_NUM_STATES I
    imul
    istore 4

    aload_0
    iconst_0
    iconst_1
    isub
    putfield Turing/_H I

    aload_0
    iconst_0
    iconst_1
    isub
    putfield Turing/_L I

    aload_0
    iconst_1
    putfield Turing/_R I

    aload_0
    iload 4
    newarray int
    putfield Turing/_WTABLE [I

    aload_0
    iload 4
    newarray int
    putfield Turing/_MTABLE [I

    aload_0
    iload 4
    newarray int
    putfield Turing/_NTABLE [I

    iload_3
    newarray int
    astore 5

    aload_0
    iconst_0
    putfield Turing/_curState I

    aload_0
    aload 5
    arraylength
    iconst_2
    idiv
    putfield Turing/_curPos I


    aload 5
    areturn

.end method

.method public ss2i(II)I
    .limit stack 2
    .limit locals 3


    iload_1
    aload_0
    getfield Turing/_NUM_STATES I
    imul
    iload_2
    iadd
    ireturn

.end method

.method public setTrans(IIIII)Z
    .limit stack 4
    .limit locals 6

    aload_0
    getfield Turing/_WTABLE [I
    aload_0
    iload_1
    iload_2
    invokevirtual Turing/ss2i(II)I
    iload_3
    iastore

    aload_0
    getfield Turing/_MTABLE [I
    aload_0
    iload_1
    iload_2
    invokevirtual Turing/ss2i(II)I
    iload 4
    iastore

    aload_0
    getfield Turing/_NTABLE [I
    aload_0
    iload_1
    iload_2
    invokevirtual Turing/ss2i(II)I
    iload 5
    iastore


    iconst_1
    ireturn

.end method
