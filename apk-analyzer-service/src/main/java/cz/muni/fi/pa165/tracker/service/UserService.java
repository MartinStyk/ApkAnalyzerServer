package cz.muni.fi.pa165.tracker.service;

import java.util.List;

/**
 * Business logic for work with users.
 *
 * @author Petra Ondřejková
 * @version 09.11. 2016
 */
public interface UserService {

    /**
     * Register new user.
     *
     * @param user user to be created
     * @param password plain password
     */
    void registerUser(User user, String password);

    /**
     * Log in user.
     *
     * @param user user logging in
     * @param password plain password
     * @return true if user was successfully logged in, false otherwise
     */
    boolean authenticateUser(User user, String password);

    /**
     * Check whether the user is admin or not.
     *
     * @param user
     * @return true if user is admin, false otherwise
     */
    boolean isAdmin(User user);

    /**
     * Remove given user.
     *
     * @param user user to be removed
     */
    void deleteUser(User user);

    /**
     * Finds an user by given id.
     *
     * @param id of user to be found
     * @return found user
     */
    User findById(Long id);

    /**
     * Finds an user by email.
     *
     * @param email of user to be found
     * @return found user
     */
    User findByEmail(String email);

    /**
     * Returns a list of all users.
     *
     * @return list of all users.
     */
    List<User> findAll();

    /**
     * Updates an user with new data.
     *
     * @param user containing new user data.
     * @return update user
     */
    User update(User user);
}
