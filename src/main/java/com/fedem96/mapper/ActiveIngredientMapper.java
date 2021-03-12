package com.fedem96.mapper;

import com.fedem96.dto.ActiveIngredientDto;
import com.fedem96.model.ActiveIngredient;

public class ActiveIngredientMapper extends BaseMapper<ActiveIngredient, ActiveIngredientDto> {
    @Override
    public ActiveIngredientDto convert(ActiveIngredient activeIngredient) {
        ActiveIngredientDto aiDto = new ActiveIngredientDto();
        aiDto.setAtc(activeIngredient.getAtc());
        aiDto.setDescription(activeIngredient.getDescription());
        return aiDto;
    }
}
