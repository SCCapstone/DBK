package edu.sc.dbkdrymatic;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;

import org.jscience.physics.amount.Amount;

import java.text.DecimalFormat;
import java.util.Arrays;

import edu.sc.dbkdrymatic.internal.Damage;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.SiteInfo;
import edu.sc.dbkdrymatic.internal.database.AppDatabase;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;
import edu.sc.dbkdrymatic.internal.viewmodels.SettingsModel;


public class CalculatorFragment extends Fragment {

  private SelectedJobModel model;
  private SettingsModel settingsModel;

  View myView;

  @Override
  public void onCreate(Bundle savedState) {
    super.onCreate(savedState);

    // Monitor for changes to the currently selected job and update the view as appropriate.
    AppDatabase appDb = Room.databaseBuilder(
        this.getActivity().getApplicationContext(), AppDatabase.class, "dbk.db").build();
    SelectedJobModel.Factory sjmFactory = new SelectedJobModel.Factory(appDb.siteInfoDao());
    this.model = ViewModelProviders.of(this.getActivity(), sjmFactory).get(SelectedJobModel.class);

    // Monitor for changes to the application preferences.
    SharedPreferences preferences = PreferenceManager
        .getDefaultSharedPreferences(this.getContext());
    SettingsModel.Factory settingsFactory = new SettingsModel.Factory(preferences);
    this.settingsModel = ViewModelProviders.of(this.getActivity(), settingsFactory)
        .get(SettingsModel.class);
  }

  @Nullable
  @Override
  //Populates view with information from layout file
  public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, Bundle
      savedInstanceState){
    this.myView = inflater.inflate(R.layout.calculator_layout, container, false);
    return this.myView;
  }

  @Override
  public void onViewCreated(View view, Bundle savedState) {
    super.onViewCreated(view, savedState);

    this.settingsModel.getSettings().observe(this, new Observer<Settings>() {
      @Override
      public void onChanged(Settings settings) {
        CalculatorFragment.this.updateView();
      }
    });

    // This has to be in onViewCreated (per #56)
    final Spinner damageClassSpinner = (Spinner) getView().findViewById(R.id.water_loss);
    ArrayAdapter<Damage> adapter = new ArrayAdapter<Damage>(
        this.getActivity(), android.R.layout.simple_spinner_item);
    adapter.addAll(Damage.values());
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    damageClassSpinner.setAdapter(adapter);

    Job job = this.model.getSelectedJob().getValue();
    Settings settings = this.settingsModel.getSettings().getValue();

    final EditText volumeField = (EditText) (getView().findViewById(R.id.volume));
    volumeField.addTextChangedListener(new UpdateWatcher() {
      @Override
      public void update(double value) {
        job.getSiteInfo().volume = Amount.valueOf(value, settings.getVolumeUnit());
        model.update(job);
      }
    });

    final EditText insideTempField = (EditText) (getView().findViewById(R.id.inside_temp));
    insideTempField.addTextChangedListener(new UpdateWatcher() {
      @Override
      public void update(double value) {
        job.getSiteInfo().insideTemp = Amount.valueOf(value, settings.getTemperatureUnit());
        model.update(job);
      }
    });

    final EditText surfaceTempField = (EditText) (getView().findViewById(R.id.surface_temp));
    insideTempField.addTextChangedListener(new UpdateWatcher() {
      @Override
      public void update(double changed) {
        job.getSiteInfo().surfaceTemp = Amount.valueOf(changed, settings.getTemperatureUnit());
        model.update(job);
      }
    });


    final EditText desiredTempField = (EditText) (getView().findViewById(R.id.desired_temp));
    insideTempField.addTextChangedListener(new UpdateWatcher() {
      @Override
      public void update(double changed) {
        job.getSiteInfo().desiredTemp = Amount.valueOf(changed, settings.getTemperatureUnit());
        model.update(job);
      }
    });

    final EditText outsideTempField = (EditText) (getView().findViewById(R.id.outside_temp));
    insideTempField.addTextChangedListener(new UpdateWatcher() {
      @Override
      public void update(double changed) {
        job.getSiteInfo().outsideTemp = Amount.valueOf(changed, settings.getTemperatureUnit());
        model.update(job);
      }
    });

    final EditText relativeHumidityField =
        (EditText) (getView().findViewById(R.id.relative_humidity));
    insideTempField.addTextChangedListener(new UpdateWatcher() {
      @Override
      public void update(double value) {
        job.getSiteInfo().relativeHumidity = value;
        model.update(job);

      }
    });

  }

  public void updateView() {
    Job job = this.model.getSelectedJob().getValue();
    Settings settings = this.settingsModel.getSettings().getValue();

    final DecimalFormat df = new DecimalFormat("#.#");
    final SiteInfo siteInfo = job.getSiteInfo();

    final EditText volumeField = (EditText) (getView().findViewById(R.id.volume));
    volumeField.setText(
        df.format(siteInfo.volume.doubleValue(settings.getVolumeUnit())));
    final EditText insideTempField = (EditText) (getView().findViewById(R.id.inside_temp));
    insideTempField.setText(
        df.format(siteInfo.insideTemp.doubleValue(settings.getTemperatureUnit())));

    final EditText surfaceTempField = (EditText) (getView().findViewById(R.id.surface_temp));
    surfaceTempField.setText(
        df.format(siteInfo.surfaceTemp.doubleValue(settings.getTemperatureUnit())));

    final EditText desiredTempField = (EditText) (getView().findViewById(R.id.desired_temp));
    desiredTempField.setText(
        df.format(siteInfo.desiredTemp.doubleValue(settings.getTemperatureUnit())));

    final EditText outsideTempField = (EditText) (getView().findViewById(R.id.outside_temp));
    outsideTempField.setText(
        df.format(siteInfo.outsideTemp.doubleValue(settings.getTemperatureUnit())));

    final EditText relativeHumidityField =
        (EditText) (getView().findViewById(R.id.relative_humidity));
    relativeHumidityField.setText(df.format(siteInfo.relativeHumidity));

    final Spinner damageClassSpinner = (Spinner) getView().findViewById(R.id.water_loss);
    damageClassSpinner.setSelection(Arrays.asList(Damage.values()).indexOf(siteInfo.waterLoss));

    Button calculate = (Button) getView().findViewById(R.id.calculate);
    calculate.setOnClickListener(new View.OnClickListener() {//when user clicks calculate button
      @Override
      public void onClick(View view) {
        //set textview of boost boxes and d2s to change on startup
        final TextView boostBoxes = (TextView) getView().findViewById(R.id.boost_boxes);
        boostBoxes.setText(df.format(siteInfo.getBoostBoxRequirement()));

        final TextView d2s = (TextView) getView().findViewById(R.id.d2s);
        d2s.setText(df.format(siteInfo.getD2Requirement()));

        //TODO Define Requirements in site Info
        //set the text view of vapor pressure air, differential, gpp and dew_point
        final TextView dew_point = (TextView) getView().findViewById(R.id.dew_point);
        dew_point.setText(df.format(siteInfo.getDewPointRequirement()));

        final TextView vp_diff = (TextView) getView().findViewById(R.id.vp_diff);
        vp_diff.setText(df.format(siteInfo.getVPDiffRequirement()));

        final TextView vp_air = (TextView) getView().findViewById(R.id.vp_air);
        vp_air.setText(df.format(siteInfo.getVPAirRequirement()));

        final TextView gpp = (TextView) getView().findViewById(R.id.gpp);
        gpp.setText(df.format(siteInfo.getGPPRequirement()));
      }

    });
  }

  private abstract class UpdateWatcher implements TextWatcher {

    public abstract void update(double value);

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      double value = 0.0;
      try {
        value = Double.parseDouble(charSequence.toString());
      } catch (NumberFormatException e) {
        value = 0.0;
      }
      this.update(value);

    }

    /**
     * This function is intentionally left blank. We do nothing in this case.
     */
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    /**
     * This function is intentionally left blank. We do nothing in this case.
     */
    @Override
    public void afterTextChanged(Editable editable) {

    }
  }
}
