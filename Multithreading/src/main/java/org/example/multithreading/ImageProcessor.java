package org.example.multithreading;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class ImageProcessor {

    private static final int THREAD_COUNT = 4;

    public static Image generateNegative(Image inputImage) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        long startTime = System.currentTimeMillis();
        Thread[] threads = createThreads(height, (startY, endY) -> {
            for (int y = startY; y < endY; y++) {
                for (int x = 0; x < width; x++) {
                    Color originalColor = reader.getColor(x, y);
                    Color negativeColor = new Color(
                            1.0 - originalColor.getRed(),
                            1.0 - originalColor.getGreen(),
                            1.0 - originalColor.getBlue(),
                            originalColor.getOpacity()
                    );
                    writer.setColor(x, y, negativeColor);
                }
            }
        });

        joinThreads(threads, "Negative");

        long endTime = System.currentTimeMillis();
        LoggerService.log("Generated negative image", "INFO", endTime - startTime);
        return outputImage;
    }

    public static Image applyThreshold(Image inputImage, int threshold) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        long startTime = System.currentTimeMillis();
        Thread[] threads = createThreads(height, (startY, endY) -> {
            for (int y = startY; y < endY; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = reader.getColor(x, y);
                    double brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
                    Color binary = (brightness * 255 >= threshold) ? Color.WHITE : Color.BLACK;
                    writer.setColor(x, y, binary);
                }
            }
        });

        joinThreads(threads, "Thresholding");

        long endTime = System.currentTimeMillis();
        LoggerService.log("Applied thresholding", "INFO", endTime - startTime);
        return outputImage;
    }

    public static Image applyEdgeDetection(Image inputImage) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(width, height);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();

        long startTime = System.currentTimeMillis();
        Thread[] threads = createThreads(height - 1, (startY, endY) -> {
            for (int y = Math.max(1, startY); y < endY; y++) {
                for (int x = 1; x < width - 1; x++) {
                    Color center = reader.getColor(x, y);
                    Color right = reader.getColor(x + 1, y);
                    Color bottom = reader.getColor(x, y + 1);

                    double grayCenter = (center.getRed() + center.getGreen() + center.getBlue()) / 3.0;
                    double grayRight = (right.getRed() + right.getGreen() + right.getBlue()) / 3.0;
                    double grayBottom = (bottom.getRed() + bottom.getGreen() + bottom.getBlue()) / 3.0;

                    double dx = Math.abs(grayCenter - grayRight);
                    double dy = Math.abs(grayCenter - grayBottom);
                    double edgeValue = dx + dy;

                    writer.setColor(x, y, edgeValue > 0.1 ? Color.WHITE : Color.BLACK);
                }
            }
        });

        joinThreads(threads, "Edge Detection");

        long endTime = System.currentTimeMillis();
        LoggerService.log("Applied edge detection", "INFO", endTime - startTime);
        return outputImage;
    }

    public static Image scaleImage(Image inputImage, int newWidth, int newHeight) {
        WritableImage scaledImage = new WritableImage(newWidth, newHeight);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = scaledImage.getPixelWriter();

        double xRatio = inputImage.getWidth() / newWidth;
        double yRatio = inputImage.getHeight() / newHeight;

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                int srcX = (int) (x * xRatio);
                int srcY = (int) (y * yRatio);
                Color color = reader.getColor(srcX, srcY);
                writer.setColor(x, y, color);
            }
        }

        return scaledImage;
    }

    public static Image rotate(Image inputImage, boolean clockwise) {
        int width = (int) inputImage.getWidth();
        int height = (int) inputImage.getHeight();
        WritableImage rotatedImage = new WritableImage(height, width);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = rotatedImage.getPixelWriter();

        long startTime = System.currentTimeMillis();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                if (clockwise) {
                    writer.setColor(height - 1 - y, x, color);
                } else {
                    writer.setColor(y, width - 1 - x, color);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        LoggerService.log("Rotated image " + (clockwise ? "clockwise" : "counter-clockwise"), "INFO", endTime - startTime);

        return rotatedImage;
    }

    // Utility: Create threads for multi-threaded image processing
    private static Thread[] createThreads(int height, ThreadedImageProcessor processor) {
        Thread[] threads = new Thread[THREAD_COUNT];
        int partHeight = height / THREAD_COUNT;

        for (int t = 0; t < THREAD_COUNT; t++) {
            final int startY = t * partHeight;
            final int endY = (t == THREAD_COUNT - 1) ? height : startY + partHeight;
            threads[t] = new Thread(() -> processor.process(startY, endY));
            threads[t].start();
        }

        return threads;
    }

    // Utility: Join all threads
    private static void joinThreads(Thread[] threads, String operation) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                LoggerService.logError(operation, e);
            }
        }
    }

    // Functional interface for processing thread logic
    @FunctionalInterface
    private interface ThreadedImageProcessor {
        void process(int startY, int endY);
    }
}
