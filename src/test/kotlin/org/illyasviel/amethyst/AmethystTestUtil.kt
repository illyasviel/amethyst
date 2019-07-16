/*
 * Copyright (c) 2019 the original author or authors.
 * Licensed under the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-2.0
 */

package org.illyasviel.amethyst

import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.util.text.StringUtil
import org.jetbrains.annotations.NotNull

object AmethystTestUtil {

    const val CLEAN_NS_ACTION = "org.illyasviel.amethyst.actions.CleanNsAction"

    val BASE_TEST_DATA_PATH = findTestDataPath()

    private fun findTestDataPath(): String {
        val pwd = System.getProperty("user.dir")
        return StringUtil.trimEnd(FileUtil.toSystemIndependentName(pwd), "/") + "/" +
                StringUtil.trimStart(FileUtil.toSystemIndependentName(getTestDataRelativePath()), "/")
    }

    @NotNull
    fun getTestDataRelativePath(): String {
        return "/src/test/testData/"
    }
}
