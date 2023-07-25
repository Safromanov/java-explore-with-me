package ru.practicum.category.piblicAPI;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.ResponseCategoryDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseCategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {

        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCategoryDto getCategory(@PathVariable long catId) {

        return categoryService.getCategory(catId);
    }

}
