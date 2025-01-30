package com.devilish.granja.services;

import com.devilish.granja.dto.request.DuckRequestDTO;
import com.devilish.granja.dto.response.DuckResponseDTO;

public interface DuckService {

    DuckResponseDTO save(DuckRequestDTO duckRequestDTO);

}
