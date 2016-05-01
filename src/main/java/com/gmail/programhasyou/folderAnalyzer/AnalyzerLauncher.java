package com.gmail.programhasyou.folderAnalyzer;

import com.gmail.programhasyou.folderAnalyzer.analyzerVisitor.PathProperties;
import com.gmail.programhasyou.folderAnalyzer.analyzerVisitor.AnalyzerVisitor;

import com.sun.management.OperatingSystemMXBean;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.*;
import static java.time.Instant.*;

/**
 * Created by Aleksandr Romanov on 27/03/16.
 */
public class AnalyzerLauncher {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 1) {
            Path root = Paths.get(args[0]);
            runAnalyzer(root);
        } else {
            System.out.println("no args, give me path");
        }
    }

    private static void runAnalyzer(Path path) throws IOException, InterruptedException {
        Instant startTime = now();
        final int[] poolSize = {Runtime.getRuntime().availableProcessors()};
        ThreadPoolExecutor executor = new ThreadPoolExecutor(poolSize[0], poolSize[0], 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        PathProperties pathProperties = new PathProperties();
        Files.walkFileTree(path, new AnalyzerVisitor(executor, path, pathProperties));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Duration between = between(startTime, now());
                double systemCpuLoad = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getSystemCpuLoad();
                long filesAndFolderCounter = pathProperties.getFileCounter().get() + pathProperties.getFolderCounter().get();
                if (between.getSeconds() > 1) {
                    long filesAndFolderPerSecond = filesAndFolderCounter / between.getSeconds();
                    int corePoolSize = executor.getCorePoolSize();
                    String systemCpuLoadString = String.format(" systemCpuLoad" + "%1.4f", systemCpuLoad);
                    String corePoolSizeString = " corePoolSize " + corePoolSize;
                    System.out.println(corePoolSizeString + systemCpuLoadString + " current files and folder counter: " + filesAndFolderCounter + ", current capacity files and folders per second: " + filesAndFolderPerSecond);

                    if (systemCpuLoad < 0.8) {
                        if (poolSize[0] < Runtime.getRuntime().availableProcessors() * 10) {
                            poolSize[0] = (poolSize[0] + 1);
                            executor.setMaximumPoolSize(poolSize[0]);
                            executor.setCorePoolSize(poolSize[0]);
                            executor.prestartAllCoreThreads();
                        }
                    } else {
                        if (poolSize[0] > 2) {
                            poolSize[0] = (poolSize[0] - 1);
                            executor.setCorePoolSize(poolSize[0]);
                            executor.setMaximumPoolSize(poolSize[0]);
                            executor.prestartAllCoreThreads();
                        }
                    }

                }
                if (executor.getActiveCount() == 0) {
                    executor.shutdown();
                    System.out.println(getAnalyzedResults(pathProperties, path));
                    System.out.println(" Analyzed ends in " + formatMilliseconds(between.toMillis()) + "\n");
                    timer.cancel();
                }
            }
        }, 0, 200);


    }

    private static String getAnalyzedResults(PathProperties pathProperties, Path root) {
        String resultString = "\n\n" + root + "\n" +
                " Size: " + getSizeInReadable(pathProperties.getSizeCounter().get()) + "\n" +
                " Folders and Files: " + (pathProperties.getFileCounter().get() + pathProperties.getFolderCounter().get()) + "\n" +
                " Files: " + pathProperties.getFileCounter().get() + "\n" +
                " Folders: " + pathProperties.getFolderCounter();
        return resultString;
    }

    private static String formatMilliseconds(long millis) {
        return String.format("%02d hours, %02d minutes, %02d seconds, %2d milliseconds",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
                TimeUnit.MILLISECONDS.toMillis(millis) -
                        TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
    }

    private static String getSizeInReadable(long size) {
        DecimalFormat decimalFormat = new DecimalFormat("###.###");
        if (size < 1024) {
            return size + "bytes";
        }
        if (size < (1024 * 1024)) {
            return decimalFormat.format(size / (double) 1024) + "K";
        }
        if (size < (1024 * 1024 * 1024)) {
            return decimalFormat.format(size / (double) (1024 * 1024)) + "M";
        }
        return decimalFormat.format(size / (double) (1024 * 1024 * 1024)) + "G";
    }
}
