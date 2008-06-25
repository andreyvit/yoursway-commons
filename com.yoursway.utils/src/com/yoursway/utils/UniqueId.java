package com.yoursway.utils;

import static com.yoursway.utils.YsStrings.emptyToNullWithTrim;

import java.util.UUID;

public final class UniqueId {
    
    private final UUID uuid;
    private final String description;

    public UniqueId(UUID uuid, String description) {
        if (uuid == null)
            throw new NullPointerException("uuid is null");
        this.uuid = uuid;
        this.description = emptyToNullWithTrim(description);
    }
    
    public UUID toUUID() {
        return uuid;
    }
    
    public String description() {
        return description;
    }
    
    public static UniqueId uniqueId(String string) {
        int pos = string.indexOf(' ');
        if (pos > 0)
            return new UniqueId(UUID.fromString(string.substring(0, pos)), string.substring(pos + 1));
        else
            return new UniqueId(UUID.fromString(string), null);
    }
    
    public String toString() {
        if (description != null)
            return uuid + " " + description;
        else
            return uuid.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UniqueId other = (UniqueId) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }
    
}
