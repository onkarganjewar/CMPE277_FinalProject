package com.example.tourguide.weather;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Onkar on 8/4/2016.
 */
public class AlertDialogFragment extends DialogFragment {
    Context context = getActivity();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ERROR!!").setMessage("Bummer!! There was an error, Please retry!").setPositiveButton("Okay!",null);

        AlertDialog dialog = builder.create();
        return dialog;
    }
}
