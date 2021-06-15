package br.com.andrebuarque.financeapi.mapper;

import br.com.andrebuarque.financeapi.dto.CategoryDto;
import br.com.andrebuarque.financeapi.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category toEntity(CategoryDto categoryDto);
}
