package entities;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "fotobeskriv_bbmhm")
@Entity
@NamedQuery(name = "Photo.deleteAllRows", query = "DELETE from Photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FotoId", nullable = false)
    private Integer id;

    @Column(name = "FilNavn", nullable = false, length = 35)
    private String fileName;

    @Column(name = "Aar", nullable = false)
    private int year;

    @Column(name = "VisNr")
    private int viewNo;

    @Lob
    @Column(name = "FotoTxt", nullable = false)
    private String photoTxt;

    @Lob
    @Column(name = "FotoTxtAdd")
    private String photoTxtAdd;

    @Column(name = "Oprettet")
    private LocalDate created;

    @Column(name = "Rettet")
    private LocalDate editted;

    @ManyToMany(mappedBy = "photos") //, cascade = CascadeType.PERSIST) //, fetch = FetchType.EAGER) //Target side of relationsship (inverse side)
    private List<Tag> tags = new ArrayList<>();

    public Photo() {
    }

    public Photo(String fileName, Integer year, String photoTxt) {
        this.fileName = fileName;
        this.year = year;
        this.photoTxt = photoTxt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getEditted() {
        return editted;
    }

    public LocalDate getCreated() {
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
        return Objects.equals(id, photo.id) && Objects.equals(fileName, photo.fileName) && Objects.equals(year, photo.year) && Objects.equals(photoTxt, photo.photoTxt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileName, year, photoTxt);
    }
}
