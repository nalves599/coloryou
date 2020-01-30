package pt.coloryou.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pt.coloryou.R;

public class ColorFragment extends Fragment {
    String pickedColor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_add_fragment, container, false);

        pickedColor = getArguments().getString("color");

        Toast.makeText(getActivity(), "Color" + pickedColor, Toast.LENGTH_LONG).show();

        // TODO Get Color information
        return view;
    }
}
