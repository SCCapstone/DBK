package edu.sc.dbkdrymatic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;
import edu.sc.dbkdrymatic.internal.database.AppDatabase;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;
import edu.sc.dbkdrymatic.internal.viewmodels.SettingsModel;

public class MockBluetooth extends Fragment {


    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this.getContext());
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = getView().findViewById(R.id.bt_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.mock_boostbox, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Button mock = (Button) getView().findViewById(R.id.mock_bt);

        mock.setOnClickListener(new View.OnClickListener() {//when user clicks calculate button
            @Override
            public void onClick(View view) {

                TextView bt_info2 = (TextView)  getView().findViewById(R.id.bt_info);

                Spinner mySpinner = (Spinner) getView().findViewById(R.id.bt_spinner);
                String text = mySpinner.getSelectedItem().toString();
                String spinner_value="";
                if(text.equals("Mock Boost Box 1"))
                {
                    spinner_value=
                            "Mock Boost Box 1\n" +
                            "13:35:13.245 Time = 1038\n" +
                            "13:35:13.257 HoursRun = 32\n" +
                            "13:35:13.282 NTC1 = 2979 K\n" +
                            "13:35:13.282 NTC1 = 250 C\n" +
                            "13:35:13.282 NTC2 = 2990 K\n" +
                            "13:35:13.285 NTC2 = 260 C\n" +
                            "13:35:13.285 FlowIn = 3461 K\n" +
                            "13:35:13.297 FlowIn = 731 C\n" +
                            "13:35:13.297 FlowOut = 3457 K\n" +
                            "13:35:13.307 FlowOut = 727 C\n" +
                            "13:35:13.333 Airflow? = 0\n" +
                            "13:35:13.333 Max Air Off = 3230 K\n" +
                            "13:35:13.333 Min Air Off = 3180 K\n" +
                            "13:35:13.334 Run? = 1\n" +
                            "13:35:13.334 AutoRestart = 1\n" +
                            "13:35:13.336 ECal = 0\n" +
                            "13:35:13.336 E+/‑ = 255\n" +
                            "13:35:13.336 PowerLevel = 0\n" +
                            "13:35:13.337 Volts = 117\n" +
                            "13:35:13.357 Amps = 0\n" +
                            "13:35:13.357 Power = 0\n" +
                            "13:35:13.382 kWh = 8\n" +
                            "13:35:13.382 Wh = 0\n" +
                            "13:35:13.382 X = 0\n" +
                            "13:35:13.383 Y = 0\n" +
                            "13:35:13.383 Z = 0\n" +
                            "13:35:13.407 Current = 1300\n" +
                            "13:35:13.407 CoordinateX = 459\n" +
                            "13:35:13.409 CoordinateY = 225\n" +
                            "13:35:13.411 ";
                }
                else if(text.equals("Mock Boost Box 2"))
                {
                    spinner_value=
                            "Mock Boost Box 2\n" +
                            "13:35:13.245 Time = 987\n" +
                            "13:35:13.257 HoursRun = 18\n" +
                            "13:35:13.282 NTC1 = 2700 K\n" +
                            "13:35:13.282 NTC1 = 210 C\n" +
                            "13:35:13.282 NTC2 = 2720 K\n" +
                            "13:35:13.285 NTC2 = 220 C\n" +
                            "13:35:13.285 FlowIn = 3300 K\n" +
                            "13:35:13.297 FlowIn = 716 C\n" +
                            "13:35:13.297 FlowOut = 3315 K\n" +
                            "13:35:13.307 FlowOut = 722 C\n" +
                            "13:35:13.333 Airflow? = 0\n" +
                            "13:35:13.333 Max Air Off = 3230 K\n" +
                            "13:35:13.333 Min Air Off = 3180 K\n" +
                            "13:35:13.334 Run? = 1\n" +
                            "13:35:13.334 AutoRestart = 1\n" +
                            "13:35:13.336 ECal = 0\n" +
                            "13:35:13.336 E+/‑ = 255\n" +
                            "13:35:13.336 PowerLevel = 0\n" +
                            "13:35:13.337 Volts = 117\n" +
                            "13:35:13.357 Amps = 0\n" +
                            "13:35:13.357 Power = 0\n" +
                            "13:35:13.382 kWh = 8\n" +
                            "13:35:13.382 Wh = 0\n" +
                            "13:35:13.382 X = 0\n" +
                            "13:35:13.383 Y = 0\n" +
                            "13:35:13.383 Z = 0\n" +
                            "13:35:13.407 Current = 1300\n" +
                            "13:35:13.407 CoordinateX = 459\n" +
                            "13:35:13.409 CoordinateY = 225\n" +
                            "13:35:13.411 ";
                }

                bt_info2.setText(spinner_value);
            }
            });
    }
}
