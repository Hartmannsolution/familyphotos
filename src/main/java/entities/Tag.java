package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "tag")
@Entity
@NamedQuery(name = "Tag.deleteAllRows", query = "DELETE from Tag")
public class Tag {
    @Id
    @Column(name = "tagname", length = 35)
    private String name;

    @ManyToMany(mappedBy = "tags") //, cascade = CascadeType.PERSIST) //, fetch = FetchType.EAGER) //Target side of relationsship (inverse side)
    private List<Photo> photos = new ArrayList();

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }
}