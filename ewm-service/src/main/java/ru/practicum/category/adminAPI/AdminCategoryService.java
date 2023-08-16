package ru.practicum.category.adminAPI;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.dto.RequestCategoryDto;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.event.EventRepository;
import ru.practicum.exceptionHandler.ConflictException;
import ru.practicum.exceptionHandler.NotFoundException;

@RequiredArgsConstructor
@Service
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public ResponseCategoryDto createCategory(RequestCategoryDto requestCategoryDto) {
        categoryRepository.findByName(requestCategoryDto.getName()).ifPresent((x) -> {
            throw new ConflictException("Category already exist");
        });
        Category category = modelMapper.map(requestCategoryDto, Category.class);
        category = categoryRepository.save(category);
        return new ResponseCategoryDto(category.getId(), category.getName());
    }

    public ResponseCategoryDto patchCategory(RequestCategoryDto requestCategoryDto, long id) {
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
        categoryRepository.findById(id).ifPresentOrElse(category -> {
                    if (eventRepository.existsByCategory(category))
                        throw new ConflictException("Category have events");
                    categoryRepository.delete(category);
                },
                () -> {
                    throw new NotFoundException("Category dont found");
                }
        );
    }
}
