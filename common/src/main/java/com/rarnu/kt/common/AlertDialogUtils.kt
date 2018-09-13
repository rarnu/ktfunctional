package com.rarnu.kt.common

import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.text.TextAlignment
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle

fun alert(title: String, message: String, btn: String, callback: () -> Unit) {
    val window = Stage()
    window.setOnCloseRequest { callback() }
    window.initModality(Modality.APPLICATION_MODAL)
    window.width = 400.0
    window.height = 100.0
    window.title = title
    window.isResizable = false
    window.initStyle(StageStyle.UTILITY)
    val layRoot = BorderPane()
    layRoot.padding = Insets(8.0, 8.0, 8.0, 8.0)
    val lblMessage = Label(message)
    lblMessage.isWrapText = true
    lblMessage.alignment = Pos.CENTER
    lblMessage.textAlignment = TextAlignment.LEFT
    val pCenter = BorderPane()
    pCenter.top = lblMessage
    val pBtn = BorderPane()
    val pBtnsub = BorderPane()
    val btnOk = Button(btn)
    btnOk.ios(-1, 20)
    btnOk.setOnAction {
        callback()
        window.close()
    }
    pBtnsub.right = btnOk
    pBtn.right = pBtnsub
    layRoot.bottom = pBtn
    layRoot.top = pCenter
    window.scene = Scene(layRoot)
    window.showAndWait()
}

fun alert(title: String, message: String, btn1: String, btn2: String, callback: (which: Int) -> Unit) {
    val window = Stage()
    window.setOnCloseRequest { callback(-1) }
    window.initModality(Modality.APPLICATION_MODAL)
    window.width = 400.0
    window.height = 100.0
    window.title = title
    window.isResizable = false
    window.initStyle(StageStyle.UTILITY)
    val layRoot = BorderPane()
    layRoot.padding = Insets(8.0, 8.0, 8.0, 8.0)
    val lblMessage = Label(message)
    lblMessage.isWrapText = true
    lblMessage.alignment = Pos.CENTER
    lblMessage.textAlignment = TextAlignment.LEFT
    val pCenter = BorderPane()
    pCenter.top = lblMessage
    val pBtn = BorderPane()
    val pBtnsub = BorderPane()
    val btnOk = Button(btn1)
    btnOk.ios(-1, 20)
    val btnCancel = Button(btn2)
    btnCancel.ios(50, 20)
    btnOk.setOnAction {
        callback(0)
        window.close()
    }
    btnCancel.setOnAction {
        callback(1)
        window.close()
    }
    pBtnsub.left = btnCancel
    pBtnsub.right = btnOk
    pBtn.right = pBtnsub
    layRoot.bottom = pBtn
    layRoot.top = pCenter
    window.scene = Scene(layRoot)
    window.showAndWait()
}

fun alert(title: String, message: String, btn1: String, btn2: String, btn3: String, callback: (which: Int) -> Unit) {
    val window = Stage()
    window.setOnCloseRequest { callback(-1) }
    window.initModality(Modality.APPLICATION_MODAL)
    window.width = 400.0
    window.height = 100.0
    window.title = title
    window.isResizable = false
    window.initStyle(StageStyle.UTILITY)
    val layRoot = BorderPane()
    layRoot.padding = Insets(8.0, 8.0, 8.0, 8.0)
    val lblMessage = Label(message)
    lblMessage.isWrapText = true
    lblMessage.alignment = Pos.CENTER
    lblMessage.textAlignment = TextAlignment.LEFT
    val pCenter = BorderPane()
    pCenter.top = lblMessage
    val pBtn = BorderPane()
    val pBtnsub = BorderPane()
    val btnFirst = Button(btn1)
    btnFirst.ios(-1, 20)
    btnFirst.setOnAction {
        callback(0)
        window.close()
    }
    val btnSecond = Button(btn2)
    btnSecond.ios(-1, 20)
    btnSecond.setOnAction {
        callback(1)
        window.close()
    }
    val btnCancel = Button(btn3)
    btnCancel.ios(-1, 20)
    btnCancel.setOnAction {
        callback(2)
        window.close()
    }
    val btnPan0 = BorderPane()
    btnPan0.left = btnFirst
    btnPan0.right = btnSecond
    pBtnsub.left = btnPan0
    pBtnsub.right = btnCancel
    pBtn.right = pBtnsub
    layRoot.bottom = pBtn
    layRoot.top = pCenter
    window.scene = Scene(layRoot)
    window.showAndWait()
}

fun alert(title: String, message: String, btn1: String, btn2: String, placeholder: String?, initText: String?, callback: (which: Int, text: String) -> Unit) {
    val window = Stage()
    window.setOnCloseRequest { callback(-1, "") }
    window.initModality(Modality.APPLICATION_MODAL)
    window.width = 400.0
    window.height = 150.0
    window.title = title
    window.isResizable = false
    window.initStyle(StageStyle.UTILITY)
    val layRoot = BorderPane()
    layRoot.padding = Insets(8.0, 8.0, 8.0, 8.0)
    val lblMessage = Label(message)
    lblMessage.isWrapText = true
    lblMessage.alignment = Pos.CENTER
    lblMessage.textAlignment = TextAlignment.LEFT
    val edit = TextField()
    edit.prefHeight = 40.0
    edit.promptText = placeholder
    edit.text = initText
    val pCenter = BorderPane()
    val paneMsg = BorderPane()
    paneMsg.padding = Insets(0.0, 0.0, 8.0, 0.0)
    paneMsg.left = lblMessage
    pCenter.top = paneMsg
    pCenter.bottom = edit
    val pBtn = BorderPane()
    val pBtnsub = BorderPane()
    val btnOk = Button(btn1)
    btnOk.ios(50, 20)
    val btnCancel = Button(btn2)
    btnCancel.ios(50, 20)
    btnOk.setOnAction {
        callback(0, edit.text)
        window.close()
    }
    btnCancel.setOnAction {
        callback(1, edit.text)
        window.close()
    }
    pBtnsub.left = btnCancel
    pBtnsub.right = btnOk
    pBtn.right = pBtnsub
    layRoot.bottom = pBtn
    layRoot.top = pCenter
    window.scene = Scene(layRoot)
    window.showAndWait()
}

fun alertEditPassword(callback: (btn: Int, oldPass: String, newPass: String) -> Unit) {
    val window = Stage()
    window.setOnCloseRequest { callback(1, "", "") }
    window.initModality(Modality.APPLICATION_MODAL)
    window.width = 400.0
    window.height = 204.0
    window.title = "Change Password"
    window.isResizable = false
    window.initStyle(StageStyle.UTILITY)

    val layRoot = FlowPane(Orientation.VERTICAL)
    layRoot.padding = Insets(8.0, 16.0, 8.0, 16.0)
    layRoot.vgap = 8.0

    val paneOldPass = BorderPane()
    val paneNewPass = BorderPane()
    val paneRepeatPass = BorderPane()
    val paneBtn = BorderPane()

    val lblOldPass = Label("Old Password")
    lblOldPass.prefWidth = 150.0
    lblOldPass.prefHeight = 40.0
    val etOldPass = PasswordField()
    etOldPass.prefWidth = 216.0
    etOldPass.prefHeight = 40.0
    paneOldPass.left = lblOldPass
    paneOldPass.center = etOldPass

    val lblNewPass = Label("New Password")
    lblNewPass.prefWidth = 150.0
    lblNewPass.prefHeight = 40.0
    val etNewPass = PasswordField()
    etNewPass.prefWidth = 216.0
    etNewPass.prefHeight = 40.0
    paneNewPass.left = lblNewPass
    paneNewPass.center = etNewPass

    val lblRepeat = Label("Repeat Password")
    lblRepeat.prefWidth = 150.0
    lblRepeat.prefHeight = 40.0
    val etRepeat = PasswordField()
    etRepeat.prefWidth = 216.0
    etRepeat.prefHeight = 40.0
    paneRepeatPass.left = lblRepeat
    paneRepeatPass.center = etRepeat

    val btnOK = Button("OK")
    btnOK.ios(50, 20)
    val btnCancel = Button("Cancel")
    btnCancel.ios(50, 20)
    val paneBtnSub = BorderPane()
    paneBtnSub.left = btnCancel
    paneBtnSub.right = btnOK
    paneBtn.right = paneBtnSub

    btnOK.setOnAction {
        val oldPwd = etOldPass.text
        val newPwd = etNewPass.text
        val repeat = etRepeat.text

        if (oldPwd == null || newPwd == null || repeat == null || oldPwd == "" || newPwd == "" || repeat == "") {
            Toast.makeText("All fields must be filled.")
            return@setOnAction
        }
        if (newPwd != repeat) {
            Toast.makeText("Password and repeat are not same.")
            return@setOnAction
        }
        callback(0, oldPwd, newPwd)
        window.close()
    }

    btnCancel.setOnAction {
        callback(1, "", "")
        window.close()
    }

    layRoot.children.add(paneOldPass)
    layRoot.children.add(paneNewPass)
    layRoot.children.add(paneRepeatPass)
    layRoot.children.add(paneBtn)
    window.scene = Scene(layRoot)
    window.showAndWait()
}

