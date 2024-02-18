package download;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class YouTubeDownloader {

    public static void main(String[] args) {
        // Get the YouTube video URL from the user.
        System.out.println("Enter the YouTube video URL:");

        try (Scanner scanner = new Scanner(System.in)) {
            String videoUrl = scanner.nextLine();

            // Create a new HttpURLConnection object.
            URL url = new URL(videoUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET.
            connection.setRequestMethod("GET");

            // Connect to the server.
            connection.connect();

            // Get the response code.
            int responseCode = connection.getResponseCode();

            // If the response code is 200, the request was successful.
            if (responseCode == 200) {
                // Get the input stream from the connection.
                try (InputStream inputStream = connection.getInputStream()) {
                    // Specify the directory on E drive
                    String downloadDirectory = "E:/downloaded";

                    // Specify the full path for the output file
                    String outputPath = downloadDirectory + "/video.mp4";

                    // Create a new FileOutputStream object to write the video file to.
                    try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
                        // Limit the download size to approximately 500 MB (adjust as needed)
                        long targetSizeBytes = 500 * 1024 * 1024; // 500 MB
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        long totalBytesRead = 0;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            totalBytesRead += bytesRead;

                            // Check if the total download size exceeds the target size
                            if (totalBytesRead > targetSizeBytes) {
                                int excessBytes = (int) (totalBytesRead - targetSizeBytes);
                                fileOutputStream.write(buffer, 0, bytesRead - excessBytes);
                                break;
                            } else {
                                fileOutputStream.write(buffer, 0, bytesRead);
                            }
                        }
                    }
                }

                // Print a success message to the console.
                System.out.println("Video downloaded successfully!");
            } else {
                // Print an error message to the console.
                System.out.println("Error downloading video: " + responseCode);
            }

            // Disconnect from the server.
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
