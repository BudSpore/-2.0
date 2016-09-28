package com.ping.myapp32.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ping.myapp32.KeyList;
import com.ping.myapp32.People;
import com.ping.myapp32.R;
public class FirstFragment extends Fragment   {
    private EditText mNameView;
    private EditText mAreaView;
    private EditText mCoachView;
    private EditText mSeatView;
    private People people=new People();
    public static FirstFragment instance() {
        FirstFragment view = new FirstFragment();
        return view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, null);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.button1);
//        button.setEnabled(false);
        mNameView = (EditText)getActivity(). findViewById(R.id.name);
        mAreaView=(EditText)getActivity().findViewById(R.id.area);
        Spinner spinner = (Spinner)getActivity(). findViewById(R.id.spinner);
        Spinner spinner1=(Spinner)getActivity().findViewById(R.id.spinner2);

        /**
         * 车厢号
         */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.degrees);
//                Toast.makeText(getActivity(), "你点击的是:" + languages[pos], Toast.LENGTH_LONG).show();
                people.setCoach(languages[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        /**
         * 座位号
         */
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = getResources().getStringArray(R.array.seats);
//                Toast.makeText(getActivity(), "你点击的是:" + languages[pos], Toast.LENGTH_LONG).show();
                people.setSeat(languages[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = mNameView.getText().toString();
                final String area = mAreaView.getText().toString();
                people.setName(name);
                people.setArea(area);
                if (people.getName() == null ||people.getName().equals("")||
                        people.getArea() == null || people.getArea().equals("")||
                        people.getCoach() == null ||people.getCoach().equals("")||
                        people.getSeat() == null||people.getSeat().equals("")) {
                    Toast.makeText(getActivity(), "输入数据为空:", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getActivity(), KeyList.class);
                    startActivity(intent);
                }
            }
        });

    }



}
