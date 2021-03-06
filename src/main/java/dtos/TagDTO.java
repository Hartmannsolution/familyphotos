package dtos;

import entities.Photo;
import entities.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TagDTO {
    String name;
    List<String> photos = new ArrayList();

    public TagDTO(Tag tag) {
        this.name = tag.getName();
        tag.getPhotos().forEach(photo->this.photos.add(photo.getFileName()));
    }

    public static List<TagDTO> toList(List<Tag> tags) {
        return tags.stream().map(TagDTO::new).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return Objects.equals(name, tagDTO.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
