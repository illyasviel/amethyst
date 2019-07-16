/*
 * Copyright (c) 2019 the original author or authors.
 * Licensed under the Eclipse Public License 2.0
 * which is available at http://www.eclipse.org/legal/epl-2.0
 */

package org.illyasviel.amethyst.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import org.illyasviel.amethyst.CleanNsUtils
import org.illyasviel.amethyst.Icons

class CleanNsAction : AnAction(Icons.defaultIcon) {
    companion object {
        private val CLOJURE_EXT = setOf("clj", "cljs")
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val document = editor.document
        if (!document.isWritable) return
        val fileDocumentManager = FileDocumentManager.getInstance()
        val currentFile = fileDocumentManager.getFile(document) ?: return
        val currentFileExt = currentFile.extension ?: return
        if (!CLOJURE_EXT.contains(currentFileExt)) return

        if (fileDocumentManager.isDocumentUnsaved(document)) {
            WriteCommandAction.runWriteCommandAction(project) {
                fileDocumentManager.saveDocument(document)
                cleanNs(project, editor, currentFile.path)
            }
        } else {
            cleanNs(project, editor, currentFile.path)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = isActionVisible(e)
    }

    private fun isActionVisible(e: AnActionEvent): Boolean {
        e.project ?: return false
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return false
        val document = editor.document
        if (!document.isWritable) return false
        val fileDocumentManager = FileDocumentManager.getInstance()
        val currentFile = fileDocumentManager.getFile(document) ?: return false
        val currentFileExt = currentFile.extension ?: return false
        if (!CLOJURE_EXT.contains(currentFileExt)) return false
        return true
    }

    private fun cleanNs(project: Project, editor: Editor, filepath: String) {
        val cleanedNs: String = CleanNsUtils.getCleanedNsByFilePath(filepath) ?: return
        val offsetVector = CleanNsUtils.getReplaceOffset(filepath, editor.document.text)
        val startOffset = (offsetVector[0] as Long?)?.toInt() ?: return
        val endOffset = (offsetVector[1] as Long?)?.toInt() ?: return
        if (editor.document.text.substring(startOffset, endOffset) == cleanedNs) return
        WriteCommandAction.runWriteCommandAction(project) {
            editor.document.replaceString(startOffset, endOffset, cleanedNs)
        }
    }
}
