package com.julien.crawler;

import java.util.List;

public record PageResult(int depth, String url, String host, List<String> keywords) { }
