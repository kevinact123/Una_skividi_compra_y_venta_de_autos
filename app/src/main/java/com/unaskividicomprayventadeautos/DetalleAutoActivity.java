package com.unaskividicomprayventadeautos;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetalleAutoActivity extends AppCompatActivity {
    private String marcaAuto;
    private LinearLayout listaArticulosLayout;

    // Clase para manejar los detalles del auto
    private class AutoInventario {
        String marca;
        String modelo;
        String precio;
        String color;
        ArrayList<String> accesorios;
        String estado;
        String fechaCompra;
        String id;

        AutoInventario(String marca, String modelo, String precio) {
            this.marca = marca;
            this.modelo = modelo;
            this.precio = precio;
            this.color = "Original";
            this.accesorios = new ArrayList<>();
            this.estado = "Nuevo";
            this.fechaCompra = new java.text.SimpleDateFormat("dd/MM/yyyy")
                .format(new java.util.Date());
            this.id = java.util.UUID.randomUUID().toString();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(marca).append(" ").append(modelo)
              .append("\nPrecio: ").append(precio)
              .append("\nColor: ").append(color)
              .append("\nEstado: ").append(estado)
              .append("\nFecha de compra: ").append(fechaCompra);
            
            if (!accesorios.isEmpty()) {
                sb.append("\nAccesorios:");
                for (String accesorio : accesorios) {
                    sb.append("\n• ").append(accesorio);
                }
            }
            return sb.toString();
        }
    }

    private ArrayList<AutoInventario> inventarioAutos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_auto);

        listaArticulosLayout = findViewById(R.id.listaArticulosLayout);
        inventarioAutos = new ArrayList<>();
        cargarInventario();

        // Verificar si se abrió para ver el inventario
        boolean mostrarInventario = getIntent().getBooleanExtra("mostrar_inventario", false);
        if (mostrarInventario) {
            // Ocultar elementos de detalle de auto
            findViewById(R.id.tituloMarcaAuto).setVisibility(View.GONE);
            findViewById(R.id.scrollView2).setVisibility(View.GONE);
            findViewById(R.id.Comprar).setVisibility(View.GONE);
            findViewById(R.id.Vender).setVisibility(View.GONE);
            findViewById(R.id.Eliminar).setVisibility(View.GONE);
            findViewById(R.id.Actualizar).setVisibility(View.GONE);
            
            // Ajustar posición del ScrollView para inventario
            ScrollView scrollViewComprados = findViewById(R.id.scrollViewComprados);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) scrollViewComprados.getLayoutParams();
            params.verticalBias = 0.3f;
            scrollViewComprados.setLayoutParams(params);
            
            // Mostrar título del inventario
            TextView tituloTextView = findViewById(R.id.tituloMarcaAuto);
            tituloTextView.setText("MI INVENTARIO");
            tituloTextView.setVisibility(View.VISIBLE);
            
            return;
        }

        // Código existente para mostrar detalles del auto
        marcaAuto = getIntent().getStringExtra("marca_auto");
        TextView tituloTextView = findViewById(R.id.tituloMarcaAuto);
        tituloTextView.setText(marcaAuto.toUpperCase());

        TextView modeloLabel = findViewById(R.id.modeloLabel);
        TextView precioLabel = findViewById(R.id.precioLabel);
        TextView caracteristicasLabel = findViewById(R.id.caracteristicasLabel);

        switch (marcaAuto.toLowerCase()) {
            case "chevrolet":
                modeloLabel.setText("Modelos disponibles:\n- Camaro\n- Cruze\n- Silverado");
                precioLabel.setText("Rango de precios:\n$25,000,000 - $3,000,000");
                caracteristicasLabel.setText("Características:\n- Motor V8\n- Transmisión automática\n- Sistema de navegación");
                break;
            case "ford":
                modeloLabel.setText("Modelos disponibles:\n- Mustang\n- F-150\n- Explorer");
                precioLabel.setText("Rango de precios:\n$15,000,000 - $5,000,000");
                caracteristicasLabel.setText("Características:\n- Motor EcoBoost\n- Sistema de seguridad avanzado\n- Conectividad SYNC");
                break;
            case "honda":
                modeloLabel.setText("Modelos disponibles:\n- Civic\n- Accord\n- CR-V");
                precioLabel.setText("Rango de precios:\n$2,900,000 - $2,000,000");
                caracteristicasLabel.setText("Características:\n- Motor VTEC\n- Sistema Honda Sensing\n- Cámara de retroceso");
                break;
            case "mazda":
                modeloLabel.setText("Modelos disponibles:\n- Mazda3\n- CX-5\n- MX-5");
                precioLabel.setText("Rango de precios:\n$3,000,000 - $280,000");
                caracteristicasLabel.setText("Características:\n- Motor Skyactiv\n- Sistema i-Activsense\n- Pantalla táctil");
                break;
            case "toyota":
                modeloLabel.setText("Modelos disponibles:\n- Corolla\n- Camry\n- RAV4");
                precioLabel.setText("Rango de precios:\n$27,000,000 - $2,000,000");
                caracteristicasLabel.setText("Características:\n- Motor híbrido disponible\n- Toyota Safety Sense\n- Apple CarPlay/Android Auto");
                break;
            case "nissan":
                modeloLabel.setText("Modelos disponibles:\n- GT-R\n- 370Z\n- Altima");
                precioLabel.setText("Rango de precios:\n$45,000,000 - $11,000,000");
                caracteristicasLabel.setText("Características:\n- Motor Twin-Turbo V6\n- Sistema ProPILOT Assist\n- Sistema de sonido Bose");
                break;
            case "bmw":
                modeloLabel.setText("Modelos disponibles:\n- Serie 3\n- X5\n- M4");
                precioLabel.setText("Rango de precios:\n$41,000,000 - $6,000,000");
                caracteristicasLabel.setText("Características:\n- Motor TwinPower Turbo\n- Sistema iDrive\n- BMW ConnectedDrive");
                break;
            case "porsche":
                modeloLabel.setText("Modelos disponibles:\n- 911\n- Cayenne\n- Panamera");
                precioLabel.setText("Rango de precios:\n$12,000,000 - $9,005,000");
                caracteristicasLabel.setText("Características:\n- Motor Boxer\n- Porsche Active Suspension\n- Sport Chrono Package");
                break;
            case "fiat":
                modeloLabel.setText("Modelos disponibles:\n- 500\n- Tipo\n- Panda");
                precioLabel.setText("Rango de precios:\n$2,000,000 - $800,000");
                caracteristicasLabel.setText("Características:\n- Motor MultiAir\n- Sistema Uconnect\n- City Brake Control");
                break;
            case "lamborghini":
                modeloLabel.setText("Modelos disponibles:\n- Huracán\n- Aventador\n- Urus");
                precioLabel.setText("Rango de precios:\n$250,000,000 - $30,000,000");
                caracteristicasLabel.setText("Características:\n- Motor V12/V10\n- Sistema LDVI\n- Suspensión magnetoreológica");
                break;
        }

        Button comprarButton = findViewById(R.id.Comprar);
        Button venderButton = findViewById(R.id.Vender);
        Button eliminarButton = findViewById(R.id.Eliminar);
        Button actualizarButton = findViewById(R.id.Actualizar);

        comprarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoSeleccionModelo();
            }
        });

        venderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacion("Vender",
                    "¿Está seguro que desea vender este " + marcaAuto + "?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(DetalleAutoActivity.this,
                                "Vendiendo " + marcaAuto, Toast.LENGTH_SHORT).show();
                            // Aquí iría la lógica de venta
                        }
                    });
            }
        });

        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacion("Eliminar",
                    "¿Está seguro que desea eliminar este " + marcaAuto + "?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(DetalleAutoActivity.this,
                                "Eliminando " + marcaAuto, Toast.LENGTH_SHORT).show();
                            finish(); // Regresa a la actividad anterior
                        }
                    });
            }
        });

        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacion("Actualizar",
                    "¿Está seguro que desea actualizar este " + marcaAuto + "?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(DetalleAutoActivity.this,
                                "Actualizando " + marcaAuto, Toast.LENGTH_SHORT).show();
                            // Aquí iría la lógica de actualización
                        }
                    });
            }
        });
    }

    private void mostrarDialogoConfirmacion(String titulo, String mensaje, 
            DialogInterface.OnClickListener listenerPositivo) {
        new AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Sí", listenerPositivo)
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .show();
    }

    private void mostrarDialogoSeleccionModelo() {
        String[] modelos;
        String[] precios;
        
        switch (marcaAuto.toLowerCase()) {
            case "chevrolet":
                modelos = new String[]{"Camaro", "Cruze", "Silverado"};
                precios = new String[]{"$25,000,000", "$20,000,000", "$3,000,000"};
                break;
            case "ford":
                modelos = new String[]{"Mustang", "F-150", "Explorer"};
                precios = new String[]{"$5,000,000", "$15,000,000", "$12,000,000"};
                break;
            case "honda":
                modelos = new String[]{"Civic", "Accord", "CR-V"};
                precios = new String[]{"$2,900,000", "$2,350,000", "$2,000,000"};
                break;
            case "mazda":
                modelos = new String[]{"Mazda3", "CX-5", "MX-5"};
                precios = new String[]{"$1,500,000", "$280,000", "$3,000,000"};
                break;
            case "toyota":
                modelos = new String[]{"Corolla", "Camry", "RAV4"};
                precios = new String[]{"$2,000,000", "$25,000,000", "$27,000,000"};
                break;
            case "nissan":
                modelos = new String[]{"GT-R", "370Z", "Altima"};
                precios = new String[]{"$11,000,000", "$45,000,000", "$25,000,000"};
                break;
            case "bmw":
                modelos = new String[]{"Serie 3", "X5", "M4"};
                precios = new String[]{"$41,000,000", "$6,000,000", "$7,000,000"};
                break;
            case "porsche":
                modelos = new String[]{"911", "Cayenne", "Panamera"};
                precios = new String[]{"$12,000,000", "$80,000,000", "$9,005,000"};
                break;
            case "fiat":
                modelos = new String[]{"500", "Tipo", "Panda"};
                precios = new String[]{"$1,000,000", "$2,000,000", "$800,000"};
                break;
            case "lamborghini":
                modelos = new String[]{"Huracán", "Aventador", "Urus"};
                precios = new String[]{"$30,000,000", "$50,000,000", "$250,000,000"};
                break;
            default:
                return;
        }

        // Crear array de strings con modelo y precio
        String[] opciones = new String[modelos.length];
        for (int i = 0; i < modelos.length; i++) {
            opciones[i] = modelos[i] + " - " + precios[i];
        }

        new AlertDialog.Builder(this)
            .setTitle("Selecciona un modelo")
            .setItems(opciones, (dialog, which) -> {
                String modeloSeleccionado = modelos[which];
                String precioSeleccionado = precios[which];
                mostrarDialogoConfirmacionCompra(modeloSeleccionado, precioSeleccionado);
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void mostrarDialogoConfirmacionCompra(String modelo, String precio) {
        new AlertDialog.Builder(this)
            .setTitle("Confirmar compra")
            .setMessage("¿Desea comprar " + marcaAuto + " " + modelo + " por " + precio + "?")
            .setPositiveButton("Comprar", (dialog, which) -> {
                agregarAlInventario(marcaAuto, modelo, precio);
                Toast.makeText(this, 
                    "Comprando " + marcaAuto + " " + modelo, 
                    Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void eliminarDelInventario(String auto) {
        inventarioAutos.remove(auto);
        guardarInventario();
        actualizarVistaInventario();
    }

    private void actualizarVistaInventario() {
        LinearLayout listaArticulosLayout = findViewById(R.id.listaArticulosLayout);
        listaArticulosLayout.removeAllViews();
        
        if (inventarioAutos == null || inventarioAutos.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No hay autos en el inventario");
            emptyText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            emptyText.setPadding(16, 16, 16, 16);
            emptyText.setTextColor(getResources().getColor(android.R.color.black));
            listaArticulosLayout.addView(emptyText);
            return;
        }

        for (AutoInventario auto : inventarioAutos) {
            View itemView = getLayoutInflater().inflate(R.layout.item_inventario, null);
            
            // Configurar el TextView con la información del auto
            TextView autoTextView = itemView.findViewById(R.id.autoTextView);
            String infoAuto = auto.marca + " " + auto.modelo + "\n" +
                            "Precio: " + auto.precio + "\n" +
                            "Color: " + auto.color;
            autoTextView.setText(infoAuto);
            autoTextView.setTextColor(getResources().getColor(android.R.color.black));
            
            // Configurar los botones
            Button venderBtn = itemView.findViewById(R.id.venderItemBtn);
            Button actualizarBtn = itemView.findViewById(R.id.actualizarItemBtn);
            Button detallesBtn = itemView.findViewById(R.id.detallesItemBtn);

            venderBtn.setOnClickListener(v -> {
                mostrarDialogoConfirmacion("Vender",
                    "¿Está seguro que desea vender este " + auto.marca + " " + auto.modelo + "?",
                    (dialog, which) -> {
                        inventarioAutos.remove(auto);
                        guardarInventario();
                        actualizarVistaInventario();
                        Toast.makeText(this, "Auto vendido exitosamente", Toast.LENGTH_SHORT).show();
                    });
            });

            actualizarBtn.setOnClickListener(v -> mostrarDialogoActualizacion(auto));
            detallesBtn.setOnClickListener(v -> mostrarDialogoDetalles(auto));

            // Agregar márgenes y padding al item
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(16, 8, 16, 8);
            itemView.setLayoutParams(params);

            listaArticulosLayout.addView(itemView);
        }
    }

    private void mostrarDialogoActualizacion(AutoInventario auto) {
        String[] opciones = {"Cambiar color", "Actualizar precio", "Agregar accesorios"};
        
        new AlertDialog.Builder(this)
            .setTitle("Actualizar " + auto.marca + " " + auto.modelo)
            .setItems(opciones, (dialog, which) -> {
                switch (which) {
                    case 0: // Cambiar color
                        mostrarDialogoColor(auto);
                        break;
                    case 1: // Actualizar precio
                        mostrarDialogoPrecio(auto);
                        break;
                    case 2: // Agregar accesorios
                        mostrarDialogoAccesorios(auto);
                        break;
                }
            })
            .show();
    }

    private void mostrarDialogoColor(AutoInventario auto) {
        String[] colores = {
            "Rojo Metálico", "Azul Eléctrico", "Negro Mate", "Blanco Perlado", "Plata Brillante", 
            "Gris Grafito", "Verde Racing", "Amarillo Solar", "Naranja Lava", "Morado Real",
            "Dorado Champagne", "Azul Marino", "Verde Militar", "Rojo Vino", "Café Metálico",
            "Rosa Metalizado", "Celeste Cielo", "Bronce Antiguo", "Turquesa", "Violeta Místico"
        };

        new AlertDialog.Builder(this)
            .setTitle("Seleccionar nuevo color")
            .setItems(colores, (dialog, which) -> {
                auto.color = colores[which];
                guardarInventario();
                actualizarVistaInventario();
                Toast.makeText(this, "Color actualizado a " + colores[which], 
                    Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void mostrarDialogoPrecio(AutoInventario auto) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_precio, null);
        EditText precioEditText = dialogView.findViewById(R.id.precioEditText);

        new AlertDialog.Builder(this)
            .setTitle("Actualizar precio")
            .setView(dialogView)
            .setPositiveButton("Actualizar", (dialog, which) -> {
                String nuevoPrecio = precioEditText.getText().toString();
                if (!nuevoPrecio.isEmpty()) {
                    auto.precio = "$" + nuevoPrecio;
                    guardarInventario();
                    actualizarVistaInventario();
                    Toast.makeText(this, "Precio actualizado", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void mostrarDialogoAccesorios(AutoInventario auto) {
        String[] accesorios = {
            "Rines deportivos de aleación", 
            "Sistema de sonido premium Bose",
            "Cámara 360° con visión nocturna",
            "Sensores de estacionamiento delanteros y traseros",
            "Techo solar panorámico",
            "Asientos de cuero premium con calefacción",
            "Sistema de navegación GPS con realidad aumentada",
            "Control crucero adaptativo con Stop & Go",
            "Sistema de arranque sin llave con botón",
            "Luces LED adaptativas",
            "Sistema de escape deportivo",
            "Suspensión deportiva ajustable",
            "Head-up display",
            "Sistema de entretenimiento trasero",
            "Cargador inalámbrico para smartphone",
            "Sistema de monitoreo de punto ciego",
            "Volante deportivo multifunción",
            "Sistema de sonido ambiente",
            "Pedales deportivos de aluminio",
            "Kit aerodinámico completo"
        };

        boolean[] seleccionados = new boolean[accesorios.length];

        new AlertDialog.Builder(this)
            .setTitle("Agregar accesorios")
            .setMultiChoiceItems(accesorios, seleccionados, (dialog, which, isChecked) -> {
                seleccionados[which] = isChecked;
            })
            .setPositiveButton("Agregar", (dialog, which) -> {
                for (int i = 0; i < seleccionados.length; i++) {
                    if (seleccionados[i] && !auto.accesorios.contains(accesorios[i])) {
                        auto.accesorios.add(accesorios[i]);
                    }
                }
                guardarInventario();
                actualizarVistaInventario();
                Toast.makeText(this, auto.accesorios.size() + " accesorios en total", 
                    Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void guardarInventario() {
        SharedPreferences prefs = getSharedPreferences("Inventario", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        // Convertir inventario a JSON
        Gson gson = new Gson();
        String jsonInventario = gson.toJson(inventarioAutos);
        editor.putString("autosJson", jsonInventario);
        editor.apply();
    }

    private void cargarInventario() {
        SharedPreferences prefs = getSharedPreferences("Inventario", MODE_PRIVATE);
        String jsonInventario = prefs.getString("autosJson", "[]");
        
        // Convertir JSON a ArrayList de AutoInventario
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AutoInventario>>(){}.getType();
        inventarioAutos = gson.fromJson(jsonInventario, type);
        
        actualizarVistaInventario();
    }

    private void agregarAlInventario(String marca, String modelo, String precio) {
        AutoInventario nuevoAuto = new AutoInventario(marca, modelo, precio);
        inventarioAutos.add(nuevoAuto);
        guardarInventario();
        actualizarVistaInventario();
    }

    private void mostrarDialogoDetalles(AutoInventario auto) {
        new AlertDialog.Builder(this)
            .setTitle("Detalles del Auto")
            .setMessage("ID: " + auto.id + 
                    "\nMarca: " + auto.marca +
                    "\nModelo: " + auto.modelo +
                    "\nPrecio: " + auto.precio +
                    "\nColor: " + auto.color +
                    "\nEstado: " + auto.estado +
                    "\nFecha de compra: " + auto.fechaCompra +
                    "\n\nAccesorios: " + (auto.accesorios.isEmpty() ? "Ninguno" : 
                        String.join("\n• ", auto.accesorios)))
            .setPositiveButton("Cerrar", null)
            .show();
    }
} 