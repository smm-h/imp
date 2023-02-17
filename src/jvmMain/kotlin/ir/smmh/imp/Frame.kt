package ir.smmh.imp

import ir.smmh.imp.expressions.Uninitalized
import ir.smmh.imp.expressions.Value

class Frame : Namespace {
    var returnedValue: Value = Uninitalized
    private val map = HashMap<String, NameBinding>()
    override fun retrieve(name: String): NameBinding? = map.get(name)
    override fun declare(nameBinding: NameBinding) {
        map.put(nameBinding.name, nameBinding)
    }
}