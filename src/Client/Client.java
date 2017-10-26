package Client;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

    private static Socket socket;
    private static OutputStream out;
    private static InputStream in;
    private static final int PORT = 9987;
    private String IP;
    private WindowClientController wcc;
    private static String message = null;
    private String num;

    public void setNum(String num) {
        this.num = num;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public static void setMessage(String message) {
        Client.message = message;
    }

    public Client(WindowClientController wcc, String IP) {
        this.wcc = wcc;
        this.IP = IP;
    }

    //отправка сообщения в Dialog
    private void ToDialog(String from, String msg) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                wcc.Dialog(from, msg);
            }
        });
    }

    //метод закрытия соединения
    public void CloseSocket(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            ToDialog("ERROR close connection","");
        }
    }

    @Override
    public void run() {
        byte buf[] = new byte[64 * 1024];
        try {

            socket = new Socket(IP, PORT);
            out = socket.getOutputStream();
            in = socket.getInputStream();

            ToDialog("Nice news to you!", "You have been conneted to server!");


            while (true) {
                //если есть запрос на сервер
                if (message != null) {

                    out.write(message.getBytes());//отправка сообщения на сервер
                    String answer = new String(buf, 0, in.read(buf)); // получение ответа с сервера

                    //условие для метода получения строки по номеру(помимо команды,нужно отправить число)
                    if(answer.equals("$sntm")){
                        out.write(num.getBytes());
                        answer = new String(buf, 0, in.read(buf));
                    }

                    //вывод ответа сервера
                    ToDialog("Answer from server :", answer);
                    message = null;

                }
                Thread.sleep(500);
            }

        } catch (IOException e) {

            ToDialog("Not good news from client : ", "Cant connect to server");
            wcc.closeThread();

        } catch (InterruptedException e) {

            ToDialog("Not good news from client : ", "Error waiting message(Thread sleep)");

        }catch (StringIndexOutOfBoundsException e){}
    }
}
