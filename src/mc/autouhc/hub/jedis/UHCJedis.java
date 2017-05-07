package mc.autouhc.hub.jedis;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import mc.autouhc.hub.AutoUHCHub;
import net.md_5.bungee.api.ChatColor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

public class UHCJedis {

    final AutoUHCHub main;
    private JedisPool pool;
    private Jedis subConn;
    private UHCPubSub uhcChannel;
    private Thread unmanagedSubThread;
    private String host;
    private String password;
    private int port;
    
    // These strings represent the keys for each Jedis update.
    public static final String SERVER_DATA_MAP = "uhcServers";
    public static final String LOBBY_DATA_MAP = "uhcLobbies";
    public static final String GAME_STATE_KEY = "state";
    public static final String GAME_PRIVACY_KEY = "status";
    public static final String GAME_MODE_KEY = "mode";
    public static final String ONLINE_PLAYERS_KEY = "onlinePlayers";
    public static final String MAX_PLAYERS_KEY = "maxPlayers";
    public static final String ACTIVE_SCENARIOS_KEY = "scenarios";
    public static final String TEAM_SIZE_KEY = "teamSize";

    public UHCJedis(AutoUHCHub instance) {
        this.main = instance;
        this.pool = null;
        this.uhcChannel = null;
        this.unmanagedSubThread = null;
    }

    public void connect(String host, int port, String password) {
        ClassLoader prevClassLdr = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(UHCJedis.class.getClassLoader());
        pool = new JedisPool(host, port);
        this.host = host;
        this.port = port;
        this.password = password;
        Thread.currentThread().setContextClassLoader(prevClassLdr);
    }
    
    public void handshake() {
        String serverName = main.getSettings().getServerName();
        
        try(Jedis jedis = pool.getResource()) {
            if(!password.isEmpty()) jedis.auth(password);
            boolean exists = jedis.sismember(LOBBY_DATA_MAP, serverName);
            
            if(!exists) {
                jedis.sadd(LOBBY_DATA_MAP, serverName);
                main.sendConsoleMessage(ChatColor.GREEN + "Successfully shook redis hand!");
            }else {
                main.sendConsoleMessage(ChatColor.RED + String.format("A UHC server with the name %s already exists according to Redis."
                   + " Change the server-name value inside of a config to a unique server name and restart the server.",
                serverName));
                main.disable();
            }
        }
    }

    public ArrayList<UHCServerResponse> getUHCServers() {
        ArrayList<UHCServerResponse> responses = new ArrayList<UHCServerResponse>();
        try(Jedis jedis = pool.getResource()) {
            if(!password.isEmpty()) jedis.auth(password);
            Pipeline p = jedis.pipelined();
            Response<Boolean> srvSetExists = p.exists(SERVER_DATA_MAP);
            Response<Set<String>> respSrvNames = p.smembers(SERVER_DATA_MAP);
            p.sync();

            boolean setExists = srvSetExists.get();

            if(setExists) {
                Set<String> serverNames = respSrvNames.get();
                for(String srvName : serverNames) {
                    Response<Map<String, String>> data = p.hgetAll(srvName);
                    responses.add(new UHCServerResponse(srvName, data));
                }

                p.sync();

                for(UHCServerResponse response : responses) {
                    response.setData(response.getResponseData().get());
                }
            }else {
                main.sendConsoleMessage(ChatColor.RED + "Redis doesn't have a list of UHC servers.");
            }
        }

        return responses;
    }

    public void subscribeToUHCChannel() {
        subConn = new Jedis(host, port);
        uhcChannel = new UHCPubSub(main);

        unmanagedSubThread = new Thread(new Runnable() {

            public void run() {
                subConn.psubscribe(uhcChannel, "uhc");
            }
        });

        unmanagedSubThread.start();
    }

    public void farewell() {
        if(uhcChannel != null) {
            uhcChannel.punsubscribe();
            try {
                unmanagedSubThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        String serverName = main.getSettings().getServerName();
        
        try(Jedis jedis = pool.getResource()) {
            if(!password.isEmpty()) jedis.auth(password);
            Pipeline p = jedis.pipelined();
            Response<Boolean> srvExists = p.sismember(LOBBY_DATA_MAP, serverName);
            p.sync();
            
            boolean exists = srvExists.get();
            
            if(exists) {
                p.srem(LOBBY_DATA_MAP, serverName);
                main.sendConsoleMessage(ChatColor.GREEN + "Success!");
            }else {
                main.sendConsoleMessage(ChatColor.RED + String.format("Redis: This server is not a member of the '%s' set.", LOBBY_DATA_MAP));
            }
        }

        close();
    }

    public void close() {
        if(pool != null) {
            pool.destroy();
            pool.close();
        }

        if(subConn != null) {
            subConn.close();
        }
    }
}
