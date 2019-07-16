/*
 * Copyright (c) 2019 the original author or authors.
 * Licensed under the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-2.0
 */

package org.illyasviel.amethyst

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.TempDirTestFixtureImpl

class CleanNsActionTest : LightPlatformCodeInsightFixtureTestCase() {

    override fun getTestDataPath(): String {
        return AmethystTestUtil.BASE_TEST_DATA_PATH
    }

    override fun createTempDirTestFixture(): TempDirTestFixture {
        return TempDirTestFixtureImpl()
    }

    private fun assertFileContentAfterAction(sourceFilePath: String, expectedFilePath: String) {
        val virtualFile = myFixture.copyFileToProject(sourceFilePath)
        myFixture.openFileInEditor(virtualFile)
        myFixture.performEditorAction(AmethystTestUtil.CLEAN_NS_ACTION)
        myFixture.checkResultByFile(expectedFilePath)
    }

    fun `test not clojure`() {
        assertFileContentAfterAction("not_clojure.txt", "not_clojure.txt")
    }

    fun `test normal ns`() {
        assertFileContentAfterAction("normal_ns.clj", "normal_ns.after.clj")
    }

    fun `test duplicated ns`() {
        assertFileContentAfterAction("duplicated_ns.clj", "duplicated_ns.after.clj")
    }

    fun `test form before ns without exception`() {
        assertFileContentAfterAction("form_before_ns.clj", "form_before_ns.clj")
    }

    fun `test unsupported ns without exception`() {
        assertFileContentAfterAction("unsupported_ns.clj", "unsupported_ns.clj")
    }
}
