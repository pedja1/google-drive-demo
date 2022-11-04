package com.demo.drive.core.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class QueryTest {

    //simulate query { } from ListParams
    private fun query() = AndOrOperator("and", false)

    @Test
    fun `stringValue empty query returns null`() {
        val stringValue = query().stringValue()
        assertThat(stringValue).isNull()
    }

    @Test
    fun `stringValue single query condition is build correctly`() {
        val stringValue = query().apply {
            "name" contains "pedja"
        }.stringValue()
        assertThat(stringValue).isEqualTo("name contains pedja")
    }

    @Test
    fun `stringValue and condition is build correctly`() {
        val stringValue = query().apply {
            "name" contains "pedja"
            "field2" equal "pedja"
        }.stringValue()
        assertThat(stringValue).isEqualTo("name contains pedja and field2 = pedja")
    }

    @Test
    fun `stringValue or condition is build correctly`() {
        val stringValue = query().apply {
            or {
                "name" contains "pedja"
                "field2" equal "pedja"
            }
        }.stringValue()
        assertThat(stringValue).isEqualTo("(name contains pedja or field2 = pedja)")
    }

    @Test
    fun `stringValue not condition is build correctly`() {
        val stringValue = query().apply {
            not {
                "name" contains "pedja"
            }
        }.stringValue()
        assertThat(stringValue).isEqualTo("not name contains pedja")
    }

    @Test
    fun `stringValue query with parentheses is build correctly`() {
        val stringValue = query().apply {
            or {
                "name" contains "pedja"
                "field2" equal "pedja"
            }
        }.stringValue()
        assertThat(stringValue).isEqualTo("(name contains pedja or field2 = pedja)")
    }

    @Test
    fun `stringValue query with multiple parentheses is build correctly`() {
        val stringValue = query().apply {
            "modifiedTime" equal "24.11.2021"
            or {
                "name" contains "pedja"
                "field2" equal "pedja"
                or {
                    "field3" contains "value"
                    "field4" equal "value"
                }
            }
        }.stringValue()
        assertThat(stringValue).isEqualTo("modifiedTime = 24.11.2021 and (name contains pedja or field2 = pedja or (field3 contains value or field4 = value))")
    }

    @Test
    fun `stringValue query with multiple ands and ors is build correctly`() {
        val stringValue = query().apply {
            "modifiedTime" equal "24.11.2021"
            or {
                "name" contains "pedja"
                "field2" equal "pedja"
            }
        }.stringValue()
        assertThat(stringValue).isEqualTo("modifiedTime = 24.11.2021 and (name contains pedja or field2 = pedja)")
    }
}