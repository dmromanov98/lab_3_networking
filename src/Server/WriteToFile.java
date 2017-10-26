package Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
    BufferedWriter bw;

    public WriteToFile(){
        try {
            File file = new File("file.txt");
            file.delete();
            file.createNewFile();
            bw = new BufferedWriter(new FileWriter("file.txt",true));
        } catch (IOException e) {
            System.out.println("Ошибка создания/удаления файла");
        }
    }

    //метод добавления строки в файл
    public void AddToFile(String str){
        try {
            bw.append(str);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Ошибка добавления строк в файл");
        }
    }

    //закрытие записи файла
    public void close(){
        try {
            bw.close();
        } catch (IOException e) {
            System.out.println("Ошибка закрытия записи в файл");
        }
    }

}
