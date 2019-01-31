package com.soaringroad.blog.service;

public interface MigrationService {

    void migrationToDynamoDB();

    void migrationArticleToDynamoDB(Long id);
}
