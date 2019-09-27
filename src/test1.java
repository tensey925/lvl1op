import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

public class test1 {
    private static int attemptNumber = 5;

    public static void main(String[] args) {

        String folderPath = checkDirectory();

        String extension = checkExtension();

        final String timePattern = "dd/MM/yy HH:mm:ss";
        final int diff = 10000;
        ArrayList<File> filesWithExtension = new ArrayList<File>();
        ArrayList<Long> dateMillis = new ArrayList<Long>();

        for (File checkedFile : getListOfFiles(folderPath)) {
            if (checkedFile.isFile()) {
                if (checkedFile.getName().endsWith(extension)) {
                    filesWithExtension.add(checkedFile);
                    Path path1 = Paths.get(folderPath + "//" + checkedFile.getName());
                    BasicFileAttributes attr;
                    try {
                        attr = Files.readAttributes(path1, BasicFileAttributes.class);
                        dateMillis.add(attr.creationTime().toMillis());
                    } catch (IOException e2) {
                        System.out.println("Attribute extraction failed: " + e2.getMessage());
                    }
                } else {
                    System.out.println("There is no any file with such extension");
                    System.exit(1);
                }
            }
        }

        Long latestTime = dateMillis.get(0);
        int latestTimeIndex = 0;
        for (Long elem : dateMillis) {
            if (elem > latestTime) {
                latestTime = elem;
                latestTimeIndex = dateMillis.indexOf(elem);
            }
        }

        SimpleDateFormat simpleDate = new SimpleDateFormat(timePattern);
        System.out.println("The latest file is:");
        System.out.println(filesWithExtension.get(latestTimeIndex) + " - time of creation is: " + simpleDate.format(dateMillis.get(latestTimeIndex)));

        System.out.println("The files which have time of creation less than 10sec from the timeCreation of the latest file: ");
        int existenceCounter = 0;
        for (Long elem : dateMillis) {
            if (elem >= latestTime - diff && elem < latestTime) {
                System.out.println(filesWithExtension.get(dateMillis.indexOf(elem)) + "- time of creation is: " + simpleDate.format(dateMillis.get(dateMillis.indexOf(elem))));
                existenceCounter++;
            }

        }
        if (existenceCounter == 0)
            System.out.println("No files found");
    }

    public static File[] getListOfFiles(String filepath) {
        File file = new File(filepath);
        return file.listFiles();
    }

    public static String getInputData() {
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            line = reader.readLine();
        } catch (IOException e2) {
            System.out.println("Input data error: " + e2.getMessage());
        } finally {
            return line;
        }
    }

    public static String checkDirectory() {
        System.out.println("Enter path to the directory with files OR press Enter to leave default path.");
        System.out.println("Number of attempts: " + attemptNumber);
        String path = getInputData();

        if (path.equals(null) || path.equals("")) {
            path = "C://Users//i.meleshko//1";
        }

        File folder = new File(path);

        if (folder.isDirectory()) {
            System.out.println("The actual path is: " + path);
        } else {
            System.out.println("There is no folder with the path provided");
            attemptNumber--;
            if (attemptNumber > 0)
            checkDirectory();
            else System.exit(1);
        }
        return path;
    }

    public static String checkExtension() {
        System.out.println("Enter extension(without dot) of the files to be found OR press Enter to leave default extension .txt");
        String extension = getInputData();

        if (extension.equals(null) || extension.equals("")) {
            extension = ".txt";
        } else extension = "." + extension;
        System.out.println("The actual extension is: " + extension);
        return extension;
    }

}