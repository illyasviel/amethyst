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
import com.intellij.openapi.fileEditor.FileDocumentManager
import org.illyasviel.amethyst.ActionUtils
import org.illyasviel.amethyst.Icons

class CleanNsAction : AnAction(Icons.defaultIcon) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val document = editor.document
        if (!document.isWritable) return
        val fileDocumentManager = FileDocumentManager.getInstance()
        val currentFile = fileDocumentManager.getFile(document) ?: return
        if (!ActionUtils.isClojureFile(currentFile)) return

        if (fileDocumentManager.isDocumentUnsaved(document)) {
            WriteCommandAction.runWriteCommandAction(project) {
                fileDocumentManager.saveDocument(document)
                ActionUtils.performCleanNs(project, document, currentFile.path)
            }
        } else {
            ActionUtils.performCleanNs(project, document, currentFile.path)
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
        if (!ActionUtils.isClojureFile(currentFile)) return false
        return true
    }
}
