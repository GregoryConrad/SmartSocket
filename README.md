# SmartSocket
Probably the easiest way to use sockets in java. Designed for simplicity and speed.


# Basic Usage (More Details Coming Soon)
```
SmartSocket smartSocket = new SmartSocket("example.com", 1234, new SmartSocket.SmartSocketCallback() {
    // Note: in the following methods, the parameter socket is a reference to the current SmartSocket, in case you might want it
    public void onInitSuccess(SmartSocket socket) {
        // What to do when the socket has been initialized
        // This is only called when passing the ip and port in the constructor
        // This is never called when you pass a pre-made socket to the constructor
        socket.send(new byte[]{"Connection initialized".getBytes("UTF-8")}); // Change this with your code
    }
    
    public void onFail(SmartSocket socket, Exception e) {
        // What to do when the socket stops working/encounters an error
        // The exception is stored in the parameter e
        socket.suicide(); // Optional but advised
    }
    
    public void onNewData(SmartSocket socket, byte[] data) {
        // What to do when new data is received
        // Data is contained in data
    }
});
```
