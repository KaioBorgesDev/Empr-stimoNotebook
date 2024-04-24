package com.example.apicadaemprestimo;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.apicadaemprestimo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura o ArrayAdapter para o Spinner com as opções de localização
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.localizacao_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.localizacaoSpinner.setAdapter(adapter);

        // Configura o listener do botão enviar para chamar enviarDadosParaAPI quando clicado
        binding.enviarButton.setOnClickListener(v -> enviarDadosParaAPI());
    }

    private void enviarDadosParaAPI() {
        // Lembre-se de substituir esta URL pela URL real do seu servidor
        String url = "http://10.0.2.2:3001/registrar";

        JSONObject postData = new JSONObject();
        try {
            postData.put("ni", binding.niEditText.getText().toString());
            postData.put("numeroDoNote", binding.numeroDoNoteEditText.getText().toString());
            postData.put("localizacao", binding.localizacaoSpinner.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Erro ao criar o JSON de envio.", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    // Aqui você pode adicionar lógica baseada na resposta específica do seu servidor
                    Toast.makeText(MainActivity.this, "Dados enviados com sucesso!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Log de erro
                    if (error.networkResponse != null) {
                        Log.e("ErroEnvioDados", "Status Code: " + error.networkResponse.statusCode);
                        Log.e("ErroEnvioDados", "Erro ao enviar dados: " + new String(error.networkResponse.data));
                        Toast.makeText(MainActivity.this, "Erro ao enviar dados. Código: " + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("ErroEnvioDados", "Erro ao enviar dados: " + error.toString());
                        Toast.makeText(MainActivity.this, "Erro de conexão ao enviar dados.", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

}
