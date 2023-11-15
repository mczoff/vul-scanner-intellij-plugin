package com.masca.plugin.masca.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "com.masca.plugin.masca.settings.MascaSettingsState", storages = [Storage("masca-settings.xml")])
class MascaSettingsState : PersistentStateComponent<MascaSettingsState?> {
    var mascaCliPath = ""
    var mascaServerURL = ""
    var mascaApiKey = ""

    override fun getState(): MascaSettingsState? {
        return this
    }

    override fun loadState(state: MascaSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        val instance: MascaSettingsState
            get() = ApplicationManager.getApplication().getService(MascaSettingsState::class.java)
    }
}