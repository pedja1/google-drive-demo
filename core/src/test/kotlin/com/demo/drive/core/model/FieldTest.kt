package com.demo.drive.core.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FieldTest {

    //commas
    @Test
    fun `stringValue with single field returns that field`() {
        val stringValue = Field().apply {
            field("name")
        }.stringValue()
        assertThat(stringValue).isEqualTo("name")
    }

    @Test
    fun `stringValue with multiple fields is separated by commas`() {
        val stringValue = Field().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        assertThat(stringValue?.split(",")?.map { it.trim() })
            .containsExactly("name", "mimeType", "fullText")
    }

    @Test
    fun `stringValue with any number of fields contains correct number of commas`() {
        val stringValue = Field().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        assertThat(stringValue?.count { it == ',' }).isEqualTo(2)
    }

    @Test
    fun `stringValue with multiple fields doesn't start with comma`() {
        val stringValue = Field().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        assertThat(stringValue?.first()).isNotEqualTo(',')
    }

    @Test
    fun `stringValue with multiple fields doesn't end with comma`() {
        val stringValue = Field().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        assertThat(stringValue?.last()).isNotEqualTo(',')
    }

    //brackets

    @Test
    fun `stringValue with no depth contains no brackets`() {
        val stringValue = Field().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        assertThat(stringValue).doesNotContain("(")
        assertThat(stringValue).doesNotContain(")")
    }

    @Test
    fun `stringValue with depth contains correct number of brackets`() {
        val stringValue = Field().apply {
            field("files") {
                field("name")
                field("mimeType")
                field("fullText")
            }
        }.stringValue()
        assertThat(stringValue?.count { it == '(' }).isEqualTo(1)
        assertThat(stringValue?.count { it == ')' }).isEqualTo(1)
    }

    @Test
    fun `stringValue with depth contains correct brackets`() {

    }

    //other
    @Test
    fun `stringValue with no fields returns null`() {
        val stringValue = Field().stringValue()
        assertThat(stringValue).isNull()
    }
}