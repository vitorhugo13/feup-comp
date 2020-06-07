.class public Life
.super java/lang/Object

.field public _LOOPS_PER_MS I
.field public _xMax I
.field public _UNDERPOP_LIM I
.field public _OVERPOP_LIM I
.field public _field [I
.field public _yMax I
.field public _REPRODUCE_NUM I

.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 2
    .limit locals 3

    new Life
    dup
    invokespecial Life/<init>()V
    astore_1

    aload_1
    invokevirtual Life/init()Z
    pop

    iconst_1
    ifeq end_while_0
  start_while_0:
    aload_1
    invokevirtual Life/printField()Z
    pop
    aload_1
    invokevirtual Life/update()Z
    pop
    invokestatic io/read()I
    istore_2
    iconst_1
    ifne start_while_0
  end_while_0:

    return

.end method

.method public init()Z
    .limit stack 3
    .limit locals 3

    iconst_1
    newarray int
    astore_1

    aload_0
    iconst_2
    putfield Life/_UNDERPOP_LIM I

    aload_0
    iconst_3
    putfield Life/_OVERPOP_LIM I

    aload_0
    iconst_3
    putfield Life/_REPRODUCE_NUM I

    aload_0
    ldc 225000
    putfield Life/_LOOPS_PER_MS I

    aload_0
    aload_0
    aload_1
    invokevirtual Life/field([I)[I
    putfield Life/_field [I

    aload_1
    iconst_0
    iaload
    istore_2

    aload_0
    iload_2
    iconst_1
    isub
    putfield Life/_xMax I

    aload_0
    aload_0
    getfield Life/_field [I
    arraylength
    iload_2
    idiv
    iconst_1
    isub
    putfield Life/_yMax I


    iconst_1
    ireturn

.end method

.method public field([I)[I
    .limit stack 3
    .limit locals 3

    bipush 100
    newarray int
    astore_2

    aload_1
    iconst_0
    bipush 10
    iastore

    aload_2
    iconst_0
    iconst_0
    iastore

    aload_2
    iconst_1
    iconst_0
    iastore

    aload_2
    iconst_2
    iconst_1
    iastore

    aload_2
    iconst_3
    iconst_0
    iastore

    aload_2
    iconst_4
    iconst_0
    iastore

    aload_2
    iconst_5
    iconst_0
    iastore

    aload_2
    bipush 6
    iconst_0
    iastore

    aload_2
    bipush 7
    iconst_0
    iastore

    aload_2
    bipush 8
    iconst_0
    iastore

    aload_2
    bipush 9
    iconst_0
    iastore

    aload_2
    bipush 10
    iconst_1
    iastore

    aload_2
    bipush 11
    iconst_0
    iastore

    aload_2
    bipush 12
    iconst_1
    iastore

    aload_2
    bipush 13
    iconst_0
    iastore

    aload_2
    bipush 14
    iconst_0
    iastore

    aload_2
    bipush 15
    iconst_0
    iastore

    aload_2
    bipush 16
    iconst_0
    iastore

    aload_2
    bipush 17
    iconst_0
    iastore

    aload_2
    bipush 18
    iconst_0
    iastore

    aload_2
    bipush 19
    iconst_0
    iastore

    aload_2
    bipush 20
    iconst_0
    iastore

    aload_2
    bipush 21
    iconst_1
    iastore

    aload_2
    bipush 22
    iconst_1
    iastore

    aload_2
    bipush 23
    iconst_0
    iastore

    aload_2
    bipush 24
    iconst_0
    iastore

    aload_2
    bipush 25
    iconst_0
    iastore

    aload_2
    bipush 26
    iconst_0
    iastore

    aload_2
    bipush 27
    iconst_0
    iastore

    aload_2
    bipush 28
    iconst_0
    iastore

    aload_2
    bipush 29
    iconst_0
    iastore

    aload_2
    bipush 30
    iconst_0
    iastore

    aload_2
    bipush 31
    iconst_0
    iastore

    aload_2
    bipush 32
    iconst_0
    iastore

    aload_2
    bipush 33
    iconst_0
    iastore

    aload_2
    bipush 34
    iconst_0
    iastore

    aload_2
    bipush 35
    iconst_0
    iastore

    aload_2
    bipush 36
    iconst_0
    iastore

    aload_2
    bipush 37
    iconst_0
    iastore

    aload_2
    bipush 38
    iconst_0
    iastore

    aload_2
    bipush 39
    iconst_0
    iastore

    aload_2
    bipush 40
    iconst_0
    iastore

    aload_2
    bipush 41
    iconst_0
    iastore

    aload_2
    bipush 42
    iconst_0
    iastore

    aload_2
    bipush 43
    iconst_0
    iastore

    aload_2
    bipush 44
    iconst_0
    iastore

    aload_2
    bipush 45
    iconst_0
    iastore

    aload_2
    bipush 46
    iconst_0
    iastore

    aload_2
    bipush 47
    iconst_0
    iastore

    aload_2
    bipush 48
    iconst_0
    iastore

    aload_2
    bipush 49
    iconst_0
    iastore

    aload_2
    bipush 50
    iconst_0
    iastore

    aload_2
    bipush 51
    iconst_0
    iastore

    aload_2
    bipush 52
    iconst_0
    iastore

    aload_2
    bipush 53
    iconst_0
    iastore

    aload_2
    bipush 54
    iconst_0
    iastore

    aload_2
    bipush 55
    iconst_0
    iastore

    aload_2
    bipush 56
    iconst_0
    iastore

    aload_2
    bipush 57
    iconst_0
    iastore

    aload_2
    bipush 58
    iconst_0
    iastore

    aload_2
    bipush 59
    iconst_0
    iastore

    aload_2
    bipush 60
    iconst_0
    iastore

    aload_2
    bipush 61
    iconst_0
    iastore

    aload_2
    bipush 62
    iconst_0
    iastore

    aload_2
    bipush 63
    iconst_0
    iastore

    aload_2
    bipush 64
    iconst_0
    iastore

    aload_2
    bipush 65
    iconst_0
    iastore

    aload_2
    bipush 66
    iconst_0
    iastore

    aload_2
    bipush 67
    iconst_0
    iastore

    aload_2
    bipush 68
    iconst_0
    iastore

    aload_2
    bipush 69
    iconst_0
    iastore

    aload_2
    bipush 70
    iconst_0
    iastore

    aload_2
    bipush 71
    iconst_0
    iastore

    aload_2
    bipush 72
    iconst_0
    iastore

    aload_2
    bipush 73
    iconst_0
    iastore

    aload_2
    bipush 74
    iconst_0
    iastore

    aload_2
    bipush 75
    iconst_0
    iastore

    aload_2
    bipush 76
    iconst_0
    iastore

    aload_2
    bipush 77
    iconst_0
    iastore

    aload_2
    bipush 78
    iconst_0
    iastore

    aload_2
    bipush 79
    iconst_0
    iastore

    aload_2
    bipush 80
    iconst_0
    iastore

    aload_2
    bipush 81
    iconst_0
    iastore

    aload_2
    bipush 82
    iconst_0
    iastore

    aload_2
    bipush 83
    iconst_0
    iastore

    aload_2
    bipush 84
    iconst_0
    iastore

    aload_2
    bipush 85
    iconst_0
    iastore

    aload_2
    bipush 86
    iconst_0
    iastore

    aload_2
    bipush 87
    iconst_0
    iastore

    aload_2
    bipush 88
    iconst_0
    iastore

    aload_2
    bipush 89
    iconst_0
    iastore

    aload_2
    bipush 90
    iconst_0
    iastore

    aload_2
    bipush 91
    iconst_0
    iastore

    aload_2
    bipush 92
    iconst_0
    iastore

    aload_2
    bipush 93
    iconst_0
    iastore

    aload_2
    bipush 94
    iconst_0
    iastore

    aload_2
    bipush 95
    iconst_0
    iastore

    aload_2
    bipush 96
    iconst_0
    iastore

    aload_2
    bipush 97
    iconst_0
    iastore

    aload_2
    bipush 98
    iconst_0
    iastore

    aload_2
    bipush 99
    iconst_0
    iastore


    aload_2
    areturn

.end method

.method public update()Z
    .limit stack 6
    .limit locals 6

    aload_0
    getfield Life/_field [I
    arraylength
    newarray int
    astore_1

    iconst_0
    istore_2

    iload_2
    aload_0
    getfield Life/_field [I
    arraylength
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq end_while_1
  start_while_1:
    aload_0
    getfield Life/_field [I
    iload_2
    iaload
    istore_3
    aload_0
    iload_2
    invokevirtual Life/getLiveNeighborN(I)I
    istore 4
    iload_3
    iconst_1
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    iconst_1
    ixor
    ifeq else_0
    aload_0
    iload 4
    aload_0
    getfield Life/_UNDERPOP_LIM I
    invokevirtual Life/ge(II)Z
    aload_0
    iload 4
    aload_0
    getfield Life/_OVERPOP_LIM I
    invokevirtual Life/le(II)Z
    iand
    istore 5
    iload 5
    iconst_1
    ixor
    ifeq else_1
    aload_1
    iload_2
    iconst_0
    iastore
    goto endif_1
  else_1:
    aload_1
    iload_2
    aload_0
    getfield Life/_field [I
    iload_2
    iaload
    iastore
  endif_1:
    goto endif_0
  else_0:
    aload_0
    iload 4
    aload_0
    getfield Life/_REPRODUCE_NUM I
    invokevirtual Life/eq(II)Z
    ifeq else_2
    aload_1
    iload_2
    iconst_1
    iastore
    goto endif_2
  else_2:
    aload_1
    iload_2
    aload_0
    getfield Life/_field [I
    iload_2
    iaload
    iastore
  endif_2:
  endif_0:
    iload_2
    iconst_1
    iadd
    istore_2
    iload_2
    aload_0
    getfield Life/_field [I
    arraylength
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifne start_while_1
  end_while_1:

    aload_0
    aload_1
    putfield Life/_field [I


    iconst_1
    ireturn

.end method

.method public printField()Z
    .limit stack 7
    .limit locals 3

    iconst_0
    istore_1

    iconst_0
    istore_2

    iload_1
    aload_0
    getfield Life/_field [I
    arraylength
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    ifeq end_while_2
  start_while_2:
    aload_0
    iload_2
    aload_0
    getfield Life/_xMax I
    invokevirtual Life/gt(II)Z
    ifeq else_3
    invokestatic io/println()V
    iconst_0
    istore_2
    goto endif_3
  else_3:
  endif_3:
    aload_0
    getfield Life/_field [I
    iload_1
    iaload
    invokestatic io/print(I)V
    iload_1
    iconst_1
    iadd
    istore_1
    iload_2
    iconst_1
    iadd
    istore_2
    iload_1
    aload_0
    getfield Life/_field [I
    arraylength
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    ifne start_while_2
  end_while_2:

    invokestatic io/println()V

    invokestatic io/println()V


    iconst_1
    ireturn

.end method

.method public trIdx(II)I
    .limit stack 3
    .limit locals 3


    iload_1
    aload_0
    getfield Life/_xMax I
    iconst_1
    iadd
    iload_2
    imul
    iadd
    ireturn

.end method

.method public cartIdx(I)[I
    .limit stack 3
    .limit locals 6

    aload_0
    getfield Life/_xMax I
    iconst_1
    iadd
    istore_2

    iload_1
    iload_2
    idiv
    istore_3

    iload_1
    iload_3
    iload_2
    imul
    isub
    istore 4

    iconst_2
    newarray int
    astore 5

    aload 5
    iconst_0
    iload 4
    iastore

    aload 5
    iconst_1
    iload_3
    iastore


    aload 5
    areturn

.end method

.method public getNeighborCoords(I)[I
    .limit stack 7
    .limit locals 10

    aload_0
    iload_1
    invokevirtual Life/cartIdx(I)[I
    astore_2

    aload_2
    iconst_0
    iaload
    istore_3

    aload_2
    iconst_1
    iaload
    istore 4

    iload_3
    aload_0
    getfield Life/_xMax I
    if_icmplt lt_true_5
    iconst_0
    goto lt_false_5
  lt_true_5:
    iconst_1
  lt_false_5:
    ifeq else_4
    iload_3
    iconst_1
    iadd
    istore 5
    aload_0
    iload_3
    iconst_0
    invokevirtual Life/gt(II)Z
    ifeq else_5
    iload_3
    iconst_1
    isub
    istore 6
    goto endif_5
  else_5:
    aload_0
    getfield Life/_xMax I
    istore 6
  endif_5:
    goto endif_4
  else_4:
    iconst_0
    istore 5
    iload_3
    iconst_1
    isub
    istore 6
  endif_4:

    iload 4
    aload_0
    getfield Life/_yMax I
    if_icmplt lt_true_6
    iconst_0
    goto lt_false_6
  lt_true_6:
    iconst_1
  lt_false_6:
    ifeq else_6
    iload 4
    iconst_1
    iadd
    istore 7
    aload_0
    iload 4
    iconst_0
    invokevirtual Life/gt(II)Z
    ifeq else_7
    iload 4
    iconst_1
    isub
    istore 8
    goto endif_7
  else_7:
    aload_0
    getfield Life/_yMax I
    istore 8
  endif_7:
    goto endif_6
  else_6:
    iconst_0
    istore 7
    iload 4
    iconst_1
    isub
    istore 8
  endif_6:

    bipush 8
    newarray int
    astore 9

    aload 9
    iconst_0
    aload_0
    iload_3
    iload 8
    invokevirtual Life/trIdx(II)I
    iastore

    aload 9
    iconst_1
    aload_0
    iload 6
    iload 8
    invokevirtual Life/trIdx(II)I
    iastore

    aload 9
    iconst_2
    aload_0
    iload 6
    iload 4
    invokevirtual Life/trIdx(II)I
    iastore

    aload 9
    iconst_3
    aload_0
    iload 6
    iload 7
    invokevirtual Life/trIdx(II)I
    iastore

    aload 9
    iconst_4
    aload_0
    iload_3
    iload 7
    invokevirtual Life/trIdx(II)I
    iastore

    aload 9
    iconst_5
    aload_0
    iload 5
    iload 7
    invokevirtual Life/trIdx(II)I
    iastore

    aload 9
    bipush 6
    aload_0
    iload 5
    iload 4
    invokevirtual Life/trIdx(II)I
    iastore

    aload 9
    bipush 7
    aload_0
    iload 5
    iload 8
    invokevirtual Life/trIdx(II)I
    iastore


    aload 9
    areturn

.end method

.method public getLiveNeighborN(I)I
    .limit stack 5
    .limit locals 5

    iconst_0
    istore_2

    aload_0
    iload_1
    invokevirtual Life/getNeighborCoords(I)[I
    astore_3

    iconst_0
    istore 4

    iload 4
    aload_3
    arraylength
    if_icmplt lt_true_7
    iconst_0
    goto lt_false_7
  lt_true_7:
    iconst_1
  lt_false_7:
    ifeq end_while_3
  start_while_3:
    aload_0
    aload_0
    getfield Life/_field [I
    aload_3
    iload 4
    iaload
    iaload
    iconst_0
    invokevirtual Life/ne(II)Z
    ifeq else_8
    iload_2
    iconst_1
    iadd
    istore_2
    goto endif_8
  else_8:
  endif_8:
    iload 4
    iconst_1
    iadd
    istore 4
    iload 4
    aload_3
    arraylength
    if_icmplt lt_true_8
    iconst_0
    goto lt_false_8
  lt_true_8:
    iconst_1
  lt_false_8:
    ifne start_while_3
  end_while_3:


    iload_2
    ireturn

.end method

.method public busyWait(I)Z
    .limit stack 3
    .limit locals 4

    iload_1
    aload_0
    getfield Life/_LOOPS_PER_MS I
    imul
    istore_2

    iconst_0
    istore_3

    iload_3
    iload_2
    if_icmplt lt_true_9
    iconst_0
    goto lt_false_9
  lt_true_9:
    iconst_1
  lt_false_9:
    ifeq end_while_4
  start_while_4:
    iload_3
    iconst_1
    iadd
    istore_3
    iload_3
    iload_2
    if_icmplt lt_true_10
    iconst_0
    goto lt_false_10
  lt_true_10:
    iconst_1
  lt_false_10:
    ifne start_while_4
  end_while_4:


    iconst_1
    ireturn

.end method

.method public eq(II)Z
    .limit stack 4
    .limit locals 3


    aload_0
    iload_1
    iload_2
    invokevirtual Life/lt(II)Z
    iconst_1
    ixor
    aload_0
    iload_2
    iload_1
    invokevirtual Life/lt(II)Z
    iconst_1
    ixor
    iand
    ireturn

.end method

.method public ne(II)Z
    .limit stack 3
    .limit locals 3


    aload_0
    iload_1
    iload_2
    invokevirtual Life/eq(II)Z
    iconst_1
    ixor
    ireturn

.end method

.method public lt(II)Z
    .limit stack 2
    .limit locals 3


    iload_1
    iload_2
    if_icmplt lt_true_11
    iconst_0
    goto lt_false_11
  lt_true_11:
    iconst_1
  lt_false_11:
    ireturn

.end method

.method public le(II)Z
    .limit stack 4
    .limit locals 3


    aload_0
    iload_1
    iload_2
    invokevirtual Life/lt(II)Z
    iconst_1
    ixor
    aload_0
    iload_1
    iload_2
    invokevirtual Life/eq(II)Z
    iconst_1
    ixor
    iand
    iconst_1
    ixor
    ireturn

.end method

.method public gt(II)Z
    .limit stack 3
    .limit locals 3


    aload_0
    iload_1
    iload_2
    invokevirtual Life/le(II)Z
    iconst_1
    ixor
    ireturn

.end method

.method public ge(II)Z
    .limit stack 4
    .limit locals 3


    aload_0
    iload_1
    iload_2
    invokevirtual Life/gt(II)Z
    iconst_1
    ixor
    aload_0
    iload_1
    iload_2
    invokevirtual Life/eq(II)Z
    iconst_1
    ixor
    iand
    iconst_1
    ixor
    ireturn

.end method
