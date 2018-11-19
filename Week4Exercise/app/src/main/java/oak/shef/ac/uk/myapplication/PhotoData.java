package oak.shef.ac.uk.myapplication.model;

import android.arch.persistence.room.*;
import android.graphics.Bitmap;

@Entity(indices={@Index(value={"title"})})
public class PhotoData {
    @PrimaryKey(autoGenerate = true)
    @android.support.annotation.NonNull
    private int id=0;
    private String title;
    private String description;
    @Ignore
    public Bitmap picture;
    public PhotoData(String title, String description) {
        this.title= title;
        this.description= description;
    }

    @android.support.annotation.NonNull
    public int getId() { return id;}
    public void setId(@android.support.annotation.NonNull int id) {
        this.id = id;
    }

    // plus all the other getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

}
