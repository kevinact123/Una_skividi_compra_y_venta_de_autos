package com.unaskividicomprayventadeautos;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetalleAutoActivity extends AppCompatActivity {
    private String marcaAuto;
    private LinearLayout listaArticulosLayout;

    // Clase para manejar los detalles del auto
    public static class AutoInventario {
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
        
        // Verificar si se abrió para ver el inventario
        boolean mostrarInventario = getIntent().getBooleanExtra("mostrar_inventario", false);
        
        // Cargar inventario primero
        cargarInventario();

        if (mostrarInventario) {
            configurarVistaInventario();
        } else {
            configurarVistaDetalleAuto();
        }
    }

    private void configurarVistaInventario() {
        // Ocultar elementos de detalle de auto
        findViewById(R.id.scrollView2).setVisibility(View.GONE);
        findViewById(R.id.botonesSuperiores).setVisibility(View.GONE);
        findViewById(R.id.botonesInferiores).setVisibility(View.GONE);
        
        // Mostrar título del inventario
        TextView tituloTextView = findViewById(R.id.tituloMarcaAuto);
        tituloTextView.setText("MI INVENTARIO");
        tituloTextView.setVisibility(View.VISIBLE);
        
        // Configurar ScrollView del inventario
        ScrollView scrollViewComprados = findViewById(R.id.scrollViewComprados);
        scrollViewComprados.setVisibility(View.VISIBLE);
        
        // Crear y configurar el LinearLayout para el inventario
        LinearLayout contenedorInventario = new LinearLayout(this);
        contenedorInventario.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT));
        contenedorInventario.setOrientation(LinearLayout.VERTICAL);
        contenedorInventario.setPadding(16, 16, 16, 16);
        
        // Limpiar el ScrollView y agregar el nuevo contenedor
        scrollViewComprados.removeAllViews();
        scrollViewComprados.addView(contenedorInventario);
        
        // Mostrar los autos en el inventario
        if (inventarioAutos == null || inventarioAutos.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("No hay autos en el inventario");
            emptyView.setTextSize(18);
            emptyView.setGravity(Gravity.CENTER);
            emptyView.setPadding(16, 32, 16, 32);
            contenedorInventario.addView(emptyView);
        } else {
            for (AutoInventario auto : inventarioAutos) {
                View itemView = getLayoutInflater().inflate(R.layout.item_inventario, null);
                TextView autoTextView = itemView.findViewById(R.id.autoTextView);
                autoTextView.setText(String.format("%s %s\nPrecio: %s\nColor: %s\nEstado: %s\nFecha: %s",
                    auto.marca,
                    auto.modelo,
                    auto.precio,
                    auto.color,
                    auto.estado,
                    auto.fechaCompra));

                // Configurar botones
                configurarBotonesItem(itemView, auto);
                
                // Agregar el item al contenedor
                contenedorInventario.addView(itemView);
            }
        }
    }

    private void configurarVistaDetalleAuto() {
        // Ocultar elementos del inventario
        findViewById(R.id.scrollViewComprados).setVisibility(View.GONE);
        
        // Mostrar elementos de detalle de auto
        findViewById(R.id.scrollView2).setVisibility(View.VISIBLE);
        findViewById(R.id.botonesSuperiores).setVisibility(View.VISIBLE);
        findViewById(R.id.botonesInferiores).setVisibility(View.VISIBLE);
        
        // Configurar título
        marcaAuto = getIntent().getStringExtra("marca_auto");
        TextView tituloTextView = findViewById(R.id.tituloMarcaAuto);
        tituloTextView.setText(marcaAuto);
        
        // Configurar botones de acciones
        configurarBotones();
    }

    private void actualizarVistaInventario() {
        if (listaArticulosLayout == null) return;
        
        listaArticulosLayout.removeAllViews();
        
        if (inventarioAutos == null || inventarioAutos.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("No hay autos en el inventario");
            emptyView.setTextSize(18);
            emptyView.setGravity(android.view.Gravity.CENTER);
            emptyView.setPadding(16, 32, 16, 32);
            listaArticulosLayout.addView(emptyView);
            return;
        }

        for (AutoInventario auto : inventarioAutos) {
            View itemView = getLayoutInflater().inflate(R.layout.item_inventario, null);
            TextView autoTextView = itemView.findViewById(R.id.autoTextView);
            autoTextView.setText(auto.toString());

            // Configurar botones
            configurarBotonesItem(itemView, auto);

            listaArticulosLayout.addView(itemView);
        }
    }

    private void configurarBotonesItem(View itemView, AutoInventario auto) {
        Button personalizarBtn = itemView.findViewById(R.id.personalizarItemBtn);
        Button venderBtn = itemView.findViewById(R.id.venderItemBtn);
        Button actualizarBtn = itemView.findViewById(R.id.actualizarItemBtn);
        Button detallesBtn = itemView.findViewById(R.id.detallesItemBtn);

        personalizarBtn.setOnClickListener(v -> mostrarDialogoPersonalizacion(auto));
        venderBtn.setOnClickListener(v -> venderAuto(auto));
        actualizarBtn.setOnClickListener(v -> mostrarDialogoPrecio(auto));
        detallesBtn.setOnClickListener(v -> mostrarDialogoDetalles(auto));
    }

    private void mostrarDetallesAuto(String marca) {
        LinearLayout contenedor = findViewById(R.id.listaArticulosLayout);
        contenedor.removeAllViews();

        // Crear y agregar TextViews para los detalles
        String[] detalles = obtenerDetallesAuto(marca);
        for (String detalle : detalles) {
            TextView textView = new TextView(this);
            textView.setText(detalle);
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(android.R.color.black));
            textView.setPadding(16, 16, 16, 16);
            textView.setBackgroundResource(R.drawable.inventario_item_background);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            textView.setLayoutParams(params);
            contenedor.addView(textView);
        }
    }

    private String[] obtenerDetallesAuto(String marca) {
        switch (marca.toLowerCase()) {
            case "chevrolet":
                return new String[]{
                    "Modelos disponibles:\n- Camaro\n- Cruze\n- Silverado",
                    "Rango de precios:\n$25,000,000 - $3,000,000",
                    "Características:\n- Motor V8\n- Transmisión automática\n- Sistema de navegación"
                };
            // ... resto de los casos ...
            default:
                return new String[]{"No hay información disponible"};
        }
    }

    private void mostrarDialogoConfirmacion(String titulo, String mensaje, 
            DialogInterface.OnClickListener onConfirm) {
        new AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("Sí", onConfirm)
            .setNegativeButton("No", null)
            .show();
    }

    private void mostrarDialogoSeleccionModelo() {
        String[] modelos = obtenerModelosPorMarca(marcaAuto);
        String[] precios = obtenerPreciosPorModelo(marcaAuto);
        
        new AlertDialog.Builder(this)
            .setTitle("Seleccionar Modelo")
            .setItems(modelos, (dialog, which) -> {
                agregarAlInventario(
                    marcaAuto,
                    modelos[which],
                    precios[which]
                );
                Toast.makeText(this, 
                    "¡" + marcaAuto + " " + modelos[which] + " agregado al inventario!", 
                    Toast.LENGTH_SHORT).show();
                finish();
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

    private void mostrarDialogoPersonalizacion(AutoInventario auto) {
        String[] opciones = {"Cambiar color", "Agregar accesorios"};
        
        new AlertDialog.Builder(this)
            .setTitle("Personalizar Auto")
            .setItems(opciones, (dialog, which) -> {
                if (which == 0) {
                    mostrarDialogoColor(auto);
                } else {
                    mostrarDialogoAccesorios(auto);
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void mostrarDialogoColor(AutoInventario auto) {
        String[] colores = {"Rojo", "Azul", "Negro", "Blanco", "Plata", "Verde"};
        
        new AlertDialog.Builder(this)
            .setTitle("Seleccionar Color")
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
        Gson gson = new Gson();
        String jsonInventario = gson.toJson(inventarioAutos);
        prefs.edit().putString("autosJson", jsonInventario).apply();
    }

    private void cargarInventario() {
        SharedPreferences prefs = getSharedPreferences("Inventario", MODE_PRIVATE);
        String jsonInventario = prefs.getString("autosJson", "[]");
        
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AutoInventario>>(){}.getType();
            inventarioAutos = gson.fromJson(jsonInventario, type);
            
            if (inventarioAutos == null) {
                inventarioAutos = new ArrayList<>();
            }
            
            // Debug: Imprimir el contenido del inventario
            Log.d("DetalleAutoActivity", "Inventario cargado: " + inventarioAutos.size() + " autos");
            for (AutoInventario auto : inventarioAutos) {
                Log.d("DetalleAutoActivity", "Auto: " + auto.marca + " " + auto.modelo);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            inventarioAutos = new ArrayList<>();
            Toast.makeText(this, "Error al cargar el inventario", Toast.LENGTH_SHORT).show();
        }
    }

    private void agregarAlInventario(String marca, String modelo, String precio) {
        // Crear nuevo auto
        AutoInventario nuevoAuto = new AutoInventario(marca, modelo, precio);
        nuevoAuto.color = "Negro"; // Color por defecto
        nuevoAuto.estado = "Nuevo";
        nuevoAuto.fechaCompra = new java.text.SimpleDateFormat("dd/MM/yyyy")
            .format(new java.util.Date());
        nuevoAuto.accesorios = new ArrayList<>();
        nuevoAuto.id = String.valueOf(System.currentTimeMillis());

        // Cargar inventario existente
        SharedPreferences prefs = getSharedPreferences("Inventario", MODE_PRIVATE);
        String jsonInventario = prefs.getString("autosJson", "[]");
        
        try {
            // Convertir JSON a ArrayList
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AutoInventario>>(){}.getType();
            inventarioAutos = gson.fromJson(jsonInventario, type);
            
            if (inventarioAutos == null) {
                inventarioAutos = new ArrayList<>();
            }
            
            // Agregar nuevo auto
            inventarioAutos.add(nuevoAuto);
            
            // Guardar inventario actualizado
            String jsonActualizado = gson.toJson(inventarioAutos);
            prefs.edit().putString("autosJson", jsonActualizado).apply();
            
            // Actualizar vista
            actualizarVistaInventario();
            
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar en el inventario", Toast.LENGTH_SHORT).show();
        }
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

    private void venderAuto(AutoInventario auto) {
        new AlertDialog.Builder(this)
            .setTitle("Vender Auto")
            .setMessage("¿Está seguro que desea vender este " + auto.marca + " " + auto.modelo + "?\n" +
                       "Precio actual: " + auto.precio)
            .setPositiveButton("Vender", (dialog, which) -> {
                inventarioAutos.remove(auto);
                guardarInventario();
                actualizarVistaInventario();
                Toast.makeText(this, 
                    "¡" + auto.marca + " " + auto.modelo + " vendido exitosamente!", 
                    Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private String[] obtenerModelosPorMarca(String marca) {
        switch (marca.toLowerCase()) {
            case "chevrolet":
                return new String[]{"Camaro", "Cruze", "Silverado"};
            case "ford":
                return new String[]{"Mustang", "F-150", "Explorer"};
            case "honda":
                return new String[]{"Civic", "Accord", "CR-V"};
            case "mazda":
                return new String[]{"Mazda3", "CX-5", "MX-5"};
            case "toyota":
                return new String[]{"Corolla", "Camry", "RAV4"};
            case "nissan":
                return new String[]{"GT-R", "370Z", "Altima"};
            case "bmw":
                return new String[]{"Serie 3", "X5", "M4"};
            case "porsche":
                return new String[]{"911", "Cayenne", "Panamera"};
            case "fiat":
                return new String[]{"500", "Tipo", "Panda"};
            case "lamborghini":
                return new String[]{"Huracán", "Aventador", "Urus"};
            default:
                return new String[]{"Modelo no disponible"};
        }
    }

    private String[] obtenerPreciosPorModelo(String marca) {
        switch (marca.toLowerCase()) {
            case "chevrolet":
                return new String[]{"$25,000,000", "$20,000,000", "$30,000,000"};
            case "ford":
                return new String[]{"$35,000,000", "$45,000,000", "$40,000,000"};
            case "honda":
                return new String[]{"$18,000,000", "$22,000,000", "$25,000,000"};
            case "mazda":
                return new String[]{"$20,000,000", "$28,000,000", "$32,000,000"};
            case "toyota":
                return new String[]{"$19,000,000", "$24,000,000", "$27,000,000"};
            case "nissan":
                return new String[]{"$85,000,000", "$45,000,000", "$35,000,000"};
            case "bmw":
                return new String[]{"$45,000,000", "$55,000,000", "$65,000,000"};
            case "porsche":
                return new String[]{"$120,000,000", "$95,000,000", "$110,000,000"};
            case "fiat":
                return new String[]{"$15,000,000", "$18,000,000", "$16,000,000"};
            case "lamborghini":
                return new String[]{"$350,000,000", "$450,000,000", "$380,000,000"};
            default:
                return new String[]{"Precio no disponible"};
        }
    }

    private void configurarBotones() {
        Button comprarButton = findViewById(R.id.Comprar);
        Button venderButton = findViewById(R.id.Vender);
        Button eliminarButton = findViewById(R.id.Eliminar);
        Button actualizarButton = findViewById(R.id.Actualizar);

        comprarButton.setOnClickListener(v -> mostrarDialogoSeleccionModelo());
        
        venderButton.setOnClickListener(v -> mostrarDialogoConfirmacion("Vender",
            "¿Está seguro que desea vender este " + marcaAuto + "?",
            (dialog, which) -> {
                Toast.makeText(this, "Vendiendo " + marcaAuto, Toast.LENGTH_SHORT).show();
            }));
            
        eliminarButton.setOnClickListener(v -> mostrarDialogoConfirmacion("Eliminar",
            "¿Está seguro que desea eliminar este " + marcaAuto + "?",
            (dialog, which) -> {
                Toast.makeText(this, "Eliminando " + marcaAuto, Toast.LENGTH_SHORT).show();
                finish();
            }));
            
        actualizarButton.setOnClickListener(v -> mostrarDialogoConfirmacion("Actualizar",
            "¿Está seguro que desea actualizar este " + marcaAuto + "?",
            (dialog, which) -> {
                Toast.makeText(this, "Actualizando " + marcaAuto, Toast.LENGTH_SHORT).show();
            }));
    }
} 