package com.example.artia.ui.result;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.artia.R;

public class ResultFragment extends Fragment {

    private ResultViewModel mViewModel;

    private ImageView image;
    private TextView title;
    private TextView date;
    private TextView origin;
    private TextView pattern;
    private TextView desc;

    public static ResultFragment newInstance() {
        return new ResultFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.result_fragment, container, false);

        image = root.findViewById(R.id.image_result_fragment);
        title = root.findViewById(R.id.title_result_fragment);


        origin = root.findViewById(R.id.origin_result_fragment);
        pattern = root.findViewById(R.id.pattern_result_fragment);
        desc = root.findViewById(R.id.desc_result_fragment);

        String origin_ = "origin";
        String pattern_ = "pattern";
        String desc_ = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim i Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. d est laborum.";

        Bundle bundle = getArguments();
        if (bundle != null) {
            int image_data = bundle.getInt("image", -1);
            String title_data = bundle.getString("title");
            String date_data = bundle.getString("date");

        image.setImageResource(image_data);
        title.setText(title_data);
        }

        origin.setText(origin_);
        pattern.setText(pattern_);
        desc.setText(desc_);



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
    }

}