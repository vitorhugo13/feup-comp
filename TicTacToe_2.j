.class public TicTacToe
.super java/lang/Object

.field public _whoseturn I
.field public _row2 [I
.field public _row0 [I
.field public _row1 [I
.field public _movesmade I
.field public _pieces [I

.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public init()Z
    .limit stack 3
    .limit locals 1

    aload_0
    iconst_3
    newarray int
    putfield TicTacToe/_row0 [I

    aload_0
    iconst_3
    newarray int
    putfield TicTacToe/_row1 [I

    aload_0
    iconst_3
    newarray int
    putfield TicTacToe/_row2 [I

    aload_0
    iconst_2
    newarray int
    putfield TicTacToe/_pieces [I

    aload_0
    getfield TicTacToe/_pieces [I
    iconst_0
    iconst_1
    iastore

    aload_0
    getfield TicTacToe/_pieces [I
    iconst_1
    iconst_2
    iastore

    aload_0
    iconst_0
    putfield TicTacToe/_whoseturn I

    aload_0
    iconst_0
    putfield TicTacToe/_movesmade I


    iconst_1
    ireturn

.end method

.method public getRow0()[I
    .limit stack 1
    .limit locals 1


    aload_0
    getfield TicTacToe/_row0 [I
    areturn

.end method

.method public getRow1()[I
    .limit stack 1
    .limit locals 1


    aload_0
    getfield TicTacToe/_row1 [I
    areturn

.end method

.method public getRow2()[I
    .limit stack 1
    .limit locals 1


    aload_0
    getfield TicTacToe/_row2 [I
    areturn

.end method

.method public MoveRow([II)Z
    .limit stack 7
    .limit locals 4

    iload_2
    iconst_0
    if_icmplt lt_true_0
    iconst_0
    goto lt_false_0
  lt_true_0:
    iconst_1
  lt_false_0:
    ifeq else_0
    iconst_0
    istore_3
    goto endif_0
  else_0:
    iconst_2
    iload_2
    if_icmplt lt_true_1
    iconst_0
    goto lt_false_1
  lt_true_1:
    iconst_1
  lt_false_1:
    ifeq else_1
    iconst_0
    istore_3
    goto endif_1
  else_1:
    iconst_0
    aload_1
    iload_2
    iaload
    if_icmplt lt_true_2
    iconst_0
    goto lt_false_2
  lt_true_2:
    iconst_1
  lt_false_2:
    ifeq else_2
    iconst_0
    istore_3
    goto endif_2
  else_2:
    aload_1
    iload_2
    aload_0
    getfield TicTacToe/_pieces [I
    aload_0
    getfield TicTacToe/_whoseturn I
    iaload
    iastore
    aload_0
    aload_0
    getfield TicTacToe/_movesmade I
    iconst_1
    iadd
    putfield TicTacToe/_movesmade I
    iconst_1
    istore_3
  endif_2:
  endif_1:
  endif_0:


    iload_3
    ireturn

.end method

.method public Move(II)Z
    .limit stack 9
    .limit locals 4

    iload_1
    iconst_0
    if_icmplt lt_true_3
    iconst_0
    goto lt_false_3
  lt_true_3:
    iconst_1
  lt_false_3:
    iconst_1
    ixor
    iconst_0
    iload_1
    if_icmplt lt_true_4
    iconst_0
    goto lt_false_4
  lt_true_4:
    iconst_1
  lt_false_4:
    iconst_1
    ixor
    iand
    ifeq else_3
    aload_0
    aload_0
    getfield TicTacToe/_row0 [I
    iload_2
    invokevirtual TicTacToe/MoveRow([II)Z
    istore_3
    goto endif_3
  else_3:
    iload_1
    iconst_1
    if_icmplt lt_true_5
    iconst_0
    goto lt_false_5
  lt_true_5:
    iconst_1
  lt_false_5:
    iconst_1
    ixor
    iconst_1
    iload_1
    if_icmplt lt_true_6
    iconst_0
    goto lt_false_6
  lt_true_6:
    iconst_1
  lt_false_6:
    iconst_1
    ixor
    iand
    ifeq else_4
    aload_0
    aload_0
    getfield TicTacToe/_row1 [I
    iload_2
    invokevirtual TicTacToe/MoveRow([II)Z
    istore_3
    goto endif_4
  else_4:
    iload_1
    iconst_2
    if_icmplt lt_true_7
    iconst_0
    goto lt_false_7
  lt_true_7:
    iconst_1
  lt_false_7:
    iconst_1
    ixor
    iconst_2
    iload_1
    if_icmplt lt_true_8
    iconst_0
    goto lt_false_8
  lt_true_8:
    iconst_1
  lt_false_8:
    iconst_1
    ixor
    iand
    ifeq else_5
    aload_0
    aload_0
    getfield TicTacToe/_row2 [I
    iload_2
    invokevirtual TicTacToe/MoveRow([II)Z
    istore_3
    goto endif_5
  else_5:
    iconst_0
    istore_3
  endif_5:
  endif_4:
  endif_3:


    iload_3
    ireturn

.end method

.method public inbounds(II)Z
    .limit stack 5
    .limit locals 4

    iload_1
    iconst_0
    if_icmplt lt_true_9
    iconst_0
    goto lt_false_9
  lt_true_9:
    iconst_1
  lt_false_9:
    ifeq else_6
    iconst_0
    istore_3
    goto endif_6
  else_6:
    iload_2
    iconst_0
    if_icmplt lt_true_10
    iconst_0
    goto lt_false_10
  lt_true_10:
    iconst_1
  lt_false_10:
    ifeq else_7
    iconst_0
    istore_3
    goto endif_7
  else_7:
    iconst_2
    iload_1
    if_icmplt lt_true_11
    iconst_0
    goto lt_false_11
  lt_true_11:
    iconst_1
  lt_false_11:
    ifeq else_8
    iconst_0
    istore_3
    goto endif_8
  else_8:
    iconst_2
    iload_2
    if_icmplt lt_true_12
    iconst_0
    goto lt_false_12
  lt_true_12:
    iconst_1
  lt_false_12:
    ifeq else_9
    iconst_0
    istore_3
    goto endif_9
  else_9:
    iconst_1
    istore_3
  endif_9:
  endif_8:
  endif_7:
  endif_6:


    iload_3
    ireturn

.end method

.method public changeturn()Z
    .limit stack 3
    .limit locals 1

    aload_0
    iconst_1
    aload_0
    getfield TicTacToe/_whoseturn I
    isub
    putfield TicTacToe/_whoseturn I


    iconst_1
    ireturn

.end method

.method public getCurrentPlayer()I
    .limit stack 2
    .limit locals 1


    aload_0
    getfield TicTacToe/_whoseturn I
    iconst_1
    iadd
    ireturn

.end method

.method public winner()I
    .limit stack 17
    .limit locals 4

    iconst_3
    newarray int
    astore_1

    aload_0
    getfield TicTacToe/_row0 [I
    invokestatic BoardBase/sameArray([I)Z
    iconst_0
    aload_0
    getfield TicTacToe/_row0 [I
    iconst_0
    iaload
    if_icmplt lt_true_13
    iconst_0
    goto lt_false_13
  lt_true_13:
    iconst_1
  lt_false_13:
    iand
    ifeq else_10
    aload_0
    getfield TicTacToe/_row0 [I
    iconst_0
    iaload
    istore_2
    goto endif_10
  else_10:
    aload_0
    getfield TicTacToe/_row1 [I
    invokestatic BoardBase/sameArray([I)Z
    iconst_0
    aload_0
    getfield TicTacToe/_row1 [I
    iconst_0
    iaload
    if_icmplt lt_true_14
    iconst_0
    goto lt_false_14
  lt_true_14:
    iconst_1
  lt_false_14:
    iand
    ifeq else_11
    aload_0
    getfield TicTacToe/_row1 [I
    iconst_0
    iaload
    istore_2
    goto endif_11
  else_11:
    aload_0
    getfield TicTacToe/_row2 [I
    invokestatic BoardBase/sameArray([I)Z
    iconst_0
    aload_0
    getfield TicTacToe/_row2 [I
    iconst_0
    iaload
    if_icmplt lt_true_15
    iconst_0
    goto lt_false_15
  lt_true_15:
    iconst_1
  lt_false_15:
    iand
    ifeq else_12
    aload_0
    getfield TicTacToe/_row2 [I
    iconst_0
    iaload
    istore_2
    goto endif_12
  else_12:
    iconst_0
    istore_3
    iload_2
    iconst_1
    if_icmplt lt_true_16
    iconst_0
    goto lt_false_16
  lt_true_16:
    iconst_1
  lt_false_16:
    iload_3
    iconst_3
    if_icmplt lt_true_17
    iconst_0
    goto lt_false_17
  lt_true_17:
    iconst_1
  lt_false_17:
    iand
    ifeq end_while_0
  start_while_0:
    aload_1
    iconst_0
    aload_0
    getfield TicTacToe/_row0 [I
    iload_3
    iaload
    iastore
    aload_1
    iconst_1
    aload_0
    getfield TicTacToe/_row1 [I
    iload_3
    iaload
    iastore
    aload_1
    iconst_2
    aload_0
    getfield TicTacToe/_row2 [I
    iload_3
    iaload
    iastore
    aload_1
    invokestatic BoardBase/sameArray([I)Z
    iconst_0
    aload_1
    iconst_0
    iaload
    if_icmplt lt_true_18
    iconst_0
    goto lt_false_18
  lt_true_18:
    iconst_1
  lt_false_18:
    iand
    ifeq else_13
    aload_1
    iconst_0
    iaload
    istore_2
    goto endif_13
  else_13:
  endif_13:
    iload_3
    iconst_1
    iadd
    istore_3
    iload_2
    iconst_1
    if_icmplt lt_true_19
    iconst_0
    goto lt_false_19
  lt_true_19:
    iconst_1
  lt_false_19:
    iload_3
    iconst_3
    if_icmplt lt_true_20
    iconst_0
    goto lt_false_20
  lt_true_20:
    iconst_1
  lt_false_20:
    iand
    ifne start_while_0
  end_while_0:
    iload_2
    iconst_1
    if_icmplt lt_true_21
    iconst_0
    goto lt_false_21
  lt_true_21:
    iconst_1
  lt_false_21:
    ifeq else_14
    aload_1
    iconst_0
    aload_0
    getfield TicTacToe/_row0 [I
    iconst_0
    iaload
    iastore
    aload_1
    iconst_1
    aload_0
    getfield TicTacToe/_row1 [I
    iconst_1
    iaload
    iastore
    aload_1
    iconst_2
    aload_0
    getfield TicTacToe/_row2 [I
    iconst_2
    iaload
    iastore
    aload_1
    invokestatic BoardBase/sameArray([I)Z
    iconst_0
    aload_1
    iconst_0
    iaload
    if_icmplt lt_true_22
    iconst_0
    goto lt_false_22
  lt_true_22:
    iconst_1
  lt_false_22:
    iand
    ifeq else_15
    aload_1
    iconst_0
    iaload
    istore_2
    goto endif_15
  else_15:
    aload_1
    iconst_0
    aload_0
    getfield TicTacToe/_row0 [I
    iconst_2
    iaload
    iastore
    aload_1
    iconst_1
    aload_0
    getfield TicTacToe/_row1 [I
    iconst_1
    iaload
    iastore
    aload_1
    iconst_2
    aload_0
    getfield TicTacToe/_row2 [I
    iconst_0
    iaload
    iastore
    aload_1
    invokestatic BoardBase/sameArray([I)Z
    iconst_0
    aload_1
    iconst_0
    iaload
    if_icmplt lt_true_23
    iconst_0
    goto lt_false_23
  lt_true_23:
    iconst_1
  lt_false_23:
    iand
    ifeq else_16
    aload_1
    iconst_0
    iaload
    istore_2
    goto endif_16
  else_16:
  endif_16:
  endif_15:
    goto endif_14
  else_14:
  endif_14:
  endif_12:
  endif_11:
  endif_10:

    iload_2
    iconst_1
    if_icmplt lt_true_24
    iconst_0
    goto lt_false_24
  lt_true_24:
    iconst_1
  lt_false_24:
    aload_0
    getfield TicTacToe/_movesmade I
    bipush 9
    if_icmplt lt_true_25
    iconst_0
    goto lt_false_25
  lt_true_25:
    iconst_1
  lt_false_25:
    iconst_1
    ixor
    iand
    bipush 9
    aload_0
    getfield TicTacToe/_movesmade I
    if_icmplt lt_true_26
    iconst_0
    goto lt_false_26
  lt_true_26:
    iconst_1
  lt_false_26:
    iconst_1
    ixor
    iand
    ifeq else_17
    iconst_0
    istore_2
    goto endif_17
  else_17:
  endif_17:


    iload_2
    ireturn

.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 11
    .limit locals 6

    new TicTacToe
    dup
    invokespecial TicTacToe/<init>()V
    astore_1

    aload_1
    invokevirtual TicTacToe/init()Z
    pop

    aload_1
    invokevirtual TicTacToe/winner()I
    iconst_1
    ineg
    if_icmplt lt_true_27
    iconst_0
    goto lt_false_27
  lt_true_27:
    iconst_1
  lt_false_27:
    iconst_1
    ixor
    iconst_1
    ineg
    aload_1
    invokevirtual TicTacToe/winner()I
    if_icmplt lt_true_28
    iconst_0
    goto lt_false_28
  lt_true_28:
    iconst_1
  lt_false_28:
    iconst_1
    ixor
    iand
    ifeq end_while_1
  start_while_1:
    iconst_0
    istore_2
    iload_2
    iconst_1
    ixor
    ifeq end_while_2
  start_while_2:
    aload_1
    invokevirtual TicTacToe/getRow0()[I
    aload_1
    invokevirtual TicTacToe/getRow1()[I
    aload_1
    invokevirtual TicTacToe/getRow2()[I
    invokestatic BoardBase/printBoard([I[I[I)V
    aload_1
    invokevirtual TicTacToe/getCurrentPlayer()I
    istore_3
    iload_3
    invokestatic BoardBase/playerTurn(I)[I
    astore 4
    aload_1
    aload 4
    iconst_0
    iaload
    aload 4
    iconst_1
    iaload
    invokevirtual TicTacToe/inbounds(II)Z
    iconst_1
    ixor
    ifeq else_18
    invokestatic BoardBase/wrongMove()V
    goto endif_18
  else_18:
    aload_1
    aload 4
    iconst_0
    iaload
    aload 4
    iconst_1
    iaload
    invokevirtual TicTacToe/Move(II)Z
    iconst_1
    ixor
    ifeq else_19
    invokestatic BoardBase/placeTaken()V
    goto endif_19
  else_19:
    iconst_1
    istore_2
  endif_19:
  endif_18:
    iload_2
    iconst_1
    ixor
    ifne start_while_2
  end_while_2:
    aload_1
    invokevirtual TicTacToe/changeturn()Z
    pop
    aload_1
    invokevirtual TicTacToe/winner()I
    iconst_1
    ineg
    if_icmplt lt_true_29
    iconst_0
    goto lt_false_29
  lt_true_29:
    iconst_1
  lt_false_29:
    iconst_1
    ixor
    iconst_1
    ineg
    aload_1
    invokevirtual TicTacToe/winner()I
    if_icmplt lt_true_30
    iconst_0
    goto lt_false_30
  lt_true_30:
    iconst_1
  lt_false_30:
    iconst_1
    ixor
    iand
    ifne start_while_1
  end_while_1:

    aload_1
    invokevirtual TicTacToe/getRow0()[I
    aload_1
    invokevirtual TicTacToe/getRow1()[I
    aload_1
    invokevirtual TicTacToe/getRow2()[I
    invokestatic BoardBase/printBoard([I[I[I)V

    aload_1
    invokevirtual TicTacToe/winner()I
    istore 5

    iload 5
    invokestatic BoardBase/printWinner(I)V

    return

.end method
