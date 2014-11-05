package status;

import java.time.LocalDateTime;

/**
 * Created by byte on 05.11.14.
 *
 */
public class Connection {
    private String scr_ip, URI;
    private int sent_bytes, received_bytes;
    private double speed;
    private LocalDateTime timestamp;

    public Connection(String scr_ip, String URI, LocalDateTime timestamp, int sent_bytes, int received_bytes, double speed) {
        this.scr_ip = scr_ip;
        this.URI = URI;
        this.timestamp = timestamp;
        this.sent_bytes = sent_bytes;
        this.received_bytes = received_bytes;
        this.speed = speed;
    }

    public String getScr_ip() {
        return scr_ip;
    }

    public void setScr_ip(String scr_ip) {
        this.scr_ip = scr_ip;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getSent_bytes() {
        return sent_bytes;
    }

    public void setSent_bytes(int sent_bytes) {
        this.sent_bytes = sent_bytes;
    }

    public int getReceived_bytes() {
        return received_bytes;
    }

    public void setReceived_bytes(int received_bytes) {
        this.received_bytes = received_bytes;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

