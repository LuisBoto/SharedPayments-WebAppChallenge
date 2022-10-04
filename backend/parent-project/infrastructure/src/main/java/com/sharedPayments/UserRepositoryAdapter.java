package com.sharedPayments;

import java.util.List;
import java.util.stream.Collectors;

import com.sharedPayments.model.User;
import com.sharedPayments.model.UserEntity;
import com.sharedPayments.ports.UserRepository;
import com.sharedPayments.repository.UserInfraJpaRepository;

import jakarta.inject.Singleton;

@Singleton
public class UserRepositoryAdapter implements UserRepository {

	private UserInfraJpaRepository userJpa;

	public UserRepositoryAdapter(UserInfraJpaRepository repository) {
		this.userJpa = repository;
	}

	@Override
	public User save(User user) {
		return this.userJpa.save(UserEntity.fromModel(user)).toModel();
	}

	@Override
	public User findById(Long id) {
		var user = this.userJpa.findById(id);
		if (user.isEmpty()) 
			return null;
		return user.get().toModel();
	}

	@Override
	public List<User> findAll() {
		return this.userJpa.findAll().stream().map(entity -> entity.toModel()).collect(Collectors.toList());
	}

	@Override
	public User update(User user) {
		return this.userJpa.update(UserEntity.fromModel(user)).toModel();
	}

	@Override
	public int count() {
		return (int) this.userJpa.count();
	}

}
