package com.masca.plugin.masca.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
class MascaSettingsConfig : Configurable {
    private var settings: MascaSettings? = null

    override fun getDisplayName(): @Nls(capitalization = Nls.Capitalization.Title) String? {
        return "MaSCA: Settings"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return settings?.preferredFocusedComponent
    }

    override fun createComponent(): JComponent? {
        settings = MascaSettings()
        return settings?.panel;
    }

    override fun isModified(): Boolean {
        val settingsState = MascaSettingsState.instance

        return settings?.let {
            return it.mascaCliPath != settingsState.mascaCliPath ||
                    it.mascaServerUrl != settingsState.mascaServerURL ||
                    it.mascaApiKey != settingsState.mascaApiKey
        } ?: false;
    }

    override fun apply() {
        settings?.let {
            MascaSettingsState.instance.apply {
                mascaCliPath = it.mascaCliPath
                mascaApiKey = it.mascaApiKey
                mascaServerURL = it.mascaServerUrl
            }
        }
    }

    override fun reset() {
        settings?.apply {
            MascaSettingsState.instance.let {
                mascaCliPath = it.mascaCliPath
                mascaApiKey = it.mascaApiKey
                mascaServerUrl = it.mascaServerURL
            }
        }
    }

    override fun disposeUIResources() {
        settings = null
    }
}