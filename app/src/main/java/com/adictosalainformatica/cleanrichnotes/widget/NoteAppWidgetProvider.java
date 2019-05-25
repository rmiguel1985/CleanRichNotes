package com.adictosalainformatica.cleanrichnotes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.adictosalainformatica.cleanrichnotes.R;
import com.adictosalainformatica.cleanrichnotes.features.list.data.DataRepository;
import com.adictosalainformatica.cleanrichnotes.features.list.data.database.Note;
import com.adictosalainformatica.cleanrichnotes.features.list.presentation.ListNotesActivity;

import java.util.List;


public class NoteAppWidgetProvider extends AppWidgetProvider {

    DataRepository dataRepository;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int appWidgetLength = appWidgetIds.length;

        dataRepository = DataRepository.getInstance(context);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<appWidgetLength; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, ListNotesActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.note_appwidget);
            views.setOnClickPendingIntent(R.id.widget_title, pendingIntent);

           List<Note> notes = dataRepository.getWidgetNotes();

           if (notes != null && notes.size() > 5) {
               views.setTextViewText(R.id.widget_title_1, notes.get(0).getTitle());
               views.setTextViewText(R.id.widget_text_1, notes.get(0).getText());

               views.setTextViewText(R.id.widget_title_2, notes.get(1).getTitle());
               views.setTextViewText(R.id.widget_text_2, notes.get(1).getText());

               views.setTextViewText(R.id.widget_title_3, notes.get(2).getTitle());
               views.setTextViewText(R.id.widget_text_3, notes.get(2).getText());

               views.setTextViewText(R.id.widget_title_4, notes.get(3).getTitle());
               views.setTextViewText(R.id.widget_text_4, notes.get(3).getText());

               views.setTextViewText(R.id.widget_title_5, notes.get(4).getTitle());
               views.setTextViewText(R.id.widget_text_5, notes.get(4).getText());

               views.setTextViewText(R.id.widget_title_6, notes.get(5).getTitle());
               views.setTextViewText(R.id.widget_text_6, notes.get(5).getText());
           }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
