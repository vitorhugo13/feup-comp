.class public Scrambler
.super java/lang/Object

.field public var1 I
.field public var3 Z
.field public var4 [I
.field public var5 LScrambler;

.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public getPIFirstDigit()I
    .limit stack 99
    .limit locals 99


    iconst_3
    ireturn
.end method

.method public computeScramble(LEightBall;I)I
    .limit stack 99
    .limit locals 99

    iconst_1
    istore_3

    iconst_5
    newarray int
    astore 4

    aload 4
    iconst_2
    bipush 7
    iastore

    aload_0
    iconst_2
    putfield Scrambler/var1 I

    aload_0
    iconst_1
    putfield Scrambler/var3 Z

    aload_0
    bipush 8
    iconst_2
    iload_3
    imul
    isub
    newarray int
    putfield Scrambler/var4 [I

    aload_0
    getfield Scrambler/var4 [I
    iconst_2
    iconst_5
    iastore

    aload_0
    new Scrambler
    dup
    invokespecial Scrambler/<init>()V
    putfield Scrambler/var5 LScrambler;

    aload_0
    astore 5

    new FormulaCalculator
    dup
    invokespecial FormulaCalculator/<init>()V
    astore 6

    iload_3
    aload_0
    getfield Scrambler/var4 [I
    iconst_2
    iaload
    if_icmplt ltTrue0
    iconst_0
    goto ltFalse0
  ltTrue0:
    iconst_1
  ltFalse0:
    aload_1
    invokevirtual EightBall/getPrediction()Z
    iand
    aload_0
    getfield Scrambler/var3 Z
    aload 4
    iconst_2
    iaload
    iload_3
    if_icmplt ltTrue1
    iconst_0
    goto ltFalse1
  ltTrue1:
    iconst_1
  ltFalse1:
    iand
    iconst_1
    ixor
    iand
    istore 7

    aload 6
    aload 4
    iconst_2
    iaload
    iconst_2
    aload_0
    getfield Scrambler/var1 I
    imul
    iconst_1
    invokevirtual FormulaCalculator/calculate(IIZ)I
    istore 8

    aload 4
    iconst_2
    iaload
    bipush 30
    iload_3
    iconst_3
    iadd
    idiv
    aload_0
    getfield Scrambler/var1 I
    imul
    isub
    aload_0
    invokevirtual Scrambler/getPIFirstDigit()I
    iadd
    aload_0
    getfield Scrambler/var5 LScrambler;
    invokevirtual Scrambler/getPIFirstDigit()I
    iadd
    iload 8
    isub
    istore 9

    iload 7
    invokestatic io/println(Z)V


    iload 9
    iconst_3
    iconst_2
    imul
    bipush 6
    idiv
    isub
    ireturn
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 99
    .limit locals 99

    new EightBall
    dup
    invokespecial EightBall/<init>()V
    astore_1

    new Scrambler
    dup
    invokespecial Scrambler/<init>()V
    astore_2

    aload_0
    iconst_0
    aaload
    invokestatic io/println(Ljava/lang/String;)V

    aload_2
    aload_1
    iconst_3
    invokevirtual Scrambler/computeScramble(LEightBall;I)I
    invokestatic io/println(I)V

    return
.end method
