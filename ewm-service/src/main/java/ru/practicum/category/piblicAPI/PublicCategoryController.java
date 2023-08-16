package ru.practicum.category.piblicAPI;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.ResponseCategoryDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseCategoryDto> getCategoriesByParam(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                          @RequestParam(defaultValue = "10") @Positive int size) {
        return categoryService.getCategoriesByParam(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCategoryDto getCategory(@PathVariable long catId) {
        return categoryService.getCategory(catId);
    }

}
