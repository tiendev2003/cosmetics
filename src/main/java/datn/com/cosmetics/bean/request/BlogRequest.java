package datn.com.cosmetics.bean.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private String image;

    @NotBlank(message = "Status is required")
    private String status;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

     private String author;

    private Set<Long> tagIds;
}
