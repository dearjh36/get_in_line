package com.project.getinline.service;

import com.project.getinline.constant.ErrorCode;
import com.project.getinline.constant.PlaceType;
import com.project.getinline.dto.PlaceDto;
import com.project.getinline.exception.GeneralException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.project.getinline.domain.Place;
import com.project.getinline.repository.PlaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 장소")
@ExtendWith(MockitoExtension.class)
class PlaceServiceTest {

    @InjectMocks private PlaceService sut;
    @Mock private PlaceRepository placeRepository;

    @DisplayName("장소를 검색하면, 결과를 출력하여 보여준다")
    @Test
    void givenNothing_whenSearchingPlaces_thenReturnsEntirePlaceList(){
        // Given
        given(placeRepository.findAll(any(Predicate.class)))
                .willReturn(List.of(
                        createPlace(PlaceType.COMMON,"레스토랑"),
                        createPlace(PlaceType.SPORTS,"체육관")
                ));

        // When
        List<PlaceDto> list = sut.getPlaces(new BooleanBuilder());

        // Then
        assertThat(list).hasSize(2);
        then(placeRepository).should().findAll(any(Predicate.class));
    }

    @DisplayName("장소를 검색하는데 에러가 발생한 경우, 줄서기 프로젝트 기본 에러로 전환하여 예외 던진다.")
    @Test
    void givenDataRelatedException_whenSearchingPlaces_thenThrowsGeneralException(){
        // Given
        RuntimeException e = new RuntimeException("This is test.");
        given(placeRepository.findAll(any(Predicate.class))).willThrow(e);

        // When
        Throwable thrown = catchThrowable(() -> sut.getPlaces(new BooleanBuilder()));

        // Then
        assertThat(thrown)
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorCode.DATA_ACCESS_ERROR.getMessage());

        then(placeRepository).should().findAll(any(Predicate.class));
    }

    @DisplayName("장소 ID 로 존재하는 장소를 조회하면, 해당 장소 정보를 출력하여 보여준다.")
    @Test
    void givenPlaceId_whenSearchingExistingPlace_thenReturnsPlace(){
        // Given
        long placeId = 1L;
        Place place = createPlace(PlaceType.SPORTS, "체육관");
        given(placeRepository.findById(placeId)).willReturn(Optional.of(place));

        // When
        Optional<PlaceDto> result = sut.getPlace(placeId);

        // Then
        assertThat(result).hasValue(PlaceDto.of(place));
        then(placeRepository).should().findById(placeId);

    }


    private Place createPlace(PlaceType placeType, String placeName) {
        return createPlace(1L, placeType, placeName);
    }

    @DisplayName("장소 ID로 장소를 조회하면, 빈 정보를 출력하여 보여준다.")
    @Test
    void givenPlaceId_whenSearchingNonexistentPlace_thenReturnsEmptyOptional(){
        // Given
        long placeId = 2L;
        given(placeRepository.findById(placeId)).willReturn(Optional.empty());

        // When
        Optional<PlaceDto> result = sut.getPlace(placeId);

        // Then
        assertThat(result).isEmpty();
        then(placeRepository).should().findById(placeId);

    }

    private Place createPlace(long id, PlaceType placeType, String placeName ){
        Place place = Place.of(
                placeType,
                placeName,
                "주소 테스트",
                "010-1234-5678",
                24,
                "메모 테스트 : 마스크 꼭 착용하세요"
        );

        ReflectionTestUtils.setField(place,"id",id);

        return place;
    }


}