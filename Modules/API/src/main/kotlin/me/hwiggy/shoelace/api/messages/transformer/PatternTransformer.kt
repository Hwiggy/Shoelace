package me.hwiggy.shoelace.api.messages.transformer

abstract class PatternTransformer<Type>(name: String) {
    open val startOnly = false
    protected val pattern by lazy {
        val builder = StringBuilder("<$name(?:\\((.+?)\\))?>")
        if (startOnly) builder.insert(0, "^")
        Regex(builder.toString())
    }

    fun transform(input: Type) = apply(input)?.also(::postTransform)

    protected fun params(result: MatchResult) = result.groupValues[1].split(",").toTypedArray()

    abstract fun stringValue(input: Type): String
    abstract fun apply(input: Type): Type?

    open fun postTransform(input: Type) = Unit
}

abstract class InputTransformer<Type>(name: String) : PatternTransformer<Type>(name) {
    override val startOnly = true

    abstract fun inputReplacement(input: Type, vararg args: String): Type
    abstract fun removeMatch(input: Type, match: MatchResult): Type

    final override fun apply(input: Type): Type? {
        val asString = stringValue(input)
        val match = pattern.find(asString) ?: return null
        val removed = removeMatch(input, match)
        return inputReplacement(removed, *params(match))
    }
}

abstract class MatchTransformer<Type>(name: String) : PatternTransformer<Type>(name) {
    final override fun apply(input: Type): Type? {
        val asString = stringValue(input)
        val matches = pattern.findAll(asString).toList()
        if (matches.isEmpty()) return null
        val transformed = pattern.split(asString).map {
            transformPart(it)
        }
        val replacements = matches.map {
            matchReplacement(*params(it))
        }
        val parts = transformed.zip(replacements).flatMap {
            listOf(it.first, it.second) } + transformed.drop(replacements.size)
        return combine(parts.filterNotNull())
    }

    abstract fun transformPart(part: String): Type?
    abstract fun matchReplacement(vararg args: String): Type?
    abstract fun combine(parts: Collection<Type>): Type
}