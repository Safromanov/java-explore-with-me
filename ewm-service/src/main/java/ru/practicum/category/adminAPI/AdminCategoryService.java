package ru.practicum.category.adminAPI;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.dto.RequestCategoryDto;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.exceptionHandler.ConflictException;
import ru.practicum.exceptionHandler.NotFoundException;

@RequiredArgsConstructor
@Service
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseCategoryDto postCategory(RequestCategoryDto requestCategoryDto) {
        Category category = new Category();
        category.setName(requestCategoryDto.getName());
        categoryRepository.findByName(requestCategoryDto.getName()).ifPresent((x) -> {
            throw new ConflictException("Category exist");
        });
        category = categoryRepository.save(category);
        return new ResponseCategoryDto(category.getId(), category.getName());
    }

    public ResponseCategoryDto patchCategory(RequestCategoryDto requestCategoryDto,
                                             long id) {
        categoryRepository.findByName(requestCategoryDto.getName()).ifPresent((x) -> {
            if (x.getId() != id)
                throw new ConflictException("Category exist");
        });
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category dont found"));

        category.setName(requestCategoryDto.getName());

        categoryRepository.save(category);

        return new ResponseCategoryDto(category.getId(), category.getName());
    }

    public void deleteCategory(Long id) {

        try {
            categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
                        throw new NotFoundException("Category dont found");
                    }
            );
        } catch (DataIntegrityViolationException exception) {
            throw new ConflictException("category exist");
        }
    }
}
