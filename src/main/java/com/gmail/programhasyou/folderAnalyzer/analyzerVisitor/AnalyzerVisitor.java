package com.gmail.programhasyou.folderAnalyzer.analyzerVisitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Executor;

/**
 * Created by Aleksandr Romanov on 27/03/16.
 */
public class AnalyzerVisitor implements FileVisitor<Path> {
    private Executor executor;
    private Path path;
    private PathProperties pathProperties;

    public AnalyzerVisitor(Executor executor, Path path, PathProperties pathProperties) {
        this.executor = executor;
        this.path = path;
        this.pathProperties = pathProperties;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        if (!path.equals(dir)) {
            pathProperties.incrementFolderCounter();
            executor.execute(() -> {
                try {
                    Files.walkFileTree(dir, new AnalyzerVisitor(executor, dir, pathProperties));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return FileVisitResult.SKIP_SUBTREE;
        } else {
            return FileVisitResult.CONTINUE;

        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        pathProperties.incrementFileCounter();
        pathProperties.setSizeCounter(attrs.size());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
}
