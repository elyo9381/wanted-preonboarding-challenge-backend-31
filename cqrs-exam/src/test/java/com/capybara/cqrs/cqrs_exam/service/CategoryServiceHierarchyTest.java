package test.java.com.capybara.cqrs.cqrs_exam.service;

import com.capybara.cqrs.cqrs_exam.category.domain.Category;
import com.capybara.cqrs.cqrs_exam.category.domain.CategoryRepository;
import com.capybara.cqrs.cqrs_exam.controller.dto.CategoryDTO;
import com.capybara.cqrs.cqrs_exam.controller.mapper.CategoryMapper;
import com.capybara.cqrs.cqrs_exam.service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceHierarchyTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private List<Category> mockCategories;
    private Category category1, category2, category3, category9, category10, category15, category45, category46;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정 (data/categories.sql 및 Category.java, CategoryDTO.java 참고)

        // Level 1
        category1 = Category.builder()
                .id(1L).name("가구").slug("furniture")
                .description("편안한 생활을 위한 다양한 가구 컬렉션").level(1)
                .imageUrl("https://example.com/categories/furniture.jpg")
                .children(new ArrayList<>())
                .build();
        category2 = Category.builder()
                .id(2L).name("가전제품").slug("electronics")
                .description("생활의 편리함을 더하는 가전제품 모음").level(1)
                .imageUrl("https://example.com/categories/electronics.jpg")
                .children(new ArrayList<>())
                .build();
        category3 = Category.builder()
                .id(3L).name("인테리어 소품").slug("interior-accessories")
                .description("공간의 분위기를 바꾸는 다양한 인테리어 소품").level(1)
                .imageUrl("https://example.com/categories/interior.jpg")
                .children(new ArrayList<>())
                .build();


        // Level 2
        // Parent: 가구 (ID: 1)
        category9 = Category.builder()
                .id(9L).name("거실 가구").slug("living-room")
                .description("거실을 위한 다양한 가구 제품").level(2).parent(category1)
                .imageUrl("https://example.com/categories/living-room.jpg")
                .children(new ArrayList<>())
                .build();
        category10 = Category.builder()
                .id(10L).name("침실 가구").slug("bedroom")
                .description("편안한 휴식을 위한 침실 가구").level(2).parent(category1)
                .imageUrl("https://example.com/categories/bedroom.jpg")
                .children(new ArrayList<>())
                .build();

        // Parent: 가전제품 (ID: 2)
        category15 = Category.builder()
                .id(15L).name("대형가전").slug("major-appliances")
                .description("생활의 편리함을 더하는 대형 가전제품").level(2).parent(category2)
                .imageUrl("https://example.com/categories/major-appliances.jpg")
                .children(new ArrayList<>())
                .build();


        // Level 3
        // Parent: 거실 가구 (ID: 9)
        category45 = Category.builder()
                .id(45L).name("소파").slug("sofa")
                .description("다양한 스타일과 크기의 소파").level(3).parent(category9)
                .imageUrl("https://example.com/categories/sofa.jpg")
                .children(new ArrayList<>())
                .build();
        // Parent: 거실 가구 (ID: 9)
        category46 = Category.builder()
                .id(46L).name("거실장/TV장").slug("tv-stands")
                .description("다양한 스타일의 TV 스탠드 및 거실장").level(3).parent(category9)
                .imageUrl("https://example.com/categories/tv-stands.jpg")
                .children(new ArrayList<>())
                .build();


        mockCategories = new ArrayList<>(Arrays.asList(category1, category2, category3, category9, category10, category15, category45, category46));

        // CategoryMapper Mock 설정
        when(categoryMapper.toCategoryResponse(any(Category.class))).thenAnswer(invocation -> {
            Category cat = invocation.getArgument(0);
            return CategoryDTO.Category.builder()
                    .id(cat.getId())
                    .name(cat.getName())
                    .slug(cat.getSlug())
                    .description(cat.getDescription())
                    .level(cat.getLevel())
                    .imageUrl(cat.getImageUrl())
                    .children(new ArrayList<>()) // 자식 DTO 리스트는 서비스 로직에서 채워짐
                    .build();
        });
    }

    @Test
    @DisplayName("getAllCategories(null) 호출 시 전체 카테고리 계층 구조를 올바르게 반환해야 한다")
    void getAllCategories_withNullLevel_shouldReturnFullHierarchy() {
        // given
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        // when
        List<CategoryDTO.Category> result = categoryService.getAllCategories(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3); // 루트 카테고리: 가구, 가전제품, 인테리어 소품

        // 1. "가구" 카테고리 검증 (ID: 1)
        Optional<CategoryDTO.Category> furnitureOpt = result.stream().filter(c -> c.getId().equals(1L)).findFirst();
        assertThat(furnitureOpt).isPresent();
        CategoryDTO.Category furnitureDto = furnitureOpt.get();
        validateCategoryDTO(furnitureDto, 1L, "가구", "furniture", 1, 2); // 자식 2개: 거실 가구, 침실 가구

        // 1.1. "가구" > "거실 가구" 검증 (ID: 9)
        Optional<CategoryDTO.Category> livingRoomOpt = furnitureDto.getChildren().stream().filter(c -> c.getId().equals(9L)).findFirst();
        assertThat(livingRoomOpt).isPresent();
        CategoryDTO.Category livingRoomDto = livingRoomOpt.get();
        validateCategoryDTO(livingRoomDto, 9L, "거실 가구", "living-room", 2, 2); // 자식 2개: 소파, 거실장/TV장

        // 1.1.1. "가구" > "거실 가구" > "소파" 검증 (ID: 45)
        Optional<CategoryDTO.Category> sofaOpt = livingRoomDto.getChildren().stream().filter(c -> c.getId().equals(45L)).findFirst();
        assertThat(sofaOpt).isPresent();
        validateCategoryDTO(sofaOpt.get(), 45L, "소파", "sofa", 3, 0); // 자식 없음

        // 1.1.2. "가구" > "거실 가구" > "거실장/TV장" 검증 (ID: 46)
        Optional<CategoryDTO.Category> tvStandOpt = livingRoomDto.getChildren().stream().filter(c -> c.getId().equals(46L)).findFirst();
        assertThat(tvStandOpt).isPresent();
        validateCategoryDTO(tvStandOpt.get(), 46L, "거실장/TV장", "tv-stands", 3, 0); // 자식 없음

        // 1.2. "가구" > "침실 가구" 검증 (ID: 10)
        Optional<CategoryDTO.Category> bedroomOpt = furnitureDto.getChildren().stream().filter(c -> c.getId().equals(10L)).findFirst();
        assertThat(bedroomOpt).isPresent();
        validateCategoryDTO(bedroomOpt.get(), 10L, "침실 가구", "bedroom", 2, 0); // 자식 없음


        // 2. "가전제품" 카테고리 검증 (ID: 2)
        Optional<CategoryDTO.Category> electronicsOpt = result.stream().filter(c -> c.getId().equals(2L)).findFirst();
        assertThat(electronicsOpt).isPresent();
        CategoryDTO.Category electronicsDto = electronicsOpt.get();
        validateCategoryDTO(electronicsDto, 2L, "가전제품", "electronics", 1, 1); // 자식 1개: 대형가전

        // 2.1. "가전제품" > "대형가전" 검증 (ID: 15)
        Optional<CategoryDTO.Category> majorAppliancesOpt = electronicsDto.getChildren().stream().filter(c -> c.getId().equals(15L)).findFirst();
        assertThat(majorAppliancesOpt).isPresent();
        validateCategoryDTO(majorAppliancesOpt.get(), 15L, "대형가전", "major-appliances", 2, 0); // 자식 없음


        // 3. "인테리어 소품" 카테고리 검증 (ID: 3)
        Optional<CategoryDTO.Category> interiorOpt = result.stream().filter(c -> c.getId().equals(3L)).findFirst();
        assertThat(interiorOpt).isPresent();
        validateCategoryDTO(interiorOpt.get(), 3L, "인테리어 소품", "interior-accessories", 1, 0); // 자식 없음
    }

    private void validateCategoryDTO(CategoryDTO.Category dto, Long expectedId, String expectedName, String expectedSlug, int expectedLevel, int expectedChildrenSize) {
        assertThat(dto.getId()).isEqualTo(expectedId);
        assertThat(dto.getName()).isEqualTo(expectedName);
        assertThat(dto.getSlug()).isEqualTo(expectedSlug);
        assertThat(dto.getLevel()).isEqualTo(expectedLevel);
        // assertThat(dto.getDescription()).isEqualTo(expectedDescription); // 필요시 추가
        // assertThat(dto.getImageUrl()).isEqualTo(expectedImageUrl); // 필요시 추가
        if (expectedChildrenSize > 0) {
            assertThat(dto.getChildren()).hasSize(expectedChildrenSize);
        } else {
            assertThat(dto.getChildren()).isEmpty();
        }
    }

    @Test
    @DisplayName("카테고리가 없을 경우 빈 리스트를 반환해야 한다 (level 파라미터 null)")
    void getAllCategories_whenNoCategoriesAndLevelIsNull_shouldReturnEmptyList() {
        // given
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        // when
        List<CategoryDTO.Category> result = categoryService.getAllCategories(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("루트 카테고리만 있을 경우 하위 카테고리 없이 루트 카테고리만 반환해야 한다 (level 파라미터 null)")
    void getAllCategories_whenOnlyRootCategoriesAndLevelIsNull_shouldReturnRootCategoriesWithoutChildren() {
        // given
        Category root1 = Category.builder().id(1L).name("루트1").slug("root1").level(1).description("루트1 설명").imageUrl("url1").children(new ArrayList<>()).build();
        Category root2 = Category.builder().id(2L).name("루트2").slug("root2").level(1).description("루트2 설명").imageUrl("url2").children(new ArrayList<>()).build();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(root1, root2));

        when(categoryMapper.toCategoryResponse(root1)).thenAnswer(invocation -> {
            Category cat = invocation.getArgument(0);
            return CategoryDTO.Category.builder()
                    .id(cat.getId()).name(cat.getName()).slug(cat.getSlug())
                    .description(cat.getDescription()).level(cat.getLevel()).imageUrl(cat.getImageUrl())
                    .children(new ArrayList<>()).build();
        });
        when(categoryMapper.toCategoryResponse(root2)).thenAnswer(invocation -> {
            Category cat = invocation.getArgument(0);
            return CategoryDTO.Category.builder()
                    .id(cat.getId()).name(cat.getName()).slug(cat.getSlug())
                    .description(cat.getDescription()).level(cat.getLevel()).imageUrl(cat.getImageUrl())
                    .children(new ArrayList<>()).build();
        });

        // when
        List<CategoryDTO.Category> result = categoryService.getAllCategories(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        Optional<CategoryDTO.Category> root1DtoOpt = result.stream().filter(c -> c.getId().equals(1L)).findFirst();
        assertThat(root1DtoOpt).isPresent();
        validateCategoryDTO(root1DtoOpt.get(), 1L, "루트1", "root1", 1, 0);


        Optional<CategoryDTO.Category> root2DtoOpt = result.stream().filter(c -> c.getId().equals(2L)).findFirst();
        assertThat(root2DtoOpt).isPresent();
        validateCategoryDTO(root2DtoOpt.get(), 2L, "루트2", "root2", 1, 0);
    }
}
