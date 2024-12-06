package com.unaskividicomprayventadeautos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.unaskividicomprayventadeautos.ui.login.LoginActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Verificar si el usuario está logueado
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        
        if (!isLoggedIn) {
            // Si no está logueado, redirigir al login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Encuentra los elementos de la UI
        TextView textView = findViewById(R.id.textView);
        Button verInventarioBtn = findViewById(R.id.verInventarioBtn);
        LinearLayout scrollViewLinearLayout = findViewById(R.id.scrollViewLinearLayout);

        // Agregar datos estáticos al ScrollView
        String[] marcasAutos = {"chevrolet", "ford", "honda", "mazda", "toyota", 
                               "nissan", "bmw", "porsche", "fiat", "lamborghini"};
        for (String marca : marcasAutos) {
            TextView marcaTextView = new TextView(this);
            marcaTextView.setText(marca);
            marcaTextView.setTextSize(18);
            marcaTextView.setTextColor(getResources().getColor(R.color.black));
            marcaTextView.setPadding(8, 8, 8, 8);
            
            // Agregar el OnClickListener a cada TextView
            marcaTextView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DetalleAutoActivity.class);
                intent.putExtra("marca_auto", marca);
                startActivity(intent);
            });
            
            scrollViewLinearLayout.addView(marcaTextView);
        }

        verInventarioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cargar el inventario desde SharedPreferences
                SharedPreferences prefs = getSharedPreferences("Inventario", MODE_PRIVATE);
                String jsonInventario = prefs.getString("autosJson", "[]");
                
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<DetalleAutoActivity.AutoInventario>>(){}.getType();
                    ArrayList<?> inventario = gson.fromJson(jsonInventario, type);
                    
                    if (inventario == null || inventario.isEmpty()) {
                        Toast.makeText(MainActivity.this, 
                            "No hay autos en el inventario", 
                            Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, DetalleAutoActivity.class);
                        intent.putExtra("mostrar_inventario", true);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, 
                        "Error al cargar el inventario", 
                        Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Cerrar sesión cuando la aplicación se detiene
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isLoggedIn", false).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegurarnos de que la sesión se cierre cuando la aplicación se destruye
        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isLoggedIn", false).apply();
    }
}
