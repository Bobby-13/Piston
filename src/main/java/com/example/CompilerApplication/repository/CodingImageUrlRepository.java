package com.example.CompilerApplication.repository;

import com.example.CompilerApplication.model.entity.table.CodingImageUrl;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodingImageUrlRepository extends JpaRepository<CodingImageUrl , Long> {
}
