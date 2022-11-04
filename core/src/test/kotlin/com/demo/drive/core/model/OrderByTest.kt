package com.demo.drive.core.model

import com.google.common.truth.Truth
import org.junit.Test

class OrderByTest {
    @Test
    fun `stringValue with single field returns that field`() {
        val stringValue = OrderBy().apply {
            field("name")
        }.stringValue()
        Truth.assertThat(stringValue).isEqualTo("name")
    }

    @Test
    fun `stringValue with multiple fields is separated by commas`() {
        val stringValue = OrderBy().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        Truth.assertThat(stringValue?.split(",")?.map { it.trim() })
            .containsExactly("name", "mimeType", "fullText")
    }

    @Test
    fun `stringValue with any number of fields contains correct number of commas`() {
        val stringValue = OrderBy().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        Truth.assertThat(stringValue?.count { it == ',' }).isEqualTo(2)
    }

    @Test
    fun `stringValue with multiple fields doesn't start with comma`() {
        val stringValue = OrderBy().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        Truth.assertThat(stringValue?.first()).isNotEqualTo(',')
    }

    @Test
    fun `stringValue with multiple fields doesn't end with comma`() {
        val stringValue = OrderBy().apply {
            field("name")
            field("mimeType")
            field("fullText")
        }.stringValue()
        Truth.assertThat(stringValue?.last()).isNotEqualTo(',')
    }
}