import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        /*
         * TODO
         * currently a test for websocket connectivity
         */
        try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://colorfightii.herokuapp.com/gameroom/public/game_channel"));
            final WebsocketClientEndpoint clientEndPoint1 = new WebsocketClientEndpoint(new URI("ws://colorfightii.herokuapp.com/gameroom/public/action_channel"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });
            clientEndPoint1.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });

            // send message to websocket
            clientEndPoint1.sendMessage("{\"action\":\"register\",\"username\":\"delin\",\"password\":\"delin\"}");

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}
