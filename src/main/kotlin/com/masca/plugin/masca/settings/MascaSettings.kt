@file:Suppress("DialogTitleCapitalization")

package com.masca.plugin.masca.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.JBSplitter
import com.intellij.ui.TitledSeparator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class MascaSettings {
    val panel: JPanel

    private val mascaCliPathTextField = TextFieldWithBrowseButton()
    private val mascaServerUrlTextField = JBTextField("")
    private val mascaApiKeyTextField = JBTextField("")

    var mascaCliPath: String
        get() = mascaCliPathTextField.text
        set(value) {
            mascaCliPathTextField.text = value;
        }

    var mascaServerUrl: String
        get() = mascaServerUrlTextField.text
        set(value) {
            mascaServerUrlTextField.text = value;
        }

    var mascaApiKey: String
        get() = mascaApiKeyTextField.text
        set(value) {
            mascaApiKeyTextField.text = value;
        }

    val preferredFocusedComponent: JComponent
        get() = mascaCliPathTextField

    init {
        mascaCliPathTextField.addBrowseFolderListener(
                "Masca CLI path",
                "Set masca CLI path explicitly",
                ProjectManager.getInstance().defaultProject, FileChooserDescriptorFactory.createSingleFileDescriptor())

        panel = FormBuilder.createFormBuilder()
                .addComponent(TitledSeparator("Masca CLI path (explicit)"))
                .addLabeledComponent(JBLabel(), mascaCliPathTextField, 1, true)
                .addComponent(JBSplitter())
                .addComponent(TitledSeparator("MaSCA server IP"))
                .addLabeledComponent(JBLabel(), mascaServerUrlTextField, 1, false)
                .addComponent(TitledSeparator("MaSCA API key"))
                .addLabeledComponent(JBLabel(), mascaApiKeyTextField, 1, false)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }
}