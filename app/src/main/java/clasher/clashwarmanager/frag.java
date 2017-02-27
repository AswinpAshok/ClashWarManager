package clasher.clashwarmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import info.hoang8f.android.segmented.SegmentedGroup;


/**
 * Created by ASWIN on 1/31/2017.
 */

public class frag extends Fragment implements View.OnClickListener {
    SegmentedGroup s;
    RadioButton bin,bout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_view, container, false);

        s=(SegmentedGroup) rootView.findViewById(R.id.segmented);
        bin=(RadioButton) rootView.findViewById(R.id.button_in);
        bout=(RadioButton) rootView.findViewById(R.id.button_out);


        return rootView;
    }

    @Override
    public void onClick(View v) {

        if(v==bin) {

            if (bin.isChecked()) {
                s.setTintColor(Color.GREEN);
            }
        }
    }
}
