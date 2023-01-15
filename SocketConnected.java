import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnected implements Runnable {
    MainGUI mainGUI;
    RSA rsa;
    AES aes;
    String publicKeyString;
    BufferedReader in = null;
    BufferedWriter out = null;
    String sendMessage;
    Socket socket = null;
    Socket fileSocket;



    Thread thread1 = new Thread(this, "in");
    Thread thread2 = new Thread(this, "out");
    Thread thread3;



    @Override
    public void run() {
        while (true) {
            if (Thread.currentThread().getName().equals("in")) {
                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String encryptedMessage = in.readLine();
                    String decryptedMessage = aes.decrypt(encryptedMessage);
                    mainGUI.showText(decryptedMessage);
                } catch (Exception e) {
                }
            } else if (Thread.currentThread().getName().equals("out")){
                try {
                    assert sendMessage != null;
                    if (sendMessage.replace("\n", "").equals("")) continue;
                    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    sendMessage = mainGUI.getTitle() + "> " + sendMessage;
                    String encryptedMessage = aes.encrypt(sendMessage);
                    out.write(encryptedMessage + "\n");
                    out.flush();
                    mainGUI.showText(sendMessage);
                    sendMessage = null;
                } catch (Exception e) {
                }
            }
        }

    }
}
