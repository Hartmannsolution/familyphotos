package businessfacades;

import entities.Photo;
import entities.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagDTO {
    String name;
    List<Integer> photos = new ArrayList();

    public TagDTO(Tag tag) {
        this.name = tag.getName();
        tag.getPhotos().forEach(photo->this.photos.add(photo.getId()));
    }
    public Tag getEntity(){
        Tag p = new Tag(this.name);
        return p;
    }
}
