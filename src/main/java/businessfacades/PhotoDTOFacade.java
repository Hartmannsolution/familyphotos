package businessfacades;

import datafacades.IDataFacade;
import datafacades.PhotoFacade;
import dtos.PhotoDTO;
import dtos.PhotoDTO;
import entities.Photo;
import entities.Photo;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import java.util.List;

public class PhotoDTOFacade implements IDataFacade<PhotoDTO> {
    private static IDataFacade<PhotoDTO> instance;
    private static IDataFacade<Photo> photoFacade;

    //Private Constructor to ensure Singleton
    private PhotoDTOFacade() {}

    public static IDataFacade<PhotoDTO> getFacade() {
        if (instance == null) {
            photoFacade = PhotoFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new PhotoDTOFacade();
        }
        return instance;
    }

    @Override
    public PhotoDTO create(PhotoDTO PhotoDTO) {
        Photo p = PhotoDTO.getEntity();
        p = photoFacade.create(p);
        return new PhotoDTO(p);
    }

    @Override
    public PhotoDTO getById(int id) throws EntityNotFoundException {
        return new PhotoDTO(photoFacade.getById(id));
    }

    @Override
    public List<PhotoDTO> getAll() {
        return PhotoDTO.toList(photoFacade.getAll());
    }

    @Override
    public List<Photo> getByTagName(String tagName) {
        //return null;
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public PhotoDTO update(PhotoDTO PhotoDTO) throws EntityNotFoundException {
        Photo p = photoFacade.update(PhotoDTO.getEntity());
        return new PhotoDTO(p);
    }

    @Override
    public PhotoDTO delete(int id) throws EntityNotFoundException {
        return new PhotoDTO(photoFacade.delete(id));
    }
}
