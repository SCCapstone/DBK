package edu.sc.dbkdrymatic;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class FirstFragment extends Fragment {

    View myView;

    public FirstFragment() {

    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState){
        myView = inflater.inflate(R.layout.first_layout, container, false);
        return myView;


    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // START OF CALCULATOR SCREEN

        Spinner spinner = getView().findViewById(R.id.water_loss);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button calculate = (Button)  getView().findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener() {//when user clicks calculate button
            @Override
            public void onClick(View view) {
                EditText volume_edit = (EditText) getView().findViewById(R.id.volume);//finds each number from xml
                EditText inside_temp_edit = (EditText)  getView().findViewById(R.id.inside_temp);
                EditText desired_temp_edit = (EditText)  getView().findViewById(R.id.desired_temp);
                EditText outside_temp_edit = (EditText)  getView().findViewById(R.id.outside_temp);
                EditText relative_humidity_edit = (EditText)  getView().findViewById(R.id.relative_humidity);
                TextView boost_boxes = (TextView)  getView().findViewById(R.id.boost_boxes);
                TextView d2s = (TextView)  getView().findViewById(R.id.d2s);

                //Receives information from settings page
                boolean incomingSwitch = false;
                int incomingCountry = 0;
                Bundle bundle = getArguments();
                if(bundle != null){
                    incomingSwitch = bundle.getBoolean("switchState");
                    incomingCountry = bundle.getInt("country");
                }

                /*Intent incomingIntent = getActivity().getIntent();
                boolean incomingSwitch = incomingIntent.getBooleanExtra("switchState", false);
                int incomingCountry = incomingIntent.getIntExtra("country", 0);*/


                double volume = Double.parseDouble(volume_edit.getText().toString());//converts values inputted to integers
                double inside_temp = Double.parseDouble(inside_temp_edit.getText().toString());
                double desired_temp = Double.parseDouble(desired_temp_edit.getText().toString());
                double outside_temp = Double.parseDouble(outside_temp_edit.getText().toString());
                double relative_humidity = Double.parseDouble(relative_humidity_edit.getText().toString());

                if(incomingSwitch == Boolean.TRUE){//Converts given values from Metric to Imperial
                    volume = volume/0.0283168;
                    inside_temp = (inside_temp*1.8)+32;
                    desired_temp = (desired_temp*1.8)+32;
                    outside_temp = (outside_temp*1.8)+32;
                }

                double fdo = desired_temp-outside_temp;//f(d-o)
                double fds = desired_temp-inside_temp;//(f(d-s)
                double vapor_pressure= (relative_humidity*7.354415529)/100;//saturation vapor pressure has its own formula?
                double gpp = 4354*(vapor_pressure/(101.33-vapor_pressure));

                Spinner mySpinner = (Spinner) getView().findViewById(R.id.water_loss);
                String text = mySpinner.getSelectedItem().toString();
                double spinner_value=0;
                if(text.equals("1-0.05"))
                {
                    spinner_value=0.05;
                }
                else if(text.equals("2-0.12"))
               {
                    spinner_value= 0.12;
                }
                else if(text.equals("3-0.16"))
                {
                    spinner_value= 0.16;
                }
                else if(text.equals("4-0.09"))
               {
                   spinner_value= 0.09;
                }
                double d2s_value = volume/6000;
                double BTU=((volume*0.0357*fdo*0.24)+(volume*spinner_value*12.6)+(volume*spinner_value*fds*0.012)+(volume*fds*0.055));
                double kw=(BTU/3.4)/1000;
                double BTU_adjusted= BTU-6000-(d2s_value*4700);
                double kw_adjusted=(BTU_adjusted/3.4)/1000;
                double boost_boxes_value = kw_adjusted/1.4;

                if(incomingCountry == 0)//US kW rating
                {
                    BTU_adjusted = BTU-6000-(d2s_value*4700);
                    kw_adjusted = (BTU_adjusted/3.4)/1000;
                    boost_boxes_value = kw_adjusted/1.4;
                }
                else if (incomingCountry == 1)//UK kW rating
                {
                    BTU_adjusted = BTU-6000-(d2s_value*9400);
                    kw_adjusted = (BTU_adjusted/3.4)/1000;
                    boost_boxes_value = kw_adjusted/2.05;
                }
                else if (incomingCountry == 2)//AUS kW rating
                {
                    BTU_adjusted = BTU-6000-(d2s_value*9400);
                    kw_adjusted = (BTU_adjusted/3.4)/1000;
                    boost_boxes_value = kw_adjusted/1.8;
                }

                boost_boxes.setText(boost_boxes_value + "");
                d2s.setText(d2s_value + "");


            }

        });
        //END OF CALCULATOR SCREEN




    }
}
