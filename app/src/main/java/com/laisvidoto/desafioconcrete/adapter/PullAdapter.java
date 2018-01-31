package com.laisvidoto.desafioconcrete.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.laisvidoto.desafioconcrete.R;
import com.laisvidoto.desafioconcrete.interfaces.Services;
import com.laisvidoto.desafioconcrete.model.PullRequest;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Laís Vidoto on 30/01/2018.
 */

public class PullAdapter extends RecyclerView.Adapter<PullAdapter.ViewHolder>
{
    private List<PullRequest> listaRequests;
    private Context context;
    private PullRequestListener pullRequestListener;
    private Services gitServices;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txtPullTitle;
        public TextView txtPullBody;
        public TextView txtData;
        public TextView txtNomeAutor;
        public ImageView imagemAutor;
        PullRequestListener pullListener;

        public ViewHolder(View itemView, PullRequestListener pullRequestListener)
        {
            super(itemView);

            txtPullTitle   = itemView.findViewById(R.id.txtPullTitle);
            txtPullBody    = itemView.findViewById(R.id.txtPullBody);
            txtData        = itemView.findViewById(R.id.txtData);
            txtNomeAutor   = itemView.findViewById(R.id.txtPullNomeAutor);
            imagemAutor    = itemView.findViewById(R.id.pullImagemAutor);

            this.pullListener = pullRequestListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            PullRequest pullRequest = getPullRequest(getAdapterPosition());
            this.pullListener.onPullRequestClick(pullRequest.getPullRequestUrl());
            notifyDataSetChanged();
        }

    }

    public PullAdapter(Context context, List<PullRequest> pullRequests,PullRequestListener pullRequestListener)
    {
        this.context = context;
        this.listaRequests = pullRequests;
        this.pullRequestListener = pullRequestListener;
    }

    @Override
    public PullAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View pullRequestView = inflater.inflate(R.layout.fragment_lista, parent, false);

        ViewHolder viewHolder = new ViewHolder(pullRequestView, this.pullRequestListener);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        PullRequest pullRequest = listaRequests.get(position);
        TextView txtPullTitle   = holder.txtPullTitle;
        TextView txtPullBody    = holder.txtPullBody;
        TextView txtData        = holder.txtData;
        TextView txtNomeAutor   = holder.txtNomeAutor;
        ImageView imagemAutor   = holder.imagemAutor;

        txtPullTitle.setText(pullRequest.getTitle());
        txtPullBody.setText(pullRequest.getBody());
        txtNomeAutor.setText(pullRequest.getUser().getUsername());
        txtData.setText(formataData(pullRequest.getDate()));

        Picasso.with(context)
                .load(pullRequest.getUser().getPhoto())
                .resize(160, 160)
                .into(imagemAutor);
    }

    @Override
    public int getItemCount()
    {
        return listaRequests.size();
    }

    public void updatePullRequest(List<PullRequest> pullRequests)
    {
        this.listaRequests = pullRequests;
        notifyDataSetChanged();
    }

    public PullRequest getPullRequest(int adapterPosition)
    {
        return listaRequests.get(adapterPosition);
    }

    public interface PullRequestListener
    {
        void onPullRequestClick(String url);
    }

    public String formataData(String data)
    {
        String dataLista = data;
        String dia = dataLista.substring(8,10);
        String mes = dataLista.substring(5,7);
        String ano = dataLista.substring(0,4);

        String date = dia +"/"+ mes +"/"+ ano;

        return date;
    }
}