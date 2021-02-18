package com.moehaemad.structuredflashcards.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

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

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                // Get navigation host and do navigate up
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
                navController.navigateUp();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return root;
    }

}
