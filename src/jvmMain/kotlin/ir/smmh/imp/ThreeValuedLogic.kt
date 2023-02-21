package ir.smmh.imp

infix fun Boolean?.or(that: Boolean?): Boolean? =
    if (this == true || that == true) true
    else if (this == false && that == false) false
    else null

infix fun Boolean?.and(that: Boolean?): Boolean? =
    if (this == false || that == false) false
    else if (this == true && that == true) true
    else null