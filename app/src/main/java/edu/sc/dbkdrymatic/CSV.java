package edu.sc.dbkdrymatic;




import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;


import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Energy;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.Job;
import edu.sc.dbkdrymatic.internal.Settings;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;

public class CSV implements View.OnClickListener {

    private NavigationActivity activity;
    private Settings settings;
    private SelectedJobModel selectedJobModel;


    protected CSV(NavigationActivity activity, SelectedJobModel selectedJobModel) {
        this.activity = activity;
        this.selectedJobModel = selectedJobModel;

    }



    public File exportCSV(Iterable<BoostBox> boxes) throws IOException {
        File out = new File(this.activity.getExternalCacheDir(), "report.csv");
        CSVWriter writer = new CSVWriter(new FileWriter(out));

        // Write header
        String[] header = {
                "name", "airInTemp", "airOutTarget", "airOutThreshold",
                 "current", "address",
                "cumulativeEnergy","hours", "airOutTemp",
                "power", "voltage"
        };
        writer.writeNext(header);

        // Write each box
        for (BoostBox box: boxes) {
            String[] row = new String[]{
                    box.getName(),
                    Double.toString(box.getAirInTemp().doubleValue(settings.getTemperatureUnit())) + settings.getTemperatureUnit().toString(),
                    Double.toString(box.getAirOutTarget().doubleValue(settings.getTemperatureUnit())) + settings.getTemperatureUnit().toString(),
                    Double.toString(box.getAirOutThreshold().doubleValue(settings.getTemperatureUnit())) + settings.getTemperatureUnit().toString(),
                    Double.toString(box.getCurrent().doubleValue(SI.AMPERE)) + SI.AMPERE.getSymbol(),
                    box.getAddress(),
                    Double.toString(box.getCumulativeEnergy().doubleValue(SI.JOULE.times(3600000))) + "kWh",
                    Double.toString(box.getHours().doubleValue(NonSI.HOUR)) + "hours",
                    Double.toString(box.getAirOutTarget().doubleValue(settings.getTemperatureUnit())) + settings.getTemperatureUnit().toString(),
                    Double.toString(box.getPower().doubleValue(SI.WATT)) + SI.WATT.getSymbol(),
                    Double.toString(box.getVoltage().doubleValue(SI.VOLT)) + SI.VOLT.getSymbol()
            };
            writer.writeNext(row);
        }
        writer.close();

        return out;
    }

    @Override
    public void onClick(View v) {
        this.selectedJobModel.getSelectedJob().observe(this.activity, (Job job) -> {
            try {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/csv");
                File file = exportCSV(job.getBoxes());
                System.out.println(file.toURI().toString());
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.toURI().toString()));
                this.activity.startActivity(share);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
