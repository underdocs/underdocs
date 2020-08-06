package underdocs.version.reader

import java.util.Properties

interface PropertiesReader {
    fun readProperties(): Properties
}