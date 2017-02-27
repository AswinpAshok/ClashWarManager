package clasher.clashwarmanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ASWIN on 1/30/2017.
 */

public class rec_adapter extends RecyclerView.Adapter<rec_adapter.MyViewHolder> {

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public   TextView name,role;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.p_row_name);
            role=(TextView) itemView.findViewById(R.id.p_row_role);
        }
    }


    public rec_adapter(List<player> emp_list) {
        this.playerList_list = emp_list;
    }

    private List<player> playerList_list;



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.player_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            player player=playerList_list.get(position);
            holder.name.setText(player.getPlayer_name());
            holder.role.setText(player.getPlayer_role());
    }

    @Override
    public int getItemCount() {
        return playerList_list.size();
    }



}
