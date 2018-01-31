package com.laisvidoto.desafioconcrete.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.laisvidoto.desafioconcrete.R;
import com.laisvidoto.desafioconcrete.model.Repositorio;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Laís Vidoto on 30/01/2018.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder>
{
    private List<Repositorio> repositories;
    private Context context;
    private RepositoryListener repositoryListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txtNomeRepositorio;
        public TextView txtDescRepositorio;
        public TextView txtForksCount;
        public TextView txtStarsCount;
        public TextView txtNomeAutor;
        public ImageView imagemAutor;

        RepositoryListener repositoryListener;

        public ViewHolder(View itemView, RepositoryListener repositoryListener)
        {
            super(itemView);

            txtNomeRepositorio  = itemView.findViewById(R.id.txtNomeRepositorio);
            txtDescRepositorio  = itemView.findViewById(R.id.txtDescRepositorio);
            txtForksCount       = itemView.findViewById(R.id.txtForksCount);
            txtStarsCount       = itemView.findViewById(R.id.txtStarsCount);
            txtNomeAutor        = itemView.findViewById(R.id.txtNomeAutor);
            imagemAutor         = itemView.findViewById(R.id.imagemAutor);

            this.repositoryListener = repositoryListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            Repositorio repositorio = getRepository( getAdapterPosition() );
            this.repositoryListener.onRepositoryClick( repositorio.getOwner().getUsername(), repositorio.getName() );

            notifyDataSetChanged();
        }
    }

    public RepoAdapter(Context context, List<Repositorio> repositories,RepositoryListener repositoryListener)
    {
        this.repositories = repositories;
        this.context = context;
        this.repositoryListener = repositoryListener;
    }

    @Override
    public RepoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View repositoryView = inflater.inflate(R.layout.fragment_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(repositoryView, this.repositoryListener);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(RepoAdapter.ViewHolder holder, int position)
    {
        final Repositorio repository = repositories.get(position);
        TextView txtNomeRepositorio  = holder.txtNomeRepositorio;
        TextView txtDescRepositorio  = holder.txtDescRepositorio;
        TextView txtForksCount = holder.txtForksCount;
        TextView txtStarsCount = holder.txtStarsCount;
        TextView txtNomeAutor  = holder.txtNomeAutor;
        ImageView imagemAutor  = holder.imagemAutor;

        txtNomeRepositorio.setText(repository.getName());
        txtDescRepositorio.setText(repository.getDescription());
        txtForksCount.setText(String.valueOf(repository.getNumForks()));
        txtStarsCount.setText(String.valueOf(repository.getStarsCount()));
        txtNomeAutor.setText(repository.getOwner().getUsername());

        Picasso.with(context)
                .load(repository.getOwner()
                        .getPhoto())
                .resize(160, 160)
                .into(imagemAutor);
    }

    @Override
    public int getItemCount() {
        return repositories == null ? 0 : repositories.size();
    }

    public void updateRepositories(List<Repositorio> repositories)
    {
        this.repositories = repositories;
        notifyDataSetChanged();
    }

    public Repositorio getRepository(int adapterPosition) { return repositories.get(adapterPosition); }

    public interface RepositoryListener { void onRepositoryClick(String authorName, String repoName); }

    public void add(Repositorio repo)
    {
        repositories.add(repo);
        notifyItemInserted(repositories.size() - 1);
    }

    public void addAll(List<Repositorio> repositories)
    {
        for (Repositorio repo : repositories)
        {
            add(repo);
        }
    }

    public List<Repositorio> getRepositories()
    {
        return repositories;
    }
}