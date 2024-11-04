package dat.dtos;

import dat.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {

    private Integer id;
    private String name;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private Double longitude;
    private Double latitude;
    private Double price;
    private Category category;
}
