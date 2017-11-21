package sk.styk.martin.apkanalyzer.facade;

import sk.styk.martin.apkanalyzer.dto.AppDataDto;
import sk.styk.martin.apkanalyzer.entity.AppData;

import java.util.List;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
public interface AppDataFacade {
    AppDataDto create(AppDataDto entity);

    AppDataDto update(AppDataDto entity);

    void remove(Long id);

    AppDataDto findById(Long id);

    List<AppDataDto> findAll();

    AppDataDto createWithExistenceCheck(AppDataDto appData);

    List<AppDataDto> findByDevice(String deviceId);
}
