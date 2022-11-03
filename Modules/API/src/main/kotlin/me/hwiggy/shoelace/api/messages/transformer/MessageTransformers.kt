package me.hwiggy.shoelace.api.messages.transformer

abstract class MessageTransformers<Type> @JvmOverloads constructor(
    init: MessageTransformers<Type>.() -> Unit = { },
) {
    private val local = LinkedHashSet<PatternTransformer<Type>>()
    fun register(tx: PatternTransformer<Type>) = local.add(tx)

    fun apply(input: Type, flag: Order = Order.GLOBALS_FIRST): Type {
        var output = preprocess(input)
        if (flag == Order.GLOBALS_FIRST)
            output = globals().applyEach(output)
        output = applyEach(output)
        if (flag == Order.GLOBALS_LAST)
            output = globals().applyEach(output)
        return output
    }

    private fun applyEach(input: Type): Type {
        var output = input
        local.forEach {
            val transformed = it.transform(output)
            if (transformed != null) output = transformed
        }
        return output
    }

    abstract fun preprocess(input: Type): Type
    abstract fun globals(): MessageTransformers<Type>

    init { this.init() }

    enum class Order {
        GLOBALS_FIRST, GLOBALS_LAST
    }
}