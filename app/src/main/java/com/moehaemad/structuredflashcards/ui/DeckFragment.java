package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moehaemad.structuredflashcards.R;


public class DeckFragment extends Fragment {

    public void DeckFragment(){
    //    TODO: configure deck View here
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*The attachToRoot is set to false because the root container is the FragmentContainerView
        *   which only allows fragments to be attached to it and not full Views that the
        *   infalter.inflate would give back.
        *solution: https://stackoverflow.com/questions/62010915/views-added-to-a-fragmentcontainerview-must-be-associated-with-a-fragment-with*/
        View root = inflater.inflate(R.layout.fragment_deck_start, container, false);
        return root;
    }
}
