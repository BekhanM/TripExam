package dat.daos;

import dat.dtos.TripDTO;

import java.util.Set;

public interface ITripGuideDAO {
    void addGuideToTrip(Integer tripId, Integer guideId);
    Set<TripDTO> getTripsByGuide(Integer guideId);
}
