package com.demo.googledrive.utils

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PathManagerTest {

    private val rootFolderList = Folder.List("root", "My Drive")
    private val rootFolderSearch = Folder.Search("search", "search result")

    @Test
    fun `when PathManager is create, initial value is current value`() {
        val pathManager = PathManager(rootFolderList)
        assertThat(pathManager.getCurrent()).isEqualTo(rootFolderList)
    }

    @Test
    fun `reset with different root folder reset path`() {
        val pathManager = PathManager(rootFolderList)
        pathManager.push(Folder.List("id1", "name1"))
        pathManager.push(Folder.List("id2", "name2"))
        pathManager.reset(rootFolderSearch)

        assertThat(pathManager.getCurrent()).isEqualTo(rootFolderSearch)
        assertThat(pathManager.getPath()).hasSize(1)
    }

    @Test
    fun `push adds folder to path`() {
        val expected = Folder.List("id1", "name1")
        val pathManager = PathManager(rootFolderList)
        pathManager.push(expected)

        assertThat(pathManager.getCurrent()).isEqualTo(expected)
    }

    @Test
    fun `pop removes folder from path`() {
        val expected = Folder.List("id1", "name1")
        val pathManager = PathManager(rootFolderList)
        pathManager.push(Folder.List("id2", "name2"))
        pathManager.push(Folder.List("id3", "name3"))
        pathManager.push(expected)
        pathManager.pop()

        assertThat(pathManager.getPath()).doesNotContain(expected)
    }

    @Test
    fun `buildDisplayValue with single folder contains that path name`() {
        val pathManager = PathManager(rootFolderList)

        assertThat(pathManager.buildDisplayValue()).isEqualTo(rootFolderList.name)
    }

    @Test
    fun `buildDisplayValue with multiple folders is built correctly`() {
        val pathManager = PathManager(rootFolderList)
        pathManager.push(Folder.List("id2", "name2"))
        pathManager.push(Folder.List("id3", "name3"))

        assertThat(pathManager.buildDisplayValue()).isEqualTo("${rootFolderList.name}/name2/name3")
    }

    @Test
    fun `pop doesn't remove root folder`() {
        val pathManager = PathManager(rootFolderList)
        pathManager.pop()

        assertThat(pathManager.getPath()).hasSize(1)
    }

    @Test
    fun `current is update when folder is added`() = runBlocking {
        val pathManager = PathManager(rootFolderList)
        pathManager.push(Folder.List("id", "name"))
        pathManager.current.test {
            val current = awaitItem()
            assertThat(current.name).isEqualTo("name")
            cancelAndConsumeRemainingEvents()
        }
    }
}