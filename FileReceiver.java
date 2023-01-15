import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileReceiver implements Runnable {

    Socket fileSocket;
    InputStream is;
    DataInputStream dis;
    FileOutputStream fos;
    private RSA rsa;
    private AES aes;

    public FileReceiver(Socket fileSocket, SocketConnected socket) {
        this.fileSocket = fileSocket;
        this.rsa = socket.rsa;
        this.aes = socket.aes;
    }

    @Override
    public void run() {

        while (true) {
            is = null;
            fos = null;
            dis = null;
            try {

                is = fileSocket.getInputStream();
                dis = new DataInputStream(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            try {
                String fileName = dis.readUTF();
                int size = dis.readInt();

                Date nowDate = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                File file = new File(simpleDateFormat.format(nowDate) + fileName);

                fos = new FileOutputStream(file);

                byte[] buf = new byte[16];
                int len = 0;

                while (size > 0) {
                    len = is.read(buf);
                    size--;
                    fos.write(buf, 0, len);
                }

                fos.flush();



            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.getStackTrace();
            }

        }
    }
}
