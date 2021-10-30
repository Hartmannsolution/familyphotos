package entities;

import javax.persistence.*;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "photo")
@Entity
@NamedQuery(name = "Photo.deleteAllRows", query = "DELETE from Photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FotoId", nullable = false)
    private Integer id;

    @Column(name = "FilNavn", nullable = false, length = 35)
    private String fileName;

    @Column(name = "Location", nullable = false, length = 35)
    private String location;


    @Column(name = "VisNr")
    private int viewNo;

    @Lob
    @Column(name = "FotoTxt")
    private String photoTxt;

    @Column(name = "FotoTxtAdd")
    private String photoTxtAdd;


//    @CreationTimestamp //For Hibernate only
//    @Temporal(TemporalType.TIMESTAMP) //NOt necessary to annotate with @Temporal when using java 8 time.LocalDateTime (translates to TIMESTAMP on mysql
    @Column(name = "Oprettet")
//    @CreationTimestamp //this adds the default timestamp on save BUT only for hibernate
//    @UpdateTimestamp //this update the timestamp everytime the entity is changed
    private java.time.LocalDateTime created;

//    @UpdateTimestamp //For Hibernate
//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Rettet")
    private LocalDateTime editted;

    @ManyToMany(mappedBy = "photos") //, cascade = CascadeType.PERSIST) //, fetch = FetchType.EAGER) //Target side of relationsship (inverse side)
    private List<Tag> tags = new ArrayList<>();

    public Photo() {
    }

    public Photo(String location, String fileName, String photoTxt) {
        this.location = location;
        this.fileName = fileName;
        this.photoTxt = photoTxt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Database is not set to handle dates, so I do it with JPA lifecycle methods. For more see: https://www.baeldung.com/jpa-entity-lifecycle-events
    @PreUpdate
    public void onUpdate() {
        editted = LocalDateTime.now();
    }

    @PrePersist
    public void onPersist(){
        editted = LocalDateTime.now();
        created = LocalDateTime.now();
    }

    public LocalDateTime getEditted() {return editted;}

    public LocalDateTime getCreated() {
        return created;
    }

    public String getPhotoTxtAdd() {
        return photoTxtAdd;
    }

    public void setPhotoTxtAdd(String photoTxtAdd) {
        this.photoTxtAdd = photoTxtAdd;
    }

    public String getPhotoTxt() {
        return photoTxt;
    }

    public void setPhotoTxt(String photoTxt) {
        this.photoTxt = photoTxt;
    }

    public int getViewNo() {
        return viewNo;
    }

    public void setViewNo(int viewNo) {
        this.viewNo = viewNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
        if(!tag.getPhotos().contains(this))
            tag.getPhotos().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return Objects.equals(id, photo.id) && Objects.equals(fileName, photo.fileName) && Objects.equals(photoTxt, photo.photoTxt);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, photoTxt);
    }

    public static void main(String[] args) {
        Photo p = new Photo();
                p.onUpdate();
        System.out.println(p.editted);

    }
}
