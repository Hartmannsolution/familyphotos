package dtos;

import businessfacades.TagDTO;
import entities.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PhotoDTO {
    private int id;
    private String name;
    private int year;
    private int viewNo;
    private String description;
    private List<TagDTO> tags = new ArrayList();

    public PhotoDTO(Photo photo) {
        if(photo.getId()!=null)
            this.id = photo.getId();
        this.name = photo.getFileName();
        this.year = photo.getYear();
        this.viewNo = photo.getViewNo();
        this.description = photo.getPhotoTxt();
        photo.getTags().forEach(tag->this.tags.add(new TagDTO(tag)));
    }

    public static List<PhotoDTO> toList(List<Photo> photos) {
        return photos.stream().map(PhotoDTO::new).collect(Collectors.toList());
    }

    public Photo getEntity(){
        Photo p = new Photo(this.name, this.year, this.description);
        if(id != 0)
            p.setId(this.id);
        this.tags.forEach(tag->p.addTag(tag.getEntity()));
        return p;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getViewNo() {
        return viewNo;
    }

    public void setViewNo(int viewNo) {
        this.viewNo = viewNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoDTO photoDTO = (PhotoDTO) o;
        return id == photoDTO.id && year == photoDTO.year && Objects.equals(name, photoDTO.name) && Objects.equals(description, photoDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, year, description);
    }
}
