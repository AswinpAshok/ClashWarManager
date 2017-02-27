package clasher.clashwarmanager;

/**
 * Created by ASWIN on 2/2/2017.
 */

public class player {
    public String player_name;
    public String player_role;

    public player() {
    }

    public player(String player_name, String player_role) {
        this.player_name = player_name;
        this.player_role = player_role;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getPlayer_role() {
        return player_role;
    }

    public void setPlayer_role(String player_role) {
        this.player_role = player_role;
    }
}
