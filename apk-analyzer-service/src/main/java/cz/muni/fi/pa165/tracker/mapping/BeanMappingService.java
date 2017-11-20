package cz.muni.fi.pa165.tracker.mapping;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;

/**
 * @author Petra Ondřejková
 * @version 09.11. 2016
 */
public interface BeanMappingService {
    <T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);

    <T> Map<T, Integer> mapTo(Map<?, Integer> objects, Class<T> mapToClass);

    <T> T mapTo(Object u, Class<T> mapToClass);

    Mapper getMapper();
}
