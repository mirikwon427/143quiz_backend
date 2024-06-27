package garlicbears.quiz.domain.common.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends BaseTimeEntity implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_seq")
	private long adminId;

	@Column(name = "admin_email", nullable = false, unique = true, length = 200)
	private String adminEmail;

	@Column(name = "admin_password", nullable = false, length = 100)
	private String adminPassword;

	@Enumerated(EnumType.STRING)
	@Column(name = "admin_active")
	@ColumnDefault("'active'")
	private Active active;

	@ManyToMany
	@JoinTable(name = "admin_role",
	joinColumns = @JoinColumn(name = "admin_seq"),
	inverseJoinColumns = @JoinColumn(name = "role_seq")
	)
	private Set<Role> roles = new HashSet<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
			.map(role -> new SimpleGrantedAuthority(role.getRoleName()))
			.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return getPassword();
	}

	@Override
	public String getUsername() {
		return getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
