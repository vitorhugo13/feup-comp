.class public ControlSructureTest
.super java/lang/Object


.method public <init>()V
    aload_0
    invokenonvirtual java/lang/Object/<init>()V
    return
.end method

.method public static main([Ljava/lang/String;)V
    .limit stack 99
    .limit locals 99

    iconst_2
    istore_1

    iconst_1
    iconst_3
    if_icmplt ltTrue0
    iconst_0
    goto ltFalse0
  ltTrue0:
    iconst_1
  ltFalse0:
    ifeq else_0
    bipush 9
    istore_2
    goto endif_0
else_0:
    iconst_3
    istore_2
endif_0:

    iload_2
    iconst_4
    if_icmplt ltTrue1
    iconst_0
    goto ltFalse1
  ltTrue1:
    iconst_1
  ltFalse1:
    ifeq end_while_0
start_while_0:
    iload_1
    iconst_2
    iadd
    istore_1
    iload_2
    iconst_4
    if_icmplt ltTrue2
    iconst_0
    goto ltFalse2
  ltTrue2:
    iconst_1
  ltFalse2:
    ifne start_while_0
end_while_0:

    return
.end method
