package org.toshiaki.aoc

import java.io.File

class Helper {
    companion object {
        fun readResourceFile(filename: String) =
            File("./src/main/resources/$filename")
    }
}
