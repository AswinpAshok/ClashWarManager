package clasher.clashwarmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASWIN on 1/31/2017.
 */

public class Second_frag extends Fragment {

    private List<player> playerList=new ArrayList<>();
    private RecyclerView recyclerView;
    private rec_adapter rec_adapter;

    public Second_frag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.second_fragment, container, false);
        //////////////////////////////////////////////////////////////////////////////////////////////////



        recyclerView=(RecyclerView) rootView.findViewById(R.id.rec_view);
        rec_adapter=new rec_adapter(playerList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(rec_adapter);

        prepareEmpData();



        //////////////////////////////////////////////////////////////////////////////////////////////////
        return rootView;
    }
    private void prepareEmpData() {

        for(int i=0;i<=Global.warlist.length-2;i++)
        {

                player p = new player(Global.warlist[i], Global.warlist[i + 1]);
                playerList.add(p);
                i++;
        }
        rec_adapter.notifyDataSetChanged();
    }
}
