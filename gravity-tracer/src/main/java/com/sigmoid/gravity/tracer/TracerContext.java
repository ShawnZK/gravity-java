package com.sigmoid.gravity.tracer;

import com.google.common.collect.Sets;
import com.sigmoid.gravity.common.config.TracerServerConfig;
import com.sigmoid.gravity.common.enums.RoleEnum;
import com.sigmoid.gravity.common.exception.GravitySystemException;
import com.sigmoid.gravity.common.sequence.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TracerContext {

    private String nodeId;

    private String masterId;

    private Set<String> slaveIds;

    private ReadWriteLock slaveLock;

    private RoleEnum role;

    private FileMetaHolder metas;

    private static TracerContext instance = null;

    private static boolean initialized = false;

    public synchronized static void createInitial(TracerServerConfig tracerServerConfig) {
        if (initialized) {
            throw new GravitySystemException("TracerContext instance already initialized");
        }
        instance = TracerContext.builder()
                .nodeId(UuidGenerator.generateUuid())
                .slaveIds(Sets.newHashSet())
                .slaveLock(new ReentrantReadWriteLock())
                .role(Optional.ofNullable(RoleEnum.getByValue(tracerServerConfig.getServerType()))
                        .orElseThrow(() -> new GravitySystemException(
                                String.format("Invalid role param as [%d]", tracerServerConfig.getServerType()))))
                .metas(FileMetaHolder.getInstance())
                .build();
        initialized = true;
    }

    public void addSlave(String slaveId) {
        slaveLock.writeLock().lock();
        try {
            slaveIds.add(slaveId);
        } finally {
            slaveLock.writeLock().unlock();
        }
    }

    public void removeSlave(String slaveId) {
        slaveLock.writeLock().lock();
        try {
            slaveIds.remove(slaveId);
        } finally {
            slaveLock.writeLock().unlock();
        }
    }

    public void assignMaster(String masterId) {
        this.masterId = masterId;
    }

}
