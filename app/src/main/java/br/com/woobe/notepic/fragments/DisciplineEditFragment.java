package br.com.woobe.notepic.fragments;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

import br.com.woobe.notepic.R;
import br.com.woobe.notepic.activity.MainActivity;
import br.com.woobe.notepic.model.Disciplina;
import br.com.woobe.notepic.model.Horario;
import br.com.woobe.notepic.persistence.DisciplinaDAO;
import br.com.woobe.notepic.util.Constants;
import br.com.woobe.notepic.util.Log;
import br.com.woobe.notepic.util.TimeUtils;


public class DisciplineEditFragment extends Fragment {

    private LinearLayout frameData, frameContainer;
    private RelativeLayout frameAction;
    private TextView horarioAtivo;
    private Button btnCancelar, btnOk, btnAdd;
    private EditText editText;

    public DisciplineEditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discipline_edit, container, false);
        Bundle bundle = getArguments();

        frameData = (LinearLayout) view.findViewById(R.id.frame_data);
        frameAction = (RelativeLayout) view.findViewById(R.id.frame_action);
        frameContainer = (LinearLayout) view.findViewById(R.id.frame_container_data);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_day);
        initSpinner(spinner);

        editText = (EditText) view.findViewById(R.id.edit_nome);

        TextView txtPicker = (TextView) view.findViewById(R.id.txt_time_picker);
        txtPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horarioAtivo = (TextView) v;
                showTimePicker();
            }
        });

        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View child = LayoutInflater.from(getActivity()).inflate(R.layout.item_horario, null);
                child = initHorario(child);
                frameContainer.addView(child);
            }
        });

        btnCancelar = (Button) view.findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        if (bundle.getInt(Constants.Fragments.DISCIPLINES_LIST_SIZE) <= 0)
            btnCancelar.setVisibility(View.GONE);

        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeDisciplina = editText.getText().toString();
                int countHorarios = frameContainer.getChildCount();
                List<Horario> horarios = new ArrayList<>();
                for (int i = 0; i < countHorarios; i++) {
                    View child = frameContainer.getChildAt(i);
                    TextView horario = (TextView) child.findViewById(R.id.txt_time_picker);
                    Spinner spinner = (Spinner) child.findViewById(R.id.spinner_day);
                    String dia = (String) spinner.getSelectedItem();
                    Horario h = new Horario(dia, horario.getText().toString());
                    horarios.add(h);
                }
                Disciplina disciplina = new Disciplina(nomeDisciplina, horarios);
                new DisciplinaDAO(getActivity()).save(disciplina);
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void initSpinner(Spinner spinner) {
        String[] dias = {"Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado"};
        ArrayAdapter<String> semanaAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style, dias);
        spinner.setAdapter(semanaAdapter);
        spinner.setOnItemSelectedListener(new SpinnerItemSelected());
    }

    private View initHorario(View view) {
        TextView txtPicker = (TextView) view.findViewById(R.id.txt_time_picker);
        txtPicker.setOnClickListener(new ListenerPicker());
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_day);
        initSpinner(spinner);
        Button btnExclude = (Button) view.findViewById(R.id.btn_exclude);
        btnExclude.setVisibility(View.VISIBLE);
        btnExclude.setOnClickListener(new ListenerExcludeHorario());
        return view;
    }

    private void showTimePicker() {
        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                horarioAtivo.setText(TimeUtils.fillTime(hourOfDay)+":"+TimeUtils.fillTime(minute));
            }
        }, 0,0, true).show();
    }

    private class SpinnerItemSelected implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            TextView txtView = (TextView) view.findViewById(R.id.spinner_text);
            txtView.setTextColor(getResources().getColor(R.color.app_green));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ListenerExcludeHorario implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ((ViewGroup)v.getParent().getParent()).removeView((ViewGroup)v.getParent());
        }
    }

    private class ListenerPicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            horarioAtivo = (TextView) v;
            showTimePicker();
        }
    }
}
