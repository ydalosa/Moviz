package com.dalosa.moviz;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class Utils {
    public static void showSnackBar(View baseView, String message){
        Snackbar.make(baseView, message, Snackbar.LENGTH_LONG).show();
    }
}
