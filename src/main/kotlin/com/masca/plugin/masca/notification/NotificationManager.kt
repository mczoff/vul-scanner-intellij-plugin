package com.masca.plugin.masca.notification

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object NotificationManager {
    private val mascaNotifications = NotificationGroup.findRegisteredGroup("MaSCA Notifications")

    public fun notifyError(project: Project?, content: String) {
        notify(project, content, NotificationType.ERROR)
    }

    public fun notifyWarning(project: Project?, content: String) {
        notify(project, content, NotificationType.WARNING)
    }

    public fun notifyInformation(project: Project?, content: String) {
        notify(project, content, NotificationType.INFORMATION)
    }

    private fun notify(project: Project?, content: String, notificationType: NotificationType) {
        mascaNotifications?.createNotification(content, notificationType)?.notify(project)
    }
}