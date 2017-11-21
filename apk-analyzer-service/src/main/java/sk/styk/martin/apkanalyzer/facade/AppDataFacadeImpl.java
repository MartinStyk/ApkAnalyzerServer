package sk.styk.martin.apkanalyzer.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.styk.martin.apkanalyzer.dto.AppDataDto;
import sk.styk.martin.apkanalyzer.entity.AppData;
import sk.styk.martin.apkanalyzer.mapping.MappingService;
import sk.styk.martin.apkanalyzer.service.AppDataService;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
@Service
@Transactional
public class AppDataFacadeImpl implements AppDataFacade {

    @Inject
    private AppDataService appDataService;

    @Inject
    private MappingService<AppData, AppDataDto> mappingService;

    @Override
    public AppDataDto create(AppDataDto appData) {
        AppData created = appDataService.create(mappingService.convertToEntity(appData));
        return mappingService.convertToDto(created);
    }

    @Override
    public AppDataDto createWithExistenceCheck(AppDataDto appData) {
        AppData created = appDataService.createWithExistenceCheck(mappingService.convertToEntity(appData));
        return mappingService.convertToDto(created);
    }

    @Override
    public AppDataDto update(AppDataDto appData) {
        AppData updated = appDataService.update(mappingService.convertToEntity(appData));
        return mappingService.convertToDto(updated);
    }

    @Override
    public void remove(Long id) throws IllegalArgumentException {
        appDataService.remove(id);
    }

    @Override
    public AppDataDto findById(Long id) {
        AppData entity = appDataService.findById(id);
        return mappingService.convertToDto(entity);
    }

    @Override
    public List<AppDataDto> findAll() {
        List<AppData> datas = appDataService.findAll();
        return datas.stream()
                .map(appData -> mappingService.convertToDto(appData))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppDataDto> findByDevice(String deviceId) {
        List<AppData> datas = appDataService.findByDevice(deviceId);
        return datas.stream()
                .map(appData -> mappingService.convertToDto(appData))
                .collect(Collectors.toList());
    }

}
