package edu.sc.dbkdrymatic;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Arrays;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.viewmodels.SettingsModel;

public class SettingsFragment extends Fragment {

  private SettingsModel settingsModel;
  private Settings settings;
  View myView;

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);

    SharedPreferences preferences = PreferenceManager
        .getDefaultSharedPreferences(this.getContext());
    SettingsModel.Factory settingsFactory = new SettingsModel.Factory(preferences);
    this.settingsModel = ViewModelProviders.of(this.getActivity(), settingsFactory)
        .get(SettingsModel.class);
    this.settingsModel.getSettings().observe(this, new Observer<Settings>() {
      @Override
      public void onChanged(@Nullable Settings settings) {
        updateView(settings);
      }
    });
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    myView = inflater.inflate(R.layout.settings_layout, container, false);
    return myView;
  }

  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final Switch unitSwitch = getView().findViewById(R.id.unitSwitch);
    unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
          settings.setVolumeUnit(SI.CUBIC_METRE);
          settings.setTemperatureUnit(SI.CELSIUS);
          settingsModel.updateSettings(settings);
        } else {
          settings.setVolumeUnit(SiteInfo.CUBIC_FOOT);
          settings.setTemperatureUnit(NonSI.FAHRENHEIT);
          settingsModel.updateSettings(settings);
        }
      }
    });

    final Spinner countrySpinner = (Spinner) (getView().findViewById(R.id.country));
    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(
        this.getActivity(), android.R.layout.simple_spinner_item);
    adapter.addAll(Country.values());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    countrySpinner.setAdapter(adapter);
    countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        settings.setCountry((Country) (countrySpinner.getSelectedItem()));
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
  }

  public void updateView(Settings settings) {
    this.settings = settings;

    final Switch unitSwitch = getView().findViewById(R.id.unitSwitch);
    unitSwitch.setChecked(settings.getVolumeUnit() == SiteInfo.CUBIC_FOOT);

    final Spinner countrySpinner = (Spinner) (getView().findViewById(R.id.country));
    countrySpinner.setSelection(Arrays.asList(Country.values()).indexOf(settings.getCountry()));
  }
}
