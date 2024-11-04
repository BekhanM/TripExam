package dat.daos;

import dat.entities.Guide;
import dat.dtos.GuideDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.stream.Collectors;

public class GuideDAO implements IDAO<GuideDTO> {

    private static EntityManagerFactory emf;
    private static GuideDAO instance;

    private GuideDAO(EntityManagerFactory emf) {
        GuideDAO.emf = emf;
    }

    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideDAO(emf);
        }
        return instance;
    }

    @Override
    public void create(GuideDTO guideDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Guide guide = new Guide();
            guide.setId(guideDTO.getId());
            guide.setFirstname(guideDTO.getFirstname());
            guide.setLastname(guideDTO.getLastname());
            guide.setEmail(guideDTO.getEmail());
            guide.setPhone(guideDTO.getPhone());
            guide.setYearsOfExperience(guideDTO.getYearsOfExperience());
            em.persist(guide);
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    @Override
    public List<GuideDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Guide> query = em.createQuery("SELECT g FROM Guide g", Guide.class);
            return query.getResultList().stream().map(guide -> new GuideDTO(
                    guide.getId(),
                    guide.getFirstname(),
                    guide.getLastname(),
                    guide.getEmail(),
                    guide.getPhone(),
                    guide.getYearsOfExperience()
            )).collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    @Override
    public GuideDTO getById(Integer id) {
        EntityManager em = emf.createEntityManager();
        try {
            Guide guide = em.find(Guide.class, id);
            if (guide != null) {
                return new GuideDTO(
                        guide.getId(),
                        guide.getFirstname(),
                        guide.getLastname(),
                        guide.getEmail(),
                        guide.getPhone(),
                        guide.getYearsOfExperience()
                );
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(GuideDTO guideDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Guide guide = em.find(Guide.class, guideDTO.getId());
            if (guide != null) {
                guide.setFirstname(guideDTO.getFirstname());
                guide.setLastname(guideDTO.getLastname());
                guide.setEmail(guideDTO.getEmail());
                guide.setPhone(guideDTO.getPhone());
                guide.setYearsOfExperience(guideDTO.getYearsOfExperience());
                em.merge(guide);
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
            Guide guide = em.find(Guide.class, id);
            if (guide != null) {
                em.remove(guide);
            }
            em.getTransaction().commit();
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }
}
