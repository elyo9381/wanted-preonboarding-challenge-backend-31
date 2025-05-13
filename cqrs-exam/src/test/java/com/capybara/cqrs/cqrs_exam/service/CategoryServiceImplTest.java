package test.java.com.capybara.cqrs.cqrs_exam.service;

import com.capybara.cqrs.cqrs_exam.category.domain.Category;
import com.capybara.cqrs.cqrs_exam.category.domain.CategoryRepository;
import com.capybara.cqrs.cqrs_exam.controller.dto.CategoryDTO;
import com.capybara.cqrs.cqrs_exam.controller.mapper.CategoryMapper;
import com.capybara.cqrs.cqrs_exam.service.CategoryServiceImpl; // 실제 서비스 구현체 import
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
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private List<Category> mockCategories;
    private Category category1, category2, category9, category10, category45;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정 (data/categories.sql 및 Category.java, CategoryDTO.java 참고)

        // Level 1
        category1 = Category.builder()
                .id(1L)
                .name("가구")
                .slug("furniture")
                .description("편안한 생활을 위한 다양한 가구 컬렉션")
                .level(1)
                .imageUrl("https://example.com/categories/furniture.jpg")
                .children(new ArrayList<>()) // 엔티티의 children은 초기화
                .build();
        category2 = Category.builder()
                .id(2L)
                .name("가전제품")
                .slug("electronics")
                .description("생활의 편리함을 더하는 가전제품 모음")
                .level(1)
                .imageUrl("https://example.com/categories/electronics.jpg")
                .children(new ArrayList<>())
                .build();

        // Level 2 (parent: 가구)
        category9 = Category.builder()
                .id(9L)
                .name("거실 가구")
                .slug("living-room")
                .description("거실을 위한 다양한 가구 제품")
                .level(2)
                .parent(category1) // 부모 설정
                .imageUrl("https://example.com/categories/living-room.jpg")
                .children(new ArrayList<>())
                .build();
        category10 = Category.builder()
                .id(10L)
                .name("침실 가구")
                .slug("bedroom")
                .description("편안한 휴식을 위한 침실 가구")
                .level(2)
                .parent(category1) // 부모 설정
                .imageUrl("https://example.com/categories/bedroom.jpg")
                .children(new ArrayList<>())
                .build();

        // Level 3 (parent: 거실 가구)
        category45 = Category.builder()
                .id(45L)
                .name("소파")
                .slug("sofa")
                .description("다양한 스타일과 크기의 소파")
                .level(3)
                .parent(category9) // 부모 설정
                .imageUrl("https://example.com/categories/sofa.jpg")
                .children(new ArrayList<>())
                .build();

        mockCategories = new ArrayList<>(Arrays.asList(category1, category2, category9, category10, category45));

        // CategoryMapper Mock 설정
        when(categoryMapper.toCategoryResponse(any(Category.class))).thenAnswer(invocation -> {
            Category cat = invocation.getArgument(0);
            // CategoryDTO.Category DTO의 빌더를 사용하거나 직접 생성
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
    @DisplayName("계층형 카테고리 조회 시 루트 카테고리 및 하위 카테고리가 올바르게 구성되어야 한다 (level 파라미터 null)")
    void getAllCategories_whenLevelIsNull_shouldReturnHierarchicalCategories() {
        // given
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        // when
        List<CategoryDTO.Category> result = categoryService.getAllCategories(null);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2); // 루트 카테고리는 "가구", "가전제품" 2개

        // "가구" 카테고리 검증
        Optional<CategoryDTO.Category> furnitureOpt = result.stream().filter(c -> c.getId().equals(1L)).findFirst();
        assertThat(furnitureOpt).isPresent();
        CategoryDTO.Category furnitureDto = furnitureOpt.get();
        assertThat(furnitureDto.getName()).isEqualTo("가구");
        assertThat(furnitureDto.getSlug()).isEqualTo("furniture");
        assertThat(furnitureDto.getDescription()).isEqualTo("편안한 생활을 위한 다양한 가구 컬렉션");
        assertThat(furnitureDto.getLevel()).isEqualTo(1);
        assertThat(furnitureDto.getImageUrl()).isEqualTo("https://example.com/categories/furniture.jpg");
        assertThat(furnitureDto.getChildren()).hasSize(2); // "거실 가구", "침실 가구"

        // "가구" > "거실 가구" 검증
        Optional<CategoryDTO.Category> livingRoomOpt = furnitureDto.getChildren().stream().filter(c -> c.getId().equals(9L)).findFirst();
        assertThat(livingRoomOpt).isPresent();
        CategoryDTO.Category livingRoomDto = livingRoomOpt.get();
        assertThat(livingRoomDto.getName()).isEqualTo("거실 가구");
        assertThat(livingRoomDto.getLevel()).isEqualTo(2);
        assertThat(livingRoomDto.getChildren()).hasSize(1); // "소파"

        // "가구" > "거실 가구" > "소파" 검증
        Optional<CategoryDTO.Category> sofaOpt = livingRoomDto.getChildren().stream().filter(c -> c.getId().equals(45L)).findFirst();
        assertThat(sofaOpt).isPresent();
        CategoryDTO.Category sofaDto = sofaOpt.get();
        assertThat(sofaDto.getName()).isEqualTo("소파");
        assertThat(sofaDto.getLevel()).isEqualTo(3);
        assertThat(sofaDto.getChildren()).isEmpty(); // "소파"는 자식이 없음

        // "가구" > "침실 가구" 검증
        Optional<CategoryDTO.Category> bedroomOpt = furnitureDto.getChildren().stream().filter(c -> c.getId().equals(10L)).findFirst();
        assertThat(bedroomOpt).isPresent();
        CategoryDTO.Category bedroomDto = bedroomOpt.get();
        assertThat(bedroomDto.getName()).isEqualTo("침실 가구");
        assertThat(bedroomDto.getLevel()).isEqualTo(2);
        assertThat(bedroomDto.getChildren()).isEmpty();

        // "가전제품" 카테고리 검증
        Optional<CategoryDTO.Category> electronicsOpt = result.stream().filter(c -> c.getId().equals(2L)).findFirst();
        assertThat(electronicsOpt).isPresent();
        CategoryDTO.Category electronicsDto = electronicsOpt.get();
        assertThat(electronicsDto.getName()).isEqualTo("가전제품");
        assertThat(electronicsDto.getLevel()).isEqualTo(1);
        assertThat(electronicsDto.getChildren()).isEmpty();
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
        assertThat(root1DtoOpt.get().getName()).isEqualTo("루트1");
        assertThat(root1DtoOpt.get().getLevel()).isEqualTo(1);
        assertThat(root1DtoOpt.get().getChildren()).isEmpty();

        Optional<CategoryDTO.Category> root2DtoOpt = result.stream().filter(c -> c.getId().equals(2L)).findFirst();
        assertThat(root2DtoOpt).isPresent();
        assertThat(root2DtoOpt.get().getName()).isEqualTo("루트2");
        assertThat(root2DtoOpt.get().getLevel()).isEqualTo(1);
        assertThat(root2DtoOpt.get().getChildren()).isEmpty();
    }
}
