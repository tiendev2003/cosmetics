package datn.com.cosmetics.bean.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @NotBlank(message = "Street address is required")
    @Size(max = 100, message = "Street address must be less than 100 characters")
    private String streetAddress;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must be less than 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 50, message = "State must be less than 50 characters")
    private String state;

    @NotBlank(message = "Zip code is required")
    @Size(max = 10, message = "Zip code must be less than 10 characters")
    private String zipCode;
}
