package com.brok1n.kotlin.fx.contentsearch.utils

import java.io.File

object FileUtils {

    /**
     * 遍历筛选文件
     * path: 文件夹路径
     * filter: 文件夹扩展名 多个用空格隔开  .jpg .png .txt
     * noExt: 是否搜索没扩展名的文件
     * */
    fun getFileList(path:String, filter:String, noExt:Boolean):List<String> {
        val list = ArrayList<String>()
        val file = File(path)
        if (!file.exists()) {
            return list
        }
        scanFile(list, file, filter.trim(), noExt)
        println(list)

        return list
    }

    private fun scanFile(list: java.util.ArrayList<String>, file: File, filter: String, noExt: Boolean) {
        file.listFiles { dir, name -> name != "." && name != ".." }.forEach {
            if (it.isDirectory) {
                scanFile(list, it, filter, noExt)
            } else {
                //扩展名
                var ex = it.name.substringAfterLast(".")
                if(ex == it.name) {
                    if (noExt) {
                        list.add(it.absolutePath)
                    }
                } else {
                    ex = ".$ex"
                    if (filter.isEmpty()) {
                        list.add(it.absolutePath)
                    } else if (filter.contains(ex)) {
                        list.add(it.absolutePath)
                    }
                }

            }
        }
    }

    fun searchFileContent(fileList: java.util.ArrayList<String>, searchKey: String): List<String> {
        val list = ArrayList<String>()
        fileList.forEach {
            val file = File(it)
            val result:String = searchContent(file, searchKey)
            if (result.isNotEmpty()) {
                list.add(result)
            }
        }
        return list
    }

    private fun searchContent(file: File, searchKey: String): String {
        var result = ""
        try {

            file.readLines().forEachIndexed { index, s ->
                if (s.contains(searchKey)) {
                    return "${file.absolutePath} : ${index+1}"
                }
            }
        }catch (e:Exception) {
            e.printStackTrace()
        }
        return result
    }


}