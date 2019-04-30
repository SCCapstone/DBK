package edu.sc.dbkdrymatic;

import android.bluetooth.BluetoothAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.sc.dbkdrymatic.internal.BoostBox;
import edu.sc.dbkdrymatic.internal.viewmodels.SelectedJobModel;

public class BoostBoxRecyclerAdapter extends RecyclerView.Adapter {
  private final Unit KWH = SI.KILO(SI.WATT).times(NonSI.HOUR);

  List<BoostBox> boostBoxes;
  BluetoothAdapter adapter;
  SelectedJobModel sjm;

  public BoostBoxRecyclerAdapter(BluetoothAdapter adapter, SelectedJobModel sjm) {
    this.boostBoxes = new ArrayList<>();
    this.adapter = adapter;
    this.sjm = sjm;
  }

  public void setBoxes(List<BoostBox> boxes) {
    this.boostBoxes.clear();
    this.boostBoxes.addAll(boxes);
    this.notifyDataSetChanged();
  }

  @NonNull
  @Override
  public BoostBoxRecyclerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    CardView card = (CardView) LayoutInflater
        .from(parent.getContext())
        .inflate(R.layout.boost_box_layout, parent, false);

    return new Holder(card);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder.getClass() != Holder.class) {
      return;
    }

    final DecimalFormat df = new DecimalFormat("#.#");
    Holder holder = (Holder) viewHolder;

    TextView title = holder.getView().findViewById(R.id.bluetooth_title);
    title.setText(boostBoxes.get(position).getName());

    TextView energy = holder.getView().findViewById(R.id.boost_box_energy);
    double kilowattHours = boostBoxes.get(position).getCumulativeEnergy().doubleValue(KWH);
    energy.setText(df.format(kilowattHours) + "kWh");

    TextView hours = holder.getView().findViewById(R.id.boost_box_hours);
    double hoursRun = boostBoxes.get(position).getHours().doubleValue(NonSI.HOUR);
    hours.setText(df.format(hoursRun) + "h");

    ImageButton refresh = holder.getView().findViewById(R.id.refresh_button);
    refresh.setOnClickListener(
        new BoostBoxRefreshListener(boostBoxes.get(position), this.adapter, this.sjm));
  }

  @Override
  public int getItemCount() {
    return boostBoxes.size();
  }

  public class Holder extends RecyclerView.ViewHolder {

    private View view;

    public Holder(View view) {
      super(view);

      this.view = view;
    }

    public View getView() {
      return this.view;
    }
  }
}
