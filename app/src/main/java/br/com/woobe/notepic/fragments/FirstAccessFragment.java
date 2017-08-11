package br.com.woobe.notepic.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.activity.MainActivity;

public class FirstAccessFragment extends Fragment {

    private Button btnComecar, btnPrimeiros;
    private MainActivity activity;

    public FirstAccessFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_access, container, false);

        btnComecar = (Button) view.findViewById(R.id.btn_comecar_usar);
        btnComecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.closeFirstAccessFragment();
            }
        });

        btnPrimeiros = (Button) view.findViewById(R.id.btn_primeiros_passos);
        btnPrimeiros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showActionButtons(true);
                getActivity().getFragmentManager().beginTransaction().remove(FirstAccessFragment.this).commit();
            }
        });

        return view;
    }

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }
}
