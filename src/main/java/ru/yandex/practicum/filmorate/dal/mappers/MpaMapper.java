package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.MpaDTO;
import ru.yandex.practicum.filmorate.model.MPA;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MpaMapper {

    private MpaMapper() {
    }

    public MPA dtoToMpa(MpaDTO mpaDTO) {
        return MPA.builder()
                .id(mpaDTO.getId())
                .rate(mpaDTO.getRate())
                .build();
    }

    public MpaDTO mpaToDto(MPA mpa) {
        return MpaDTO.builder()
                .id(mpa.getId())
                .rate(mpa.getRate())
                .build();
    }

    public List<MpaDTO> listMpaToListDto(Collection<MPA> mpas) {
        List<MpaDTO> mpaDTOS = new ArrayList<>();
        for (MPA mpa : mpas) {
            mpaDTOS.add(mpaToDto(mpa));
        }
        return mpaDTOS;
    }

}