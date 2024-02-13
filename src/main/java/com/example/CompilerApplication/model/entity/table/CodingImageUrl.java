package com.example.CompilerApplication.model.entity.table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "coding_image_url")
public class CodingImageUrl {

    @Id
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("imageUrl")
    private CodingQuestion codingQuestion;

    private String url;
}
