@file:Suppress("ClassName")

package com.demo.drive.core.model

@DslMarker
internal annotation class ListParamsMarker

/**
 * Kotlin DSL Builder for files.list Drive API params
 * https://developers.google.com/drive/api/v3/reference/files/list
 */
@ListParamsMarker
class ListParams internal constructor() {
    var query: String? = null
        private set
    var fields: String? = null
        private set
    var orderBy: String? = null
        private set
    var pageSize: Int = 100
        private set

    fun query(init: Operator.() -> Unit) {
        query = AndOrOperator("and", false).apply(init).stringValue()
    }

    fun fields(init: Field.() -> Unit) {
        fields = Field().apply(init).stringValue()
    }

    fun orderBy(init: OrderBy.() -> Unit) {
        orderBy = OrderBy().apply(init).stringValue()
    }

    fun pageSize(pageSize: Int) {
        this.pageSize = pageSize
    }
}

/**
 * possible query terms and operators for each term are described here
 * https://developers.google.com/drive/api/guides/ref-search-terms
 */
@ListParamsMarker
abstract class Operator internal constructor() {

    fun or(init: Operator.() -> Unit) {
        addOperator(AndOrOperator("or").apply(init))
    }

    fun not(init: Operator.() -> Unit) {
        addOperator(NotOperator().apply(init))
    }

    infix fun Any.contains(value: Any) {
        addOperator(TermValueOperator(this, "contains", value))
    }

    infix fun Any.equal(value: Any) {
        addOperator(TermValueOperator(this, "=", value))
    }

    infix fun Any.notEqual(value: Any) {
        addOperator(TermValueOperator(this, "!=", value))
    }

    infix fun Any.lessThan(value: Any) {
        addOperator(TermValueOperator(this, "<", value))
    }

    infix fun Any.lessThenOrEqualTo(value: Any) {
        addOperator(TermValueOperator(this, "<=", value))
    }

    infix fun Any.greaterThan(value: Any) {
        addOperator(TermValueOperator(this, ">", value))
    }

    infix fun Any.greaterThenOrEqualTo(value: Any) {
        addOperator(TermValueOperator(this, ">=", value))
    }

    infix fun Any.isIn(value: Any) {
        addOperator(TermValueOperator(this, "in", value))
    }

    infix fun Any.has(value: Any) {
        addOperator(TermValueOperator(this, "has", value))
    }

    abstract fun stringValue(): String?
    abstract fun addOperator(operator: Operator)
}

internal class AndOrOperator(private val name: String, private val wrapWithParentheses: Boolean = true) : Operator() {
    private val operators = mutableListOf<Operator>()

    override fun stringValue(): String? {
        return if(operators.isEmpty()) {
            null
        } else if (operators.size == 1) {
            operators.first().stringValue()
        } else {
            val prefix = if(wrapWithParentheses) "(" else ""
            val postfix = if(wrapWithParentheses) ")" else ""
            operators.joinToString(prefix = prefix, postfix = postfix, separator = " $name ") {
                it.stringValue() ?: throw IllegalStateException("empty condition operator")
            }
        }
    }

    override fun addOperator(operator: Operator) {
        operators.add(operator)
    }
}

internal class TermValueOperator(
    private val term: Any,
    private val operator: String,
    private val value: Any
) : Operator() {

    override fun stringValue() = "$term $operator $value"

    override fun addOperator(operator: Operator) {
        throw IllegalStateException("'$operator' does not support nested conditions")
    }
}

internal class NotOperator: Operator() {

    private lateinit var operator: Operator

    override fun stringValue(): String {
        if(!this::operator.isInitialized) {
            throw IllegalStateException("condition for not is required")
        }
        return "not ${operator.stringValue()}"
    }

    override fun addOperator(operator: Operator) {
        if(this::operator.isInitialized) {
            throw IllegalStateException("only one condition is allowed with `not`")
        }
        this.operator = operator
    }
}


@ListParamsMarker
class Field internal constructor() {
    private var name: String? = null
    private val fields: MutableList<Field> = ArrayList()

    fun field(name: String, init: Field.() -> Unit = {}) {
        val field = Field()
        field.init()
        field.name = name
        fields.add(field)
    }

    fun stringValue(): String? {
        if (name == null && fields.isEmpty()) {
            return null
        }
        return buildString {
            val appendBrackets = name != null && fields.isNotEmpty()
            if (name != null) {
                append(name)
            }
            if (appendBrackets) {
                append("(")
            }
            fields.forEachIndexed { index, field ->
                if (index > 0) {
                    append(", ")
                }
                append(field.stringValue())
            }
            if (appendBrackets) {
                append(")")
            }
        }
    }
}

@ListParamsMarker
class OrderBy internal constructor() {
    private val orderByFields: MutableList<OrderByField> = ArrayList()

    fun field(name: String, direction: OrderByDirection? = null) {
        orderByFields.add(OrderByField(name, direction))
    }

    fun stringValue(): String? {
        if (orderByFields.isEmpty()) {
            return null
        }
        return buildString {
            orderByFields.forEach {
                if (this.isNotEmpty()) {
                    append(", ")
                }
                append(it.stringValue())
            }
        }
    }
}

internal class OrderByField(
    private val name: String,
    private val direction: OrderByDirection?
) {
    fun stringValue() = buildString {
        append(name)
        if (direction != null) {
            append(" ")
            append(direction.stringValue())
        }
    }
}

enum class OrderByDirection(private val value: String) {
    Ascending("asc"), Descending("desc");

    fun stringValue() = value
}

fun listParams(init: ListParams.() -> Unit): ListParams {
    val listParams = ListParams()
    listParams.init()
    return listParams
}