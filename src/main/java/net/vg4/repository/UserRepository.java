package net.vg4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.vg4.domain.User;

public interface UserRepository extends JpaRepository<User, String> {

}
