package com.example.geovangoes.materialstudy.ui.recycler.helper.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.geovangoes.materialstudy.dao.NotaDAO;
import com.example.geovangoes.materialstudy.ui.recycler.adapter.ListaNotasAdapter;

/**
 * Created by geovangoes
 */
public class NotaItemTouchHelperCallback extends ItemTouchHelper.Callback
{

    /***
     *
     */
    private final ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallback(ListaNotasAdapter adapter)
    {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        int marcacoesDeDeslize = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
    {
        int posicaoInicial = viewHolder.getAdapterPosition();
        int posicaoFinal = target.getAdapterPosition();
        trocaNotas(posicaoInicial, posicaoFinal);
        return true;
    }

    /***
     *
     * @param posicaoInicial
     * @param posicaoFinal
     */
    private void trocaNotas(int posicaoInicial, int posicaoFinal)
    {
        new NotaDAO().troca(posicaoInicial, posicaoFinal);
        adapter.troca(posicaoInicial, posicaoFinal);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
    {
        int posicao = viewHolder.getAdapterPosition();
        removeNota(posicao);
    }

    /***
     *
     * @param posicao
     */
    private void removeNota(int posicao)
    {
        new NotaDAO().remove(posicao);
        adapter.remove(posicao);
    }
}
