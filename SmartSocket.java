import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class SmartSocket extends Thread {
    public HashMap<Object, Object> objects = new HashMap<>();
    private Socket socket;
    private String ip = null;
    private int port;
    private SmartSocketCallback callback;
    public final int BLOCK = 1000000;

    public SmartSocket(String ip, int port, SmartSocketCallback callback) {
        this.ip = ip;
        this.port = port;
        this.callback = callback;
        this.start();
    }

    public SmartSocket(Socket socket, SmartSocketCallback callback) {
        this.socket = socket;
        this.callback = callback;
        this.start();
    }

    @Override
    public void run() {
        try {
            if (this.ip != null) {
                this.socket = new Socket(this.ip, this.port);
                this.callback.onInitSuccess(this);
            }
            while (socket != null) {
                byte[] data = new byte[1];

                { //This is to prevent unwanted CPU use
                    int currByte = this.socket.getInputStream().read();
                    if (currByte < 0) throw new Exception("Connection closed");
                    data[0] = (byte) currByte;
                }

                while (socket.getInputStream().available() > 0) {
                    byte[] tempHolder = new byte[BLOCK];
                    int bytesRead = socket.getInputStream().read(tempHolder);
                    if (bytesRead < 0) throw new Exception("Connection closed: " + bytesRead);

                    byte[] tempCopy = new byte[data.length + bytesRead];
                    System.arraycopy(data, 0, tempCopy, 0, data.length);
                    System.arraycopy(tempHolder, 0, tempCopy, data.length, bytesRead);

                    data = new byte[tempCopy.length];
                    System.arraycopy(tempCopy, 0, data, 0, data.length);
                }

                this.callback.onNewData(this, data);
            }
            throw new Exception ("Socket was destroyed");
        } catch (Exception e) {
            this.suicide();
            this.callback.onFail(this, e);
        }
    }

    public void suicide() {
        try {
            if (this.socket != null) this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.socket = null;
        this.interrupt();
    }

    public void send(byte[] info, byte[] data) throws IOException {
        this.socket.getOutputStream().write(info);
        this.socket.getOutputStream().write(data);
        this.socket.getOutputStream().flush();
    }

    public void send(byte info, byte[] data) throws IOException {
        this.socket.getOutputStream().write(info);
        this.socket.getOutputStream().write(data);
        this.socket.getOutputStream().flush();
    }

    public void send(byte[] data) throws IOException {
        this.socket.getOutputStream().write(data);
        this.socket.getOutputStream().flush();
    }

    public static byte[][] extractFirstBytes(int amountOfBytesToExtract, byte[] data){
        byte[][] returnVal = new byte[2][];
        byte[] first = new byte[amountOfBytesToExtract];
        System.arraycopy(data, 0, first, 0, first.length);
        returnVal[0] = first;
        byte[] remainingData = new byte[data.length - first.length];
        System.arraycopy(data, first.length, remainingData, 0, remainingData.length);
        returnVal[1] = remainingData;
        return returnVal;
    }

    public interface SmartSocketCallback {
        public void onFail(SmartSocket socket, Exception e);

        public void onInitSuccess(SmartSocket socket);

        public void onNewData(SmartSocket socket, byte[] data);
    }
}
