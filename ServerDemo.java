import java.net.ServerSocket;

public class SmartServer {
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(6789);
			while (true) {
				SmartSocket client = new SmartSocket(server.accept(), new SmartSocket.SmartSocketCallback() {

					@Override
					public void onNewData(SmartSocket socket, byte[] data) {
						try {
							System.out.println("New data: " + new String(data, "UTF-8"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onInitSuccess(SmartSocket socket) {
						System.out.println("Client connected");
						try {
							socket.send("Hello from the server!".getBytes("UTF-8"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFail(SmartSocket socket, Exception e) {
						e.printStackTrace();
						socket.suicide();
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
