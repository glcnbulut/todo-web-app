package com.GoDo.todo_api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

// Kullanıcı entity'si. Veritabanı ile kullanıcı bilgisini tutar ve Spring Security UserDetails implementasyonu sağlar.
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String name;
private String surname;
private String email;
private String password;
private String role = "ROLE_USER";

// User'a ait olan ToDo'lar listesi
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonIgnore // Bu etiketi ekliyoruz ki sonsuz döngü hatası oluşmasın
private List<Todo> todos = new ArrayList<>();
	// Kullanıcının yetkilerini döndürür
	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(this.role));
	}

	// Kullanıcının username bilgisini döndürür (email)
	@Override
	public String getUsername() {
		return this.email;
	}

	// Hesap süresi doldu mu kontrolü
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// Hesap kilitli mi kontrolü
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// Şifre süresi doldu mu kontrolü
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// Kullanıcı aktif mi kontrolü
	@Override
	public boolean isEnabled() {
		return true;
	}
}