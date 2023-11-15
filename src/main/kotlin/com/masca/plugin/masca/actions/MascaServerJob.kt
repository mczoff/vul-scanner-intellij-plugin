package com.masca.plugin.masca.actions

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.remoteDev.util.addPathSuffix
import com.masca.plugin.masca.dto.LibraryRequestDto
import com.masca.plugin.masca.dto.PackageRequestDto
import com.masca.plugin.masca.dto.ScanRequestDto
import com.masca.plugin.masca.models.Application
import com.masca.plugin.masca.notification.NotificationManager
import com.masca.plugin.masca.settings.MascaSettingsState
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object MascaServerJob {
    fun findVulnerabilities(project: Project, resultFile: File) {
        val typeToken = object : TypeToken<List<Application>>() {}.type
        val applications = Gson().fromJson<List<Application>>(resultFile.reader(), typeToken)

        var packagesCount = 0;

        for(app in applications) {
            packagesCount += app.Libraries.size;
        }

        if (packagesCount == 0) {
            NotificationManager.notifyInformation(project, "No package managers found in project.")
            return
        }

        val items = applications.map { application ->
            PackageRequestDto(application.Type, application.FilePath, application.Libraries.map {
                LibraryRequestDto(it.ID, it.Name, it.Version, it.License, it.Indirect, it.DependsOn)
            })
        }

        val requestBody = ScanRequestDto(MascaSettingsState.instance.mascaApiKey, "ide_intellij", project.name, items)
        val body = Gson().toJson(requestBody)
        val httpClient = HttpClient.newBuilder().build()

        val request = HttpRequest.newBuilder()
                .uri(URI.create(MascaSettingsState.instance.mascaServerURL).addPathSuffix("scanner/pkg-ide"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("content-type", "application/json")
                .build()

        try {

            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            val statusCode = response.statusCode()

            if (statusCode == 422 || statusCode == 403) {
                NotificationManager.notifyError(project, "Failed to scan project vulnerabilities. Access token is wrong or invalid")
            }

            if (response.statusCode() == 201) {
                NotificationManager.notifyInformation(project, "Vulnerabilities scanning initiated for $packagesCount packages")
            }
        } catch (ex: Exception) {
            NotificationManager.notifyError(project, "Server ${MascaSettingsState.instance.mascaServerURL} is not available")
        }
    }
}