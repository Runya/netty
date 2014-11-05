package status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by byte on 05.11.14.
 *
 */
public class Status {

    private final int MAXSIZE = 16;

    private static Status statistics = new Status();
    private int count_requests;
    private int count_active_connections;
    private final LinkedList<Connection> connections;
    private HashMap<String, Integer> ip_count;
    private HashMap<String, LocalDateTime> ip_last_time;
    private HashMap<String, Integer> url_count;


    private Status(){
        count_requests = 0;
        connections = new LinkedList<Connection>(){
            @Override
            public synchronized void addLast(Connection conection) {
                if (super.size() >= MAXSIZE) remove(0);
                super.addLast(conection);
            }
        };
        ip_count = new HashMap<String, Integer>();
        ip_last_time = new HashMap<String, LocalDateTime>();
        url_count = new HashMap<String, Integer>();

    }

    public static Status getStatistics(){
        return statistics;
    }

    public synchronized void addRequestCount(){
        count_requests++;
    }

    public synchronized int getCountRequest(){
        return count_requests;
    }

    public synchronized void addActiveConection(String ip) {
        if (this.ip_count.containsKey(ip)) {
            int count = this.ip_count.get(ip) + 1;
            this.ip_count.put(ip, count);
        } else
        this.ip_count.put(ip, 1);

        count_active_connections++;
    }

    public synchronized int getActive_conections(){
        return (count_active_connections>=0)?count_active_connections:0;
    }

    public synchronized void addUrlrediurect(String url){
        if (!url_count.containsKey(url)){
            url_count.put(url,0);
        }
        url_count.put(url, url_count.get(url)+1);
    }

    public void add(Connection conection) {
        count_active_connections--;
        connections.addLast(conection);
        ip_last_time.put(conection.getScr_ip(), conection.getTimestamp());
    }

    public String getConnectinInfo(){
        StringBuilder buf = new StringBuilder();
        buf.append("Last 16 connections\n");
        buf.append("<table border=\"1\" cellpadding = \"0\" cellspacing = \"0\">\n");
        buf.append(getRow(getTd("src_ip") + getTd("URI") + getTd("timestamp") + getTd("sent_bytes") + getTd("received_bytes") + getTd("speed") )).append("\n");

        for(Connection conection: connections){
            buf.append(getRow(getTd(conection.getScr_ip()) +
                    getTd(conection.getURI()) +
                    getTd(conection.getTimestamp() + "") +
                    getTd(conection.getSent_bytes() + "") +
                    getTd(conection.getReceived_bytes() + "") +
                    getTd(String.format("%2.2f", conection.getSpeed())))).append("\n");
        }

        buf.append("</table>");
        return buf.toString();
    }


    public String getActivConnectionsTableLastSesion(){
        StringBuilder buf = new StringBuilder();
        buf.append("<table border=\"1\" cellpadding = \"0\" cellspacing = \"0\">\n");
        buf.append(getRow(getTd("IP" + getTd("count") + getTd("time"))));
        for(String key: ip_last_time    .keySet()){
            if (getTd(key)!=null && ip_count.get(key)!=null && ip_last_time.get(key)!=null)
            buf.append(getRow(getTd(key) + getTd(ip_count.get(key)+"") + getTd(ip_last_time.get(key).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
        }

        buf.append("</table>");

        return buf.toString();
    }

    public String getActivConnectionsTable(){
        StringBuilder buf = new StringBuilder();
        buf.append("<table border=\"1\" cellpadding = \"0\" cellspacing = \"0\">\n");
        buf.append(getRow(getTd("IP" + getTd("count"))));
        for(String key: ip_count.keySet()){
            buf.append(getRow(getTd(key) + getTd(ip_count.get(key)+"")));
        }

        buf.append("</table>");

        return buf.toString();
    }

    public String getRedirectUrlTable(){
        StringBuilder buf = new StringBuilder();
        buf.append("<table border=\"1\" cellpadding = \"0\" cellspacing = \"0\">\n");
        buf.append(getRow(getTd("url" + getTd("count"))));
        for(String key: url_count.keySet()){
            buf.append(getRow(getTd(key) + getTd(url_count.get(key)+"")));
        }

        buf.append("</table>");

        return buf.toString();
    }


    private String getRow(String con){
        return ("<tr>" + con + "</tr>");
    }

    private String getTd(String con){
        return ("<td>" + con + "</td>");
    }
}
