import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class FileSender implements Runnable{

    private RSA rsa;
    private AES aes;
    Socket fileSocket;
    OutputStream os;
    DataOutputStream dos;
    FileInputStream fis;
    File file;

    public FileSender(Socket fileSocket, File file, SocketConnected socket){
        this.fileSocket = fileSocket;
        this.file = file;
        this.rsa = socket.rsa;
        this.aes = socket.aes;
    }


    @Override
    public void run() {

        try {

            os = fileSocket.getOutputStream();
            dos = new DataOutputStream(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        if (!file.exists()) {
            System.out.println("File not exists!");
            return;
        }

        try {

            fis = new FileInputStream(file);

            byte[] buf = new byte[16];
            int size = 0;
            int i = 0;

            while ((i = fis.read(buf)) != -1)
                size++;

            fis.close();
            fis = new FileInputStream(file);

            dos.writeUTF(file.getName());
            dos.writeInt(size);

            while(size>0){
                size--;
                int len = fis.read(buf);
                os.write(buf, 0, len);
            }

            os.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
