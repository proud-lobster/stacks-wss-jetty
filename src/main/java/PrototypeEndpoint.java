import java.io.IOException;

import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;

public class PrototypeEndpoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {

            @Override
            public void onMessage(String message) {
                try {
                    System.out.println(session.getId() + " : " + message);
                    session.getBasicRemote().sendText("Echo: " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
