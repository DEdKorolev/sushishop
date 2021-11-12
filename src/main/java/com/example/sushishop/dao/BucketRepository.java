package com.example.sushishop.dao;

import com.example.sushishop.domain.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
//    void deleteById(Bucket byId);
}
