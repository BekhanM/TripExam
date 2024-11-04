package dat.routes;

import dat.controllers.TripController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoutes {

    private final TripController tripController = new TripController();

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", tripController::getAllTrips, Role.ANYONE);
            get("/{id}", tripController::getTripById, Role.ANYONE);
            post("/", tripController::createTrip, Role.ADMIN);
            put("/{id}", tripController::updateTrip, Role.ADMIN);
            delete("/{id}", tripController::deleteTrip, Role.ADMIN);
            put("/{tripId}/guides/{guideId}", tripController::addGuideToTrip, Role.ADMIN);
            post("/populate", tripController::populateTrips, Role.ADMIN);
            get("/category/{category}", tripController::getTripsByCategory, Role.USER);
            get("/guides/totalprice", tripController::getTotalPriceByGuide, Role.USER);
            get("/{id}/packing-weight", tripController::getTotalPackingWeight, Role.USER);
        };
    }
}
