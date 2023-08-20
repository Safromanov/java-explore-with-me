package ru.practicum.category.service;

import ru.practicum.category.dto.RequestCategoryDto;
import ru.practicum.category.dto.ResponseCategoryDto;

import java.util.List;

public interface CategoryService {
    ResponseCategoryDto createCategory(RequestCategoryDto requestCategoryDto);

    ResponseCategoryDto patchCategory(RequestCategoryDto requestCategoryDto, long id);

    void deleteCategory(Long id);

    List<ResponseCategoryDto> getCategoriesByParam(int from, int size);

    ResponseCategoryDto getCategory(long catId);
}
