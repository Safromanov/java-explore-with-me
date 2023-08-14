package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.category.adminAPI.AdminCategoryService;
import ru.practicum.category.dto.RequestCategoryDto;
import ru.practicum.exceptionHandler.ConflictException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdminCategoryServiceTest {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @Test
    void postCategory_returnConflict() {
        adminCategoryService.postCategory(new RequestCategoryDto("Category"));
        assertThrows(ConflictException.class, () ->
                adminCategoryService.postCategory(new RequestCategoryDto("Category")));
    }

    @Test
    void patchCategory_returnConflict() {
        adminCategoryService.patchCategory(new RequestCategoryDto("Categoryv"), 1);
        assertThrows(ConflictException.class, () ->
                adminCategoryService.patchCategory(new RequestCategoryDto("Categoryv"), 2));
    }

    @Test
    void deleteCategory_returnConflict() {
        assertThrows(ConflictException.class, () -> adminCategoryService.deleteCategory(1L));
    }
}