package com.gmail.programhasyou.folderAnalyzer.analyzerVisitor;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Aleksandr Romanov on 27/03/16.
 */
public class PathProperties {
    private AtomicLong folderCounter = new AtomicLong(0);
    private AtomicLong fileCounter = new AtomicLong(0);
    private AtomicLong sizeCounter = new AtomicLong(0);

    public AtomicLong getFolderCounter() {
        return folderCounter;
    }

    public AtomicLong getFileCounter() {
        return fileCounter;
    }

    public AtomicLong getSizeCounter() {
        return sizeCounter;
    }

    public void incrementFolderCounter() {
        folderCounter.incrementAndGet();
    }

    public void incrementFileCounter() {
        fileCounter.incrementAndGet();
    }

    public void setSizeCounter(long size) {
        sizeCounter.addAndGet(size);
    }
}
