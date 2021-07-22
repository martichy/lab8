package ru.mai;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class FileWork {

    /**
     * словарь с ключевым полем марки машины и
     * количеством машин и суммарной стоимости
     */

    HashMap<String, long[]> cars = new HashMap<>();
    String text;
    //BufferedReader bufferedReader = null;

    /**
     * открыть файл
     */

    public void open() {

        //try {
        //    bufferedReader = new BufferedReader(new FileReader("input.txt", Charset.forName("windows-1251")));
        //} catch (IOException e) {
        //    Logging.writeError(e);
        //}
    }

    /**
     * прочитать файл и заполнить словарь cars
     */

    public void fill() {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("input.txt", Charset.forName("windows-1251")));
        } catch (IOException e) {
            Logging.writeError(e);
        }
        while (true) {
            try {
                if ((text = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                Logging.writeError(e);
            }
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(text);
            String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();
            assertEquals(text, utf8EncodedString);
            String[] words = utf8EncodedString.trim().split(":");
            boolean error = false;
            long price = 0;
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].replaceAll("\"", "");
            }
            try {
                price = Long.parseLong(words[1]);
            } catch (Exception e) {
                Logging.writeWarn(utf8EncodedString, e);
                error = true;
            }
            Pattern pattern = Pattern.compile("[а-яё]");
            Matcher matcher = pattern.matcher(utf8EncodedString);
            if (!error && !matcher.find()) {
                Logging.writeWarn(utf8EncodedString, 0);
                error = true;
            }
            if (!error) {
                if (cars.containsKey(words[0])) {
                    long[] values = cars.get(words[0]);
                    values[0]++;
                    values[1] += price;
                    cars.put(words[0], values);
                } else {
                    cars.put(words[0], new long[]{1, price});
                }
            }
        }
    }

    /**
     * считать марку машины и записать в файл
     * с выходными данными итоговую строку
     */

    public void write() {
        Scanner input = new Scanner(System.in);
        final String accessText = "Средняя стоимость машин марки <";
        final String failText = "Автомобилей введенной марки не найдено.";
        FileWriter fw = null;
        try {
            fw = new FileWriter("output.txt", StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logging.writeError(e);
            System.exit(-1);
        }
        String brand = input.nextLine().toLowerCase();
        if (cars.containsKey(brand)) {
            long[] array = cars.get(brand);
            long average = array[1] / array[0];
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(accessText);
            String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();
            assertEquals(accessText, utf8EncodedString);
            String a = brand.substring(0,1).toUpperCase();
            brand = a.concat(brand.substring(1));
            try {
                fw.write(utf8EncodedString + brand + "> " + average + ".");
            } catch (IOException e) {
                Logging.writeError(e);
                System.exit(-1);
            }
        } else {
            ByteBuffer buffer = StandardCharsets.UTF_8.encode(failText);
            String utf8EncodedString = StandardCharsets.UTF_8.decode(buffer).toString();
            assertEquals(failText, utf8EncodedString);
            try {
                fw.write(utf8EncodedString);
            } catch (IOException e) {
                Logging.writeError(e);
                System.exit(-1);
            }
        }
        try {
            fw.close();
        } catch (IOException e) {
            Logging.writeError(e);
        }
    }
}