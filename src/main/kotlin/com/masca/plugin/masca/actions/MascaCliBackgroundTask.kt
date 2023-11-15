package com.masca.plugin.masca.actions

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ScriptRunnerUtil
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.masca.plugin.masca.notification.NotificationManager
import com.masca.plugin.masca.settings.MascaSettingsState
import java.io.File
import java.util.function.BiConsumer
import javax.swing.SwingUtilities

internal class MascaCliBackgroundTask(
        private val project: Project,
        private val resultFile: File,
        private val callback: BiConsumer<Project, File>
) : Task.Backgroundable(project, "Running MaSCA", false), Runnable {

    override fun run(indicator: ProgressIndicator) {
        this.run()
    }

    override fun run() {
        val commandParts: MutableList<String> = ArrayList()

        if (MascaSettingsState.instance.mascaCliPath.isBlank()) {
            commandParts.add("masca.exe")
        } else {
            commandParts.add(MascaSettingsState.instance.mascaCliPath)
        }

        var commandLine = GeneralCommandLine(commandParts)

        var process: Process = try {
            commandLine.createProcess()
        } catch (e: ExecutionException) {
            NotificationManager.notifyError(project, "MaSCA CLI is not found")
            return
        }

        commandParts.add("--folder")
        commandParts.add("-path")
        commandParts.add(this.project.basePath.toString())
        commandParts.add("-out")
        commandParts.add(resultFile.absolutePath)

        commandLine = GeneralCommandLine(commandParts);
        process = try {
            commandLine.createProcess()
        } catch (e: ExecutionException) {
            NotificationManager.notifyError(project, e.localizedMessage)
            return
        }

        val handler = OSProcessHandler(process, commandLine.commandLineString)

        try {
            ScriptRunnerUtil.getProcessOutput(handler,
                    ScriptRunnerUtil.STDOUT_OR_STDERR_OUTPUT_KEY_FILTER,
                    100000000)

            NotificationManager.notifyInformation(project, "Masca CLI run completed.")
            SwingUtilities.invokeLater { callback.accept(project, resultFile) }
        } catch (e: ExecutionException) {
            NotificationManager.notifyError(project, e.localizedMessage)
        }
    }
}
