package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.dto.RequestCategoryDto;
import ru.practicum.category.dto.ResponseCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.EventRepository;
import ru.practicum.exceptionHandler.ConflictException;
import ru.practicum.exceptionHandler.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final EventRepository eventRepository;

    @Override
    public ResponseCategoryDto createCategory(RequestCategoryDto requestCategoryDto) {
        categoryRepository.findByName(requestCategoryDto.getName()).ifPresent((x) -> {
            throw new ConflictException("Category already exist");
        });
        Category category = modelMapper.map(requestCategoryDto, Category.class);
        category = categoryRepository.save(category);
        return new ResponseCategoryDto(category.getId(), category.getName());
    }

    @Override
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

    @Override
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

    @Override
    public List<ResponseCategoryDto> getCategoriesByParam(int from, int size) {
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);
        return categoryRepository.findAll(pageRequest).stream()
                .map(category -> modelMapper.map(category, ResponseCategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseCategoryDto getCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return modelMapper.map(category, ResponseCategoryDto.class);
    }

}
