package br.comm.a4kontrol.ponto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.comm.a4kontrol.ponto.helper.LogHelper;
import br.comm.a4kontrol.ponto.modelo.Feriado;

/**
 * Created by geovan.goes on 09/05/2017.
 */
public class FeriadoDao extends DAO<Feriado> {

    public FeriadoDao(Context context) {
        super(context,"Feriados");
    }

    @Override
    public ContentValues getContentValues(Feriado feriado) {
        ContentValues dados = new ContentValues();
        dados.put("data", feriado.getData());
        return dados;
    }

    @Override
    public String prepareConsultQuery(String data) {
        String sql = "SELECT * FROM " + getTableName() + " where data = '"+data+"';";

        if (data == null)
            sql = "SELECT * FROM " + getTableName() + ";";

        return sql;
    }

    @Override
    public List<Feriado> convertCursorToObject(Cursor cursor) {
        List<Feriado> feriados = new ArrayList<Feriado>();

        while (cursor.moveToNext()){

            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String date = cursor.getString(cursor.getColumnIndex("data"));

            Feriado feriado = new Feriado(id, date);

            feriados.add(feriado);
        }

        return feriados;
    }

    @Override
    public String getParamsName() {
        return "data = ?";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createTabelFeriados());
        }catch (Exception e){
            LogHelper.error(this, e, null);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS "+ getTableName();
        db.execSQL(sql);
        onCreate(db);
    }

    private String createTabelFeriados(){
        String createTableFeriados = "CREATE TABLE " +
                getTableName() +
                " (" +
                "id INTEGER PRIMARY KEY," +
                "data TEXT NOT NULL);";
        return createTableFeriados;
    }
}