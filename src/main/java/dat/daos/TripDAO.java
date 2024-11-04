package dat.daos;

import dat.entities.Trip;
import dat.entities.Guide;
import dat.dtos.TripDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<TripDTO>, ITripGuideDAO {

    private static EntityManagerFactory emf;
    private static TripDAO instance;

    private TripDAO(EntityManagerFactory emf) {
        TripDAO.emf = emf;
    }

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO(emf);
        }
        return instance;
    }

    @Override
    public void create(TripDTO tripDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = new Trip();
            trip.setId(tripDTO.getId());
            trip.setName(tripDTO.getName());
            trip.setStarttime(tripDTO.getStarttime());
            trip.setEndtime(tripDTO.getEndtime());
            trip.setLongitude(tripDTO.getLongitude());
            trip.setLatitude(tripDTO.getLatitude());
            trip.setPrice(tripDTO.getPrice());
            trip.setCategory(tripDTO.getCategory());
            em.persist(trip);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Override
    public List<TripDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t", Trip.class);
            return query.getResultList().stream().map(trip -> new TripDTO(
                    trip.getId(),
                    trip.getName(),
                    trip.getStarttime(),
                    trip.getEndtime(),
                    trip.getLongitude(),
                    trip.getLatitude(),
                    trip.getPrice(),
                    trip.getCategory()
            )).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    @Override
    public TripDTO getById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Trip trip = em.find(Trip.class, id);
            if (trip != null) {
                return new TripDTO(
                        trip.getId(),
                        trip.getName(),
                        trip.getStarttime(),
                        trip.getEndtime(),
                        trip.getLongitude(),
                        trip.getLatitude(),
                        trip.getPrice(),
                        trip.getCategory()
                );
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(TripDTO tripDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripDTO.getId());
            if (trip != null) {
                trip.setName(tripDTO.getName());
                trip.setStarttime(tripDTO.getStarttime());
                trip.setEndtime(tripDTO.getEndtime());
                trip.setLongitude(tripDTO.getLongitude());
                trip.setLatitude(tripDTO.getLatitude());
                trip.setPrice(tripDTO.getPrice());
                trip.setCategory(tripDTO.getCategory());
                em.merge(trip);
            }
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Override
    public void delete(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip != null) {
                em.remove(trip);
            }
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Override
    public void addGuideToTrip(Integer tripId, Integer guideId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripId);
            Guide guide = em.find(Guide.class, guideId);
            if (trip != null && guide != null) {
                trip.setGuide(guide);
                em.merge(trip);
            }
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuide(Integer guideId) {
        EntityManager em = emf.createEntityManager();
        try {
            Guide guide = em.find(Guide.class, guideId);
            if (guide != null) {
                return guide.getTrips().stream().map(trip -> new TripDTO(
                        trip.getId(),
                        trip.getName(),
                        trip.getStarttime(),
                        trip.getEndtime(),
                        trip.getLongitude(),
                        trip.getLatitude(),
                        trip.getPrice(),
                        trip.getCategory()
                )).collect(Collectors.toSet());
            }
            return new HashSet<>();
        } finally {
            em.close();
        }
    }
}
