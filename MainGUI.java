import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class MainGUI extends JFrame {
    private Container contentPane = this.getContentPane();
    private JTextField messageTextField;
    private JTextArea textArea;
    private JPanel northPanel;
    private JPanel southPanel;
    SocketConnected socketObj;
    private JButton fileSenderBtn;
    public MainGUI(String title, SocketConnected socketObj){
        super(title);
        this.socketObj = socketObj;
        this.socketObj.mainGUI = this;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    socketObj.socket.close();
                    System.exit(0);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        init();
        this.setSize(700, 500);
        this.setVisible(true);
    }

    private void init() {
        this.southPanel = new JPanel();
        this.northPanel = new JPanel();
        this.fileSenderBtn = new JButton("파일 전송");
        this.messageTextField = new JTextField(10);
        this.messageTextField.setActionCommand(this.getTitle());
        this.messageTextField.addActionListener(e->{
            String message = messageTextField.getText();
            socketObj.sendMessage = message;
            messageTextField.setText("");
        });

        this.fileSenderBtn.addActionListener(e->{
            File file;
            JFileChooser chooser = new JFileChooser();
            int sel = chooser.showOpenDialog(MainGUI.this);
            if (sel == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();

                if (!file.exists()) {
                    System.out.println("File not exists!");
                    return;
                }

                FileSender fileSender = new FileSender(socketObj.fileSocket, file, socketObj);
                Thread fileSendingThread = new Thread(fileSender);
                fileSendingThread.start();
            }

        });
        this.southPanel.add(messageTextField);
        this.southPanel.add(fileSenderBtn);
        this.add(southPanel, BorderLayout.SOUTH);

        initTextArea();
    }

    private void initTextArea() {
        this.textArea = new JTextArea(10, 20);
        this.textArea.setEditable(false);
        this.textArea.setAutoscrolls(true);
        this.northPanel.add(new JScrollPane((textArea)));

        this.add(northPanel, BorderLayout.NORTH);
    }
    public void showText(String str){
        this.textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());  // 맨아래로 스크롤
    }
}
