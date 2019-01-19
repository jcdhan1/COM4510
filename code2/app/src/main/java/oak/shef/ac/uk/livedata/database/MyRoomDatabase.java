package oak.shef.ac.uk.livedata.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

@android.arch.persistence.room.Database(entities = {NumberData.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends RoomDatabase {
	public abstract MyDAO myDao();
	// marking the instance as volatile to ensure atomic access to the variable
	private static volatile MyRoomDatabase INSTANCE;
	public static MyRoomDatabase getDatabase(final Context context) {
		if (INSTANCE == null) {
			synchronized (MyRoomDatabase.class) {
				if (INSTANCE == null) {
					INSTANCE =
							android.arch.persistence.room.Room.databaseBuilder(context.getApplicationContext(),
									MyRoomDatabase.class, "number_database")
									// Wipes and rebuilds instead of migrating if no Migration object.
									// Migration is not part of this codelab.
									.fallbackToDestructiveMigration()
									.addCallback(sRoomDatabaseCallback)
									.build();
				}}}
		return INSTANCE;}

	private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
		@Override
		public void onOpen(@NonNull SupportSQLiteDatabase db) {
			super.onOpen(db);
			// do any init operation about any initialisation here
		}
	};
}
