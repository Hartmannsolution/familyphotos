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
    @ManyToMany
    @JoinTable( // This is now the owner side of the relationsship
            name = "photo_tag",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id"))
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
}