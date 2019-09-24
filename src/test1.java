import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;

public class test1 {
    public static void main(String[] args) {
        // input path or default path will be set
        System.out.println("Enter path to the directory with files OR press Enter to leave default path:");
        String path = InputData.getInputData();
        if (path.equals(null) || path.equals("")) {
            path = "C://Users//i.meleshko//1"; //дефолтный путь
        }
        System.out.println("The actual path is: " + path);

        // input extension or default extension will be set
        System.out.println("Enter extension(without dot) of the files to be found OR press Enter to leave default extension .txt:");
        String extension = InputData.getInputData();

        if (extension.equals(null) || extension.equals("")) {
            extension = ".txt";// дефолтное расширение
        } else extension = "." + extension;
        System.out.println("The actual extension is: " + extension);
        final String timePattern = "dd/MM/yy HH:mm:ss"; //маска для вывода даты создания файла


        File folder = new File(path);   //папка с файлами
        File[] listOfFiles = folder.listFiles(); //массив с именами всех файлов
        ArrayList<File> filesWithExtension = new ArrayList<File>(); //коллекция с именами файлов с определенным расширением
        ArrayList<Long> dateMillis = new ArrayList<Long>(); //коллекция с датами создания в милисекундах

        // проверяем файлы в папке, имена подходящих складываем в коллекцию filesWithExtension, а время создания файлов складываем в коллекцию dateMillis
        for (File checkedFile : listOfFiles) {
            if (checkedFile.isFile()) {
                if (checkedFile.getName().endsWith(extension)) {
                    filesWithExtension.add(checkedFile);
                    Path path1 = Paths.get(path + "//" + checkedFile.getName());
                    BasicFileAttributes attr;
                    try {
                        attr = Files.readAttributes(path1, BasicFileAttributes.class);
                        dateMillis.add(attr.creationTime().toMillis());
                    } catch (IOException e2) {
                        System.out.println("oops un error! " + e2.getMessage());
                    }
                }
            }
        }

        //находим самое свежее время создания в коллекции dateMillis
        Long latestTime = dateMillis.get(0);
        int latestTimeIndex = 0;
        for (Long elem : dateMillis) {
            if (elem > latestTime) {
                latestTime = elem;
                latestTimeIndex = dateMillis.indexOf(elem);
            }
        }

        //выводим самый свежий файл и время создания
        SimpleDateFormat simpleDate = new SimpleDateFormat(timePattern);
        System.out.println("The latest file is:");
        System.out.println(filesWithExtension.get(latestTimeIndex) + "- time of creation is: " + simpleDate.format(dateMillis.get(latestTimeIndex)));

        //ищем файлы которые отличаются на 10 секунд по времени создания и выводим их. Иначе "файлы не найдены"
        System.out.println("The files which have time of creation less than 10sec from the timeCreation of the latest file: ");
        int existenceCounter = 0;
        for (Long elem : dateMillis) {
            if (elem >= latestTime - 10000 && elem < latestTime) {
                System.out.println(filesWithExtension.get(dateMillis.indexOf(elem)) + "- time of creation is: " + simpleDate.format(dateMillis.get(dateMillis.indexOf(elem))));
                existenceCounter++;
            }

        }
        if (existenceCounter == 0)
            System.out.println("No files found");
    }
}

class InputData {
    public static String getInputData() {
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            line = reader.readLine();
        } catch (IOException e2) {
            System.out.println("oops un error! " + e2.getMessage());
        } finally {
            return line;
        }

    }
}

