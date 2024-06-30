package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MpaMapper {

    private MpaMapper() {
    }

    public Mpa dtoToMpa(MpaDto mpaDTO) {
        Mpa mpa = new Mpa();
        mpa.setId(mpaDTO.getId());
        mpa.setName(mpaDTO.getName());
        return mpa;
    }

    public MpaDto mpaToDto(Mpa mpa) {
        return MpaDto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }

    public List<MpaDto> listMpaToListDto(Collection<Mpa> mpas) {
        List<MpaDto> mpaDTOS = new ArrayList<>();
        for (Mpa mpa : mpas) {
            mpaDTOS.add(mpaToDto(mpa));
        }
        return mpaDTOS;
    }

}