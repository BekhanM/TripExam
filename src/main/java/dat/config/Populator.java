package dat.config;

import dat.entities.Category;
import dat.entities.Guide;
import dat.entities.Trip;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class Populator {
    private final EntityManagerFactory emf;

    public Populator(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("trips");
        Populator populator = new Populator(emf);
        populator.populateData();
        emf.close();
    }

    public void populateData() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Guide guide1 = new Guide();
            guide1.setFirstname("John");
            guide1.setLastname("Doe");
            guide1.setEmail("john@example.com");
            guide1.setPhone("1234567890");
            guide1.setYearsOfExperience(5);

            Guide guide2 = new Guide();
            guide2.setFirstname("Jane");
            guide2.setLastname("Smith");
            guide2.setEmail("jane@example.com");
            guide2.setPhone("0987654321");
            guide2.setYearsOfExperience(10);

            Trip trip1 = new Trip();
            trip1.setName("Beach Adventure");
            trip1.setStarttime(LocalDateTime.now());
            trip1.setEndtime(LocalDateTime.now().plusDays(1));
            trip1.setLongitude(34.0522);
            trip1.setLatitude(-118.2437);
            trip1.setPrice(200.0);
            trip1.setCategory(Category.BEACH);
            trip1.setGuide(guide1);

            Trip trip2 = new Trip();
            trip2.setName("City Tour");
            trip2.setStarttime(LocalDateTime.now());
            trip2.setEndtime(LocalDateTime.now().plusDays(2));
            trip2.setLongitude(40.7128);
            trip2.setLatitude(-74.0060);
            trip2.setPrice(150.0);
            trip2.setCategory(Category.CITY);
            trip2.setGuide(guide2);

            Trip trip3 = new Trip();
            trip3.setName("Forest Exploration");
            trip3.setStarttime(LocalDateTime.now());
            trip3.setEndtime(LocalDateTime.now().plusDays(3));
            trip3.setLongitude(47.6062);
            trip3.setLatitude(-122.3321);
            trip3.setPrice(300.0);
            trip3.setCategory(Category.FOREST);
            trip3.setGuide(guide1);

            guide1.setTrips(List.of(trip1, trip3));
            guide2.setTrips(List.of(trip2));

            em.persist(guide1);
            em.persist(guide2);
            em.persist(trip1);
            em.persist(trip2);
            em.persist(trip3);

            em.getTransaction().commit();
            System.out.println("Database populated successfully.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
