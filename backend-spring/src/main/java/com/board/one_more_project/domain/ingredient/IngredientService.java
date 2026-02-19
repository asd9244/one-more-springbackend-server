package com.board.one_more_project.domain.ingredient;

import com.board.one_more_project.domain.ingredient.dto.IngredientResponse;

import java.util.List;

// 비즈니스 로직의 수행 방법을 정의한 추상화 interface
public interface IngredientService {

    List<IngredientResponse> getAllIngredients();

    List<IngredientResponse> searchIngredients(String keyword);

}