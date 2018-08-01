package com.example.investigacion.fourth.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.investigacion.fourth.R;

public class Messages {

    public static ProgressDialog waitSendPhoto2Mark(Context context){

        ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle("Cargando");
        progress.setMessage("Procesando respuesta...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        return progress;
    }

    public static void noFaceDetected(Activity activity) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView =  activity.getLayoutInflater().inflate(R.layout.confirm_mark_message,null);
        Button btn_ok = (Button) mView.findViewById(R.id.confirm_mark_message_ok);

        TextView textView_body =  (TextView) mView.findViewById(R.id.confirm_mark_message_body);
        TextView textView_title = (TextView) mView.findViewById(R.id.confirm_mark_message_title);
        textView_body.setText("No se reconocio ningun usuario");
        textView_title.setText("Error");
        mBuilder.setView(mView);
        final AlertDialog dialog2 = mBuilder.create();
        dialog2.setCancelable(false);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    public static void introExtraPictures(FragmentActivity activity) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView =  activity.getLayoutInflater().inflate(R.layout.confirm_mark_message,null);
        TextView title = (TextView) mView.findViewById(R.id.confirm_mark_message_title);
        title.setText("Ayudas y recomendaciones previas.");
        TextView body = (TextView) mView.findViewById(R.id.confirm_mark_message_body);

        // todo cambiar la informacion que se muestra en este cuadro de dialogo, dando recomendaciones para una sola foto
        body.setText("Para registrarse en el sistema de marcación debe guardarse 5 retratos, de manera que el sistema puede reconocerlo para realizar la marcación. En el lado izquierdo de la pantalla aparecerán las fotos para su verificación. En el caso que haya salido mal, las puede eliminar presionando las mismas. No deben salir movidas y debe mirar a la camara.");
        Button ok =  (Button) mView.findViewById(R.id.confirm_mark_message_ok);
        mBuilder.setView(mView);
        final AlertDialog dialog2 = mBuilder.create();
        dialog2.setCancelable(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    public static void introNewUserPicture(FragmentActivity activity) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView =  activity.getLayoutInflater().inflate(R.layout.confirm_mark_message,null);
        TextView title = (TextView) mView.findViewById(R.id.confirm_mark_message_title);
        title.setText("Ayudas y recomendaciones previas.");
        TextView body = (TextView) mView.findViewById(R.id.confirm_mark_message_body);
        body.setText("Para registrarse en el sistema de marcación debe guardarse 5 retratos, de manera que el sistema puede reconocerlo para realizar la marcación. En el lado izquierdo de la pantalla aparecerán las fotos para su verificación. En el caso que haya salido mal, las puede eliminar presionando las mismas. No deben salir movidas y debe mirar a la camara.");
        Button ok =  (Button) mView.findViewById(R.id.confirm_mark_message_ok);
        mBuilder.setView(mView);
        final AlertDialog dialog2 = mBuilder.create();
        dialog2.setCancelable(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }
}
