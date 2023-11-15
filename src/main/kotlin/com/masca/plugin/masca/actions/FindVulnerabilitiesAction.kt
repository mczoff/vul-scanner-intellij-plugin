package com.masca.plugin.masca.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.masca.plugin.masca.notification.NotificationManager
import java.io.File
import java.io.IOException
import javax.swing.SwingUtilities

class FindVulnerabilitiesAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val resultFile: File = try {
            File.createTempFile("masca-dependencies-temp", ".json")
        } catch (ex: IOException) {
            NotificationManager.notifyError(project, ex.localizedMessage)
            return
        }

        val runner = MascaCliBackgroundTask(project, resultFile, MascaServerJob::findVulnerabilities)
        if (SwingUtilities.isEventDispatchThread()) {
            ProgressManager.getInstance().run(runner)
        } else {
            ApplicationManager.getApplication().invokeLater(runner)
        }
    }
}

