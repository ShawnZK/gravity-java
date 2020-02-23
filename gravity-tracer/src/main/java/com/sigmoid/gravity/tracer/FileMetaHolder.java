package com.sigmoid.gravity.tracer;

import com.google.common.collect.Maps;
import com.sigmoid.gravity.common.enums.FileStatusEnum;
import com.sigmoid.gravity.common.exception.GravitySystemException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Builder
public class FileMetaHolder {

    private static final int FILE_META_BUCKET_SIZE = 4096;

    @Getter
    private static FileMetaHolder instance;

    //namespace -> bucket array mapping, use buckets rather one mapping to reduce concurrency
    private ConcurrentMap<String, FileMetaBucket[]> namespaceToMetaMapping;

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class FileMetaBucket {
        private ReadWriteLock readWriteLock;
        private ConcurrentMap<String, FileMeta> metas;

        static FileMetaBucket[] buildBuckets(int size) {
            FileMetaBucket[] buckets = new FileMetaBucket[size];
            IntStream.range(0, size).forEach(i -> {
                FileMetaBucket bucket = FileMetaBucket.builder()
                        .readWriteLock(new ReentrantReadWriteLock())
                        .metas(Maps.newConcurrentMap())
                        .build();
                buckets[i] = bucket;
            });
            return buckets;
        }

        void iterate(Consumer<Map.Entry<String, FileMeta>> consumer) {
            readWriteLock.readLock().lock();
            try {
                metas.entrySet().forEach(pair -> consumer.accept(pair));
            } finally {
                readWriteLock.readLock().unlock();
            }
        }

        void add(FileMeta fileMeta) {
            readWriteLock.writeLock().lock();
            try {
                metas.put(fileMeta.getUuid(), fileMeta);
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }

    }

    private int getBucketIndex(String fileId) {
        return Optional.ofNullable(fileId)
                .map(id -> {
                    int index = id.hashCode() % FILE_META_BUCKET_SIZE;
                    return index < 0 ? index + FILE_META_BUCKET_SIZE : index;
                })
                .orElseThrow(() -> new GravitySystemException(String.format("Invalid fileId [%s]", fileId)));
    }

    public static void init(Set<String> namespaces) {
        ConcurrentMap<String, FileMetaBucket[]> namespaceMapping = Maps.newConcurrentMap();
        namespaces.forEach(namespace -> namespaceMapping.put(namespace, FileMetaBucket.buildBuckets(FILE_META_BUCKET_SIZE)));
        instance = FileMetaHolder.builder().namespaceToMetaMapping(namespaceMapping).build();
    }

    public synchronized void addNamespace(String namespace) {
        if (namespaceToMetaMapping.containsKey(namespace)) {
            throw new GravitySystemException(String.format("Namespace [%s] already existed", namespace));
        }
        namespaceToMetaMapping.put(namespace, FileMetaBucket.buildBuckets(FILE_META_BUCKET_SIZE));
    }

    public FileMeta getFileMeta(String namespace, String fileId) {
        return Optional.ofNullable(namespaceToMetaMapping.get(namespace))
                .map(buckets -> buckets[getBucketIndex(fileId)])
                .map(FileMetaBucket::getMetas)
                .map(m -> m.get(fileId))
                .orElse(null);
    }

    public FileMeta getValidFileMeta(String namespace, String fileId) {
        return Optional.ofNullable(namespaceToMetaMapping.get(namespace))
                .map(buckets -> buckets[getBucketIndex(fileId)])
                .map(FileMetaBucket::getMetas)
                .map(m -> m.get(fileId))
                .filter(f -> f.getStatus() == FileStatusEnum.VALID.getValue())
                .orElse(null);
    }

    public void iterate(String namespace, Consumer<Map.Entry<String, FileMeta>> consumer) {
        Optional.ofNullable(namespaceToMetaMapping.get(namespace))
                .ifPresent(buckets -> {
                    for (FileMetaBucket bucket : buckets) {
                        bucket.iterate(consumer);
                    }
                });
    }

    public void addFile(String namespace, String fileId, int sizeInByte) {
        Optional.ofNullable(namespaceToMetaMapping.get(namespace))
                .ifPresent(fileMetaBuckets -> {
                    FileMeta fileMeta = FileMeta.builder()
                            .uuid(fileId)
                            .readCount(0)
                            .prebuildTime(System.currentTimeMillis())
                            .createTime(System.currentTimeMillis())
                            .lastReadTime(System.currentTimeMillis())
                            .status(FileStatusEnum.PRE_BUILD.getValue())
                            .sizeInByte(sizeInByte)
                            //TODO
                            .build();
                    fileMetaBuckets[getBucketIndex(fileId)].add(fileMeta);
                });
    }

}
