package com.example.toni.liquidcalccompatible.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toni.liquidcalccompatible.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment
{

    public NoticeFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        init();
        return view;
    }

    @SuppressWarnings("EmptyMethod")
    private void init()
    {
//        NonScrollListView nonScrollListView = getActivity().findViewById(R.id.listView);
    }

}
