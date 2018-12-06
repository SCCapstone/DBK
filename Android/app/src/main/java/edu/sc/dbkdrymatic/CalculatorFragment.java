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

import java.text.DecimalFormat;
import java.util.Arrays;

import edu.sc.dbkdrymatic.internal.Damage;
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

    final DecimalFormat df = new DecimalFormat("#.#");

    final EditText volumeField = (EditText) (getView().findViewById(R.id.volume));
    volumeField.setText(
        df.format(siteInfo.volume.doubleValue(settings.getVolumeUnit())));
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
        df.format(siteInfo.insideTemp.doubleValue(settings.getTemperatureUnit())));
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
        df.format(siteInfo.desiredTemp.doubleValue(settings.getTemperatureUnit())));
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
        df.format(siteInfo.outsideTemp.doubleValue(settings.getTemperatureUnit())));
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
    relativeHumidityField.setText(df.format(siteInfo.relativeHumidity));
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

    Button calculate = (Button) getView().findViewById(R.id.calculate);
    calculate.setOnClickListener(new View.OnClickListener() {//when user clicks calculate button
      @Override
      public void onClick(View view) {
        TextView boost_boxes = (TextView)  getView().findViewById(R.id.boost_boxes);
        TextView d2s = (TextView)  getView().findViewById(R.id.d2s);

        boost_boxes.setText(df.format(siteInfo.getBoostBoxRequirement()));
        d2s.setText(df.format(siteInfo.getD2Requirement()));
      }

    });
    //END OF CALCULATOR SCREEN
  }
}
