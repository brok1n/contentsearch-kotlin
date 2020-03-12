package com.brok1n.kotlin.fx.contentsearch.data

class DataCenter {

    // 是否搜索没扩展名的文件
    var noExtType = false

    // 默认选择路径
    var defaultDirPath = ""

    //当前搜索关键字
    var searchKey = ""

    //扩展名
    var searchFileType = ""

    //是否正在搜索
    var isSearching = false

    //文件列表
    var fileList = ArrayList<String>()

    companion object {
        @JvmStatic
        val instance = DataCenter()
    }
}