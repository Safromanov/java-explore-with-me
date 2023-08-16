package ru.practicum.category.piblicAPI;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.exceptionHandler.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<ResponseCategoryDto> getCategoriesByParam(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        return categoryRepository.findAll(pageRequest).stream()
                .map(category -> modelMapper.map(category, ResponseCategoryDto.class))
                .collect(Collectors.toList());
    }

    public ResponseCategoryDto getCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return modelMapper.map(category, ResponseCategoryDto.class);
    }

}
