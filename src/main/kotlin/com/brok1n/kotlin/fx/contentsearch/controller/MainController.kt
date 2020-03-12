package com.brok1n.kotlin.fx.contentsearch.controller

import com.brok1n.kotlin.fx.contentsearch.SELECT_OUT_DIR_TITLE
import com.brok1n.kotlin.fx.contentsearch.data.DataCenter
import com.brok1n.kotlin.fx.contentsearch.utils.FileUtils
import com.brok1n.kotlin.fx.contentsearch.utils.WindowDragListener
import com.brok1n.kotlin.fx.contentsearch.utils.log
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.Pane
import javafx.stage.DirectoryChooser
import javafx.stage.Stage
import java.io.File
import java.lang.System.exit

open class MainController {

    lateinit var scene: Scene
    lateinit var stage: Stage

    //标题栏
    @FXML
    lateinit var titlePane: Pane

    //搜索关键字输入框
    @FXML
    lateinit var searchKeyInp: TextField

    //搜索文件夹
    @FXML
    lateinit var searchDirInp: TextField

    //搜索文件夹选择按钮
    @FXML
    lateinit var selectDirBtn: Button

    //搜索文件扩展名
    @FXML
    lateinit var searchFileTypeInp: TextField

    //是否搜索没有扩展名的文件
    @FXML
    lateinit var noExtCbox: CheckBox

    //搜索按钮
    @FXML
    lateinit var searchBtn: Button

    //搜索结果输出框
    @FXML
    lateinit var resultTxtArea: TextArea

    // dataCenter
    val dc = DataCenter.instance


    //初始化
    fun init() {
        WindowDragListener(stage).enableDrag(titlePane)

        val noExtCboxTip = Tooltip()
        noExtCboxTip.text = "是否搜索无扩展名的文件"
        noExtCboxTip.style = "-fx-background-color:#1F8FE8;" +
                "-fx-text-fill:#FFFFFF;" +
                "-fx-font-size:14"
        noExtCbox.tooltip = noExtCboxTip

    }

    //当选择文件夹按钮被点击
    @FXML
    fun selectDirBtnClicked() {
        val dirChooser = DirectoryChooser()
        dirChooser.initialDirectory = File(dc.defaultDirPath)
        dirChooser.title = SELECT_OUT_DIR_TITLE
        val selectFile = dirChooser.showDialog(stage)

        "file:$selectFile".log()

        selectFile?.let {
            dc.defaultDirPath = it.absolutePath
            searchDirInp.text = it.absolutePath
        }
    }

    //当搜索按钮被点击
    @FXML
    fun searchBtnClicked() {
        clearMsg()
        dc.searchKey = searchKeyInp.text.trim()
        if (dc.searchKey.isEmpty()) {
            resultTxtArea.style = "-fx-text-fill:#FF0000"
//            resultTxtArea.stylesheets.add("-fx-font-fill:#FF0000")-fx-text-fill:pink
            resultTxtArea.text = "搜索关键字为空！请在搜索框输入要搜索的字符串!"
            return
        }
        val searchDir = File(DataCenter.instance.defaultDirPath)
        if ( searchDirInp.text.trim().isEmpty() ) {
            resultTxtArea.style = "-fx-text-fill:#FF0000"
            resultTxtArea.text = "搜索文件夹为空！请选择要搜索的文件夹!"
            return
        }

        dc.searchFileType = searchFileTypeInp.text.trim()
        "搜索的扩展名为:${dc.searchFileType}".log()

        "是否搜索没扩展名的文件:${dc.noExtType}".log()

        clearMsg()

        addMsg("搜索文件夹:${dc.defaultDirPath}")
        addMsg("搜索的文件类型:${dc.searchFileType}")
        addMsg("是否搜索无扩展名的文件:${if (dc.noExtType) {"是"} else {"否"}}")

        resultTxtArea.style = "-fx-text-fill:#22AA22"
        addMsg("开始查找文件...")

        dc.isSearching = true

        val fileList = FileUtils.getFileList(dc.defaultDirPath, dc.searchFileType, dc.noExtType)
        dc.fileList.clear()
        dc.fileList.addAll(fileList)

        addMsg("文件查找完毕！")
        addMsg("文件个数:${fileList.size}")

        addMsg("开始检索内容...")

        val contentList:List<String> = FileUtils.searchFileContent(dc.fileList, dc.searchKey)
        addMsg("检索完毕!")
        if (contentList.isEmpty()) {
            addMsg("在 ${fileList.size} 个文件中，未检索到关键字:${dc.searchKey}")
        } else {
            contentList.forEach {
                addMsg(it)
            }
        }

        dc.isSearching = false
    }

    fun addMsg(msg:String) {
        resultTxtArea.appendText("$msg\n")
    }

    fun clearMsg() {
        resultTxtArea.style = "-fx-text-fill:#FFFFFF"
        resultTxtArea.text = ""
    }

    @FXML
    fun noExtCboxClicked() {
        DataCenter.instance.noExtType = noExtCbox.isSelected
    }

    @FXML
    fun miniBtnClicked() {
        "onMiniBtnClicked...............".log()
        stage.isIconified = true
    }

    @FXML
    fun closeBtnClicked() {
        if ( !DataCenter.instance.isSearching ) {
            Platform.exit()
            exit(0)
        } else {
            val alert = Alert(Alert.AlertType.CONFIRMATION)
            alert.title = "提示"
            alert.headerText = null
            alert.graphic = null
            alert.contentText = "正在查找！确定要退出么？"

            val result = alert.showAndWait()
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                alert.close()
                DataCenter.instance.isSearching = false
                stage.close()
                Platform.exit()
                System.exit(0)
            } else {
                // ... user chose CANCEL or closed the dialog
                alert.close()
            }
        }
    }


}