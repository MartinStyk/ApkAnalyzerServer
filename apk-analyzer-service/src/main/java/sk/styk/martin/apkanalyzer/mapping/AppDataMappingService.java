package sk.styk.martin.apkanalyzer.mapping;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import sk.styk.martin.apkanalyzer.dto.AppDataDto;
import sk.styk.martin.apkanalyzer.entity.AppData;

import javax.inject.Inject;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
@Service
public class AppDataMappingService implements MappingService<AppData, AppDataDto> {

    @Inject
    private ModelMapper modelMapper;

    public AppDataDto convertToDto(AppData entity) {
        AppDataDto appDataDto = modelMapper.map(entity, AppDataDto.class);
        return appDataDto;
    }

    public AppData convertToEntity(AppDataDto appDataDto) {
        AppData appData = modelMapper.map(appDataDto, AppData.class);
        return appData;
    }
}
