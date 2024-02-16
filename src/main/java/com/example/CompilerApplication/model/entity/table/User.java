package com.example.CompilerApplication.model.entity.table;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Table(name = "user_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

//   @Email
   @Column(unique = true)
   private String email;

   private String name;

   private String password;
   private String collegeName;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
           name = "contest_user",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "contest_id")
   )
   private Set<Contest> contest;

   @OneToOne(mappedBy = "user")
   private Resume resume;

   private boolean isPassed;

   @Override
   public String toString() {
      return "User{" +
              "userId='" + userId + '\'' +
              ", email='" + email + '\'' +
              ", name='" + name + '\'' +
              ", password='" + password + '\'' +
              ", collegeName='" + collegeName + '\'' +
              ", contest=" + contest +
              ", resume=" + resume +
              ", isPassed=" + isPassed +
              '}';
   }
}
