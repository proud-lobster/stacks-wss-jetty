import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.server.Server;

import com.proudlobster.stacks.Stacks;

import jakarta.websocket.server.ServerEndpointConfig;

public interface Main {
    public static void main(String[] args) throws Exception {
        final Stacks stacks = Stacks.create();
        System.out.println("Stacks!!!");

        Server s = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler("/");
        s.setHandler(handler);

        PrototypeEndpoint endpoint = new PrototypeEndpoint();

        ServerEndpointConfig.Configurator conf = new ServerEndpointConfig.Configurator() {
            @Override
            public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
                if (endpointClass.isInstance(endpoint)) {
                    return endpointClass.cast(endpoint);
                } else {
                    throw new InstantiationException("No endpoint for type " + endpointClass.getName());
                }
            }
        };
        ServerEndpointConfig endpointConfig = ServerEndpointConfig.Builder.create(PrototypeEndpoint.class, "/test")
                .configurator(conf)
                .build();
        JakartaWebSocketServletContainerInitializer.configure(handler,
                (ctx, srv) -> srv.addEndpoint(endpointConfig));

        s.start();
        System.out.println("Server running.");
    }
}