import jakarta.websocket.OnMessage;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/test")
public class PrototypeEndpoint {

    @OnMessage
    public String handleMessage(String message) {
        return "Echo: " + message;
    }

}
