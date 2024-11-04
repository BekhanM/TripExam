package dat.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.config.HibernateConfig;
import dat.config.Populator;
import dat.daos.TripDAO;
import dat.dtos.GuideDTO;
import dat.dtos.PackingItem;
import dat.dtos.TripDTO;
import dat.entities.Category;
import dat.entities.Guide;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TripController {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("trips");
    private TripDAO tripDAO;
    public TripController() {
        this.tripDAO = TripDAO.getInstance(emf);
    }

    private List<PackingItem> fetchPackingItemsByCategory(String category) throws IOException {
        URL url = new URL("https://packingapi.cphbusinessapps.dk/packinglist/" + category.toLowerCase());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            JsonNode rootNode = objectMapper.readTree(connection.getInputStream());
            return objectMapper.convertValue(rootNode.get("items"), objectMapper.getTypeFactory().constructCollectionType(List.class, PackingItem.class));
        } else {
            throw new IOException("Failed to fetch packing items for category: " + category);
        }
    }

    public void getAllTrips(Context ctx) {
        try {
            List<TripDTO> trips = tripDAO.getAll();
            ctx.json(trips);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving all trips", e);
        }
    }

    public void getTripsByCategory(Context ctx) {
        try {
            String categoryParam = ctx.pathParam("category").toUpperCase();
            Category category = Category.valueOf(categoryParam);

            List<TripDTO> tripsByCategory = tripDAO.getAll().stream()
                    .filter(trip -> trip.getCategory() == category)
                    .collect(Collectors.toList());

            ctx.json(tripsByCategory);
        } catch (IllegalArgumentException e) {
            handleException(ctx, HttpStatus.BAD_REQUEST, "Invalid category: " + ctx.pathParam("category"), e);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving trips by category", e);
        }
    }

    public void getTotalPriceByGuide(Context ctx) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Object[]> results = em.createQuery(
                            "SELECT g.id, SUM(t.price) FROM Guide g JOIN g.trips t GROUP BY g.id", Object[].class)
                    .getResultList();

            List<Map<String, Object>> response = results.stream()
                    .map(result -> {
                        Map<String, Object> guideSummary = new HashMap<>();
                        guideSummary.put("guideId", result[0]);
                        guideSummary.put("totalPrice", result[1]);
                        return guideSummary;
                    })
                    .collect(Collectors.toList());

            ctx.json(response);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving total price by guide", e);
        } finally {
            em.close();
        }
    }


    public void getTripById(Context ctx) {
        EntityManager em = emf.createEntityManager();
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = tripDAO.getById(id);

            if (tripDTO != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("trip", tripDTO);

                Guide guide = em.createQuery("SELECT g FROM Guide g JOIN g.trips t WHERE t.id = :tripId", Guide.class)
                        .setParameter("tripId", id)
                        .getSingleResult();
                if (guide != null) {
                    GuideDTO guideDTO = new GuideDTO(
                            guide.getId(),
                            guide.getFirstname(),
                            guide.getLastname(),
                            guide.getEmail(),
                            guide.getPhone(),
                            guide.getYearsOfExperience()
                    );
                    response.put("guide", guideDTO);
                }

                List<PackingItem> packingItems = fetchPackingItemsByCategory(tripDTO.getCategory().toString().toLowerCase());
                response.put("packingItems", packingItems);

                ctx.json(response);
            } else {
                handleException(ctx, HttpStatus.NOT_FOUND, "Trip with id " + id + " not found", null);
            }
        } catch (NumberFormatException e) {
            handleException(ctx, HttpStatus.BAD_REQUEST, "Invalid trip ID format", e);
        } catch (NoResultException e) {
            handleException(ctx, HttpStatus.NOT_FOUND, "No guide found for the trip with id ", null);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving trip with id ", e);
        } finally {
            em.close();
        }
    }


    public void getTotalPackingWeight(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = tripDAO.getById(id);

            if (tripDTO != null) {
                List<PackingItem> packingItems = fetchPackingItemsByCategory(tripDTO.getCategory().toString().toLowerCase());

                double totalWeight = packingItems.stream()
                        .mapToDouble(PackingItem::getWeightInGrams)
                        .sum();

                Map<String, Object> response = new HashMap<>();
                response.put("tripId", id);
                response.put("totalPackingWeight", totalWeight);

                ctx.json(response);
            } else {
                handleException(ctx, HttpStatus.NOT_FOUND, "Trip with id " + id + " not found", null);
            }
        } catch (NumberFormatException e) {
            handleException(ctx, HttpStatus.BAD_REQUEST, "Invalid trip ID format", e);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving packing items for trip id", e);
        }
    }


    public void createTrip(Context ctx) {
        try {
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            tripDAO.create(tripDTO);
            ctx.status(HttpStatus.CREATED).json(tripDTO);
        } catch (IllegalArgumentException e) {
            handleException(ctx, HttpStatus.BAD_REQUEST, "Invalid values for trip", e);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create trip", e);
        }
    }

    public void updateTrip(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            tripDTO.setId(id);

            tripDAO.update(tripDTO);

            TripDTO updatedTrip = tripDAO.getById(id);
            if (updatedTrip != null) {
                ctx.json(updatedTrip);
            } else {
                handleException(ctx, HttpStatus.NOT_FOUND, "Trip with id " + id + " not found after update", null);
            }
        } catch (NumberFormatException e) {
            handleException(ctx, HttpStatus.BAD_REQUEST, "Invalid trip ID format", e);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update trip", e);
        }
    }


    public void deleteTrip(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO trip = tripDAO.getById(id);
            if (trip != null) {
                tripDAO.delete(id);
                ctx.status(HttpStatus.NO_CONTENT);
            } else {
                handleException(ctx, HttpStatus.NOT_FOUND, "Trip with id " + id + " not found", null);
            }
        } catch (NumberFormatException e) {
            handleException(ctx, HttpStatus.BAD_REQUEST, "Invalid trip ID format", e);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting trip with id ", e);
        }
    }

    public void addGuideToTrip(Context ctx) {
        try {
            int tripId = Integer.parseInt(ctx.pathParam("tripId"));
            int guideId = Integer.parseInt(ctx.pathParam("guideId"));
            tripDAO.addGuideToTrip(tripId, guideId);
            ctx.status(HttpStatus.NO_CONTENT);
        } catch (NumberFormatException e) {
            handleException(ctx, HttpStatus.BAD_REQUEST, "Invalid trip or guide ID format", e);
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add guide to trip", e);
        }
    }

    public void populateTrips(Context ctx) {
        try {
            Populator populator = new Populator(emf); // Use the shared EntityManagerFactory instance
            populator.populateData();
            ctx.status(HttpStatus.CREATED).result("Database populated with trips and guides");
        } catch (Exception e) {
            handleException(ctx, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to populate database", e);
        }
    }


    private void handleException(Context ctx, HttpStatus status, String message, Exception e) {
        ctx.status(status.getCode());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.getCode());
        errorResponse.put("message", message);
        errorResponse.put("timestamp", LocalDateTime.now().toString());

        if (e != null) {
            errorResponse.put("error", e.getMessage());
        }

        ctx.json(errorResponse);
    }
}
