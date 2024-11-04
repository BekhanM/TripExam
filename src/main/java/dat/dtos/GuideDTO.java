package dat.dtos;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideDTO {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Integer yearsOfExperience;
}
