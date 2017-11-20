package cz.muni.fi.pa165.tracker.service;

import cz.muni.fi.pa165.tracker.dao.AppDataDao;
import cz.muni.fi.pa165.tracker.enums.UserRole;
import cz.muni.fi.pa165.tracker.exception.TranslatePersistenceExceptions;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Inject;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

/**
 * Implementation of the {@link UserService}. This class is part of the service
 * module of the application that provides the implementation of the business
 * logic for user.
 *
 * @author Petra Ondřejková
 * @version 09.11. 2016
 */
@Service
@TranslatePersistenceExceptions
public class UserServiceImpl implements UserService {

    @Inject
    private UserDao userDao;

    @Inject
    private AppDataDao activityReportDao;

    @Override
    public void registerUser(User user, String password) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }

        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }
        user.setPasswordHash(createHash(password));
        userDao.create(user);
    }

    @Override
    public boolean authenticateUser(User user, String password) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }
        return validatePassword(password, user.getPasswordHash());
    }

    @Override
    public boolean isAdmin(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        User actual = findById(user.getId());
        return actual.getRole() == UserRole.ADMIN;
    }

    @Override
    public void deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        if (user.getId() == null) {
            throw new IllegalArgumentException("user does not have set id");
        }
        User actual = findById(user.getId());
        activityReportDao.deleteUserReports(actual);
        userDao.remove(user);
    }

    @Override
    public User findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        return userDao.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("email is null");
        }
        return userDao.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User update(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }
        return userDao.update(user);
    }

    private static String createHash(String password) {
        final int saltByteSize = 24;
        final int hashByteSize = 24;
        final int numberIterations = 1000;
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltByteSize];
        random.nextBytes(salt);
        // Hash the password
        byte[] hash = pbkdf2(password.toCharArray(), salt, numberIterations, hashByteSize);
        // format iterations:salt:hash
        return numberIterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validatePassword(String password, String correctHash) {
        if (password == null) {
            return false;
        }
        if (correctHash == null) {
            throw new IllegalArgumentException("password hash is null");
        }
        String[] params = correctHash.split(":");
        int iterations = Integer.parseInt(params[0]);
        byte[] salt = fromHex(params[1]);
        byte[] hash = fromHex(params[2]);
        byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);
        return slowEquals(hash, testHash);
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    private static String toHex(byte[] array) {
        BigInteger bigInt = new BigInteger(1, array);
        String hex = bigInt.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        return paddingLength > 0 ? String.format("%0" + paddingLength + "d", 0) + hex : hex;
    }
}
