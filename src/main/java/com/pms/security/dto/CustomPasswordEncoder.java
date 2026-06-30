/**
 * 
 */
package com.pms.security.dto;

/**
 * 
 */
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        // Not used for saving in your scenario
        return rawPassword.toString();
    }

    
    
    @Override
    public boolean matches(CharSequence rawPassword, String storedPassword) {

//        System.out.println("Raw Password : " + rawPassword);
//        System.out.println("Stored Password : " + storedPassword);

        if (storedPassword != null && storedPassword.startsWith("$2")) {
            boolean result = new BCryptPasswordEncoder().matches(rawPassword, storedPassword);
            System.out.println("BCrypt Match : " + result);
            return result;
        }

        boolean result = rawPassword.toString().equals(storedPassword);
        System.out.println("Plain Match : " + result);
        return result;
    }
}