package sk.styk.martin.apkanalyzer.mapping;

/**
 * @author Martin Styk
 * @version 21.11.2017
 */
public interface MappingService<Entity, Dto> {
    Dto convertToDto(Entity entity);

    Entity convertToEntity(Dto dto);
}
