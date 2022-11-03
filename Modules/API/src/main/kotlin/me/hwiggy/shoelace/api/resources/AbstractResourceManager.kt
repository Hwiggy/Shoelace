package me.hwiggy.shoelace.api.resources

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

abstract class AbstractResourceManager protected constructor(
    private val mainClass: Class<*>,
    private val dataFolder: Path
) {
    fun <Resource> loadResource(
        loader: ResourceLoader<Resource>,
        sourcePath: Path,
        targetPath: Path = sourcePath
    ): Resource? {
        val absolute = dataFolder.resolve(targetPath)
        if (!Files.exists(absolute)) {
            Files.createDirectories(absolute.parent)
            mainClass.getResourceAsStream("/$sourcePath").use {
                if (it != null) Files.copy(it, absolute)
                else Files.createFile(absolute)
            }
        }
        return loader.loadResource(absolute.toFile())
    }

    fun <Resource> loadResourceThrowing(
        loader: ResourceLoader<Resource>,
        sourcePath: Path,
        targetPath: Path
    ) = loadResource(loader, sourcePath, targetPath) ?: throw IllegalStateException("Could not load resource!")

    fun <Resource> saveResource(
        resource: Resource,
        saver: ResourceSaver<Resource>,
        targetPath: Path
    ) {
        val absolute = dataFolder.resolve(targetPath)
        if (!Files.exists(absolute)) {
            Files.createDirectories(absolute.parent)
            Files.createFile(absolute)
        }
        saver.saveResource(resource, absolute.toFile())
    }

    fun deleteResource(
        pathFirst: String,
        vararg pathRem: String
    ) = Files.delete(dataFolder.resolve(Paths.get(pathFirst, *pathRem)))
}

fun interface ResourceLoader<Resource> {
    fun loadResource(file: File): Resource?
}

fun interface ResourceSaver<Resource> {
    fun saveResource(resource: Resource, file: File)
}