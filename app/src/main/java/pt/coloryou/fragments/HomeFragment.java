package pt.coloryou.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.coloryou.R;
import pt.coloryou.enums.FragmentsEnum;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        FloatingActionButton fabColorPicker = view.findViewById(R.id.fab_color_picker);

        fabColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ColorPickerFragment(), FragmentsEnum.COLOR_PICKER_FRAGMENT.getValor()).commit();            }
        });

        return view;
    }
}
