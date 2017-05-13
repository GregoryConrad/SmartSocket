# SmartSocket
Probably the easiest way to use sockets in java. Designed for simplicity and speed.

Main selling points:
- Reads all available bytes of a socket
- Provides an easy way to interact with the socket and send data
- Requires no external libraries
- Small size (just more than 100 lines of code!)
- Lightweight
- Fast

# Basic Usage
_See SmartClientDemo.java and SmartServerDemo.java for a complete working demo_  
**On a client**
```
SmartSocket socket = new SmartSocket("example.com", 1234, new SmartSocket.SmartSocketCallback() {
			
			@Override
			public void onNewData(SmartSocket socket, byte[] data) {
                // What to do when new data is received
                // "socket" is a reference to the SmartSocket
                // "data" is the data that was received

                // Sample code:
				try {
					System.out.println("New data: " + new String(data, "UTF-8"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onInitSuccess(SmartSocket socket) {
                // What to do when the SmartSocket was initialized successfully
                // "socket" is a reference to the SmartSocket

                // Sample code:
				try {
					socket.send("Hello from the client!".getBytes("UTF-8"));
					System.out.println("Sent data");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFail(SmartSocket socket, Exception e) {
                // What to do when the socket stops working/encounters an error
                // If this is called, the SmartSocket is in an unusable state
                // "socket" is a reference to the SmartSocket
                // "e" is the Exception
                
                //Sample code:
                e.printStackTrace();
			}
		});
```
**On a server**
```
try {
ServerSocket server = new ServerSocket(1234); // Demo
SmartSocket socket = new SmartSocket(server.accept(), new SmartSocket.SmartSocketCallback() {
			
			@Override
			public void onNewData(SmartSocket socket, byte[] data) {
                // What to do when new data is received
                // "socket" is a reference to the SmartSocket
                // "data" is the data that was received

                // Sample code:
				try {
					System.out.println("New data: " + new String(data, "UTF-8"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onInitSuccess(SmartSocket socket) {
                // What to do when the SmartSocket was initialized successfully
                // "socket" is a reference to the SmartSocket

                // Sample code:
				try {
					socket.send("Hello from the client!".getBytes("UTF-8"));
					System.out.println("Sent data");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFail(SmartSocket socket, Exception e) {
                // What to do when the socket stops working/encounters an error
                // If this is called, the SmartSocket is in an unusable state
                // "socket" is a reference to the SmartSocket
                // "e" is the Exception
                
                //Sample code:
                e.printStackTrace();
			}
		});
		} catch (Exception e) {
		e.printStackTrace();
		}
```
