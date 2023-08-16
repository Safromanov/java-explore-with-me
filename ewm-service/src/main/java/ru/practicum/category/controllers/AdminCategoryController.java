package ru.practicum.category.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.RequestCategoryDto;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.category.service.CategoryServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryServiceImpl adminCategoryServiceImpl;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCategoryDto createCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        log.debug("POST /admin/categories with dto: {}.", requestCategoryDto);
        return adminCategoryServiceImpl.createCategory(requestCategoryDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseCategoryDto updateCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto,
                                              @PathVariable Long id) {
        log.debug("PATCH /admin/categories with: {}, {}.", id, requestCategoryDto);
        return adminCategoryServiceImpl.patchCategory(requestCategoryDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive long id) {
        log.debug("DELETE /admin/categories with id: {}.", id);
        adminCategoryServiceImpl.deleteCategory(id);
    }
}
