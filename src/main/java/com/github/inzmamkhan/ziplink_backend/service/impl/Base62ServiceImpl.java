package com.github.inzmamkhan.ziplink_backend.service.impl;

import com.github.inzmamkhan.ziplink_backend.service.Base62Service;
import org.springframework.stereotype.Service;

@Service
public class Base62ServiceImpl implements Base62Service {

    private static final String BASE62_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = BASE62_CHARACTERS.length();

    @Override
    public String encode(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("ID must be a non-negative number");
        }

        if (id == 0) {
            return String.valueOf(BASE62_CHARACTERS.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            int remainder = (int) (id % BASE);
            sb.append(BASE62_CHARACTERS.charAt(remainder));
            id /= BASE;
        }

        return sb.reverse().toString();
    }

    @Override
    public Long decode(String base62Str) {
        if (base62Str == null || base62Str.trim().isEmpty()) {
            throw new IllegalArgumentException("Base62 string cannot be empty");
        }

        long result = 0;
        for (int i = 0; i < base62Str.length(); i++) {
            char c = base62Str.charAt(i);
            int index = BASE62_CHARACTERS.indexOf(c);
            if (index == -1) {
                throw new IllegalArgumentException("Invalid Base62 character: " + c);
            }
            result = result * BASE + index;
        }

        return result;
    }
}