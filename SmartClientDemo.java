public class SmartClientDemo {
	public static void main(String[] args) {
		SmartSocket socket = new SmartSocket("127.0.0.1", 6789, new SmartSocket.SmartSocketCallback() {
			
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
				try {
					socket.send("Hello from the client!".getBytes("UTF-8"));
					System.out.println("Sent data");
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
}
