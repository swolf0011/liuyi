package com.swolf.ly.testapp


import java.io.File


fun main(args: Array<String>) {
    val file = File("F:\\temp")
    pathContainsDel(file)
}

private fun pathContainsDel(file: File) {
    if (file.isDirectory) {
        if (file.name == "build" || file.name == ".idea" ||
            file.name == ".git" || file.name == ".gradle") {
            delFile(file)
        } else {
            file.listFiles().forEach { pathContainsDel(it) }
        }
    }
}

fun delFile(file: File) {
    val ls = file.listFiles()
    if(ls != null && ls.size > 0){
        ls.forEach { delFile(it) }
    }
    println("0011== delFile ===== ${file.absolutePath}")
    file.delete()
}

