package benicio.soluces.appdegesto.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import benicio.soluces.appdegesto.R;
import benicio.soluces.appdegesto.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.entrarBtn.setOnClickListener( view->{
            startActivity(new Intent(getApplicationContext(), ContabilidadeActivity.class));
        });
    }


}