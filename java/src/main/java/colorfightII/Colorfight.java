package colorfightII;

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
    public Map<Integer, User> users = new HashMap<>();
    public Map<Integer, Object> error = new HashMap<>();
    public GameMap game_map = null;

    private WebsocketClientEndpoint clientEndPoint_action;
    private WebsocketClientEndpoint clientEndPoint_info;
    private BlockingQueue<String> action_queue = new LinkedBlockingQueue<>();
    private BlockingQueue<String> info_queue = new LinkedBlockingQueue<>();

    private String parse_url( String url ){
        if ( url.startsWith( "http" ) ) {
            return url.replaceFirst( "http", "wss" );
        }
        else if ( !url.startsWith( "ws" ) ) {
            return  "wss://" + url;
        }
        return url;
    }

    private void _update( JSONObject info ){
        turn = ( (Long) info.get("turn") ).intValue();
        error = (Map<Integer, Object>) info.get("error");
        max_turn = ( (Long) ( (JSONObject)info.get("info") ).get("max_turn") ).intValue();
        round_time = ( (Long) ( (JSONObject) info.get("info") ).get("round_time") ).intValue();

        game_map = new GameMap(
                ( (Long) ( (JSONObject)info.get("info") ).get("width") ).intValue(),
                ( (Long) ( (JSONObject)info.get("info") ).get("height") ).intValue()
        );
        game_map._update_info( (JSONObject) info.get("game_map") );

        for ( Object o:( (Map)info.get("users") ).values() ) {
            JSONObject user =  (JSONObject) o;
            User temp = new User();
            temp._update_info( user );
            ( (Long) user.get("uid") ).intValue();
            users.put( ((Long) user.get("uid") ).intValue(), temp );
        }
        if ( users!=null && users.containsKey(uid) ) {
            me = users.get( uid );
        }
    }

    public void connect() throws URISyntaxException {
        clientEndPoint_action = new WebsocketClientEndpoint(
                new URI( "wss://colorfightii.herokuapp.com/gameroom/public/action_channel" )
        );
        clientEndPoint_info = new WebsocketClientEndpoint(
                new URI( "wss://colorfightii.herokuapp.com/gameroom/public/game_channel" )
        );

        // add listener
        clientEndPoint_action.addMessageHandler( new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage( String message ) {
                //System.out.println(message);
                action_queue.add( message );
            }
        } );

        // add listener
        clientEndPoint_info.addMessageHandler( new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage( String message ) {
                //System.out.println(message);
                info_queue.add( message );
            }
        } );
    }

    public void connect( String room ) throws URISyntaxException {
        String url = "wss://colorfightii.herokuapp.com/gameroom/" + room;
        clientEndPoint_action = new WebsocketClientEndpoint( new URI( url+"/action_channel" ) );
        clientEndPoint_info = new WebsocketClientEndpoint( new URI( url+"/game_channel" ) );

        // add listener
        clientEndPoint_action.addMessageHandler( new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage( String message ) {
                //System.out.println(message);
                action_queue.add( message );
            }
        } );

        // add listener
        clientEndPoint_info.addMessageHandler( new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage( String message ) {
                //System.out.println(message);
                info_queue.add( message );
            }
        } );
    }

    public void connect_url( String url ) throws URISyntaxException {
        url = parse_url(url);
        clientEndPoint_action = new WebsocketClientEndpoint( new URI( url+"/action_channel" ) );
        clientEndPoint_info = new WebsocketClientEndpoint( new URI( url+"/game_channel" ) );

        // add listener
        clientEndPoint_action.addMessageHandler( new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage( String message ) {
                //System.out.println(message);
                action_queue.add( message );
            }
        } );

        // add listener
        clientEndPoint_info.addMessageHandler( new WebsocketClientEndpoint.MessageHandler() {
            public void handleMessage( String message ) {
                //System.out.println(message);
                info_queue.add( message );
            }
        } );
    }

    public void update_turn() throws InterruptedException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse( info_queue.take() );
        while ( true ) {
            while (!info_queue.isEmpty()) {
                json = (JSONObject) parser.parse(info_queue.poll(0, TimeUnit.SECONDS));
            }
            if ( ( (Long) json.get("turn") ).intValue() != turn ) {
                break;
            }
        }
        _update( json );
    }

    public boolean register( String username, String password ) throws InterruptedException, ParseException {
        clientEndPoint_action.sendMessage(
                "{\"action\":\"register\",\"username\":\""+
                        username+
                        "\",\"password\":\""+
                        password+"\",\"join_key\":\"\"}"
        );
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse( action_queue.poll( 2, TimeUnit.SECONDS ) );
        if ( json.get("uid")==null ) {
            System.out.println( json.get("err_msg") );
            return false;
        }
        else {
            uid = ( (Long) json.get("uid") ).intValue();
        }
        return true;
    }

    public boolean register( String username, String password, String join_key ) throws InterruptedException, ParseException {
        clientEndPoint_action.sendMessage(
                "{\"action\":\"register\",\"username\":\""+
                        username+
                        "\",\"password\":\""+
                        password+"\""+
                        "\"join_key\":\""+
                        join_key+"\"}"
        );
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse( action_queue.poll( 2, TimeUnit.SECONDS ) );
        if ( json.get("uid")==null ) {
            System.out.println( json.get("err_msg") );
            return false;
        }
        else {
            uid = ( (Long) json.get("uid") ).intValue();
        }
        return true;
    }

    public String attack( Position position, int energy ){
        return Constants.CMD_ATTACK
                + " " + position.x
                + " " + position.y
                + " " + energy;
    }

    public String build( Position position, char building ){
        return Constants.CMD_BUILD
                + " " + position.x
                + " " + position.y
                + " " + building;
    }

    public String upgrade( Position position ){
        return Constants.CMD_UPGRADE
                + " " + position.x
                + " " + position.y;
    }

    public JSONObject send_cmd( ArrayList<String> cmd_list ) throws InterruptedException, ParseException {
        Map<String, Object> msg = new HashMap<>();
        msg.put( "action", "command" );
        msg.put( "cmd_list", cmd_list );
        clientEndPoint_action.sendMessage( new JSONObject(msg).toJSONString() );
        JSONParser parser = new JSONParser();
        // JSONObject is basically the same as a java HashMap.
        return (JSONObject) parser.parse( action_queue.take() );
    }
}
