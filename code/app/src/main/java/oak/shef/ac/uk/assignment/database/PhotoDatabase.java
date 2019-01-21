package oak.shef.ac.uk.assignment.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;

@android.arch.persistence.room.Database(entities = {PhotoData.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PhotoDatabase extends RoomDatabase {
    public abstract PhotoDAO photoDao();

    private static volatile PhotoDatabase INSTANCE;

    /**
     * Only one instance is allowed at one time
     * @param context
     * @return
     */
    public static PhotoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PhotoDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = android.arch.persistence.room.Room.databaseBuilder(context.getApplicationContext(),
                            PhotoDatabase.class, "photo_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Populate database with existing files in the device's PhotoApp folder.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // do any init operation about any initialisation here
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PhotoDAO photoDAO;

        private PopulateDbAsyncTask(PhotoDatabase db) {
            photoDAO = db.photoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File[] files_array;
            files_array = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/PhotoApp").listFiles();
            if (files_array!=null) {
                for (File f : files_array) {
                    PhotoData pD=new PhotoData(f.getAbsolutePath());
                    pD.setTitle(f.getName());
                    Log.i("PhotoDatabase", pD.toString());
                    photoDAO.insert(pD);
                }
            }
            return null;
        }
    }
}
