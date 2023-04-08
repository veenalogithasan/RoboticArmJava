package com.example.roboticarmjava.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.roboticarmjava.BluetoothLeService;
import com.example.roboticarmjava.MainActivity;
import com.example.roboticarmjava.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link inp_out_orientation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inp_out_orientation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public inp_out_orientation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inp_out_orientation.
     */
    // TODO: Rename and change types and number of parameters
    public static inp_out_orientation newInstance(String param1, String param2) {
        inp_out_orientation fragment = new inp_out_orientation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    View rootView;
    TextView roll;
    TextView yaw;

    TextView pitch;

    TextView pitchToUser;
    TextView rollToUser;
    TextView yawToUser;

    ImageButton start;
    Button receive;
    Button reset;

    float pitchReset = 0;
    float rollReset = 0;
    float yawReset = 0;

    BluetoothLeService bluetoothLeService;
    //TODO: RECEIVE BROADCAST
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inp_out_orientation, container, false);

        pitch = rootView.findViewById(R.id.PitchInput);
        roll = rootView.findViewById(R.id.RollInput);
        yaw = rootView.findViewById(R.id.YawInput);

        pitchToUser = rootView.findViewById(R.id.ActualPitch);
        rollToUser = rootView.findViewById(R.id.ActualRoll);
        yawToUser = rootView.findViewById(R.id.ActualYaw);

        bluetoothLeService =((MainActivity) this.getContext()).mBluetoothLeService;
        start = rootView.findViewById(R.id.start);
        receive = rootView.findViewById(R.id.receiveAngle);

        reset = rootView.findViewById(R.id.neutral);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pitchString = pitch.getText().toString();
                String rollString = roll.getText().toString();
                String yawString = yaw.getText().toString();
                float pitchFloat = Float.parseFloat(pitchString);
                float rollFloat = Float.parseFloat(rollString);
                float yawFloat = Float.parseFloat(yawString);
                byte[] arr = new byte[]{(byte) pitchFloat, (byte)((pitchFloat - (byte)pitchFloat)*100),
                        (byte) rollFloat, (byte)((rollFloat - (byte)rollFloat)*100),
                        (byte) yawFloat, (byte)((yawFloat - (byte)yawFloat)*100)
                };
                bluetoothLeService.sendWriteRequest(arr);
            }


        });
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothLeService.sendReadRequest();

                String pitchInputFloat = pitchData.toString();
                String rollInputFloat = yawData.toString();
                String yawInputFloat = rollData.toString();

                pitchToUser.setText(String.format("%.2f",pitchInputFloat));
                rollToUser.setText(String.format("%.2f",pitchInputFloat));
                yawToUser.setText(String.format("%.2f",pitchInputFloat));
            }



        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] arr = new byte[]{(byte) pitchReset, (byte)((pitchReset - (byte)pitchReset)*100),
                        (byte) rollReset, (byte)((rollReset - (byte)rollReset)*100),
                        (byte) yawReset, (byte)((yawReset - (byte)yawReset)*100)
                };
                bluetoothLeService.sendWriteRequest(arr);
            }


        });

        return inflater.inflate(R.layout.fragment_inp_out_orientation, container, false);
    }


}