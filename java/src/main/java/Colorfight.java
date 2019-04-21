import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Colorfight {
    public int uid = 0;
    public int turn = 0;
    public int max_turn = 0;
    public int round_time = 0;
    public User me = null;
    public Map<Integer, User> users;
    public Map<Integer, ArrayList<String>> error;
    public GameMap game_map = null;

    private WebsocketClientEndpoint clientEndPoint_action;
    private WebsocketClientEndpoint clientEndPoint_info;
    private BlockingQueue<String> action_queue = new LinkedBlockingQueue<>();
    private BlockingQueue<String> info_queue = new LinkedBlockingQueue<>();

    private String parse_url(String url){
        if(url.startsWith("http")){
            return url.replaceFirst("http", "ws");
        }
        else if(!url.startsWith("ws")){
            return  "ws://" + url;
        }
        return url;
    }

    private void _update(JSONObject info){
        turn = (int) info.get("turn");
        error = (Map<Integer, ArrayList<String>>) info.get("error");
        max_turn = (int) ((JSONArray)info.get("info")).get(0);
        round_time = (int) ((JSONArray) info.get("info")).get(3);
        for (Object o:(JSONArray) info.get("users")
             ) {
            JSONObject user =  (JSONObject) o;
            User temp = new User();
            temp._update_info(user);
            this.users.put((Integer) user.get("uid"), temp);
        }
        if(users.containsKey(uid)){
            me = users.get(uid);
        }
        game_map = new GameMap(
                (int) ((JSONArray)info.get("info")).get(1),
                (int) ((JSONArray)info.get("info")).get(2)
        );

    }

    public void connect(String url) throws URISyntaxException {
        url = parse_url(url);
        clientEndPoint_action = new WebsocketClientEndpoint(new URI(url+"/action_channel"));
        clientEndPoint_info = new WebsocketClientEndpoint(new URI(url+"/game_channel"));

        // add listener
        clientEndPoint_action.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                System.out.println(message);
                action_queue.add(message);
            }
        });

        // add listener
        clientEndPoint_info.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage(String message) {
                System.out.println(message);
                info_queue.add(message);
            }
        });
    }

    public void update_turn() throws InterruptedException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(info_queue.poll(0, TimeUnit.SECONDS));
        while(!info_queue.isEmpty()){
            json = (JSONObject) parser.parse(info_queue.poll(0, TimeUnit.SECONDS));
        }
        _update(json);
    }

    public boolean register(String username, String password) throws InterruptedException, ParseException {
        clientEndPoint_action.sendMessage("{\"action\":\"register\",\"username\":"+username+",\"password\":"+password+"}");
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(action_queue.poll(2, TimeUnit.SECONDS));
        if(json.get("uid")==null) {
            System.out.println(json.get("err_msg"));
            return false;
        }
        else {
            uid = (int) json.get("uid");
        }
        return true;
    }

    public String attack(Position position, int energy){
        return Constants.CMD_ATTACK
                + " " + position.getX()
                + " " + position.getY()
                + " " + energy;
    }

    public String build(Position position, char building){
        return Constants.CMD_BUILD
                + " " + position.getX()
                + " " + position.getY()
                + " " + building;
    }

    public String upgrade(Position position){
        return Constants.CMD_UPGRADE
                + " " + position.getX()
                + " " + position.getY();
    }

    public JSONObject send_cmd(ArrayList<String> cmd_list) throws InterruptedException, ParseException {
        Map<String, Object> msg = new HashMap<>();
        msg.put("action", "command");
        msg.put("cmd_list", cmd_list);
        clientEndPoint_action.sendMessage(new JSONObject(msg).toJSONString());
        JSONParser parser = new JSONParser();
        // JSONObject is basically the same as a java HashMap.
        return (JSONObject) parser.parse(action_queue.poll(2, TimeUnit.SECONDS));
    }
}
