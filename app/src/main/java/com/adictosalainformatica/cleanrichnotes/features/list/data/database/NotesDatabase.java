package com.adictosalainformatica.cleanrichnotes.features.list.data.database;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.adictosalainformatica.cleanrichnotes.BuildConfig;
import com.adictosalainformatica.cleanrichnotes.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 5, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract NotesDao notesDao();

    private static volatile NotesDatabase sInstance = null;

    /**
     * Returns an instance of Room Database.
     *
     * @param context application context
     * @return The singleton NotesDatabase
     */
    @NonNull
    public static synchronized NotesDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (NotesDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            NotesDatabase.class, DataNotesName.TABLE_NAME + "_database")
                            // Wipes and rebuilds instead of migrating
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    if (BuildConfig.DEBUG) {
                                       Executors.newSingleThreadScheduledExecutor()
                                                .execute(() -> fillWithDemoData(context));
                                    }
                                }
                            })
                            .build();
                }
            }
        }
        return sInstance;
    }

    @WorkerThread
    private static void fillWithDemoData(Context context) {
        NotesDao dao = getInstance(context).notesDao();
        JSONArray emoji = loadJsonArray(context);
        try {
            for (int i = 0; i < emoji.length(); i++) {
                JSONObject item = emoji.getJSONObject(i);
                boolean isPinned = false;

                String title = item.getString("title");

                if (title.equals("normal")  || title.equals("game")) {
                    isPinned = true;
                }

                dao.insert(new Note(item.getInt("id"),
                        item.getString("text"),
                        title,
                        isPinned));
            }
        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    private static JSONArray loadJsonArray(Context context) {
        StringBuilder builder = new StringBuilder();
        InputStream in = context.getResources().openRawResource(R.raw.notes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject json = new JSONObject(builder.toString());
            return json.getJSONArray("notes");

        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
