package oak.shef.ac.uk.assignment;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import oak.shef.ac.uk.assignment.database.PhotoDAO;
import oak.shef.ac.uk.assignment.database.PhotoData;
import oak.shef.ac.uk.assignment.database.PhotoDatabase;

public class PhotoRepository {
    private PhotoDAO photoDAO;

    private LiveData<List<PhotoData>> allPhotos;

    public PhotoRepository(Application application) {
        PhotoDatabase database = PhotoDatabase.getDatabase(application);
        photoDAO = database.photoDao();
        allPhotos = photoDAO.getAllPhotos();
    }

    public void insert(PhotoData photoData) {
        new InsertPhotoAsyncTask(photoDAO).execute(photoData);
    }

    public void update(PhotoData photoData) {
        new UpdatePhotoAsyncTask(photoDAO).execute(photoData);
    }

    public void delete(PhotoData photoData) {
        new DeletePhotoAsyncTask(photoDAO).execute(photoData);
    }

    public void deleteAllPhotos() {
        new DeleteAllPhotoAsyncTask(photoDAO).execute();
    }

    public LiveData<List<PhotoData>> getAllPhotos() {
        return allPhotos;
    }

    private static class InsertPhotoAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private PhotoDAO photoDAO;

        private InsertPhotoAsyncTask(PhotoDAO photoDAO) {
            this.photoDAO = photoDAO;
        }

        @Override
        protected  Void doInBackground(PhotoData... photoDatas) {
            photoDAO.insert(photoDatas[0]);
            return null;
        }
    }
    private static class UpdatePhotoAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private PhotoDAO photoDAO;

        private UpdatePhotoAsyncTask(PhotoDAO photoDAO) {
            this.photoDAO = photoDAO;
        }

        @Override
        protected  Void doInBackground(PhotoData... photoDatas) {
            photoDAO.update(photoDatas[0]);
            return null;
        }
    }
    private static class DeletePhotoAsyncTask extends AsyncTask<PhotoData, Void, Void> {
        private PhotoDAO photoDAO;

        private DeletePhotoAsyncTask(PhotoDAO photoDAO) {
            this.photoDAO = photoDAO;
        }

        @Override
        protected  Void doInBackground(PhotoData... photoDatas) {
            photoDAO.delete(photoDatas[0]);
            return null;
        }
    }
    private static class DeleteAllPhotoAsyncTask extends AsyncTask<Void, Void, Void> {
        private PhotoDAO photoDAO;

        private DeleteAllPhotoAsyncTask(PhotoDAO photoDAO) {
            this.photoDAO = photoDAO;
        }

        @Override
        protected  Void doInBackground(Void... voids) {
            photoDAO.deleteAllPhotos();
            return null;
        }
    }
}
