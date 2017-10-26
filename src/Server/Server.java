package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

    private static ServerSocket socket;
    private static Socket socketclient;
    private static OutputStream out;
    private static InputStream in;
    private static final int PORT = 9987;
    private static ArrayList<String> array;
    private static WriteToFile wfile;

    public static void main(String[] args) {
        ServerGO();
    }

    //серверная часть
    private static void ServerGO(){
        array = new ArrayList<>();
        byte buf[] = new byte[64*1024]; //для приема данных
        boolean b;
        try {
            socket = new ServerSocket(PORT);

            while(true) {
                System.out.println("Watiting for clients");
                socketclient = socket.accept();
                wfile = new WriteToFile();
                System.out.println("Client has been connected : " + socketclient.getInetAddress().getHostAddress() + ":" + socketclient.getPort());
                wfile.AddToFile("IP: " + socketclient.getInetAddress().getHostAddress() + " PORT: " + socketclient.getPort());
                wfile.AddToFile("-------------------------------------------------------------------");

                b = true;
                ///инициализия входных/выходных потоков
                out = socketclient.getOutputStream();
                in = socketclient.getInputStream();

                while (b) {

                    int r = in.read(buf);
                    String commands = new String(buf, 0, r);

                    switch (commands) {
                        case ("$get"):
                            out.write(GetString().getBytes());
                            break;
                        case ("$deletemax"):
                            out.write(DelMax().getBytes());
                            break;
                        case ("$c10se"):
                            CloseConnectin();
                            b = false;
                            break;
                        case ("$clear"):
                            out.write(Clear().getBytes());
                            break;
                        default:
                            out.write(AddString(commands).getBytes());
                            break;
                    }
                }
            }
        }catch (SocketException ex){
            System.out.println("ERROR Connection interrupted!");
            wfile.close();
        }catch (IOException e) {
            System.out.println("ERROR Can't create ServerSocket!");
            wfile.close();
        }
    }

    //при отключении клиента
    private static void CloseConnectin(){

        try {
            socketclient.close();
        } catch (IOException e) {
            System.out.println("ERROR disconnecting client");
        }

        wfile.AddToFile("Клиент отключился!");
        System.out.println("Клиент отключился");
        wfile.close();
    }

    //метод получения строки по номеру в массиве
    private static String GetString(){
        String s;
        int n=0;
        String commands;
        byte buf[] = new byte[64*1024];

        try {
            //отправка запроса на номер элемента в массиве
            out.write("$sntm".getBytes());

            //получение номера и строки
            commands = new String(buf,0,in.read(buf));
            n = Integer.parseInt(commands);
            s = array.get(n);

            //запись в файл информации
            wfile.AddToFile("Клиент запросил строку с номером "+(String.valueOf(n)+1)+" Ответ сервера : "+ s);
            wfile.AddToFile("-------------------------------------------------------------------");
            outArray();

        }catch (IndexOutOfBoundsException ex){

            System.out.println("ERROR IndexOutOfBoundsException! Index of element is invalid!");
            s = "ERROR Index of element is invalid!";

            //запись в файл информации
            wfile.AddToFile("Клиент запросил строку с номером "+(String.valueOf(n)+1)+" Ответ сервера : "+ s);
            wfile.AddToFile("-------------------------------------------------------------------");
            outArray();

        } catch (IOException e) {
            System.out.println("Can't send request on number to client!");
            s = "ERROR of sending request on number to client!";
        }

        return s;
    }

    //метод удаления строки с максимальной длинной
    private static String DelMax(){
        int i = -1,max=0;
        for(int j = 0;j<array.size();j++){
            if(array.get(j).length() > max){
                max = array.get(j).length();
                i = j;
            }
        }

        if (i > -1) {
            array.remove(i);

            wfile.AddToFile("Клиент запросил удалить наибольшую строку. Ответ сервера : The maximum length string was deleted");
            wfile.AddToFile("-------------------------------------------------------------------");
            outArray();

            return "The maximum length string was deleted";
        }else {

            wfile.AddToFile("Клиент запросил удалить наибольшую строку. Ответ сервера : The maximum length string wasn't deleted. Array is empty!");
            wfile.AddToFile("-------------------------------------------------------------------");
            outArray();

            return "The maximum length string wasn't deleted. Array is empty!";
        }

    }

    //метод очистки массива
    private static String Clear(){
        array.clear();

        wfile.AddToFile("Клиент запросил очистить массив . Ответ сервера : List was cleaned");
        wfile.AddToFile("-------------------------------------------------------------------");
        outArray();

        return "List was cleaned";
    }

    //добавление строки в массив
    private static String AddString(String s){
        array.add(s);

        wfile.AddToFile("Клиент запросил добавить строку : <<"+s+">> В массив . Ответ сервера : String added to array");
        wfile.AddToFile("-------------------------------------------------------------------");
        outArray();

        return "String added to array";
    }

    //метод вывода массива в файл
    private static void outArray(){
        wfile.AddToFile("Эллементы массива на данный момент : ");
        for(String s:array)
            wfile.AddToFile(s);
        wfile.AddToFile("-------------------------------------------------------------------");
    }

}
