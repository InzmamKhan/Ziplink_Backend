package com.github.inzmamkhan.ziplink_backend.service;

public interface Base62Service {

    /**
     * Encodes a database numeric ID into a Base62 string.
     *
     * @param id The numeric ID from PostgreSQL
     * @return Base62 encoded string representation
     */
    String encode(Long id);

    /**
     * Decodes a Base62 string back into its numeric ID.
     *
     * @param base62Str The Base62 string to decode
     * @return Decoded numeric ID
     */
    Long decode(String base62Str);
}