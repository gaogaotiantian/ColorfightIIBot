package colorfightII;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.time.Duration;
import java.time.Instant;
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
    public Double round_time = 0.0;
    public User me = null;
    public Map<Integer, User> users = new HashMap<>();
    public Map<Integer, Object> error = new HashMap<>();
    public String game_id = "0";
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

    private void _reset() {
        this.uid = 0;
        this.turn = 0;
        this.max_turn = 0;
        this.round_time = 0.0;
        this.me = null;
        this.users = new HashMap<>();
        this.error = new HashMap<>();
        this.game_id = "0";
        this.game_map = null;
        this.action_queue = new LinkedBlockingQueue<>();
        this.info_queue = new LinkedBlockingQueue<>();
    }

    private void _update( JSONObject info ){
        turn = ( (Long) info.get("turn") ).intValue();
        error = (Map<Integer, Object>) info.get("error");
        max_turn = ( (Long) ( (JSONObject)info.get("info") ).get("max_turn") ).intValue();
        round_time = (double) ( (JSONObject) info.get("info") ).get("round_time");

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
                new URI( "wss://www.colorfightai.com/gameroom/public/action_channel" )
        );
        clientEndPoint_info = new WebsocketClientEndpoint(
                new URI( "wss://www.colorfightai.com/gameroom/public/game_channel" )
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
        String url = "wss://www.colorfightai.com/gameroom/" + room;
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

    public void disconnect() throws IOException {
        System.out.println( "disconnect!" );
        clientEndPoint_action.userSession.close();
        clientEndPoint_info.userSession.close();
        action_queue.clear();
        info_queue.clear();
        action_queue = null;
        info_queue = null;
        _reset();
    }

    public Boolean update_turn() throws InterruptedException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse( info_queue.take() );
        Instant start = Instant.now();
        while ( true ) {
            while (!info_queue.isEmpty()) {
                json = (JSONObject) parser.parse(info_queue.poll(0, TimeUnit.SECONDS));
            }
            if (this.game_id.equals("0")) {
                this.game_id = (String) ((JSONObject) json.get("info")).get("game_id");
            }

            if (!this.game_id.equals((String) ((JSONObject) json.get("info")).get("game_id"))) {
                return false;
            }

            if ( ( (Long) json.get("turn") ).intValue() != turn ) {
                if ( ( (Long) ( (JSONObject) json.get("info") ).get("game_version") ).intValue() != Constants.GAME_VERSION ) {
                    System.out.println("Your bot is out of date, please update your bot by git pull or download from the website");
                }
                break;
            }
            Thread.sleep( 5 );
        }
        _update( json );
        return true;
    }

    public Boolean update_turn( double timeout ) throws InterruptedException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse( info_queue.take() );
        Instant start = Instant.now();
        while ( true ) {
            while (!info_queue.isEmpty()) {
                json = (JSONObject) parser.parse(info_queue.poll(0, TimeUnit.SECONDS));
            }
            if (this.game_id.equals("0")) {
                this.game_id = (String) ((JSONObject) json.get("info")).get("game_id");
            }

            if ( timeout > 0 && ( Duration.between( start, Instant.now() ).toSeconds() > timeout ) ) {
                return false;
            }

            if (!this.game_id.equals((String) ((JSONObject) json.get("info")).get("game_id"))) {
                return false;
            }

            if ( ( (Long) json.get("turn") ).intValue() != turn ) {
                if ( ( (Long) ( (JSONObject) json.get("info") ).get("game_version") ).intValue() != Constants.GAME_VERSION ) {
                    System.out.println("Your bot is out of date, please update your bot by git pull or download from the website");
                }
                break;
            }
            Thread.sleep( 5 );
        }
        _update( json );
        return true;
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

    public JSONObject get_gameroom_list( String host ) throws IOException, ParseException {
        URL url = new URL(host + "get_gameroom_list");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.90 Safari/537.36");
        System.out.println("\nSending 'GET' request to URL : " + url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return (JSONObject) new JSONParser().parse(response.toString());
    }

    public JSONArray get_gameroom_list() throws IOException, ParseException {
        URL url = new URL( "https://www.colorfightai.com/get_gameroom_list" );
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.90 Safari/537.36");
        System.out.println("\nSending 'GET' request to URL : " + url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return (JSONArray) new JSONParser().parse(response.toString());
    }
}
