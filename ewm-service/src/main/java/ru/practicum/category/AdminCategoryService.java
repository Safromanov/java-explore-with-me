package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.RequestCategoryDto;
import ru.practicum.category.dto.ResponseCategoryDto;

@RequiredArgsConstructor
@Service
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseCategoryDto postCategory(RequestCategoryDto requestCategoryDto) {
        Category category = new Category();
        category.setName(requestCategoryDto.getName());
        category = categoryRepository.save(category);
        return new ResponseCategoryDto(category.getId(), category.getName());
    }

    public ResponseCategoryDto patchCategory(RequestCategoryDto requestCategoryDto,
                                             long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category dont found"));
        category.setName(requestCategoryDto.getName());
        categoryRepository.save(category);
        return new ResponseCategoryDto(category.getId(), category.getName());
    }

    public void deleteCategory(long id) {

        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new NotFoundException("Category dont found");
                }
        );
    }
}
