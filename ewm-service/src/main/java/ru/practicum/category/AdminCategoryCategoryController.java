package ru.practicum.category;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.RequestCategoryDto;
import ru.practicum.category.dto.ResponseCategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCategoryDto postCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        log.debug("POST /admin/categories with dto: {}.", requestCategoryDto);
        return adminCategoryService.postCategory(requestCategoryDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCategoryDto patchCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto,
                                             @PathVariable long id) {
        log.debug("PATCH /admin/categories with: {}, {}.", id, requestCategoryDto);
        return adminCategoryService.patchCategory(requestCategoryDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long id) {
        log.debug("DELETE /admin/categories with id: {}.", id);
        adminCategoryService.deleteCategory(id);
    }
}
