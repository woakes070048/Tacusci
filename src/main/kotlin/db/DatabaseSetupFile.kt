package db

import java.io.File
import java.util.*

/**
 * Created by alewis on 07/12/2016.
 */
class DatabaseSetupFile(var file: File) {

    val schemas = HashMap<String, String>()
    val tables = mutableListOf<String>()

    fun pass() {
        if (file.exists()) {
            if (file.isFile) {
                if (file.name.endsWith(".sql")) {
                    passAsSQL()
                }
            }
        }
    }

    private fun passAsSQL() {
        file.readLines().forEach { line -> val lowerCaseLine = line.toLowerCase()
            if (lowerCaseLine.contains("create")) {
                if (lowerCaseLine.contains("schema")) {
                    val schemaNameRegex = """([a-zA-Z]\S+;)""".toRegex()
                    if (schemaNameRegex.containsMatchIn(lowerCaseLine)) {
                        val matches = schemaNameRegex.find(lowerCaseLine) ?: return@forEach
                        if (matches.groups.size > 1) {
                            matches.groups.forEachIndexed { i, matchGroup ->
                                if (i > 0) if (matchGroup != null) schemas.put(matchGroup.value.replace(";",""), line)
                            }
                        }
                    }
                }
            }
        }
    }
}