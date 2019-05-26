package fr.sushi.app.ui.fullscreen;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.sushi.app.R;

public class FullScreenDialog extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.full_screen_dialog, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        //toolbar.setNavigationOnClickListener(view1 -> cancelUpload());
        toolbar.setTitle("My Dialog");

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void showFullScreen(){
        FullScreenDialog dialog = new FullScreenDialog();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft, "wde");
    }

    //Pass value to dialog
    /*
    Bundle b = new Bundle();
    b.putString("KEY", "VALUE");
    b.putSerializable("KEY", OBJECT);
   // or anything else

   //initialize the dialog object
     dialog.setArguments(b);


   Bundle b = getArguments();
    String name = b.getString("KEY", "DEFAULT_VALUE");
     */
}
