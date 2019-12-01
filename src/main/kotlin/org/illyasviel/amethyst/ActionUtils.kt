/*
 * Copyright (c) 2019 the original author or authors.
 * Licensed under the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-2.0
 */

package org.illyasviel.amethyst

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile


object ActionUtils {
    private val CLOJURE_EXT = setOf("clj", "cljs")

    fun isClojureFile(file: VirtualFile): Boolean {
        val fileExt = file.extension ?: return false
        return CLOJURE_EXT.contains(fileExt)
    }

    fun performCleanNs(project: Project, document: Document, filepath: String) {
        val cleanedNs: String = CleanNsUtils.getCleanedNsByFilePath(filepath) ?: return
        val offsetVector = CleanNsUtils.getReplaceOffset(filepath, document.text) ?: return
        val startOffset = (offsetVector[0] as Long?)?.toInt() ?: return
        val endOffset = (offsetVector[1] as Long?)?.toInt() ?: return
        if (document.text.substring(startOffset, endOffset) == cleanedNs) return
        WriteCommandAction.runWriteCommandAction(project) {
            document.replaceString(startOffset, endOffset, cleanedNs)
        }
    }
}
