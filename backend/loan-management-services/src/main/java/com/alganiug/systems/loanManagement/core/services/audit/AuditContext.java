package com.alganiug.systems.loanManagement.core.services.audit;

import java.util.UUID;

public final class AuditContext {

    private static final ThreadLocal<Entry> CURRENT = new ThreadLocal<>();

    private AuditContext() {
    }

    public static void set(UUID actorId, String ipAddress) {
        CURRENT.set(new Entry(actorId, ipAddress));
    }

    public static UUID getActorId() {
        Entry entry = CURRENT.get();
        return entry == null ? null : entry.actorId;
    }

    public static String getIpAddress() {
        Entry entry = CURRENT.get();
        return entry == null ? null : entry.ipAddress;
    }

    public static void clear() {
        CURRENT.remove();
    }

    private static final class Entry {
        private final UUID actorId;
        private final String ipAddress;

        private Entry(UUID actorId, String ipAddress) {
            this.actorId = actorId;
            this.ipAddress = ipAddress;
        }
    }
}
