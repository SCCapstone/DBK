package edu.sc.dbkdrymatic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;


public class ThirdFragment extends Fragment {
    public static boolean switchValue;
    public static int spinnerValue;

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.third_layout, container, false);
        return myView;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Switch and spinner value variables
        switchValue = false;
        spinnerValue = 0;

        //Country Spinner Functionality
        Spinner countrySpinnerDisplay = (Spinner) getView().findViewById(R.id.country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinnerDisplay.setAdapter(adapter);



        //Imperial/Metric Switch
        Switch unitSwitch = (Switch) getView().findViewById(R.id.unitSwitch);
        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) switchValue = true;
                else switchValue = false;
            }
        });

        //Apply Settings Button listener
        Button applyBtn = (Button) getView().findViewById(R.id.applyBtn);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check for country spinner value
                Spinner countrySpinnerValue = (Spinner) getView().findViewById(R.id.country);
                String text = countrySpinnerValue.getSelectedItem().toString();
                if (text.equals("United States"))
                {
                    spinnerValue = 0;
                }
                else if (text.equals("United Kingdom"))
                {
                    spinnerValue = 1;
                }
                else if (text.equals("Australia"))
                {
                    spinnerValue = 2;
                }

                //Sends new settings to calculations page
                Bundle bundle = new Bundle();
                bundle.putInt("country", spinnerValue);
                bundle.putBoolean("switchState", switchValue);
                FirstFragment firstFragment = new FirstFragment();
                firstFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, firstFragment).commit();

                /*Intent intent = new Intent(getActivity(), FirstFragment.class);
                intent.putExtra("switchState", switchValue);
                intent.putExtra("country", spinnerValue);
                startActivity(intent);*/
            }
        });

    }
}
