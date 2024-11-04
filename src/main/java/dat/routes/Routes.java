package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final TripRoutes tripRoute = new TripRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/trips", tripRoute.getRoutes());
        };
    }
}
