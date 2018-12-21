package tk.lorddarthart.pushovertestapp;

public class PushMessage {
    private String pushkey;
    private String pushtitle;
    private String pushtext;
    private long pushtime;

    public PushMessage(String pushkey, String pushtitle, String pushtext, long pushtime) {
        this.pushkey = pushkey;
        this.pushtitle = pushtitle;
        this.pushtext = pushtext;
        this.pushtime = pushtime;
    }

    public PushMessage() {

    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public String getPushtitle() {
        return pushtitle;
    }

    public void setPushtitle(String pushtitle) {
        this.pushtitle = pushtitle;
    }

    public String getPushtext() {
        return pushtext;
    }

    public void setPushtext(String pushtext) {
        this.pushtext = pushtext;
    }

    public long getPushtime() {
        return pushtime;
    }

    public void setPushtime(long pushtime) {
        this.pushtime = pushtime;
    }
}
