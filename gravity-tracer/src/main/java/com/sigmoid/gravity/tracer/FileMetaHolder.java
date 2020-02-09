package com.sigmoid.gravity.tracer;

import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

public class FileMetaHolder {

    //TODO LRU etc.
    private ConcurrentMap<String, FileMeta> metas = Maps.newConcurrentMap();

}
