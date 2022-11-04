package com.demo.drive.core.model

import com.google.common.truth.Truth
import org.junit.Test


class OrderByFieldTest {
    @Test
    fun `stringValue with name returns only name`() {
        val stringValue = OrderByField("name", null).stringValue()
        Truth.assertThat(stringValue).isEqualTo("name")
    }
    @Test
    fun `stringValue with name and direction returns name and direction`() {
        val stringValue = OrderByField("name", OrderByDirection.Ascending).stringValue()
        Truth.assertThat(stringValue).isEqualTo("name ${OrderByDirection.Ascending.stringValue()}")
    }
}