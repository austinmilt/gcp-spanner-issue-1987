package com.example.demo;

import com.google.cloud.spring.data.spanner.core.mapping.Column;
import com.google.cloud.spring.data.spanner.core.mapping.PrimaryKey;
import com.google.cloud.spring.data.spanner.core.mapping.Table;

@Table(name = "users")
class User {
    @PrimaryKey
    @Column(name = "id")
    String id;

    @Column(name = "credits")
    int credits;
}
