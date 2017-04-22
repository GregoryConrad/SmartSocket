package testapp.testapplication;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class SmartSocket extends Thread {
    public HashMap<Object, Object> objects = new HashMap<>();
    private Socket socket;
    private String ip;
    private int port;
    private int bytesToSeparate;
    private SmartSocketCallback callback;
    public final int BLOCK = 1000000;

    SmartSocket(String ip, int port, int bytesToSeparate, SmartSocketCallback callback) {
        this.ip = ip;
        this.port = port;
        this.bytesToSeparate = bytesToSeparate;
        this.callback = callback;
        this.start();
    }

    @Override
    public void run() {
        try {
            this.socket = new Socket(this.ip, this.port);
            this.callback.onInitSuccess();
            while (socket != null) {
                byte[] totalInput = new byte[0];
                boolean newDataWasFound = false;

                while (socket.getInputStream().available() > 0) {
                    byte[] tempHolder = new byte[BLOCK];
                    int bytesRead = socket.getInputStream().read(tempHolder);
                    if (bytesRead < 0) {
                        throw new Exception("Connection closed: " + bytesRead);
                    }

                    byte[] tempCopy = new byte[totalInput.length + bytesRead];
                    System.arraycopy(totalInput, 0, tempCopy, 0, totalInput.length);
                    System.arraycopy(tempHolder, 0, tempCopy, totalInput.length, bytesRead);

                    totalInput = new byte[tempCopy.length];
                    System.arraycopy(tempCopy, 0, totalInput, 0, totalInput.length);
                    newDataWasFound = true;
                }

                if (newDataWasFound) {
                    byte[] info = new byte[this.bytesToSeparate];
                    System.arraycopy(totalInput, 0, info, 0, info.length);
                    byte[] data = new byte[totalInput.length - info.length];
                    System.arraycopy(totalInput, info.length, data, 0, data.length);
                    this.callback.onNewData(info, data);
                }
            }
        } catch (Exception e) {
            this.socket = null;
            this.callback.onFail(e);
            this.interrupt();
        }
    }

    public void send(byte[] info, byte[] data) throws IOException {
        this.socket.getOutputStream().write(info);
        this.socket.getOutputStream().write(data);
        this.socket.getOutputStream().flush();
    }

    interface SmartSocketCallback {
        void onFail(Exception e);

        void onInitSuccess();

        void onNewData(byte[] info, byte[] data);
    }
}
