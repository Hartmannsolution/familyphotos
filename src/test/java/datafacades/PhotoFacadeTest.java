package datafacades;

import entities.Photo;
import errorhandling.EntityNotFoundException;
import entities.Photo;
import entities.Tag;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
class PhotoFacadeTest {

    private static EntityManagerFactory emf;
    private static IDataFacade<Photo> facade;
    private static IExtraFunc<Tag> tagsFacade;
    Photo p1,p2;
    Tag t1, t2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PhotoFacade.getFacade(emf);
        tagsFacade = PhotoFacade.getTagFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
        emf.close();
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Tag.deleteAllRows").executeUpdate();
            em.createNamedQuery("Photo.deleteAllRows").executeUpdate();
            p1 = new Photo("my location","Somewhere", "Dette er et meget gammelt billede");
            p2 = new Photo("some place","Some file name", "Dette er et billede af noget");
            t1 = new Tag("Dorthea");
            t2 = new Tag("Frederik");
            em.persist(p1);
            em.persist(p2);
            em.persist(t1);
            em.persist(t2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }


    @Test
    void create() {
        System.out.println("Testing create(Photo p)");
        Photo p = new Photo("Somewhere","TestPhoto", "Something");
        Photo expected = p;
        Photo actual   = facade.create(p);
        assertEquals(expected, actual);
    }

    @Test
    void createWithTags() {
        System.out.println("Testing create(Photo p) with tags added");
        Photo p = new Photo("Somewhere","TestPhoto", "description");
        p.addTag(new Tag("Alfred"));
        p.addTag(new Tag("Roberta"));
        Photo expected = p;
        Photo actual   = facade.create(p);
        assertEquals(expected, actual);
    }

    @Test
    void createWithKnownTags() {
        System.out.println("Testing create(Photo p) with Tags added");
        Photo p = new Photo("Somewhere","TestPhoto","Something");
        p.addTag(t1);
        p.addTag(t2);
        Photo expected = p;
        Photo actual   = facade.create(p);
        assertEquals(expected, actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        System.out.println("Testing getbyid(id)");
        Photo expected = p1;
        Photo actual = facade.getById(p1.getFileName());
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        System.out.println("Testing getAll()");
        int expected = 2;
        int actual = facade.getAll().size();
        assertEquals(expected,actual);
    }
@Test
    void getAllTags() {
        System.out.println("Testing getAllTags()");
        int expected = 2;
        int actual = tagsFacade.getAllElements().size();
        assertEquals(expected,actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        System.out.println("Testing Update(Photo p)");
        p2.setPhotoTxt("HOHO");
        Photo expected = p2;
        Photo actual = facade.update(p2);
        assertEquals(expected,actual);
    }

    @Test
    void updateWithTags() throws EntityNotFoundException {
        System.out.println("Testing Update(Photo p) with known Tags");
        p2.addTag(t1);
        p2.addTag(t2);
        Photo p = facade.update(p2);
        int expected = 2;
        int actual = p.getTags().size();
        assertEquals(expected,actual);
    }

    @Test
    void delete() throws EntityNotFoundException {
        System.out.println("Testing delete(id)");
        Photo p = facade.delete(p1.getFileName());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(p,p1);
    }
}