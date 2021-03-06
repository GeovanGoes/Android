package br.com.alura.agenda.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import br.com.alura.agenda.R;
import br.com.alura.agenda.dao.AlunoDAO;
import br.com.alura.agenda.helper.FormularioHelper;
import br.com.alura.agenda.modelo.Aluno;
import br.com.alura.agenda.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 567;
    private FormularioHelper formularioHelper;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        formularioHelper =  new FormularioHelper(this);
        final Intent intent = getIntent();
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");

        if (aluno != null){
            formularioHelper.preencheFormulario(aluno);
        }

        Button botaoFotoFormulario = (Button) findViewById(R.id.formulario_botao_foto);
        botaoFotoFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                File arquivoFoto = new File(caminhoFoto);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                startActivityForResult(camera, CODIGO_CAMERA);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODIGO_CAMERA && resultCode == RESULT_OK){
            formularioHelper.carregaImagem(caminhoFoto);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_formulario_ok:

                Aluno aluno = this.formularioHelper.getAluno();
                aluno.desincroninza();
                AlunoDAO dao = new AlunoDAO(this);

                if (aluno.getId() != null){
                    dao.atualizar(aluno);
                    Toast.makeText(this, "Atualizado com sucessso!", Toast.LENGTH_SHORT).show();
                } else {
                    dao.insereAluno(aluno);
                    Toast.makeText(this, "Inserido com sucesso!", Toast.LENGTH_SHORT).show();

                }
                Call insere = new RetrofitInicializador().getAlunoService().insere(aluno);
                insere.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Log.i("response", "sucesso");
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.e("failure", "deu merda");
                    }
                });
                dao.close();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
