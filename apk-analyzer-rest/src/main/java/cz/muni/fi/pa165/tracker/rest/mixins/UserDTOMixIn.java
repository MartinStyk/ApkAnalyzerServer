package cz.muni.fi.pa165.tracker.rest.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * We don't want to provide password hash on REST request for user.
 *
 * @author Martin Styk
 * @version 29.11.2016
 */
@JsonIgnoreProperties({"passwordHash"})
public class UserDTOMixIn {
}
