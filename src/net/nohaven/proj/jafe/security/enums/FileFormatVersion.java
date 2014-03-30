package net.nohaven.proj.jafe.security.enums;

import net.nohaven.proj.gettext4j.*;
import net.nohaven.proj.jafe.security.exceptions.*;

public class FileFormatVersion {
    public static final FileFormatVersion FIRST = new FileFormatVersion(
            (byte) 0x01);

    private byte id;

    private FileFormatVersion(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }

    public static FileFormatVersion fromId(byte id)
            throws UnsupportedCharacteristicException {
        if (id == FIRST.id)
            return FIRST;
        throw new UnsupportedCharacteristicException(J18n._("Unknown file format"));
    }

    public static FileFormatVersion getCurrent() {
        return FIRST;
    }
}
