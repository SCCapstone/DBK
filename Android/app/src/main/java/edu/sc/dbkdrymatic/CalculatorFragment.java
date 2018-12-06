package edu.sc.dbkdrymatic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.jscience.physics.amount.Amount;

import java.util.Arrays;

import edu.sc.dbkdrymatic.internal.Damage;
import edu.sc.dbkdrymatic.internal.JobFactory;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;


public class CalculatorFragment extends Fragment {

  private SiteInfo siteInfo;
  private Settings settings;
    View myView;

    public CalculatorFragment() {}

    public CalculatorFragment(SiteInfo siteInfo, Settings settings) {
      this.siteInfo = siteInfo;
      this.settings = settings;
    }

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState){
        myView = inflater.inflate(R.layout.first_layout, container, false);
        return myView;
    }

    public void setSiteInfo(SiteInfo siteInfo) {
      this.siteInfo = siteInfo;
    }

    public void setSettings(Settings settings) {
      this.settings = settings;
    }

  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // START OF CALCULATOR SCREEN

    final EditText volumeField = (EditText) (getView().findViewById(R.id.volume));
    volumeField.setText(
        Double.toString(siteInfo.volume.doubleValue(settings.getVolumeUnit())));
    volumeField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

      @Override
      public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        double value = 0.0;
        try {
          value = Double.parseDouble(volumeField.getText().toString());
        } catch (NumberFormatException e) {
          value = 0.0;
        }
        siteInfo.volume = Amount.valueOf(value, settings.getVolumeUnit());
      }

      @Override
      public void afterTextChanged(Editable editable) {}
    });

    final EditText insideTempField = (EditText) (getView().findViewById(R.id.inside_temp));
    insideTempField.setText(
        Double.toString(siteInfo.insideTemp.doubleValue(settings.getTemperatureUnit())));
    insideTempField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        double value = 0.0;
        try {
          value = Double.parseDouble(insideTempField.getText().toString());
        } catch (NumberFormatException e) {
          value = 0.0;
        }
        siteInfo.insideTemp = Amount.valueOf(value, settings.getTemperatureUnit());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    final EditText desiredTempField = (EditText) (getView().findViewById(R.id.desired_temp));
    desiredTempField.setText(
        Double.toString(siteInfo.desiredTemp.doubleValue(settings.getTemperatureUnit())));
    desiredTempField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        double value = 0.0;
        try {
          value = Double.parseDouble(desiredTempField.getText().toString());
        } catch (NumberFormatException e) {
          value = 0.0;
        }
        siteInfo.desiredTemp = Amount.valueOf(value, settings.getTemperatureUnit());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    final EditText outsideTempField = (EditText) (getView().findViewById(R.id.outside_temp));
    outsideTempField.setText(
        Double.toString(siteInfo.outsideTemp.doubleValue(settings.getTemperatureUnit())));
    outsideTempField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        double value = 0.0;
        try {
          value = Double.parseDouble(outsideTempField.getText().toString());
        } catch (NumberFormatException e) {
          value = 0.0;
        }
        siteInfo.outsideTemp = Amount.valueOf(value, settings.getTemperatureUnit());
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    final EditText relativeHumidityField =
        (EditText) (getView().findViewById(R.id.relative_humidity));
    relativeHumidityField.setText(Double.toString(siteInfo.relativeHumidity));
    relativeHumidityField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        double value = 0.0;
        try {
          value = Double.parseDouble(relativeHumidityField.getText().toString());
        } catch (NumberFormatException e) {
          value = 0.0;
        }
        siteInfo.relativeHumidity = value;
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    final Spinner damageClassSpinner = (Spinner) getView().findViewById(R.id.water_loss);

    ArrayAdapter<Damage> adapter = new ArrayAdapter<Damage>(this.getActivity(), android.R.layout.simple_spinner_item);
    adapter.addAll(Damage.values());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    damageClassSpinner.setAdapter(adapter);

    damageClassSpinner.setSelection(Arrays.asList(Damage.values()).indexOf(siteInfo.waterLoss));
    damageClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        siteInfo.waterLoss = (Damage) damageClassSpinner.getSelectedItem();
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });

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

        /*
        double volume = Double.parseDouble(volume_edit.getText().toString());//converts values inputted to integers
        double inside_temp = Double.parseDouble(inside_temp_edit.getText().toString());
        double desired_temp = Double.parseDouble(desired_temp_edit.getText().toString());
        double outside_temp = Double.parseDouble(outside_temp_edit.getText().toString());
        double relative_humidity = Double.parseDouble(relative_humidity_edit.getText().toString());
        double fdo = desired_temp-outside_temp;//f(d-o)
        double fds = desired_temp-inside_temp;//(f(d-s)
        double vapor_pressure= (relative_humidity*7.354415529)/100;//saturation vapor pressure has its own formula?
        double gpp = 4354*(vapor_pressure/(101.33-vapor_pressure));
        */

        /*
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
        */


        boost_boxes.setText(Double.toString(siteInfo.getBoostBoxRequirement()));
        d2s.setText(Double.toString(siteInfo.getD2Requirement()));
      }

    });
    //END OF CALCULATOR SCREEN
  }
}
