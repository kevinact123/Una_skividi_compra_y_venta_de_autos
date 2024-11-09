package com.unaskividicomprayventadeautos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Encuentra los elementos de la UI
        TextView textView = findViewById(R.id.textView);
        Button comprarButton = findViewById(R.id.Comprar);
        Button venderButton = findViewById(R.id.Vender);
        Button eliminarButton = findViewById(R.id.Eliminar);
        Button actualizarButton = findViewById(R.id.Actualizar);
        LinearLayout scrollViewLinearLayout = findViewById(R.id.scrollViewLinearLayout);

        // Agregar datos estáticos al ScrollView
        String[] datosEstaticos = {"Dato estático 1", "Dato estático 2", "Dato estático 3"};
        for (String dato : datosEstaticos) {
            TextView datoTextView = new TextView(this);
            datoTextView.setText(dato);
            datoTextView.setTextSize(18);
            datoTextView.setPadding(8, 8, 8, 8);
            scrollViewLinearLayout.addView(datoTextView);
        }


        comprarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Comprar", Toast.LENGTH_SHORT).show();
            }
        });

        venderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Vender", Toast.LENGTH_SHORT).show();
            }
        });

        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Encuentra el LinearLayout dentro del ScrollView
        LinearLayout linearLayout = findViewById(R.id.scrollViewLinearLayout); // Cambia el ID según el XML

        // Datos estáticos para agregar
        String[] datosEstaticos = {"Dato estático 1", "Dato estático 2", "Dato estático 3"};

        // Agrega TextViews con datos estáticos al LinearLayout
        for (String dato : datosEstaticos) {
            TextView textView = new TextView(this);
            textView.setText(dato);
            textView.setTextSize(18);
            textView.setPadding(8, 8, 8, 8);

            // Añade el TextView al LinearLayout
            linearLayout.addView(textView);
        }
    }
}
