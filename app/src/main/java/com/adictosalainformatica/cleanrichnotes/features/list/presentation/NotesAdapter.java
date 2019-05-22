package com.adictosalainformatica.cleanrichnotes.features.list.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adictosalainformatica.cleanrichnotes.R;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.Note;

public class NotesAdapter extends PagedListAdapter<Note, NotesAdapter.NotesViewHolder> {

    NotesAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note item = getItem(position);
        holder.bindTo(item);
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView text;
        private ImageView isPinned;
        private Note note;

        NotesViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.noteTitle);
            text = itemView.findViewById(R.id.noteText);
            isPinned = itemView.findViewById(R.id.noteIsPinned);
        }

        public Note getNote() {
            return note;
        }

        void bindTo(Note note) {
            this.note = note;
            this.title.setText(note.getTitle());
            this.text.setText(note.getText());

            if(note.isPinned()) {
                isPinned.setVisibility(View.VISIBLE);
            } else {
                isPinned.setVisibility(View.GONE);
            }
        }
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldItem,
                                                  @NonNull Note newItem) {

                    return oldItem == newItem;
                }
            };
}