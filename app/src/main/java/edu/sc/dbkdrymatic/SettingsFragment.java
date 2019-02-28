package edu.sc.dbkdrymatic;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;

import java.util.Arrays;

import edu.sc.dbkdrymatic.internal.Country;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.viewmodels.SettingsModel;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

  private SettingsModel settingsModel;
  View myView;

  @Override
  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);

    SharedPreferences preferences = PreferenceManager
        .getDefaultSharedPreferences(this.getContext());
    SettingsModel.Factory settingsFactory = new SettingsModel.Factory(preferences);
    this.settingsModel = ViewModelProviders.of(this.getActivity(), settingsFactory)
        .get(SettingsModel.class);
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
    unitSwitch.setChecked(
        settingsModel.getSettings().getValue().getVolumeUnit() == SiteInfo.CUBIC_FOOT);
    unitSwitch.setOnCheckedChangeListener(((compoundButton, isChecked) -> {
      settingsModel.setImperial(isChecked);
      System.out.println("Unit switch callback called");
    }));

    final Spinner countrySpinner = (Spinner) (getView().findViewById(R.id.country));
    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(
        this.getActivity(), android.R.layout.simple_spinner_item);
    adapter.addAll(Country.values());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    countrySpinner.setAdapter(adapter);
    countrySpinner.setSelection(
        Arrays.asList(Country.values()).indexOf(
            settingsModel.getSettings().getValue().getCountry()));

    countrySpinner.setOnItemSelectedListener(this);


  }

  /**
   * Update the selected Country in preferences when a country is selected in the spinner.
   */
  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    final Spinner countrySpinner = (Spinner) (getView().findViewById(R.id.country));
    settingsModel.selectCountry((Country) (countrySpinner.getSelectedItem()));
  }

  /**
   * This method is intentionally left blank; we do nothing here.
   */
  @Override
  public void onNothingSelected(AdapterView<?> adapterView) {

  }
}
